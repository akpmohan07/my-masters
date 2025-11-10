/* A simple example to help understand process organisation and stack layout */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

/* Define BIG as 64K */
#define	BIG (1 << 16)

/* Declare some global data */
char big_array[BIG];
char buffer[BIG] = "I'm a string";
int z = 101;

/* Prototype */
int foo(void);

/* The recursive sumup function */
int
sumup(int x)
{
  int rval;

  /* Base case */
  if (x == 0) {
    return (0);
  }

  /* Recursive case */
  rval = sumup(x - 1);

  return (x + rval);
}

/* The swap function */
void
swap(int *x, int *y)
{
  int t;
  unsigned int *p = (unsigned int*)&t;

  t = *x;
  *x = *y;
  *y = t;

    /* Make p point to saved frame pointer */
  p++;

  /* Make p point to saved return address */
  p++;

  p++;

  /* Overwrite return address with address of foo */
  *p = (unsigned int)foo;
}

/* We will force execution of foo() on returning from swap */
int
foo(void)
{
  printf("You called foo()\n");
  exit(0);
}

/* Do a few bits and pieces */
int
main(int argc, char **argv)
{
  char local[128];
  int a, b;
  char *ptr;

  /* Check usage */
  if (argc != 1) {
    fprintf(stderr, "Usage: %s\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  /* Copy a string */
  strcpy(local, "I am a C program");

  /* Allocate some memory */
  ptr = (char *)malloc(BIG * sizeof (*ptr));

  /* Check allocation succeeded */
  if (ptr == NULL) {
    perror("malloc()");
    exit(EXIT_FAILURE);
  }

  /* Copy another string */
  strcpy(ptr, "I'm going on the heap");

  /* Free memory */
  free(ptr);

  /* Call some functions */
  a = 5;
  b = sumup(a);
  printf("a: %d, b:%d\n", a, b);
  swap(&a, &b);
  printf("a: %d, b:%d\n", a, b);

  return (0);
}
