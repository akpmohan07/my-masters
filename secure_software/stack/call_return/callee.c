#include <stdio.h>

int callee(int z) {
  return ++z;
}
/*
(gdb) disass callee
Dump of assembler code for function callee:
   0x08048372 <+0>:	push   %ebp
   0x08048373 <+1>:	mov    %esp,%ebp

   0x08048375 <+3>:	incl   0x8(%ebp)
   0x08048378 <+6>:	mov    0x8(%ebp),%eax

   0x0804837b <+9>:	leave  
   0x0804837c <+10>:	ret    
End of assembler dump.
*/


void caller() {
  int x = 0, y;
  y = callee(x);
}

/*
Dump of assembler code for function caller:
   0x0804837d <+0>:	push   %ebp 
   0x0804837e <+1>:	mov    %esp,%ebp

   0x08048380 <+3>:	sub    $0x8,%esp 
=> 0x08048383 <+6>:	movl   $0x0,-0x4(%ebp)
   0x0804838a <+13>:	pushl  -0x4(%ebp)
   0x0804838d <+16>:	call   0x8048372 <callee>
   0x08048392 <+21>:	add    $0x4,%esp
   0x08048395 <+24>:	mov    %eax,-0x8(%ebp)

   0x08048398 <+27>:	leave  
   0x08048399 <+28>:	ret    
End of assembler dump.
(gdb) 
*/


int main() {
  caller();
  return (0);
}

/*
Dump of assembler code for function main:
   0x0804839a <+0>:	push   %ebp
   0x0804839b <+1>:	mov    %esp,%ebp
   0x0804839d <+3>:	call   0x804837d <caller>
   0x080483a2 <+8>:	mov    $0x0,%eax
   0x080483a7 <+13>:	leave  
   0x080483a8 <+14>:	ret    
End of assembler dump.
*/


