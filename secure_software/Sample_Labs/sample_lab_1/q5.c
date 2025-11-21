#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>

void
foo() {
  unsigned int pps;
  unsigned int *pps_ptr = &pps;
  char name[32];

  printf("Enter your name:\n");
  scanf("%s", name);

  printf("Enter your PPS number:\n");
  scanf("%u", pps_ptr);

  printf("Name: %s\n", name);
  printf("PPS: %d\n", pps);  
}

int
main() {
  foo();

  return (0);
}
