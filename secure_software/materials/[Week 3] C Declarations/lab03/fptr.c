#include <stdio.h>

/*
 * Write the apply_f() function. It should apply the f() function below to
 * its parameters and return the result.
 */

int
f(int *a, int *b)
{
  return (*a) + (*b);
}

int
main()
{
  int x = 5;
  int y = 7;
  int z;

  z = f(&x, &y);
  printf("%d\n", z);
  
  z = apply_f(&x, &y, f);
  printf("%d\n", z);

  return (0);
}

/*
 * Correct output:
 *
 * gcc -Wall -pedantic -o fptr fptr.c
 * ./fptr
 * 12
 * 12
 */
