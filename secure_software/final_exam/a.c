#include <stdio.h>
#include <string.h>
void
moo(short int x[]) {
short int *z = x;
for (int i = 0; i <= 1; i++) {
(*z) *= 3;
z++;
}
printf("F: %d\n", sizeof (x));
printf("G: %d\n", x[0]);
printf("H: %d\n", x[1]);
}
void
foo(void (*f)(short int [])) {
char const *a = "Rapunzel";
char b[] = "Gretel";
short int z[] = {1, 2, 3, 4};
printf("A: %d\n", sizeof (a));
printf("B: %d\n", sizeof (*a));
a++;
printf("C: %d\n", strlen (a));
printf("D: %d\n", sizeof (b));
b[sizeof (b) - 3] = '\0';
printf("E: %s\n", b);
f(&z[1]);
}
int main() {
foo(moo);
return (0);
}