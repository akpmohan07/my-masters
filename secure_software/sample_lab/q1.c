/* 
 * Print the number of moves required to solve the Towers of Hanoi e.g.
 *
 * $ ./q1 5
 * Moves required: 31
 */
#include <stdlib.h>
#include <stdio.h>

int
hanoi(int n) {
  if (n == 1) {
    /* Point X */
    return (1);
  }

  return (hanoi(n - 1) * 2 + 1);
}

int
check(int n) {
  if (n <= 0) {
    return (-1);
  }

  return (hanoi(n));
}

void
error(char *s) {
  printf("Usage: %s n (where n is a positive integer)\n", s);
}

int
main(int argc, char *argv[]) {
  int n, moves;

  if (argc != 2) {
    error(argv[0]);
    return (-1);
  }

  n = strtol(argv[1], NULL, 10);

  moves = check(n);

  if (moves < 0) {
    error(argv[0]);
    return (-1);
  }

  printf("Moves required: %d\n", moves);

  return (0);
}
