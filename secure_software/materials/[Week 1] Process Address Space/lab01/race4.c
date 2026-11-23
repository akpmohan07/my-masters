#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

int LIMIT = 1000000;

void inc(int *n) { (*n)++; }
void dec(int *n) { (*n)--; }

struct combo_t { int *n; void (*f)(int *); };

void *
updater(void *c_in)
{
  struct combo_t *c = (struct combo_t *)c_in;
  int i;

  for (i = 0; i < LIMIT; i++) {
    c->f(c->n);
  }
  return (0);
}

int
main()
{
  int n;
  pthread_t i, d;
  struct combo_t p, c;

  n = 0;
  p.n = c.n = &n;
  p.f = inc;
  c.f = dec;

  pthread_create(&i, NULL, updater, (void *)&p);
  pthread_create(&d, NULL, updater, (void *)&c);
  pthread_join(i, NULL);
  pthread_join(d, NULL);

  printf("Result: %d\n", n);

  return (0);
}
