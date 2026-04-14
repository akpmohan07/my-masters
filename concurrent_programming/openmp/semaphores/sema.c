#include<stdio.h>
#include <omp.h>
#include <unistd.h>

#define MIN 0
#define MAX 5

typedef struct {
    omp_lock_t mutex;
    omp_lock_t semphore;
    int count;
} Semaphore;


// Setttig the locks
void init(Semaphore *s) {
    omp_init_lock(&s->mutex);
    omp_init_lock(&s->semphore);
    omp_set_lock(&s->semphore);
    s->count = MAX;
}

// Acquire and reduce count
//If count <= 0 -> wait
void P(Semaphore *s) {
    int id = omp_get_thread_num();
    printf("Thread %d trying to acquire...\n", id);
    omp_set_lock(&s->mutex);
    while (s->count <= MIN) {
        printf("Thread %d waiting — count = %d\n", id, s->count);
        omp_unset_lock(&s->mutex);
        omp_set_lock(&s->semphore);
        omp_set_lock(&s->mutex);
    }
    s->count--;
    printf("Thread %d acquired — count now = %d\n", id, s->count);
    omp_unset_lock(&s->mutex);
}

// Release and increase count
void V(Semaphore *s) {
    int id = omp_get_thread_num();
    omp_set_lock(&s->mutex);
    // if (s->count == MIN){ -> Segmenation fault for the last thread because relaseing always,
    // If chekced , it will only invoke the 
        printf("Thread %d releasing — waking a waiter\n", id);
        omp_unset_lock(&s->semphore);
    // }
    if (s->count < MAX) {
        s->count++;
    }
    printf("Thread %d released — count now = %d\n", id, s->count);
    omp_unset_lock(&s->mutex);
}

int main () {

    Semaphore s;
    init(&s);

    printf("Starting — MAX slots = %d, Threads = 10\n\n", MAX);

    #pragma omp parallel num_threads(10)
    {
        P(&s);
        sleep(3);
        V(&s);
    }

    printf("\nAll threads done.\n");
    return 0;
}