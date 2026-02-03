Below is a **concise, exam-focused final revision summary** tailored for a **Secure Programming (C on Linux)** exam, distilled from all the worked solutions and discussions above.
It is written to **maximise marks under time pressure**.

---

# Secure Programming – Final Revision Summary (Exam Focus)

> **Context assumed by examiners:**
> Most Secure Programming exams **implicitly assume 32-bit x86 Linux**, unless stated otherwise.
> Differences for **64-bit** are tested mainly via `sizeof`, pointer size, and calling conventions.

---

## 1. C Integer Basics (High-Frequency Exam Topic)

### Bit Widths (Same on 32 & 64 bit)

| Type    | Bits | Signed Range   | Unsigned Range |
| ------- | ---- | -------------- | -------------- |
| `char`  | 8    | −128 … 127     | 0 … 255        |
| `short` | 16   | −32768 … 32767 | 0 … 65535      |
| `int`   | 32   | −2³¹ … 2³¹−1   | 0 … 2³²−1      |

> **Key:** `int`, `short`, `long` are **signed by default**.
> `char` is **implementation-defined** → always write `signed char` / `unsigned char` in exams.

---

### Two’s Complement (Signed Integers)

* MSB = **sign bit**

  * `0` → positive
  * `1` → negative
* Negative value decoding:

  * **Invert bits → add 1**
* Alternative **exam-friendly method** (used by lecturers):

  * MSB weight = `−2^(N−1)`
  * Remaining bits = positive weights
    Example (8-bit):
    `11110100 = −128 + 64 + 32 + 16 + 4 = −12`

**Common mistake:** Treating bits as unsigned when MSB = 1.
**Exam tip:** Always **check MSB first**.

---

### Integer Promotions (VERY IMPORTANT)

* `char` and `short` are **promoted to `int` before arithmetic**
* Arithmetic happens in `int`
* Overflow only occurs when **assigning back** to smaller type

```c
signed char a = 120, b = 10;
signed char c = a + b;   // overflow here, not in a+b
```

---

### Assignment & Truncation (Core Rule)

When assigning to an **N-bit integer**:

1. Keep only **lowest N bits**
   → mathematically: `value % 2^N`
2. If **signed** and MSB = 1 → subtract `2^N`
3. If **unsigned** → use modulo result directly

**Never use `% 127`, `% 255`, etc.**
Always modulo **`2^N`**, not the max value.

---

## 2. `sizeof` vs `strlen` (Classic Trap)

| Expression  | Meaning                  |
| ----------- | ------------------------ |
| `sizeof(x)` | Memory size in bytes     |
| `strlen(s)` | Characters before `'\0'` |

### Critical Rules

* `sizeof` is **compile-time**
* `strlen` walks memory at runtime
* **Array parameters decay to pointers**

```c
void f(char s[]) {
    sizeof(s);   // size of pointer, NOT array
}
```

---

### 32-bit vs 64-bit Comparison

| Item            | 32-bit | 64-bit |
| --------------- | ------ | ------ |
| `sizeof(char*)` | 4      | 8      |
| `sizeof(int)`   | 4      | 4      |
| `sizeof(short)` | 2      | 2      |
| `sizeof(long)`  | 4      | 8      |

> **Exams usually assume 32-bit** unless stated.

---

## 3. Stack Frames & Linux Process Memory (Very Common)

### Process Address Space (32-bit Linux)

```
High addresses
+------------------+
| Stack            | ← grows down
+------------------+
| Heap             | ← grows up
+------------------+
| BSS (zeroed)     |
| Data             |
| RO Data          |
| Text (code)      |
+------------------+
Low addresses
```

---

### Stack Frame Layout (cdecl, 32-bit)

```
higher addresses
-----------------
function arguments     ← 8(%ebp), 12(%ebp)
return address         ← 4(%ebp)
saved EBP              ← 0(%ebp)
local variables        ← -4(%ebp), -8(%ebp)
-----------------
lower addresses
```

**Exam rule:**
A local variable **exists only if accessed**.
Extra `sub $0x10,%esp` ≠ 4 locals (padding/alignment).

---

## 4. Assembly → C Translation (AT&T Syntax)

### Operand Order (EXAM TRAP)

```asm
cmp SRC, DST   // compares DST − SRC
```

```asm
cmpl $0, x
jg label
```

means:

```c
if (x > 0)
```

### Common Instructions

| Instruction  | Meaning             |
| ------------ | ------------------- |
| `mov`        | assignment          |
| `add`, `sub` | arithmetic          |
| `jg`, `jge`  | signed comparison   |
| `ja`, `jae`  | unsigned comparison |

---

## 5. Arrays, Pointers, Parameters

### Arrays in Functions

```c
void f(int a[])
```

≡

```c
void f(int *a)
```

* Arrays are **never copied**
* Address is passed (pointer copied by value)

```c
int *p = a;   // copies address, not data
```

---

## 6. Recursion & Stack Diagrams (High Marks Topic)

* Each recursive call = **new stack frame**
* At base case, multiple frames exist simultaneously
* Must show:

  * parameters
  * locals
  * saved EBP
  * return address

**Examiner reward:** clear boxed stack diagrams with labels.

---

## 7. Overflow & Security Relevance

### Signed Overflow

* Result outside range → **undefined behaviour**
* Detected when:

  * operands same sign
  * result different sign

### Unsigned Overflow

* **Defined wraparound** modulo `2^N`
* Common source of vulnerabilities (size checks bypassed)

---

## 8. Secure Programming Connections

### Common Vulnerabilities Tested

* Stack buffer overflow
* Integer overflow → incorrect bounds
* Signed/unsigned mismatch
* Off-by-one (`sizeof` vs `strlen`)
* Unchecked recursion depth

### Defences You Should Mention

* Bounds checking
* Use of correct integer types
* `-fstack-protector`
* ASLR, NX stack
* Avoid mixing signed/unsigned
* Explicit casts with justification

---

# One-Page Mental Checklist (Night Before Exam)

* What is the **bit width** of the destination type?
* Signed or unsigned?
* Do I need **modulo `2^N`**?
* Check **MSB** for signed interpretation
* `char`/`short` → promoted to `int`
* `sizeof(array)` ≠ `sizeof(pointer)`
* Stack grows **down**
* Arguments are **above** return address
* AT&T syntax: `cmp SRC, DST`
* Arrays in functions → pointers
* 32-bit assumed unless stated

---

# Things to Remember Under Pressure (Avoid Easy Mistakes)

* ❌ `% 127`, `% 255` → WRONG
  ✅ Always `% 2^N`
* ❌ Counting unused stack space as variables
* ❌ Forgetting integer promotion
* ❌ Confusing `jg` vs `ja`
* ❌ Assuming `char` is signed
* ❌ Using `strlen` to size buffers

---

### Final Examiner Insight

> **Marks are awarded for correct reasoning, not just final numbers.**
> A short, precise explanation using the rules above scores higher than a long guess.

If you want, I can condense this into a **single handwritten-page exam cheat sheet** or run a **30-minute mock exam walkthrough**.
