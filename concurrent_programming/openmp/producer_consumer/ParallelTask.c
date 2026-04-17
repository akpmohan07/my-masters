/*

gcc -Xpreprocessor -fopenmp \
  -I/opt/homebrew/opt/libomp/include \
  -L/opt/homebrew/opt/libomp/lib \
  -lomp -O3 -ffast-math -mcpu=apple-m1 -flto \
  -o part ParallelTask.c

./part
Result: 333.333330


Without using the Producer-Consumer model, write an OpenMP program that uses
tasks to processes all of the elements of a list concurrently. Clearly discribe the behaviour
of your code. Outline an alternative solution that uses the Producer-Consumer
model, indicating how you would ensure an orderly termination of all Producers and
Consumers.
[

*/


#include <stdio.h>
#include <math.h>

// The function we're integrating — f(x) = x²
double f(double x) {
    return x * x;
}

typedef struct {
    double start;
    double end;
    int divisions;
} Slice;

// Calculate area for one slice
double calculate(Slice slice) {
    double width = (slice.end - slice.start) / slice.divisions;
    double total = 0;

    for (int i = 0; i < slice.divisions; i++) {
        double mid = slice.start + i * width + width / 2;
        total += f(mid) * width;
    }
    return total;
}

int main() {
    double low = 0;
    double high = 10;
    int slices = 5;


    double width = (high - low) / slices;

    int divisions = 1000;
    Slice slices_array[slices];
    for (int i = 0; i < slices; i++) {
        slices_array[i].start     = low + i * width;
        slices_array[i].end       = slices_array[i].start + width;
        slices_array[i].divisions = divisions;
    }

    double total = 0.0;

    // Default -> Paralall
    #pragma omp parallel default(none) shared(total,low,high,width,slices,divisions,slices_array)
    {
        // Single -> Main thread single exection
        for (int i = 0; i < slices; i++) {
            #pragma omp task shared(total, slices_array) firstprivate(i)
            {
                double local = calculate(slices_array[i]);

                #pragma omp critical
                total += local;
            }
        }
    }

    printf("Result: %f\n", total);
}