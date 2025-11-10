#include <stdio.h>
#include <stdint.h>
#include <arpa/inet.h>

int
main() {
  uint32_t a = 0xfffeaabb, b;
  uint8_t *p, *q;

  b = htons(a); /* Convert to big-endian */

  p = (uint8_t *)&a;
  q = (uint8_t *)&b;

  printf("%x\n", a);
  printf("%x\n", htonl(a));

  printf("0x%x, 0x%x\n", *p, *q);

  return (0);
}

/*
Output:
[student@ca647 call_return]$ ./indian 
fffeaabb
bbaafeff
0xbb, 0xaa
*/