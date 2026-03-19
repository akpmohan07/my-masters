# Secure Software — Subject Context

> **Module:** Secure Software Development | **Programme:** MSc Computing | **Student:** Mohankumar Muthusamy

## Overview

This module covers low-level systems security: C memory management, process address space, vulnerability exploitation (buffer overflows, format strings, integer bugs), and defence mechanisms. Assessment includes a **100-minute timed lab exam** using GDB on a Linux system.

## Folder Structure

```
secure_software/
├── format_string_attack.c                 # Format string vulnerability demo
├── lab01_multi_threading/                 # Lab 1: fork(), pthreads, race conditions
│   ├── race1.c                            #   Race condition without sync
│   ├── race2.c                            #   Stack variable lifetime issues
│   ├── race4.c                            #   Synchronised solution with mutexes
│   ├── LEARNING_SUMMARY.md                #   Lab writeup
│   └── ...
├── lab04_string_linking/                  # Lab 4: String ops, function hooking
│   ├── conc.c, concsol.c                  #   String concatenation exercises
│   ├── copy.c, copysol.c                  #   String copy exercises
│   ├── foo.c, bar.c                       #   Linking demo files
│   ├── hook.c, hooked.c                   #   Function hooking via LD_PRELOAD
│   ├── dy_link.c                          #   Dynamic linking demo
│   └── linking/                           #   Static linking exercise
│       ├── linker.c, linkee.c             #   Multi-file compilation
│       └── flow.txt                       #   Linking flow documentation
├── lab_env/                               # Lab environment setup
│   ├── Dockerfile                         #   Container for lab exercises
│   ├── utm_steps.txt                      #   VM setup instructions
│   └── aws/                               #   Cloud lab environment
├── Sample_Labs/                           # Exam preparation
│   ├── exam_workflow.md                   #   Exam day procedure
│   └── EXAM_PREPARATION_SUMMARY.md        #   Comprehensive study guide
├── exam/
│   └── Stack.md                           #   Stack frame analysis notes
└── test                                   # Test file
```

## Key Topics

### 1. C Compilation Pipeline
`.c` → preprocessing (cpp) → compilation (cc1) → assembly (as) → linking (ld) → executable. Understanding each stage is critical for exploit development.

### 2. Process Address Space (32-bit x86 Linux)
```
.text     — executable code (read-only)
.rodata   — read-only data, string literals, const globals
.data     — initialised global/static variables
.bss      — uninitialised global/statics (zero-filled)
heap      — malloc allocations (grows upward)
stack     — local variables, return addresses (grows downward)
```

**Critical memory placement rules:**
- `int x;` (global) → `.bss`
- `int x = 5;` (global) → `.data`
- `const int x = 5;` → `.rodata`
- String literals `"hello"` → `.rodata`
- Local arrays → stack
- `static` controls lifetime/scope; `const` controls mutability (independent properties)

### 3. Stack Frames & Calling Convention (cdecl, 32-bit)
Stack grows downward. Frame layout: arguments → return address → saved EBP → local variables. Key registers: `eip` (instruction pointer/return address), `ebp` (frame pointer), `esp` (stack pointer). `lea` = load address; `mov` = load value.

### 4. Vulnerability Classes

**Buffer Overflow:** Writing past array bounds to overwrite return address or adjacent data. Exploited to redirect execution flow.

**Format String Attacks:** Passing user input directly as format string to `printf(user_input)` allows reading/writing arbitrary memory via `%x`, `%n` specifiers.

**Integer Overflow/Truncation:** Two's complement wraparound, integer promotion (char/short → int), truncation (keep lowest N bits). Can bypass bounds checks leading to buffer overflows.

**Race Conditions:** Shared memory with non-atomic operations. Demonstrated in lab1 with pthreads — unsynchronised counter increments produce incorrect results.

### 5. Defence Mechanisms
- **Stack canaries** — detect buffer overflow before return
- **NX/DEP** — non-executable stack/heap
- **ASLR** — randomise address space layout
- **PIE** — position-independent executables
- **Fortify Source** — compile-time buffer overflow checks

### 6. Linux Security
File permissions: directory `x` = enter, `w` = create/delete entries. `setuid`/`setgid` bits for privilege escalation. umask: new file mode = 0666 − umask. Hard links (same inode) vs symlinks (own inode, store path).

### 7. Multi-threading (Lab 1)
`fork()` for process creation, `pthread_create()` for threads, race conditions, mutex synchronisation. race1.c demonstrates unsynchronised shared counter; race4.c shows mutex-based fix.

### 8. Linking & Hooking (Lab 4)
Static vs dynamic linking. Function hooking via `LD_PRELOAD` to intercept library calls. Multi-file compilation with separate compilation units.

## Lab Exam Format

**Duration:** 100 minutes, 15% of grade. **Section A (40 marks):** Stack frame analysis using GDB — disassemble functions, identify variables, trace execution. **Section B (60 marks):** Exploit development — buffer overflow, array overflow, format string, stack protection bypass.

**GDB commands needed:** `info frame`, `disassemble`, `x/bx`, `p/d`, `break`, `run`, `next`, `step`.

**Common exam traps:** Don't assume `const` implies static lifetime. Don't forget `.bss` is zero-initialised. Don't reverse bits instead of bytes for endianness. Don't count unused stack space as variables.

---

*Related: [MASTER.md](./MASTER.md)*
