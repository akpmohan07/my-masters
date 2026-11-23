#include <stdio.h>

int
main()
{
  char c;
  unsigned char uc;
  signed char sc;

  printf("%d\n", sizeof (c));
  printf("%d\n", sizeof (uc));
  printf("%d\n", sizeof (sc));

  int i;
  unsigned int ui;
  signed int si;
  
  printf("%d\n", sizeof (i));
  printf("%d\n", sizeof (ui));
  printf("%d\n", sizeof (si));

  short s;
  unsigned short us;
  signed short ss;
  
  printf("%d\n", sizeof (s));
  printf("%d\n", sizeof (us));
  printf("%d\n", sizeof (ss));

  long int li;
  unsigned long int uli;
  signed long int sli;
  
  printf("%d\n", sizeof (li));
  printf("%d\n", sizeof (uli));
  printf("%d\n", sizeof (sli));

  long long int lli;
  unsigned long long int ulli;
  signed long long slli;
  
  printf("%d\n", sizeof (lli));
  printf("%d\n", sizeof (ulli));
  printf("%d\n", sizeof (slli));

  float f;
  
  printf("%d\n", sizeof (f));

  double d;
  long double ld;
  
  printf("%d\n", sizeof (d));
  printf("%d\n", sizeof (ld));

  void *v;
  
  printf("%d\n", sizeof (v));

  return (0);
}
