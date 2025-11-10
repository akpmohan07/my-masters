#include <stdio.h>
#include <stdlib.h>

int global_init = 10;
int global_uninit;

int main() {
    int local = 5;
    int *heap = malloc(sizeof(int));
    *heap = 20;

    printf("Code (main):    %p\n", (void*)main);
    printf("Data:           %p\n", (void*)&global_init);
    printf("BSS:            %p\n", (void*)&global_uninit);
    printf("Heap:           %p\n", (void*)heap);
    printf("Stack:          %p\n", (void*)&local);

    free(heap);
    return 0;
}
