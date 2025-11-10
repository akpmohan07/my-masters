#include <stdio.h>

int inc(int b) {
  return b++;
}

int main() {
  int a = 0;
  a = inc(a);
  printf("%d", a);
  return (0);
}


/*
Dump of assembler code for function inc:
   0x080483a2 <+0>:	push   %ebp
   0x080483a3 <+1>:	mov    %esp,%ebp
   0x080483a5 <+3>:	incl   0x8(%ebp)
   0x080483a8 <+6>:	mov    0x8(%ebp),%eax
   0x080483ab <+9>:	leave  
   0x080483ac <+10>:	ret    
End of assembler dump.
*/



/*
b++:
   0x080483a5 <+3>:	mov    0x8(%ebp),%eax
   0x080483a8 <+6>:	incl   0x8(%ebp)

++b:
   0x080483a5 <+3>:	incl   0x8(%ebp)
   0x080483a8 <+6>:	mov    0x8(%ebp),%eax
*/
