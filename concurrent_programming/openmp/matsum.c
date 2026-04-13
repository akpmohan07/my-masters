#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

#define N 4

int main() {
    int matrixA[N][N];
    int matrixB[N][N];
    int sum[N][N];

    int n = 1;
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++) {
            matrixA[i][j] = n;
            matrixB[i][j] = n;
            n++;
        }

    int total_cells = N * N;
    int num_threads = 8;

    printf("========================================\n");
    printf("  Matrix Addition %dx%d\n", N, N);
    printf("  Total cells     : %d\n", total_cells);
    printf("  Threads         : %d\n", num_threads);
    printf("========================================\n\n");

    // -------------------------------------------------------
    // EXPERIMENT 1: schedule(static)
    // Chunk = total_cells / num_threads = 16/8 = 2 cells each
    // Assignment: Thread 0 → cells 0-1, Thread 1 → cells 2-3 ...
    // -------------------------------------------------------
    printf("--- schedule(static) ---\n");
    printf("Chunk size: %d | Each thread gets %d consecutive cells\n\n",
           total_cells / num_threads, total_cells / num_threads);
    #pragma omp parallel for collapse(2) schedule(static) num_threads(8)
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++) {
            sum[i][j] = matrixA[i][j] + matrixB[i][j];
            printf("Thread %-2d | C[%d][%d] = %4d | A[%d][%d] = %4d | B[%d][%d] = %4d\n",
                omp_get_thread_num(), i, j, sum[i][j], i, j, matrixA[i][j], i, j, matrixB[i][j]);
        }

    // -------------------------------------------------------
    // EXPERIMENT 2: schedule(static, 3)
    // Chunk = 3 cells at a time, round-robin across threads
    // Assignment: Thread 0 → cells 0-2, Thread 1 → cells 3-5 ...
    // Remaining cells assigned back to Thread 0 in next round
    // -------------------------------------------------------
    printf("\n--- schedule(static, 3) ---\n");
    printf("Chunk size: 3 | Threads get 3 cells each in round-robin\n");
    printf("16 cells / 3 = 5 full chunks + 1 leftover\n\n");
    #pragma omp parallel for collapse(2) schedule(static, 3) num_threads(8)
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++) {
            sum[i][j] = matrixA[i][j] + matrixB[i][j];
            printf("Thread %-2d | C[%d][%d] = %4d | A[%d][%d] = %4d | B[%d][%d] = %4d\n",
                omp_get_thread_num(), i, j, sum[i][j], i, j, matrixA[i][j], i, j, matrixB[i][j]);
        }

    // -------------------------------------------------------
    // EXPERIMENT 3: schedule(dynamic)
    // Chunk = 1 cell at a time (default)
    // Assignment: non-deterministic — first thread to finish picks next cell
    // Run multiple times — thread assignments will differ each run
    // -------------------------------------------------------
    printf("\n--- schedule(dynamic) ---\n");
    printf("Chunk size: 1 | Threads pick up next available cell when free\n");
    printf("Non-deterministic — run multiple times to see different assignments\n\n");
    #pragma omp parallel for collapse(2) schedule(dynamic) num_threads(8)
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++) {
            sum[i][j] = matrixA[i][j] + matrixB[i][j];
            printf("Thread %-2d | C[%d][%d] = %4d | A[%d][%d] = %4d | B[%d][%d] = %4d\n",
                omp_get_thread_num(), i, j, sum[i][j], i, j, matrixA[i][j], i, j, matrixB[i][j]);
        }

    // -------------------------------------------------------
    // EXPERIMENT 4: schedule(guided)
    // Chunk starts large (total_cells/num_threads) and shrinks
    // First chunks: ~2 cells | Later chunks: 1 cell
    // Good balance between overhead and load balancing
    // -------------------------------------------------------
    printf("\n--- schedule(guided) ---\n");
    printf("Chunk starts at %d cells and shrinks to 1\n", total_cells / num_threads);
    printf("Reduces scheduling overhead while adapting to load imbalance\n\n");
    #pragma omp parallel for collapse(2) schedule(guided) num_threads(8)
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++) {
            sum[i][j] = matrixA[i][j] + matrixB[i][j];
            printf("Thread %-2d | C[%d][%d] = %4d | A[%d][%d] = %4d | B[%d][%d] = %4d\n",
                omp_get_thread_num(), i, j, sum[i][j], i, j, matrixA[i][j], i, j, matrixB[i][j]);
        }

    // -------------------------------------------------------
    // EXPERIMENT 5: collapse(2) only — default static scheduling
    // Both loops parallelised — 16 cells distributed across 8 threads
    // Default: 2 cells per thread
    // -------------------------------------------------------
    printf("\n--- collapse(2) default ---\n");
    printf("Both loops collapsed: %d total cells across %d threads = %d cells each\n\n",
           total_cells, num_threads, total_cells / num_threads);
    #pragma omp parallel for collapse(2) num_threads(8)
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++) {
            sum[i][j] = matrixA[i][j] + matrixB[i][j];
            printf("Thread %-2d | C[%d][%d] = %4d | A[%d][%d] = %4d | B[%d][%d] = %4d\n",
                omp_get_thread_num(), i, j, sum[i][j], i, j, matrixA[i][j], i, j, matrixB[i][j]);
        }

    return 0;
}