#include<stdio.h>

int main(){
    puts("Hello ");
    puts("World!");
}

/*
[root@ca647 lab04_string_linking]# gdb dylink 
GNU gdb (GDB) Fedora 7.11.1-86.fc24
Copyright (C) 2016 Free Software Foundation, Inc.
License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.  Type "show copying"
and "show warranty" for details.
This GDB was configured as "i686-redhat-linux-gnu".
Type "show configuration" for configuration details.
For bug reporting instructions, please see:
<http://www.gnu.org/software/gdb/bugs/>.
Find the GDB manual and other documentation resources online at:
<http://www.gnu.org/software/gdb/documentation/>.
For help, type "help".
Type "apropos word" to search for commands related to "word"...
Reading symbols from dylink...done.
(gdb) info file
Symbols from "/mnt/utm/secure_software/lab04_string_linking/dylink".
Local exec file:
	`/mnt/utm/secure_software/lab04_string_linking/dylink', file type elf32-i386.
	Entry point: 0x8048310
	0x08048154 - 0x08048167 is .interp
	0x08048168 - 0x08048188 is .note.ABI-tag
	0x08048188 - 0x080481ac is .note.gnu.build-id
	0x080481ac - 0x080481cc is .gnu.hash
	0x080481cc - 0x0804821c is .dynsym
	0x0804821c - 0x08048266 is .dynstr
	0x08048266 - 0x08048270 is .gnu.version
	0x08048270 - 0x08048290 is .gnu.version_r
	0x08048290 - 0x08048298 is .rel.dyn
	0x08048298 - 0x080482a8 is .rel.plt
	0x080482a8 - 0x080482cb is .init
	0x080482d0 - 0x08048300 is .plt
	0x08048300 - 0x08048308 is .plt.got
	0x08048310 - 0x080484a2 is .text
	0x080484a4 - 0x080484b8 is .fini
	0x080484b8 - 0x080484d2 is .rodata
	0x080484d4 - 0x08048500 is .eh_frame_hdr
	0x08048500 - 0x080485c0 is .eh_frame
	0x08049f08 - 0x08049f0c is .init_array
	0x08049f0c - 0x08049f10 is .fini_array
	0x08049f10 - 0x08049f14 is .jcr
	0x08049f14 - 0x08049ffc is .dynamic
	0x08049ffc - 0x0804a000 is .got
	0x0804a000 - 0x0804a014 is .got.plt
	0x0804a014 - 0x0804a018 is .data
	0x0804a018 - 0x0804a01c is .bss
(gdb) disass 0x080482d0 0x08048300
A syntax error in expression, near `0x08048300'.
(gdb) disass 0x080482d0  0x08048300
A syntax error in expression, near `0x08048300'.
(gdb) disass 0x080482d0, 0x08048300
Dump of assembler code from 0x80482d0 to 0x8048300:
   0x080482d0:	pushl  0x804a004
   0x080482d6:	jmp    *0x804a008
   0x080482dc:	add    %al,(%eax)
   0x080482de:	add    %al,(%eax)
   0x080482e0 <puts@plt+0>:	jmp    *0x804a00c
   0x080482e6 <puts@plt+6>:	push   $0x0
   0x080482eb <puts@plt+11>:	jmp    0x80482d0
   0x080482f0 <__libc_start_main@plt+0>:	jmp    *0x804a010
   0x080482f6 <__libc_start_main@plt+6>:	push   $0x8
   0x080482fb <__libc_start_main@plt+11>:	jmp    0x80482d0
End of assembler dump.
(gdb) disass main
Dump of assembler code for function main:
   0x0804841b <+0>:	push   %ebp
   0x0804841c <+1>:	mov    %esp,%ebp
   0x0804841e <+3>:	push   $0x80484c4
   0x08048423 <+8>:	call   0x80482e0 <puts@plt>
   0x08048428 <+13>:	add    $0x4,%esp
   0x0804842b <+16>:	push   $0x80484cb
   0x08048430 <+21>:	call   0x80482e0 <puts@plt>
   0x08048435 <+26>:	add    $0x4,%esp
   0x08048438 <+29>:	mov    $0x0,%eax
   0x0804843d <+34>:	leave  
   0x0804843e <+35>:	ret    
End of assembler dump.
(gdb)   x/a 0x804a00c
0x804a00c:	0x80482e6 <puts@plt+6>
(gdb) x/a 0x80482d0
0x80482d0:	0xa00435ff
(gdb) break main
Breakpoint 1 at 0x804841e: file dy_link.c, line 4.
(gdb) run
Starting program: /mnt/utm/secure_software/lab04_string_linking/dylink 
Missing separate debuginfos, use: dnf debuginfo-install glibc-2.23.1-12.fc24.i686

Breakpoint 1, main () at dy_link.c:4
4	    puts("Hello ");
(gdb) x/a 0x80482d0
0x80482d0:	0xa00435ff
(gdb) x/a 0x80482d0
0x80482d0:	0xa00435ff
(gdb)   x/a 0x804a00c
0x804a00c:	0x80482e6 <puts@plt+6>
(gdb) step
Hello 
5	    puts("World!");
(gdb)   x/a 0x804a00c
0x804a00c:	0xb7e55880
(gdb)  info sympol puts
Undefined info command: "sympol puts".  Try "help info".
(gdb)  info symbol puts
puts in section .text of /lib/libc.so.6
(gdb)  info addr  puts
Symbol "puts" is at 0xb7e55880 in a file compiled without debugging.
(gdb) 

*/