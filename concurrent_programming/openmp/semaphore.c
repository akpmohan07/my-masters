#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <omp.h>

omp_lock_t mutex;
omp_lock_t delay;
int count;

void init(int n) {
    count = n;
    omp_init_lock(&mutex);
    omp_init_lock(&delay);
    omp_set_lock(&delay);
}

void P(int thread_id) {
    omp_set_lock(&mutex);
    count--;
    if (count < 0) {
        printf("[Thread %d][BLOCKED  ] Count=%-3d Waiting for resource...\n", thread_id, count);
        omp_unset_lock(&mutex);
        omp_set_lock(&delay);
        printf("[Thread %d][UNBLOCKED] Count=%-3d Resource acquired\n", thread_id, count);
    } else {
        printf("[Thread %d][ACQUIRED ] Count=%-3d No wait needed\n", thread_id, count);
        omp_unset_lock(&mutex);
    }
}

void V(int thread_id) {
    omp_set_lock(&mutex);
    count++;
    if (count <= 0) {
        printf("[Thread %d][RELEASED ] Count=%-3d Waking blocked thread\n", thread_id, count);
        omp_unset_lock(&delay);
    } else {
        printf("[Thread %d][RELEASED ] Count=%-3d No threads to wake\n", thread_id, count);
    }
    omp_unset_lock(&mutex);
}

void destroy() {
    omp_destroy_lock(&mutex);
    omp_destroy_lock(&delay);
}

void access_resource(int thread_id) {
    int work_time = (thread_id % 3) + 1;

    printf("[Thread %d][WAITING  ] Trying to acquire semaphore\n", thread_id);
    P(thread_id);

    printf("[Thread %d][WORKING  ] Doing work for %d second(s)\n", thread_id, work_time);
    sleep(work_time);

    V(thread_id);
    printf("[Thread %d][DONE     ] Finished\n", thread_id);
}

int main() {
    int max_concurrent = 3;
    int num_threads    = 8;

    printf("\n");
    printf("+----------------------------------------+\n");
    printf("|       Counting Semaphore Demo           |\n");
    printf("+----------------------------------------+\n");
    printf("| Threads         : %-4d                 |\n", num_threads);
    printf("| Max concurrent  : %-4d                 |\n", max_concurrent);
    printf("| Work time       : 1-3 seconds/thread   |\n");
    printf("+----------------------------------------+\n\n");

    printf("%-10s %-12s %-8s %s\n", "Thread", "Status", "Count", "Message");
    printf("%-10s %-12s %-8s %s\n", "----------", "------------", "--------", "--------------------");

    init(max_concurrent);

    #pragma omp parallel num_threads(8)
    {
        access_resource(omp_get_thread_num());
    }

    destroy();

    printf("\n+----------------------------------------+\n");
    printf("|          All threads completed          |\n");
    printf("+----------------------------------------+\n\n");

    return 0;
}