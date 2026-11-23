#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

int LIMIT = 1000000;

void *
inc(void *n_in)
{
  int *n = (int *)n_in;
  int i;

  for (i = 0; i < LIMIT; i++) {
    *n += 1;
  }

  return (0);
}

void *
dec(void *n_in)
{
  int *n = (int *)n_in;
  int i;

  for (i = 0; i < LIMIT; i++) {
    *n -= 1;
  }

  return (0);
}

int
main()
{
  int n;
  pthread_t i, d;

  n = 0;

  pthread_create(&i, NULL, inc, (void *)&n);
  pthread_create(&d, NULL, dec, (void *)&n);
  pthread_join(i, NULL);
  pthread_join(d, NULL);

  printf("Result: %d\n", n);

  return (0);
}
