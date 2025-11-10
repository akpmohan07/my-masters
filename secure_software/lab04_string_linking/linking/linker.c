int y = 5;
int main()
{
    int x = 1;
    x= increment(x);
    printf("x: %d\n", x);
    return (0);
}



/*
[root@ca647 linking]# objdump -r linker.o

linker.o:     file format elf32-i386

RELOCATION RECORDS FOR [.text]:
OFFSET   TYPE              VALUE 
00000011 R_386_PC32        increment
0000001f R_386_32          .rodata
00000024 R_386_PC32        printf


RELOCATION RECORDS FOR [.eh_frame]:
OFFSET   TYPE              VALUE 
00000020 R_386_PC32        .text


[root@ca647 linking]# objdump -d linker.o

linker.o:     file format elf32-i386


Disassembly of section .text:

00000000 <main>:
   0:	55                   	push   %ebp
   1:	89 e5                	mov    %esp,%ebp
   3:	83 ec 04             	sub    $0x4,%esp
   6:	c7 45 fc 01 00 00 00 	movl   $0x1,-0x4(%ebp)
   d:	ff 75 fc             	pushl  -0x4(%ebp)
  10:	e8 fc ff ff ff       	call   11 <main+0x11>
  15:	83 c4 04             	add    $0x4,%esp
  18:	89 45 fc             	mov    %eax,-0x4(%ebp)
  1b:	ff 75 fc             	pushl  -0x4(%ebp)
  1e:	68 00 00 00 00       	push   $0x0
  23:	e8 fc ff ff ff       	call   24 <main+0x24>
  28:	83 c4 08             	add    $0x8,%esp
  2b:	b8 00 00 00 00       	mov    $0x0,%eax
  30:	c9                   	leave  
  31:	c3                   	ret    
[root@ca647 linking]# 


*/