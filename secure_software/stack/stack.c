// file: regs.c
#include <stdio.h>

void foo(int a, int b) {
    int sum = a + b;
    printf("Sum = %d\n", sum);
}

int main() {
    foo(2, 3);
    return 0;
}
