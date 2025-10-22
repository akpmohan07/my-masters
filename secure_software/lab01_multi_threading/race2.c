#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

struct pair_t { int a, b; };

void *
add(void *p_in)
{
  struct pair_t *p = (struct pair_t *)p_in;
  
  printf("Answer is: %d\n", p->a + p->b);
  
  return (0);
}

void
adder(int x, int y) {
  struct pair_t p;
  pthread_t t;

  p.a = x;
  p.b = y;
  pthread_create(&t, NULL, add, (void *)&p);
  pthread_join(t, NULL);
}

int
main()
{
  printf("Submitting your sum for calculation...\n");

  adder(2, 3);

  pthread_exit(NULL);

  return (0);
}
