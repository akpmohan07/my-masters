#include <stdio.h>
#include <stdlib.h>

int answer = 0;

int
main(int argc, char *argv[])
{
  int x, y;

  if (argc != 3) {
    printf("Usage: %s int int\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  x = atoi(argv[1]);
  y = atoi(argv[2]);

  multiply(x, y);

  printf("Answer: %d\n", answer);

  return (0);
}
