#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <omp.h>

#define TOLERANCE  1e-6
#define BLOCK_SIZE 64

void generate_matrix(double *M, int n) {
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            M[i*n + j] = ((i * n + j) % 10000) / 100.0;
}

void naive_sequential(double *A, double *B, double *C, int n) {
    for (int i = 0; i < n; i++) {
        int i_offset = i * n;
        for (int k = 0; k < n; k++) {
            double a_value = A[i_offset + k];
            int k_offset = k * n;
            for (int j = 0; j < n; j++) {
                C[i_offset + j] += a_value * B[k_offset + j];
            }
        }
    }
}

void cell_parallel(double *A, double *B, double *C, int n, int threads, long *tasks) {
    long t = 0;
    #pragma omp parallel for collapse(2) num_threads(threads) reduction(+:t)
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            t++;
            double sum = 0.0;
            for (int k = 0; k < n; k++)
                sum += A[i*n + k] * B[k*n + j];
            C[i*n + j] = sum;
        }
    }
    *tasks = t;
}

void blocked_parallel(double *A, double *B, double *C, int n, int threads, long *tasks) {
    long t = 0;
    #pragma omp parallel for collapse(2) num_threads(threads) reduction(+:t)
    for (int i_block = 0; i_block < n; i_block += BLOCK_SIZE) {
        for (int j_block = 0; j_block < n; j_block += BLOCK_SIZE) {
            t++;
            int row_end = i_block + BLOCK_SIZE < n ? i_block + BLOCK_SIZE : n;
            int col_end = j_block + BLOCK_SIZE < n ? j_block + BLOCK_SIZE : n;
            for (int k_block = 0; k_block < n; k_block += BLOCK_SIZE) {
                int blk_end = k_block + BLOCK_SIZE < n ? k_block + BLOCK_SIZE : n;
                for (int i = i_block; i < row_end; i++) {
                    int i_offset = i * n;
                    for (int k = k_block; k < blk_end; k++) {
                        double a_value = A[i_offset + k];
                        int k_offset = k * n;
                        for (int j = j_block; j < col_end; j++) {
                            C[i_offset + j] += a_value * B[k_offset + j];
                        }
                    }
                }
            }
        }
    }
    *tasks = t;
}

int verify(double *C1, double *C2, int n) {
    for (int i = 0; i < n * n; i++)
        if (fabs(C1[i] - C2[i]) > TOLERANCE)
            return 0;
    return 1;
}

void print_header(FILE *f) {
    fprintf(f, "+---------------------+------------+--------+---------+------------+---------+-----------+----------------+\n");
    fprintf(f, "| %-19s | %-10s | %-6s | %-7s | %-10s | %-7s | %-9s | %-14s |\n",
            "Algorithm", "Type", "Size", "Threads", "Time (ms)", "Speedup", "Correct?", "Tasks Created");
    fprintf(f, "+---------------------+------------+--------+---------+------------+---------+-----------+----------------+\n");
}

void print_row(FILE *f, char *algo, char *type, int size, int threads,
               double time_ms, double speedup, int correct, long tasks) {
    fprintf(f, "| %-19s | %-10s | %-6d | %-7d | %-10.0f | %-7.2f | %-9s | %-14ld |\n",
            algo, type, size, threads, time_ms, speedup,
            correct ? "YES" : "NO", tasks);
}

int main() {
    int sizes[]         = {256, 512, 1024, 2048};
    int thread_counts[] = {2, 4, 6, 8};
    int num_sizes       = 4;
    int num_threads     = 4;

    // Warmup
    int wn = 64;
    double *Aw = malloc(wn*wn*sizeof(double));
    double *Bw = malloc(wn*wn*sizeof(double));
    double *Cw = calloc(wn*wn, sizeof(double));
    generate_matrix(Aw, wn); generate_matrix(Bw, wn);
    naive_sequential(Aw, Bw, Cw, wn);
    free(Aw); free(Bw); free(Cw);

    FILE *f = fopen("report_c.txt", "w");
    if (!f) { printf("Cannot open report_c.txt\n"); return 1; }

    fprintf(f,      "MATRIX MULTIPLICATION BENCHMARK RESULTS (C / OpenMP)\n");
    printf(         "MATRIX MULTIPLICATION BENCHMARK RESULTS (C / OpenMP)\n");
    print_header(f); print_header(stdout);

    for (int s = 0; s < num_sizes; s++) {
        int n = sizes[s];
        double *A         = malloc(n*n*sizeof(double));
        double *B         = malloc(n*n*sizeof(double));
        double *C_seq     = calloc(n*n, sizeof(double));
        double *C_cell    = calloc(n*n, sizeof(double));
        double *C_blk     = calloc(n*n, sizeof(double));

        generate_matrix(A, n); generate_matrix(B, n);

        double start = omp_get_wtime();
        naive_sequential(A, B, C_seq, n);
        double seq_time = (omp_get_wtime() - start) * 1000.0;

        print_row(f,      "NAIVE", "SEQUENTIAL", n, 1, seq_time, 1.00, 1, 1L);
        print_row(stdout, "NAIVE", "SEQUENTIAL", n, 1, seq_time, 1.00, 1, 1L);

        for (int t = 0; t < num_threads; t++) {
            int tc = thread_counts[t];
            long tasks = 0;
            double par_time, speedup;
            int correct;

            memset(C_cell, 0, n*n*sizeof(double));
            start = omp_get_wtime();
            cell_parallel(A, B, C_cell, n, tc, &tasks);
            par_time = (omp_get_wtime() - start) * 1000.0;
            speedup = seq_time / par_time;
            correct = verify(C_seq, C_cell, n);
            print_row(f,      "CELL", "PARALLEL", n, tc, par_time, speedup, correct, tasks);
            print_row(stdout, "CELL", "PARALLEL", n, tc, par_time, speedup, correct, tasks);

            memset(C_blk, 0, n*n*sizeof(double));
            start = omp_get_wtime();
            blocked_parallel(A, B, C_blk, n, tc, &tasks);
            par_time = (omp_get_wtime() - start) * 1000.0;
            speedup = seq_time / par_time;
            correct = verify(C_seq, C_blk, n);
            print_row(f,      "BLOCKED", "PARALLEL", n, tc, par_time, speedup, correct, tasks);
            print_row(stdout, "BLOCKED", "PARALLEL", n, tc, par_time, speedup, correct, tasks);
        }
        free(A); free(B); free(C_seq); free(C_cell); free(C_blk);
    }

    fprintf(f,      "+---------------------+------------+--------+---------+------------+---------+-----------+----------------+\n");
    printf(         "+---------------------+------------+--------+---------+------------+---------+-----------+----------------+\n");
    fclose(f);
    printf("Results written to report_c.txt\n");
    return 0;
}