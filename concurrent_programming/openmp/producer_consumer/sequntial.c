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
    double lower    = 0;
    double upper    = 10;
    int    slices   = 5;
    int    divisions = 1000;   // per slice

    double slice_width = (upper - lower) / slices;
    double total = 0;

    for (int i = 0; i < slices; i++) {
        Slice s;
        s.start     = lower + i * slice_width;
        s.end       = s.start + slice_width;
        s.divisions = divisions;

        total += calculate(s);
    }

    printf("Result: %f\n", total);
    return 0;
}