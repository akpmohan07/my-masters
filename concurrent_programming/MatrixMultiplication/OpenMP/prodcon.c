#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <omp.h>

// -------------------------------------------------------
// Configuration
// -------------------------------------------------------
#define BUFFER_SIZE   10
#define LOWER_LIMIT   0.0
#define UPPER_LIMIT   10.0
#define NUM_CONSUMERS 2
#define DIVISIONS     1000

// -------------------------------------------------------
// Counting semaphore using 2 binary locks + counter
// -------------------------------------------------------
typedef struct {
    int count;
    omp_lock_t mutex;   // protects count
    omp_lock_t delay;   // blocks threads when count = 0
} Semaphore;

void sem_init_s(Semaphore *s, int n) {
    s->count = n;
    omp_init_lock(&s->mutex);
    omp_init_lock(&s->delay);
    if (n == 0) omp_set_lock(&s->delay);  // block if starts at 0
}

void sem_acquire(Semaphore *s) {
    omp_set_lock(&s->mutex);
    s->count--;
    if (s->count < 0) {
        omp_unset_lock(&s->mutex);
        omp_set_lock(&s->delay);   // block here
    } else {
        omp_unset_lock(&s->mutex);
    }
}

void sem_release(Semaphore *s) {
    omp_set_lock(&s->mutex);
    s->count++;
    if (s->count <= 0) {
        omp_unset_lock(&s->delay); // wake one blocked thread
    }
    omp_unset_lock(&s->mutex);
}

void sem_destroy_s(Semaphore *s) {
    omp_destroy_lock(&s->mutex);
    omp_destroy_lock(&s->delay);
}

// -------------------------------------------------------
// Slice — unit of work passed via buffer
// -------------------------------------------------------
typedef struct {
    double start;
    double end;
    int divisions;     // 0 = sentinel (no more work)
} Slice;

// -------------------------------------------------------
// Integration function
// -------------------------------------------------------
double func(double x) {
    return sin(x);
}

// -------------------------------------------------------
// Consumer — reads slices from buffer, integrates them
// -------------------------------------------------------
void consume(Slice *buffer, Semaphore *avail, Semaphore *buff_slots,
             omp_lock_t *out_lock, int *out, omp_lock_t *res_lock, double *result) {

    int thread_id = omp_get_thread_num();

    while (1) {
        sem_acquire(avail);         // wait if buffer empty

        omp_set_lock(out_lock);     // protect out index
        int idx = *out;
        *out = (*out + 1) % BUFFER_SIZE;
        omp_unset_lock(out_lock);

        double st  = buffer[idx].start;
        double en  = buffer[idx].end;
        int    div = buffer[idx].divisions;

        sem_release(buff_slots);    // free the slot

        if (div == 0) {
            printf("[Consumer %d] Received sentinel — stopping\n", thread_id);
            break;
        }

        printf("[Consumer %d] Integrating [%.2f, %.2f]\n", thread_id, st, en);

        // compute trapezoidal integration
        double step = (en - st) / div;
        double x = st;
        double local_res = (func(st) + func(en)) / 2.0;
        for (int i = 0; i < div; i++) {
            x += step;
            local_res += func(x);
        }
        local_res *= step;

        omp_set_lock(res_lock);     // protect shared result
        *result += local_res;
        omp_unset_lock(res_lock);
    }
}

// -------------------------------------------------------
// Main
// -------------------------------------------------------
int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Usage: prodcon <num_jobs>\n");
        exit(1);
    }

    int J = atoi(argv[1]);

    printf("\n+------------------------------------------+\n");
    printf("|        Producer-Consumer Demo             |\n");
    printf("+------------------------------------------+\n");
    printf("| Jobs            : %-4d                   |\n", J);
    printf("| Consumers       : %-4d                   |\n", NUM_CONSUMERS);
    printf("| Buffer size     : %-4d                   |\n", BUFFER_SIZE);
    printf("| Range           : [%.1f, %.1f]             |\n", LOWER_LIMIT, UPPER_LIMIT);
    printf("+------------------------------------------+\n\n");

    Slice *buffer = (Slice *)malloc(BUFFER_SIZE * sizeof(Slice));
    int in = 0, out = 0;
    double integral = 0.0;

    Semaphore avail, buff_slots;
    sem_init_s(&avail,      0);           // starts empty — consumers wait
    sem_init_s(&buff_slots, BUFFER_SIZE); // starts full  — producer can add

    omp_lock_t out_lock, res_lock;
    omp_init_lock(&out_lock);
    omp_init_lock(&res_lock);

    #pragma omp parallel sections default(none) \
        shared(buffer, in, out, avail, buff_slots, out_lock, res_lock, integral, J)
    {
        // ---- PRODUCER ----
        #pragma omp section
        {
            printf("[Producer  ] Starting — creating %d jobs\n", J);
            double div_len = (UPPER_LIMIT - LOWER_LIMIT) / J;
            double st, en = LOWER_LIMIT;

            for (int i = 0; i < J; i++) {
                st = en;
                en += div_len;
                if (i == J-1) en = UPPER_LIMIT;

                sem_acquire(&buff_slots);       // wait if buffer full
                buffer[in].start     = st;
                buffer[in].end       = en;
                buffer[in].divisions = DIVISIONS;
                printf("[Producer  ] Added slice [%.2f, %.2f] to buffer[%d]\n", st, en, in);
                in = (in + 1) % BUFFER_SIZE;
                sem_release(&avail);            // signal consumer
            }

            // send sentinels
            for (int i = 0; i < NUM_CONSUMERS; i++) {
                sem_acquire(&buff_slots);
                buffer[in].divisions = 0;
                printf("[Producer  ] Added sentinel to buffer[%d]\n", in);
                in = (in + 1) % BUFFER_SIZE;
                sem_release(&avail);
            }
            printf("[Producer  ] Done\n");
        }

        // ---- CONSUMER 1 ----
        #pragma omp section
        {
            consume(buffer, &avail, &buff_slots, &out_lock, &out, &res_lock, &integral);
        }

        // ---- CONSUMER 2 ----
        #pragma omp section
        {
            consume(buffer, &avail, &buff_slots, &out_lock, &out, &res_lock, &integral);
        }
    }

    printf("\n+------------------------------------------+\n");
    printf("| Result = %-10.6f                      |\n", integral);
    printf("+------------------------------------------+\n\n");

    sem_destroy_s(&avail);
    sem_destroy_s(&buff_slots);
    omp_destroy_lock(&out_lock);
    omp_destroy_lock(&res_lock);
    free(buffer);

    return 0;
}