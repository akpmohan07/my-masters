/*
 * memlayout.c
 *
 * Demonstrates process memory segments: text, data, bss, heap, stack.
 * Also prints /proc/self/maps so you can see the ranges and permissions.
 *
 * Compile:
 *   gcc -Wall -Wextra -O0 -g memlayout.c -o memlayout
 *
 * Run:
 *   ./memlayout
 *
 * (or run under gdb, or run then inspect /proc/<pid>/maps while it sleeps)
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

/* ---------------------------
   Global (initialized) -> Data segment
   --------------------------- */
int global_initialized = 42;
char global_str[] = "I am in the data segment";

/* ---------------------------
   Global (uninitialized) -> BSS segment
   --------------------------- */
int global_uninitialized; /* contains 0 at startup */


int main(void); 

/* ---------------------------
   Function (code) -> Text segment
   --------------------------- */
void my_function(void) {
    /* Just a small function so we can print its address */
    volatile int x = 1234; /* volatile to avoid optimization away */
    (void)x;
}

/* ---------------------------
   Helper: print addresses and labels
   --------------------------- */
void print_addresses(void) {
    int local_var = 7;           /* stack */
    char *heap_ptr = malloc(64); /* heap */

    if (!heap_ptr) {
        perror("malloc");
        exit(EXIT_FAILURE);
    }
    strcpy(heap_ptr, "Hello from the heap");

    printf("=== Addresses ===\n");
    printf("Address of function my_function (text) : %p\n", (void*)my_function);
    printf("Address of main (text)                 : %p\n", (void*)main);
    printf("Address of global_initialized (data)   : %p -> value=%d\n", (void*)&global_initialized, global_initialized);
    printf("Address of global_str (data)           : %p -> \"%s\"\n", (void*)global_str, global_str);
    printf("Address of global_uninitialized (bss)  : %p -> value=%d\n", (void*)&global_uninitialized, global_uninitialized);
    printf("Address of heap_ptr (heap)             : %p -> \"%s\"\n", (void*)heap_ptr, heap_ptr);
    printf("Address of local_var (stack)           : %p -> value=%d\n", (void*)&local_var, local_var);

    /* Also print approximate ranges by taking &local_var and &main as markers */
    printf("\nNote: Stack typically grows downward; heap grows upward.\n\n");

    free(heap_ptr);
}

/* ---------------------------
   Helper: print /proc/self/maps
   --------------------------- */
void print_proc_maps(void) {
    printf("=== /proc/self/maps ===\n");
    FILE *f = fopen("/proc/self/maps", "r");
    if (!f) {
        perror("fopen /proc/self/maps");
        return;
    }

    char line[512];
    while (fgets(line, sizeof(line), f)) {
        fputs(line, stdout);
    }
    fclose(f);
}

/* ---------------------------
   main
   --------------------------- */
int main(void) {
    printf("Process ID (PID): %d\n\n", getpid());

    print_addresses();

    /* Pause briefly so you can run external inspection commands (optional) */
    printf("\n(Program will now print /proc/self/maps and then sleep 60 seconds)\n\n");
    print_proc_maps();

    /* Sleep gives you a chance to inspect /proc/<pid>/maps or pmap externally */
    sleep(60);

    printf("\nExiting.\n");
    return 0;
}
