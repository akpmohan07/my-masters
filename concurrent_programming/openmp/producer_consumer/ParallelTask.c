/*

gcc -Xpreprocessor -fopenmp \
  -I/opt/homebrew/opt/libomp/include \
  -L/opt/homebrew/opt/libomp/lib \
  -lomp -O3 -ffast-math -mcpu=apple-m1 -flto \
  -o part ParallelTask.c

./part
Result: 333.333330


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
    int divisions = 1000;

    double width = (high - low) / slices;

    double total = 0.0;

    #pragma omp parallel default(none) shared(total,low,high,width,slices,divisions)
    {
        #pragma omp single
        {
            for(int i=0; i<slices;i++){
                #pragma omp task firstprivate(i) shared(total,width,low,divisions)
                {
                    Slice s;
                    s.start = low + i * width;
                    s.end = s.start + width;
                    s.divisions = divisions;
                    double local = calculate(s);
                    
                    #pragma omp critical
                    total += local;
                }
            }
        }
    }

    printf("Result: %f\n", total);
}