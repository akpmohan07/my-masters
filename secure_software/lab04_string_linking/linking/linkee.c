extern int y;
int *yp = &y;
int increment (int i) {
    return (i + *yp);
}


/*
[root@ca647 linking]# objdump -r linkee.o

linkee.o:     file format elf32-i386

RELOCATION RECORDS FOR [.text]:
OFFSET   TYPE              VALUE 
00000004 R_386_32          yp


RELOCATION RECORDS FOR [.data]:
OFFSET   TYPE              VALUE 
00000000 R_386_32          y


RELOCATION RECORDS FOR [.eh_frame]:
OFFSET   TYPE              VALUE 
00000020 R_386_PC32        .text


[root@ca647 linking]# objdump -d linkee.o

linkee.o:     file format elf32-i386


Disassembly of section .text:

00000000 <increment>:
   0:	55                   	push   %ebp
   1:	89 e5                	mov    %esp,%ebp
   3:	a1 00 00 00 00       	mov    0x0,%eax
   8:	8b 10                	mov    (%eax),%edx
   a:	8b 45 08             	mov    0x8(%ebp),%eax
   d:	01 d0                	add    %edx,%eax
   f:	5d                   	pop    %ebp
  10:	c3                   	ret    
[root@ca647 linking]# 
*/