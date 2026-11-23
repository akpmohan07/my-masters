#include <stdio.h>

void
foo(int z[])
{
  printf("%d\n", sizeof (z));
  printf("%d\n", z[0]);
  printf("%d\n", *(z+2));
  printf("%p\n", (void *)&z);
  printf("%p\n", (void *)z);
  printf("%p\n", (void *)&z[0]);
  printf("%p\n", (void *)&z[1]);
  printf("%p\n", (void *)(z+1));
}

void
bar(int z[3])
{
  printf("%d\n", sizeof (z));
  printf("%d\n", z[0]);
  printf("%p\n", (void *)&z);
  printf("%p\n", (void *)z);
}

void
moo(int *z)
{
  printf("%d\n", sizeof (z));
  printf("%d\n", z[0]);
  printf("%p\n", (void *)&z);
  printf("%p\n", (void *)z);
}

int
main()
{
  int a[] = {7, 8, 9};
  int *p = a;
  int *z = a;
  int i;

  for (i = 0; i < sizeof (a) / sizeof (*a); i++) {
    printf("%d\n", a[i]);
    printf("%d\n", *(a + i));
    printf("%d\n", p[i]);
    printf("%d\n", *(p + i));
    printf("%d\n", *z);
    z++;
  }

  foo(a); moo(a); bar(a);
  bar(a); bar(&a[0]); bar((int *)&a);

  printf("%d\n", sizeof (a));
  printf("%d\n", sizeof (*a));
  printf("%p\n", (void *)a); 
  printf("%p\n", (void *)&a); 
  printf("%p\n", (void *)p); 
  printf("%p\n", (void *)&p);
  printf("%d\n", sizeof (p));
  printf("%d\n", sizeof (*p));
}
