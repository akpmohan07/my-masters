#include <stdio.h>

int main(void) {
    char buf[200];
    fgets(buf, sizeof(buf), stdin); // user input
    printf(buf); // DANGEROUS: user controls the format string
    return 0;
}
