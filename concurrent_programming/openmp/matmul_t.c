#include<stdio.h>
#include <stdlib.h>
#include <omp.h>

int main(int argc, char *argv[]) {
    // #pragma omp parallel
    // printf("Hello from %d of %d\n", omp_get_thread_num(), omp_get_num_threads());

    #pragma omp parallel for collapse(2)
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            printf("Mohan from %d of %d\n", omp_get_thread_num(), omp_get_num_threads());
        }
    }

    return 0;
}