#include<stdio.h>

int a[] = {7, 8, 9};
int *p = a;
int *z = a;

void foo(int z[])
{
  printf("%d\n", sizeof (z));
  /*
4
7 
9
0x4
0x4
0x4
0x8
0x8 */
  printf("%d\n", z[0]);
  printf("%d\n", *(z+2));
  printf("%p\n", (void *)&z);
  printf("%p\n", (void *)z);
  printf("%p\n", (void *)&z[0]);
  printf("%p\n", (void *)&z[1]);
  printf("%p\n", (void *)(z+1));
}


int main()
{
//   for (int i = 0; i < sizeof (a) / sizeof (*a); i++) {
//     printf("%d\n", a[i]);
//     printf("%d\n", *(a + i));
//     printf("%d\n", p[i]);
//     printf("%d\n", *(p + i));
//     printf("%d\n", *z);
//     z++;
//   }

  foo(a);
  return 0;
}