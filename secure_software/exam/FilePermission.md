Below is a **concise, exam-focused Secure Programming final revision summary**, written exactly in the style that **maximises marks under time pressure**. It integrates **lecture theory, labs, and repeated past-exam patterns** from our discussion.

---

# Secure Programming (C on Linux) – Final Revision Summary

> **Examiner mindset:**
> Marks are awarded for **correct mental models**, **precise wording**, **clear assumptions**, and **avoiding classic traps**.
> Most questions assume **32-bit x86 Linux**, unless explicitly stated otherwise.

---

## 1. Architectures the Exam Assumes (CRITICAL)

| Aspect             | 32-bit x86 Linux (MOST COMMON IN EXAMS) | 64-bit x86-64 Linux |
| ------------------ | --------------------------------------- | ------------------- |
| Pointer size       | 4 bytes                                 | 8 bytes             |
| `sizeof(int)`      | 4                                       | 4                   |
| Stack slot         | 4 bytes                                 | 8 bytes             |
| Calling convention | stack-based args                        | registers + stack   |
| `sizeof(ptr)`      | 4                                       | 8                   |

**Exam tip:**
If not stated → **assume 32-bit**. State this explicitly for marks.

---

## 2. Stack Frames & Memory Layout (High-Frequency Topic)

### Stack facts (x86):

* Stack grows **downwards**
* Each `push` = `ESP -= word_size`
* **Stack frame starts at the return address (RA)**, not arguments

### Typical 32-bit stack layout

```
higher addresses
arg n
arg 2
arg 1
RET ADDR   ← start of callee frame
saved EBP
local vars
lower addresses
```

**Exam trap:**
Arguments are **not part of the callee’s frame**, even though accessed via `EBP`.

---

## 3. Arrays vs Pointers (VERY COMMON EXAM TRAP)

### Core rule (memorise):

> **Arrays own memory. Pointers do not.**

| Declaration              | `sizeof(x)`             |
| ------------------------ | ----------------------- |
| `int x[10];`             | 40                      |
| `int *x;`                | 4 (32-bit) / 8 (64-bit) |
| function param `int x[]` | pointer size            |

**Important:**
Array-to-pointer decay **does NOT happen inside `sizeof`**.

### String version (often tested):

```c
char s[] = "ABC";   // sizeof(s) = 4
char *s = "ABC";    // sizeof(s) = 4 (ptr), NOT string
```

**Classic vulnerability:**

```c
memset(buf, 0, sizeof(buf)); // WRONG if buf is char*
```

---

## 4. Integer Conversion & Truncation (C Semantics)

### Key rules:

* Assigning to smaller type → **low bits kept**
* Signedness changes interpretation, not bits

Example:

```c
unsigned short x = 0xFFFFFFFF;  // → 0xFFFF
signed char y = x;              // → 0xFF → -1
```

**Examiner wants:**
Correct bit-level reasoning + final decimal output.

---

## 5. `sizeof` vs `strlen`

| Expression   | Meaning                  |
| ------------ | ------------------------ |
| `sizeof(x)`  | compile-time size        |
| `strlen(s)`  | runtime count until `\0` |
| `sizeof(*x)` | size of one element      |

**Exam trap:**
You **cannot** get total buffer size from a pointer alone.

---

## 6. `lea` vs `mov` (Assembly Questions)

```asm
mov -0x4(%ebp), %eax   ; load VALUE
lea -0x4(%ebp), %eax   ; load ADDRESS
```

**Mental model:**
`lea` = “address arithmetic”, **no memory access**.

Exam pattern:

```asm
lea -0x4(%ebp), %eax
incl (%eax)
```

→ `local++`

---

## 7. `++x` vs `x++` (C + Assembly)

| Case           | Assembly difference |
| -------------- | ------------------- |
| Value not used | SAME (`incl`)       |
| Prefix `++x`   | increment → use     |
| Postfix `x++`  | save → increment    |

**Exam tip:**
If result unused → no difference.

---

## 8. Linux Permissions & Execution

### Files

* `x` required to execute
* `s` = setuid / setgid
* `t` on **files** → ignored

### Directories

| Permission | Meaning               |
| ---------- | --------------------- |
| `r`        | list names            |
| `w`        | create/delete entries |
| `x`        | **enter (`cd`)**      |

**Classic trap:**
`chmod 655 dir` → **cannot `cd`** (no `x` for owner).

---

## 9. setuid / setgid (Exam Favourite)

### setgid executable

```bash
chown tom:mect moo
chmod 2755 moo
```

Effect:

* Effective GID = `mect`
* Real GID unchanged

**Examiner expects:**

* correct ownership
* correct bit
* assumption: root access

---

## 10. umask (Exact Wording Matters)

New file mode = `666 − umask`

| Requirement            | Correct umask |
| ---------------------- | ------------- |
| readable only by owner | `266` → `400` |
| read/write only owner  | `066` → `600` |

**Key insight:**
File creation depends on **directory permissions**, not file write permission.

---

## 11. Links (VERY COMMON DIAGRAM QUESTION)

### Hard link

* Same inode
* Increases link count
* Survives deletion of other names

### Soft (symbolic) link

* Own inode
* Stores path
* Can dangle

**Critical rule (often tested):**

> Hard-linking a symlink links to the **symlink inode**, not the target.

Example:

```bash
ln -s dog bunny
ln bunny duck
rm dog
```

→ `bunny` and `duck` both dangling

---

## 12. Deletion: Hard vs Soft (1-liner exam gold)

> Deleting a hard link removes only a name; deleting a symlink target leaves a dangling link.

---

## 13. C Declarations (Function Pointers)

Example:

```c
void (*sig())(int);
```

English:

> A function returning a pointer to a function taking an int and returning void.

**Exam tip:**
Use the **inside-out rule**.

---

## 14. Vulnerabilities & Defences (Conceptual Marks)

### Common vulnerabilities:

* Buffer overflow (stack/heap)
* Integer truncation
* TOCTOU (symlinks)
* Incorrect `sizeof`
* Hard-link abuse

### Defences (name at least one):

* Stack canaries
* ASLR
* NX / DEP
* Fortify Source
* Dropping privileges
* Refusing symlinks

---

# One-Page Mental Checklist (Night-Before)

* □ Assume **32-bit x86** unless stated
* □ State assumptions explicitly
* □ Arrays ≠ pointers (`sizeof`)
* □ Stack grows down
* □ `lea` = address
* □ `x` needed to execute / `cd`
* □ setgid → group owner matters
* □ umask math correct
* □ Hard links share inode
* □ Symlinks store paths
* □ Bit truncation → low bits

---

# Things to Remember Under Pressure (Avoid Easy Losses)

* Don’t invent register values
* Don’t ignore missing `x` bit
* Don’t confuse sticky bit with `s`
* Don’t say “copy” for links
* Don’t assume pointer knows size
* Don’t forget little-endian

---

**If you can explain each bullet above in one sentence, you are exam-ready.**
