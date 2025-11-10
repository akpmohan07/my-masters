#include <stdio.h>

int inc(int *b) {
  return ++(*b);
}

int main() {
  int a = 0, b =1;
  int c = 3;
  a = inc(&a);
  printf("%d", a);
  return (0);
}

/*
Dump of assembler code for function inc:
   0x080483a2 <+0>:	push   %ebp
   0x080483a3 <+1>:	mov    %esp,%ebp
   0x080483a5 <+3>:	mov    0x8(%ebp),%eax
   0x080483a8 <+6>:	incl   (%eax)
   0x080483aa <+8>:	mov    (%eax),%eax
   0x080483ac <+10>:	leave  
   0x080483ad <+11>:	ret    
End of assembler dump.
*/



/*
Direct Sum:
   0x080483a5 <+3>:	incl   0x8(%ebp)
   0x080483a8 <+6>:	mov    0x8(%ebp),%eax


Point Sum:
   0x080483a5 <+3>:	mov    0x8(%ebp),%eax
   0x080483a8 <+6>:	incl   (%eax)
   0x080483aa <+8>:	mov    (%eax),%eax

*/