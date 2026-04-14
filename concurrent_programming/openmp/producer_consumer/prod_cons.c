#include <stdio.h>
#include <math.h>
#include <omp.h>

#define BUFFER_SIZE 10
#define DIVISIONS 1000
#define LOW 0
#define HIGH 10
#define SLICES 20

double total;

// The function we're integrating — f(x) = x²
double f(double x) {
    return x * x;
}

omp_lock_t mutex;
omp_lock_t read_slice;
omp_lock_t  insert_slice;
int available_size;


typedef struct {
    double start;
    double end;
} Slice;

typedef struct {
    Slice buffer[BUFFER_SIZE];
    int in; // Produer next insertion point in buffer
    int out; // Consumer next consumption point in buffer
} Data;

void init(Data *d) {
    available_size = BUFFER_SIZE;
    d->in = 0;
    d->out = 0;
    total = 0.0;
    omp_init_lock(&mutex);
    omp_init_lock(&read_slice);
    omp_init_lock(&insert_slice);
}

// Calculate area for one slice
double calculate(Slice slice) {
    double width = (double) (slice.end - slice.start) / DIVISIONS;
    double total = 0;

    for (int i = 0; i < DIVISIONS; i++) {
        double mid = slice.start + i * width + width / 2;
        total += f(mid) * width;
    }
    return total;
}

void produce(Data *d) {
    double width = (double) (HIGH - LOW) / SLICES;


    for(int i =0; i < SLICES+1;i++){

        Slice s;
        // Sending sentinel values
        if(i >= SLICES){
            s.start = -1;
            s.end = -1;
        } else {
            s.start = LOW + i * width;
            s.end = s.start + width;
        }


        // Insert into buffer
        omp_set_lock(&mutex);
        // Insert -> if sapce is available 1. Available 2. Not available
        // 2. Not available
        while (available_size <= 0) {
            printf("Buffer is full [%d]. Waiting for consumer to consume...\n", available_size);
            omp_unset_lock(&mutex);
            omp_set_lock(&insert_slice);
            omp_set_lock(&mutex);
        }

        // 1. Available
        int write_pos = d->in;
        printf("Slice [%f, %f] produced at %d\n", s.start, s.end, write_pos);
        d->buffer[write_pos] = s;
        d->in = (write_pos + 1) % BUFFER_SIZE ;
        available_size--;

        // omp_unset_lock(&read_slice);
        omp_unset_lock(&mutex);

    }
}

void consume(Data *d) {
    while(1) {
        omp_set_lock(&mutex);
        while (available_size >= BUFFER_SIZE) {
            printf("Buffer is empty [%d]. Waiting for producer to produce...\n", available_size);
            omp_unset_lock(&mutex);
            omp_set_lock(&read_slice);
            omp_set_lock(&mutex);
        }

        // 1. Available
        int read_pos = d->out;
        Slice s = d->buffer[read_pos];
        if (s.start == -1 && s.end == -1) {
            printf("Consumer received sentinel — stopping\n");
            omp_unset_lock(&mutex);
            break;
        }
        printf("Slice [%f, %f] consumed at %d\n", s.start, s.end, read_pos);
        #pragma omp critical
            total += calculate(s);
        d->out = (read_pos + 1) % BUFFER_SIZE;
        available_size++;

        // Releasing the write lock as space is available
        // omp_unset_lock(&insert_slice);
        omp_unset_lock(&mutex);
    }
}

int main() {
    printf("Starting buffer...\n");
    Data d;
    init(&d);

    #pragma omp parallel sections
    {
        #pragma omp section
        {
            printf("Producer started...\n");
            produce(&d);
        }

        #pragma omp section
        {
            printf("Consumer started...\n");
            consume(&d);
        }
    }
    
    printf("Result: %f\n", total);
}