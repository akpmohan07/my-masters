#include <stdlib.h>
#include <stdio.h>

enum { RANGE = 512 };

void
foo() {
  printf("What a catastrophe!\n");
}

int
main() {
  int index, value;
  int values[RANGE];

  for (;;) {

      printf("Enter index and value:\n");

      fscanf(stdin, "%d %d", &index, &value);

      if (index < 0) {
	  return (0);
      }

      values[index] = value;
  }

  return (0);
}
