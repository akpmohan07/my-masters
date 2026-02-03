Below is a **high-yield, exam-focused Secure Programming revision summary**, written the way examiners expect answers. It integrates **lectures, labs, and recurring past-exam patterns** and prioritises **C on Linux**.

---

# Secure Programming — Final Revision (Night-Before Version)

## What the exam usually assumes

* **Architecture:** **32-bit x86 Linux** is assumed **most often** (simpler stack layout, classic exploits).
* **64-bit x86-64 Linux** appears for comparison or modern defences.
* **Language:** C
* **OS:** Linux (ELF binaries, gcc, gdb)

If not specified → **assume 32-bit** and say so.

---

## 1) C Compilation & Toolchain (exam favourite)

### Pipeline (know order + purpose)

```
.c ──> cpp ──> cc1 ──> as ──> ld ──> executable
```

* **Preprocessing (`cpp`)**: `#include`, `#define`, remove comments
* **Compilation (`cc1`)**: C → assembly (where stack frames appear)
* **Assembly (`as`)**: assembly → object (`.o`)
* **Linking (`ld`)**: resolve symbols, layout sections

**GCC** = *GNU Compiler Collection* (driver orchestrating all stages)

**Exam trap:** Saying “gcc compiles C directly to binary” ❌

---

## 2) Process Address Space (very high yield)

### Typical layout (low → high addresses)

```
.text      (code, RX)
.rodata    (read-only data, R)
.data      (initialized globals/statics, RW)
.bss       (uninitialized globals/statics, RW, zeroed)
heap       (malloc, grows ↑)
stack      (locals, return addr, grows ↓)
```

### Where things go (must memorise)

| Declaration                        | Location                   |
| ---------------------------------- | -------------------------- |
| `int x;` (global/static)           | `.bss`                     |
| `int x = 5;` (global/static)       | `.data`                    |
| `const int x = 5;` (global/static) | `.rodata`                  |
| `char *p = "hi";`                  | `.rodata` (string literal) |
| `char a[] = "hi";` (local)         | stack                      |
| `static int x;` (local)            | `.bss`                     |
| `static int x = 5;` (local)        | `.data`                    |

**Common mistake:** Thinking `const` ⇒ stack or static lifetime ❌
**Rule:** `const` = *read-only*, `static` = *lifetime/scope*.

---

## 3) 32-bit vs 64-bit Linux (compare when relevant)

| Feature            | 32-bit x86 | 64-bit x86-64          |
| ------------------ | ---------- | ---------------------- |
| Pointer size       | 4 bytes    | 8 bytes                |
| Virtual addr space | 4 GB       | Huge (48 bits typical) |
| Stack args         | On stack   | Mostly registers       |
| Calling convention | cdecl      | SysV AMD64             |
| Exploits           | Easier     | Harder (more defences) |
| Exam default       | ✅ Yes      | Sometimes              |

**Exam tip:** If exploiting stack overflow → **32-bit assumed** unless stated.

---

## 4) Endianness (always tested)

* **Little-endian (x86):** least significant byte first
* **Big-endian:** most significant byte first
* **Network byte order:** big-endian

Example: `0x12345678`

* Little-endian in memory: `78 56 34 12`
* Big-endian in memory: `12 34 56 78`

**Trap:** Reversing *bits* instead of *bytes* ❌

---

## 5) Hex ↔ Binary (quick marks)

* **Rule:** 1 hex digit = **4 bits**
* **Binary → Hex:** group from right in 4s, pad left with zeros
* **Hex → Binary:** replace each digit with 4 bits

**Memory aid:** 8-4-2-1 bit weights (no table needed)

---

## 6) Virtual vs Physical Memory, MMU & PAE (confusing topic)

### MMU

* Hardware that translates **virtual → physical** addresses
* Enforces permissions (R/W/X), isolation → segfaults

### PAE (32-bit only)

* **Physical Address Extension**
* Virtual addresses stay **32-bit**
* Physical addresses become **36-bit** (up to 64 GB RAM)
* Each process still sees **4 GB** (window model)

**Exam phrasing to use:**

> “PAE increases physical memory available to the OS, not per-process virtual address space.”

---

## 7) Strings, `const`, `static` (frequent confusion)

```c
char *p = "hi";        // .rodata (do NOT write)
char a[] = "hi";       // stack or .data (writable)
static char a[]="hi";  // .data (writable)
static const char s[]="hi"; // .rodata
```

**Common bug (tested conceptually):**

```c
char *p = "hi";
p[0] = 'H';   // ❌ segfault (write to .rodata)
```

---

## 8) Vulnerabilities (core of the module)

### Buffer Overflows

* Stack overflows overwrite **return address**
* Heap overflows corrupt **metadata / objects**
* Triggered by unsafe functions: `gets`, `strcpy`, `sprintf`

### Format String Bugs

* User input as format string: `printf(user)`
* Can read/write memory via `%x`, `%n`

### Integer Issues

* Overflow, truncation, sign errors
* Often lead to buffer mis-sizes → exploitable

---

## 9) Defences (name + what they stop)

| Defence        | What it does              |
| -------------- | ------------------------- |
| Stack canaries | Detect stack overwrite    |
| NX / DEP       | Non-executable stack/heap |
| ASLR           | Randomise addresses       |
| PIE            | Randomise main executable |
| RELRO          | Protect GOT               |
| Fortify Source | Safer libc checks         |

**Lab reality:** Defences often **disabled** to demonstrate exploits.

---

## 10) Tools you’re expected to recognise/use

* **gcc**: compile with/without defences
* **gdb**: inspect stack, registers, memory
* **objdump / readelf**: sections, symbols
* **VirtualBox VM**: controlled Linux environment

---

# Common Examiner Traps (lose easy marks)

* Mixing **virtual** and **physical** memory
* Saying `const` implies static lifetime
* Forgetting `.bss` is **zero-initialised**
* Wrong endian byte order
* Not stating assumed architecture

---

# One-Page Exam Mental Checklist

**Before answering:**

* What architecture? (assume 32-bit unless stated)
* What section of memory?
* Stack vs heap vs data?

**When explaining memory:**

* Name the segment
* Say why it’s there
* Mention permissions (R/W/X)

**When exploits appear:**

* Identify vulnerability class
* Say what is overwritten
* Mention defence (even if disabled)

---

# Things to Remember Under Pressure

* `.bss` = uninitialised, zeroed
* `static` ≠ `const`
* Strings literals → `.rodata`
* Little-endian = reverse **bytes**
* 4 bits per hex digit
* PAE ≠ bigger process memory
* If unsure → explain clearly, not briefly

---

If you want, I can also give:

* **Model answers** to typical 10-mark questions
* A **last-minute diagram sheet**
* A **“what to write for full marks” template**
