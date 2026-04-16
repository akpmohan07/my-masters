# OpenMP Complete Study Guide

## CSC1141 — OpenMP Complete Study Guide

#### Exam-focused · Intuition-first · Every topic from the lecture slides

***

### How to use this guide

Every topic from the OpenMP lecture is here, ordered as the slides present them. Each section has:

* The **intuition** (why it exists, what problem it solves)
* The **code** (how to write it)
* The **exam angle** (what David Sinclair actually asks)
* **Your questions** from our sessions (the confusions that came up and their resolutions)

Topics marked **\[EXAM EVERY YEAR]** have appeared in every past paper from 2019–2025.

***

### 1. What is OpenMP?

OpenMP enables **incremental conversion** — you take a working sequential program and parallelise it by adding pragma lines. You don't rewrite it from scratch.

The model is called **Globally Sequential Locally Parallel (GSLP)**:

```
Main thread:  ────────[fork]─────────────────[join]────────
                       │                       │
Child threads:     ────┤──────  ──────  ──────┤
                       │      \/      \/      │
                       └──── parallel region ─┘
```

OpenMP has three parts:

* **Compiler directives** — `#pragma omp ...` lines that tell the compiler to parallelise
* **Runtime library** — functions like `omp_get_thread_num()`
* **Environment variables** — `OMP_NUM_THREADS=4 ./program`

***

### 2. Hello World — The Basic Structure

```c
#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

int main(int argc, char *argv[]) {
    int nthreads = atoi(argv[1]);

    #pragma omp parallel num_threads(nthreads)
    printf("Hello from thread %d of %d\n",
           omp_get_thread_num(), omp_get_num_threads());

    return 0;
}
```

**Compile on Mac:**

```bash
gcc -Xpreprocessor -fopenmp \
  -I/opt/homebrew/opt/libomp/include \
  -L/opt/homebrew/opt/libomp/lib \
  -lomp -o hello hello.c
```

#### Key concepts from this example

**Implicit barrier** — the master thread waits for all child threads to finish before continuing past the parallel region. This is automatic. You don't write it.

**Team of threads** — master + all children together.

**Parallel region vs construct** — a construct is the pragma + its code block (static, written in source). A parallel region is what runs at runtime (dynamic — the actual threads executing it).

#### Setting thread count — 3 ways

```c
OMP_NUM_THREADS=4 ./program      // environment variable
omp_set_num_threads(4);          // runtime function in code
#pragma omp parallel num_threads(4)  // pragma clause
```

**Common trap you asked about**: `omp_get_num_threads()` called _outside_ a parallel region returns 1 — not the configured count. Use `omp_get_max_threads()` outside parallel regions instead.

***

### 3. Variable Scope **\[EXAM 2019, 2021, 2024]**

#### The problem

Variables declared outside a parallel region are **shared by default**. Multiple threads writing to the same shared variable = race condition = wrong results.

OpenMP will not warn you about this. It is your responsibility.

#### All scope types

**`shared`** — all threads see the same memory location. Default for variables declared outside the parallel region.

```c
int x = 5;
#pragma omp parallel
{
    printf("%d\n", x);  // all threads read same x
}
```

**`private`** — each thread gets its own copy, **uninitialised**. The original value is not visible inside, and the thread's value does not persist after.

```c
int x = 5;
#pragma omp parallel private(x)
{
    x = omp_get_thread_num();  // each thread has own x, original 5 gone
}
// x here is undefined
```

_When to use_: scratch variables used only inside the loop, where you don't care about the value before or after.

**`firstprivate`** — same as `private` but initialised with the value from outside.

```c
int x = 5;
#pragma omp parallel firstprivate(x)
{
    x += omp_get_thread_num();  // each thread starts at 5, modifies own copy
}
```

**`lastprivate`** — same as `private` but after the parallel region, the value from the **last iteration** is copied back to the outside variable.

```c
int x;
#pragma omp parallel for lastprivate(x)
for (int i = 0; i < 100; i++) {
    x = i * 2;
}
// x is now 198 (last iteration i=99)
```

**`reduction`** — each thread gets a private copy initialised to the identity value (0 for +, 1 for \*). At the end of the parallel region, all copies are combined into the shared variable.

```c
int sum = 0;
#pragma omp parallel for reduction(+: sum)
for (int i = 0; i < 100; i++) {
    sum += i;  // each thread accumulates privately, combined at end
}
```

**`threadprivate`** — a persistent per-thread variable with global scope. Survives across multiple parallel regions — unlike `private` which is destroyed when the thread exits.

**`copyin`** — used with `threadprivate` to initialise each thread's copy from the master thread's value.

**`default(none)`** — forces you to explicitly declare every variable's scope. OpenMP will give a compile error for any variable not declared. Best practice — catches accidental sharing.

```c
int x = 5, y = 0;
#pragma omp parallel default(none) shared(x) private(y)
{
    y = x * 2;
}
```

#### Reduction identifiers table

| Identifier                  | Initial value           |
| --------------------------- | ----------------------- |
| `+`, `-`, `\|`, `\|\|`, `^` | 0                       |
| `*`, `&&`                   | 1                       |
| `&`                         | 0xFF..FF                |
| `max`                       | Smallest possible value |
| `min`                       | Largest possible value  |

#### Your question: what's the use of `private` if there's no initialisation and no return?

It's used for **scratch variables** — temporaries that each thread needs its own copy of, but where you don't care about the value before or after. If you declare variables inside the loop body, they are automatically private — that's the cleaner approach and avoids needing the clause at all.

***

### 4. The `parallel for` Construct

The most common parallelism construct. Splits loop iterations across threads automatically.

```c
#pragma omp parallel for
for (int i = 0; i < N; i++) {
    data[i] = compute(i);
}
```

This is shorthand for:

```c
#pragma omp parallel
#pragma omp for
    for (int i = 0; i < N; i++) { ... }
```

#### Canonical form requirements

`parallel for` cannot be applied to any for loop. The loop must be in **canonical form**:

* Loop variable must be `int`, pointer, or iterator
* Loop variable **must not be modified** in the loop body
* The limit (`i < N`) must be **loop invariant** — N cannot change during execution
* The step must be loop invariant
* Operators limited to `>`, `>=`, `<`, `<=`
* No `break`, `goto`, or `throw` inside

**Why these rules?** OpenMP calculates the total number of iterations _before_ the loop starts to divide work across threads. If the iteration count were unpredictable, it couldn't distribute work.

**Loop invariant** means: does not change during loop execution. `N` is loop invariant if nothing inside the loop modifies it.

***

### 5. Scheduling **\[EXAM 2019]**

Scheduling decides which thread gets which iterations.

```
You have 8 rows to compute and 4 threads.
Who gets which row?
That's scheduling.
```

#### Static

Divide iterations into chunks upfront, assign in round-robin before any thread starts.

```c
#pragma omp parallel for schedule(static)        // default chunk = N/threads
#pragma omp parallel for schedule(static, 2)     // chunk of 2 at a time
```

```
8 rows, 4 threads, schedule(static, 2):
Thread 0 → rows 0, 1
Thread 1 → rows 2, 3
Thread 2 → rows 4, 5
Thread 3 → rows 6, 7
```

```
8 rows, 4 threads, schedule(static, 1) — round-robin:
Thread 0 → rows 0, 4
Thread 1 → rows 1, 5
Thread 2 → rows 2, 6
Thread 3 → rows 3, 7
```

**When to use**: all iterations take roughly equal time. Low overhead, predictable.

#### Dynamic

No upfront assignment. Thread finishes a chunk → picks up next available chunk from queue.

```c
#pragma omp parallel for schedule(dynamic)       // chunk = 1 (default)
#pragma omp parallel for schedule(dynamic, 10)   // chunk = 10
```

**When to use**: iterations have unequal work. Fast threads naturally get more iterations. Non-deterministic — run multiple times and thread assignments differ.

**Overhead**: each chunk request = lock + read counter + update + unlock. With 1000 iterations and chunk=1, that's 1000 lock/unlock cycles.

#### Guided

Like dynamic but chunk size starts large and shrinks over time.

```
chunk = max(min_chunk, remaining / num_threads)
```

```
1000 iterations, 4 threads, schedule(guided):
Round 1: chunk = max(1, 1000/4) = 250  → Thread 0 gets 250
Round 2: chunk = max(1, 750/4)  = 187  → Thread 1 gets 187
Round 3: chunk = max(1, 563/4)  = 140  → Thread 2 gets 140
...
Eventually chunks shrink to 1
```

**Intuition**: Start big (low scheduling overhead, threads stay busy) → shrink at the end (fine-grained load balancing where stragglers are most likely). Best of static and dynamic.

**`schedule(guided, 8)`** — minimum chunk size is 8. Chunks shrink down to 8, then stay at 8.

#### Exam question 2019 — guided worked example

> Give a detailed worked example of how guided scheduling would deal with a for loop containing 1000 iterations.

Answer: State the formula `chunk = max(min_chunk, remaining / p)`, then show Round 1, Round 2, Round 3... with actual numbers until chunks stop shrinking. Show total iterations add up to 1000.

#### `collapse(2)` — nested loops

Merges two nested loops into one pool of iterations for better load balancing.

```c
// Without collapse — only outer loop parallelised
#pragma omp parallel for
for (int i = 0; i < M; i++)
    for (int j = 0; j < N; j++)
        c[i][j] = 0;

// With collapse(2) — M*N cells distributed across threads
#pragma omp parallel for collapse(2)
for (int i = 0; i < M; i++)
    for (int j = 0; j < N; j++)
        c[i][j] = 0;
```

***

### 6. Data Dependencies **\[EXAM EVERY YEAR]**

#### The core intuition

Two workers share a whiteboard. The only question that matters:

> Is anyone **writing**?

* Both reading → safe (Input dependency — RAR)
* One reading, one writing → dangerous
* Both writing → dangerous

#### The four types

**Flow dependency (RAW — Read After Write)**

Write happens first, then read. The reader needs the writer's result.

```c
x = 5;      // write
y = 3 * x;  // read — needs x from above
```

In a loop: iteration `i` reads `data[i-1]` which iteration `i-1` wrote.

```c
data[i] = data[i] + data[i-1];  // reads previous iteration's result
```

**Antidependency (WAR — Write After Read)**

Read happens first, then write. The writer would overwrite something the reader still needs.

```c
y = 5 * x;  // read x first
x = 3;      // write x — too late if read hasn't happened yet
```

In a loop: reading from a future index that will be overwritten.

```c
a[i] = a[i+1] + c;  // reads a[i+1] which iteration i+1 will later overwrite
```

**Output dependency (WAW — Write After Write)**

Two writes to the same variable. Order matters — last write wins.

```c
x = 5;
x = x + a;  // two writes — which survives depends on thread ordering
```

**Input dependency (RAR — Read After Read)**

Both read the same variable. Safe — reading simultaneously never causes problems.

```c
y = x + a;
z = 3 * x;  // both read x — fine
```

#### The naming intuition

* **Flow**: data _flows_ naturally from write to read (forward direction)
* **Anti**: _opposite_ of flow — read then write (backwards)
* **Output**: two _outputs_ fighting over the same destination
* **Input**: two _inputs_ reading the same source — no conflict

#### Loop-carried dependency

A dependency where iteration `i` depends on the result of iteration `i-1`. Cannot parallelise directly.

```
iteration 1 → writes data[1]
                    ↓
iteration 2 → reads data[1], writes data[2]
                    ↓
iteration 3 → reads data[2], writes data[3]
```

Chain — can't skip a link. Sequential.

***

### 7. Removing Flow Dependencies **\[EXAM EVERY YEAR]**

#### Case 1 — Reduction and Induction Variable

```c
double v = start;
double sum = 0;
for (int i = 0; i < 100; i++) {
    sum = sum + f(v);   // flow dependency — reduction variable
    v = v + step;       // flow dependency — induction variable
}
```

Two problems:

* `sum` — accumulates across iterations (reduction)
* `v` — each iteration needs previous `v` (induction)

**Fix for `sum`**: use `reduction(+: sum)` — each thread gets private copy, combined at end.

**Fix for `v`**: `v` is an **affine function** of `i` — meaning `v = a*i + b`. Replace with direct formula:

```c
v = i * step + start;   // any iteration can compute its own v independently
```

**Result**:

```c
#pragma omp parallel for reduction(+: sum) private(v) shared(step)
for (int i = 0; i < 100; i++) {
    v = i * step + start;   // no dependency — direct formula
    sum = f(v);             // reduction handles accumulation
}
```

**Affine function intuition**: instead of counting pages one by one from the start of a book, jump directly to page `i`. Any page is reachable independently.

#### Case 2 — Loop Skewing

```c
for (int i = 0; i < 100; i++) {
    y[i] = f(x[i-1]);   // reads x[i-1] which previous iteration wrote
    x[i] = x[i] + c[i];
}
```

Fix: pre-compute first value, swap order inside loop.

```c
y[0] = f(x[0]);
for (int i = 0; i < 100; i++) {
    x[i] = x[i] + c[i];   // update x first
    y[i+1] = f(x[i]);     // then read updated x — no inter-iteration dependency
}
```

#### Case 3 — Partial Parallelisation (ISDG Case)

```c
for (int i = 1; i < 100; i++) {
    for (int j = 1; j < 200; j++) {
        data[i][j] = data[i-1][j] + data[i-1][j-1];
    }
}
```

ISDG shows vertical arrows (i depends on i-1) but **no horizontal arrows** (j iterations are independent).

→ Parallelise the j loop:

```c
for (int i = 1; i < 100; i++) {
    #pragma omp parallel for
    for (int j = 1; j < 200; j++) {
        data[i][j] = data[i-1][j] + data[i-1][j-1];
    }
}
```

#### Case 4 — Refactoring (Wavefront) **\[EXAM 2019, 2024]**

```c
for (int i = 1; i < N; i++) {
    for (int j = 1; j < M; j++) {
        data[i][j] = data[i-1][j] + data[i][j-1] + data[i-1][j-1];
    }
}
```

ISDG shows arrows in all directions — neither i nor j loop can be parallelised directly.

**Solution — wavefront (diagonal) parallelism**:

Nodes on the same diagonal (`i+j = constant`) have no dependencies between them.

```
        j=1   j=2   j=3   j=4
i=1      ○─────○─────○─────○
         │╲    │╲    │╲
i=2      ○─────○─────○─────○
         │╲    │╲    │╲
i=3      ○─────○─────○─────○

Diagonal 2: (1,1)
Diagonal 3: (1,2), (2,1)         ← these can run in parallel
Diagonal 4: (1,3), (2,2), (3,1)  ← these can run in parallel
```

```c
for (int diag = 1; diag < large_dim; diag++) {
    #pragma omp parallel for
    for (int k = 0; k < diag_length; k++) {
        int i = diag - k;
        int j = k + 1;
        data[i][j] = data[i-1][j] + data[i][j-1] + data[i-1][j-1];
    }
}
```

#### Case 5 — Fissioning (Loop Splitting) **\[EXAM EVERY YEAR]**

```c
// The exact exam code — appears in 2020, 2021, 2025
result = temp[0];
for (int i = 1; i < N; i++) {
    data[i] = data[i] + data[i-1];   // loop-carried — cannot parallelise
    result = result + temp[i];        // reduction — can parallelise
}
```

Fix: split into two loops.

```c
// Loop 1 — sequential (loop-carried dependency cannot be removed)
for (int i = 1; i < N; i++) {
    data[i] = data[i] + data[i-1];
}

// Loop 2 — parallel (reduction)
result = temp[0];
#pragma omp parallel for reduction(+: result)
for (int i = 1; i < N; i++) {
    result = result + temp[i];
}
```

**This is the most tested conversion question**. Know it cold.

#### Case 6 — Algorithm Change

```c
for (i = 2; i < N; i++) {
    f[i] = f[i-1] + f[i-2];   // Fibonacci — each iteration needs two previous
}
```

This loop-carried dependency cannot be removed by any of the previous techniques. The only solution is a **completely different algorithm** — Binet's formula:

```
Fn = (φⁿ - (1-φ)ⁿ) / √5    where φ = (1 + √5) / 2
```

This computes any Fibonacci number directly without needing previous values — fully parallelisable.

***

### 8. Removing Antidependencies

```c
for (i = 0; i < N; i++) {
    a[i] = a[i+1] + c;   // reads a[i+1] which iteration i+1 will overwrite
}
```

**Intuition**: someone is erasing the whiteboard while you're still copying it. Fix: make a photocopy first.

```c
// Step 1: copy the data you need to read
for (i = 0; i < N; i++)
    a2[i] = a[i+1];

// Step 2: parallelise safely — reads from copy, writes to original
#pragma omp parallel for
for (i = 0; i < N; i++)
    a[i] = a2[i] + c;
```

***

### 9. Removing Output Dependencies

```c
for (i = 0; i < N; i++) {
    x = a[i] + b[i];   // all iterations write to same x
    c[i] = x * 2;
}
```

Fix: make `x` private — each thread gets its own copy.

```c
#pragma omp parallel for private(x)
for (i = 0; i < N; i++) {
    x = a[i] + b[i];   // each thread's own x — no conflict
    c[i] = x * 2;
}
```

***

### 10. Iteration Space Dependency Graph (ISDG) **\[EXAM 2019, 2024]**

#### What it is

A visual tool to identify which loops can be parallelised when there are dependencies.

* Each **node** = one `(i, j)` iteration pair
* Each **arrow** = dependency — points from producer to consumer

#### How to draw it

For every read in `data[i][j] = ...`, draw an arrow from the source iteration to `(i, j)`.

For `data[i][j] = data[i-1][j] + data[i][j-1] + data[i-1][j-1]`:

```
        j=1    j=2    j=3
i=1      ○──→──○──→──○
         ↓ ↘   ↓ ↘   ↓
i=2      ○──→──○──→──○
         ↓ ↘   ↓ ↘   ↓
i=3      ○──→──○──→──○

→  horizontal arrow = data[i][j-1]  (j dependency)
↓  vertical arrow   = data[i-1][j]  (i dependency)
↘  diagonal arrow   = data[i-1][j-1]
```

#### How to read it — the rule

**Look at arrows within the same row (horizontal arrows between nodes in same row):**

* Horizontal arrows exist → j iterations depend on each other → **cannot parallelise j loop**
* No horizontal arrows → j iterations are independent → **parallelise j loop**

**Look at arrows within the same column (vertical arrows between nodes in same column):**

* Vertical arrows exist → i iterations depend on each other → **cannot parallelise i loop**
* No vertical arrows → i iterations are independent → **parallelise i loop**

#### The three ISDG patterns you need

**Pattern 1 — only vertical arrows** (Case 3): → j loop can be parallelised, i loop is sequential

**Pattern 2 — horizontal + vertical + diagonal** (Case 4, the exam question): → Neither loop directly parallelisable → wavefront diagonal approach

**Pattern 3 — only horizontal arrows**: → i loop can be parallelised, j loop is sequential

***

### 11. The Integration Example — 5 Levels of Optimisation

This is the main teaching example in the lecture. Understanding all 5 levels demonstrates mastery of variable scope, race conditions, critical pragma, and reduction.

#### The function

```c
double testf(double x) {
    return (x*x + 2*sin(x));
}
```

Since the integral of `x² + 2sin(x)` is `x³/3 - 2cos(x)`, you can verify results.

**Trapezoidal rule**: approximate integral by summing trapezoid areas.

#### Level 1 — Sequential (baseline)

```c
double integrate(double start, double end, int div, double (*f)(double)) {
    double local_res = 0.0;
    double step = (end - start) / div;

    local_res = (f(end) + f(start)) / 2;   // endpoints count as half

    for (int i = 0; i < div; i++) {
        double x = start + i * step;
        local_res += f(x);
    }

    local_res *= step;
    return local_res;
}
```

Each iteration is independent — `x` is computed directly from `i` (no dependency). Only `local_res +=` creates a problem when parallelised.

#### Level 2 — Manual partitioning (broken)

Split range manually between threads. Bug: `final_res += integrate(...)` — all threads update shared `final_res` simultaneously. Race condition.

#### Level 3 — Shared partial array (works, verbose)

Each thread writes to its own `partial[ID]` slot. No race. Sum after.

**Bug**: `omp_get_num_threads()` called before parallel region returns 1. Use `omp_get_max_threads()` instead.

Goes against OpenMP's spirit of incremental conversion — too much manual restructuring.

#### Level 4 — `parallel for` + `critical`

```c
#pragma omp parallel for private(x)
for (int i = 0; i < div; i++) {
    x = start + i * step;       // affine formula — no loop dependency
    double temp = f(x);
    #pragma omp critical
    local_res += temp;           // one thread at a time — bottleneck
}
```

`private(x)` needed because `x` is declared outside the loop. If declared inside, it's automatically private.

`x = start + i * step` replaces the original `x += step` which had a loop-carried induction variable dependency.

**Problem**: `critical` creates a bottleneck — all 8 threads queue up at the accumulation. Parallelism mostly lost.

#### Level 5 — `parallel for` + `reduction` (best)

```c
#pragma omp parallel for reduction(+: local_res)
for (int i = 0; i < div; i++) {
    double x = start + i * step;   // declared inside — automatically private
    local_res += f(x);              // each thread accumulates privately
}
```

* No `critical` needed
* `x` declared inside → automatically private → no `private(x)` clause needed
* Each thread has private `local_res` initialised to 0
* At end, all private copies summed into shared `local_res`
* Full parallelism, correct result

**This is the idiomatic OpenMP solution.**

***

### 12. Synchronisation Directives

#### Mutual exclusion

**`critical`** — only one thread at a time executes the block.

```c
#pragma omp critical
result += temp;
```

**`atomic`** — lightweight `critical` for simple operations only: `x++`, `x--`, `x += expr`, `x = x op expr`. Maps to a single hardware instruction — faster than `critical`.

```c
#pragma omp atomic
result += temp;   // faster than critical for this case
```

#### Event synchronisation

**`barrier`** — all threads must reach this point before any continue.

```c
#pragma omp barrier
```

**`taskwait`** — current task waits for all its **child tasks** to complete. Different from `barrier` which waits for all threads.

```c
#pragma omp taskwait
```

**`master`** — only thread 0 executes the block. **No implicit barrier** — other threads skip and continue immediately.

```c
#pragma omp master
{ /* only thread 0 */ }
```

**`single`** — one thread (any thread) executes the block. **Has implicit barrier** — all threads wait at the end. Add `nowait` to remove the barrier.

```c
#pragma omp single
{ /* one thread, others wait */ }

#pragma omp single nowait
{ /* one thread, others continue without waiting */ }
```

**`ordered`** — forces a section inside `parallel for` to execute in sequential iteration order.

```c
#pragma omp parallel for ordered schedule(static, 1)
for (int i = 0; i < N; i++) {
    // parallel work
    #pragma omp ordered
    cout << data[i];   // printed in order 0,1,2,3... despite parallel execution
}
```

**`flush`** — forces all pending memory writes on specified variables to complete, ensuring other threads see the latest values. Happens implicitly at barriers, critical sections, and lock operations.

```c
#pragma omp flush(x)
```

#### Master vs Single — the key exam distinction

```
master:  only thread 0 executes, NO barrier — other threads skip and continue
single:  any one thread executes, HAS barrier — other threads wait
critical: ALL threads execute it, but one at a time
atomic:  ALL threads execute it, one at a time, simple ops only — faster
```

***

### 13. Tasks in OpenMP **\[EXAM EVERY YEAR — Q3(a)]**

#### Why tasks?

`parallel for` requires a fixed, known iteration count. Tasks handle:

* Linked lists — unknown length
* Recursive algorithms — dynamic depth (Fibonacci, merge sort)
* Irregular work — variable per element

#### What a task consists of

* The code to execute
* The data owned by the task
* A reference to the thread executing it

Key concept: **decouple specification from execution**. Define the task now, execute it later by any available thread.

#### Basic structure

```c
#pragma omp parallel
{
    #pragma omp single          // one thread traverses list and creates tasks
    {
        Node *tmp = head;
        while (tmp != NULL) {
            #pragma omp task
            process(tmp);       // creates one task per node
            tmp = tmp->next;
        }
    }
}
// implicit barrier — all threads wait for all tasks to finish
```

**Why `single`?** Without it, all 8 threads would traverse the list simultaneously and create 8× too many tasks. Each node would be processed 8 times.

**How tasks are assigned**: like dynamic scheduling — any idle thread picks up the next available task from the pool. Non-deterministic, naturally load-balanced.

#### `taskwait`

```c
int fib(int n) {
    int i1, i2;
    if (n == 0 || n == 1) return 1;

    #pragma omp task shared(i1) if(n > 25) mergeable
    i1 = fib(n-1);

    #pragma omp task shared(i2) if(n > 25) mergeable
    i2 = fib(n-2);

    #pragma omp taskwait        // wait for i1 and i2 before returning
    return i1 + i2;
}

int main(int argc, char *argv[]) {
    int n = atoi(argv[1]);
    #pragma omp parallel
    {
        #pragma omp single
        cout << "Result is " << fib(n) << endl;
    }
}
```

#### All task clauses

**`if(expr)`** — if expression is false (0), task runs immediately (undeferred) in the current thread. Used to cut off recursion: `if(n > 25)` prevents creating millions of tiny tasks for small `n`.

Without this, `fib(40)` creates 165,580,140 tasks. Program takes 108 seconds instead of 1 second.

**`final(expr)`** — if true, this task AND all its descendant tasks execute on a single thread. Used to completely stop recursion at a certain depth.

**`untied`** — by default a task is **tied** to the thread that started it. If suspended, it waits for that same thread even if others are idle. `untied` allows any free thread to resume it — better load balancing but loses cache locality.

**`mergeable`** — if the task is undeferred (runs immediately due to `if` being false), it shares the parent's data environment instead of creating its own. Saves overhead.

**`depend(in/out/inout: vars)`** — enforces ordering between sibling tasks based on variables. Task with `depend(in: x)` waits for all previous sibling tasks that have `depend(out: x)`.

```c
#pragma omp task shared(x) depend(out: x)   // T1 — produces x
printf("T1\n");

#pragma omp task shared(x) depend(in: x)    // T2 — waits for T1
printf("T2\n");

#pragma omp task shared(x) depend(in: x)    // T3 — waits for T1
printf("T3\n");
// T2 and T3 can run in parallel after T1 finishes
```

***

### 14. Semaphores **\[EXAM EVERY YEAR — Q3(b)]**

#### The concept

A semaphore has a counter `C` and a queue `Q`.

**P operation (acquire):**

```
C = C - 1
if C < 0: block this thread, add to queue Q
```

**V operation (release):**

```
C = C + 1
if C <= 0: remove one thread from Q, let it run
```

#### Intuition — the printer analogy

Imagine a printer shared between 8 people. A semaphore is a token system:

* To use the printer: pick up a token (`P`)
* Done printing: put token back (`V`)
* No token available: wait in queue

With 1 token: only 1 person at a time (**binary semaphore**) With 3 tokens: up to 3 people simultaneously (**counting semaphore**)

#### Binary semaphore — C starts at 1

```
Thread 1: P → C=0 → not negative → proceeds
Thread 2: P → C=-1 → negative → BLOCKED in queue
Thread 1: V → C=0 → wakes Thread 2
Thread 2: proceeds
```

#### Counting semaphore — C starts at N

```
C=3, 5 threads:
Thread 1: P → C=2 → proceeds
Thread 2: P → C=1 → proceeds
Thread 3: P → C=0 → proceeds
Thread 4: P → C=-1 → BLOCKED
Thread 5: P → C=-2 → BLOCKED

Thread 1 calls V → C=-1 → wakes Thread 4
Thread 4: proceeds
```

#### What OpenMP provides

OpenMP provides **only binary semaphore** via `omp_lock_t`:

```c
omp_lock_t lock;
omp_init_lock(&lock);        // initialise
omp_set_lock(&lock);         // P — acquire (blocks if locked)
omp_unset_lock(&lock);       // V — release
omp_test_lock(&lock);        // try to acquire — non-blocking, returns true/false
omp_destroy_lock(&lock);     // cleanup
```

#### Implementing counting semaphore without Qt **\[EXACT EXAM QUESTION]**

Use 2 binary locks + 1 counter:

```c
typedef struct {
    int count;
    omp_lock_t mutex;   // protects the counter
    omp_lock_t delay;   // blocks threads when count < 0
} Semaphore;

void sem_init(Semaphore *s, int n) {
    s->count = n;
    omp_init_lock(&s->mutex);
    omp_init_lock(&s->delay);
    if (n == 0) omp_set_lock(&s->delay);  // start locked if count=0
}

// P — acquire
void P(Semaphore *s) {
    omp_set_lock(&s->mutex);     // protect count
    s->count--;
    if (s->count < 0) {
        omp_unset_lock(&s->mutex);
        omp_set_lock(&s->delay); // BLOCK HERE — delay is locked
    } else {
        omp_unset_lock(&s->mutex);
    }
}

// V — release
void V(Semaphore *s) {
    omp_set_lock(&s->mutex);     // protect count
    s->count++;
    if (s->count <= 0) {
        omp_unset_lock(&s->delay); // WAKE one blocked thread
    }
    omp_unset_lock(&s->mutex);
}
```

**Why two locks?**

* `mutex`: held briefly just to update `count`, released immediately → short lock
* `delay`: held until a resource becomes available, could be seconds → long lock

If you used one lock for both — a blocked thread would hold `mutex` indefinitely → no other thread could update `count` → deadlock.

**Which line actually blocks the thread?**

```c
omp_set_lock(&s->delay);   // blocks here — delay is already locked in init()
```

`delay` starts locked. Any thread trying to acquire it blocks until someone calls `omp_unset_lock(&s->delay)` in `V()`.

**Only one thread can hold `delay` at a time** — that's correct. Each `V()` releases `delay` once, waking exactly one blocked thread. That thread immediately re-locks `delay`. Next `V()` call wakes the next one.

***

### 15. Producer-Consumer **\[EXAM EVERY YEAR — Q3(c)]**

#### The problem

Producer generates work. Consumer processes it. They share a buffer.

Three scenarios:

1. **Buffer full** — producer must wait
2. **Buffer empty** — consumer must wait
3. **Simultaneous access** — must coordinate

#### Two semaphores — why?

```
buff_slots: counts empty slots   (starts at BUFFER_SIZE)
avail:      counts filled slots  (starts at 0)
```

**Signal = `sem_release()`. Receive signal = `sem_acquire()`.**

```
Producer flow:                    Consumer flow:
sem_acquire(buff_slots)  ←─────── sem_release(buff_slots)
  [add to buffer]                   [read from buffer]
sem_release(avail)       ──────→  sem_acquire(avail)
```

#### Termination — sentinel values

Producer sends one **sentinel** per consumer. Sentinel = special value meaning "no more work" (`divisions = 0` or `job = -1`).

Each consumer reads its sentinel and stops. If you have 2 consumers, producer sends 2 sentinels.

#### OpenMP structure using `sections`

```c
#pragma omp parallel sections default(none) shared(...)
{
    // PRODUCER
    #pragma omp section
    {
        for (int i = 0; i < J; i++) {
            sem_acquire(&buff_slots);   // wait if full
            buffer[in] = work_item;
            in = (in + 1) % BUFFER_SIZE;
            sem_release(&avail);        // signal consumer
        }
        // sentinels
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            sem_acquire(&buff_slots);
            buffer[in].divisions = 0;   // sentinel
            in = (in + 1) % BUFFER_SIZE;
            sem_release(&avail);
        }
    }

    // CONSUMER 1
    #pragma omp section
    {
        while (1) {
            sem_acquire(&avail);         // wait if empty

            // protect out index — two consumers could read same slot
            omp_set_lock(&out_lock);
            int idx = out;
            out = (out + 1) % BUFFER_SIZE;
            omp_unset_lock(&out_lock);

            int item = buffer[idx];
            sem_release(&buff_slots);    // free the slot

            if (item == SENTINEL) break; // orderly termination
            process(item);
        }
    }

    // CONSUMER 2 — identical to Consumer 1
    #pragma omp section
    { ... }
}
```

**Why `out_lock`?** Two consumers running simultaneously could both read `out` and get the same index — processing the same item twice. The mutex ensures they get different indices.

**`sections` vs `task`**: `sections` is a fixed number of named blocks assigned to different threads. `task` creates dynamic units of work at runtime. Use `sections` for Producer-Consumer (fixed number of producers/consumers).

#### Orderly termination

The exam specifically asks how to ensure orderly termination:

1. Producer sends **one sentinel per consumer**
2. Each consumer checks for sentinel **after reading from buffer**
3. Consumer reads sentinel → breaks out of while loop → thread ends
4. Implicit barrier at end of `sections` block → all threads finish cleanly

***

### 16. Past Exam Paper Analysis

#### What appears every year

| Topic                                | Question | Years            |
| ------------------------------------ | -------- | ---------------- |
| Data dependencies (describe 4 types) | Q2       | Every year       |
| Convert sequential to OpenMP         | Q2(c)    | 2020, 2021, 2025 |
| Critical pragma example              | Q2(a)    | 2020, 2025       |
| Task directive description           | Q3(a)    | 2020, 2021, 2025 |
| Semaphores + counting implementation | Q3(b)    | Every year       |
| Producer-Consumer without Qt         | Q3(c)    | Every year       |

#### What rotates

| Topic                              | Years            |
| ---------------------------------- | ---------------- |
| Variable scope                     | 2019, 2021, 2024 |
| ISDG — description + usage         | 2019, 2024       |
| ISDG — resolve nested loop         | 2019, 2023, 2024 |
| Scheduling (static/dynamic/guided) | 2019             |
| Guided worked example              | 2019             |
| Antidependency removal             | 2023             |

#### The conversion question — know it cold

This code appears in 2020, 2021, and 2025 — word for word:

```c
result = temp[0];
for (int i = 1; i < N; i++) {
    data[i] = data[i] + data[i-1];
    result = result + temp[i];
}
```

**Answer pattern:**

1. Identify two dependencies:
   * `data[i] = data[i] + data[i-1]` — loop-carried flow dependency (cannot parallelise)
   * `result = result + temp[i]` — reduction (can parallelise)
2. Split into two loops:

```c
// Loop 1 — sequential (loop-carried dependency cannot be removed)
for (int i = 1; i < N; i++) {
    data[i] = data[i] + data[i-1];
}

// Loop 2 — parallel (reduction removes dependency)
result = temp[0];
#pragma omp parallel for reduction(+: result)
for (int i = 1; i < N; i++) {
    result = result + temp[i];
}
```

3. Explain: `data[i]` reads `data[i-1]` from the previous iteration — loop-carried RAW dependency. Cannot reorder. `result` is a reduction variable — order of addition doesn't matter, so each thread accumulates privately and combines at the end.

***

### 17. Confusions and Clarifications from Study Sessions

These are the exact questions that came up — worth knowing because they represent genuine traps.

**Q: `omp_get_num_threads()` outside parallel region returns 1. Why?** Outside a parallel region only the master thread is running. The function returns how many threads are _currently_ executing — which is 1. Use `omp_get_max_threads()` to get the configured thread count from outside.

**Q: In static scheduling, `(1000/8)*8 = 1000`?** Yes if 1000 is evenly divisible by 8. The formula rounds down to the nearest multiple of N to ensure equal chunks. `(1003/8)*8 = 125*8 = 1000` — drops 3 iterations for perfect division.

**Q: Is blocking at `omp_set_lock(&delay)` or somewhere else?** The thread blocks at `omp_set_lock(&s->delay)` in `P()`. `delay` was locked in `init()` and stays locked until `V()` calls `omp_unset_lock(&s->delay)`.

**Q: Why two locks in counting semaphore? Can't we use one?** If you used one lock for both `mutex` and `delay` purposes — a blocked thread would hold that lock while waiting. No other thread could update `count`. Result: deadlock. Two locks keep concerns separate: `mutex` is held briefly (count update only), `delay` is held long (until resource available).

**Q: Is `sem_release()` like Java's `signalAll()`?** No — `sem_release()` wakes exactly one blocked thread (like Java's `signal()`). To wake all, call `sem_release()` N times. In Producer-Consumer you want exactly one consumer waking per item added — `signal()`, not `signalAll()`.

**Q: Why `single` instead of `master` for task creation?** `single` has an implicit barrier — other threads wait for task creation to begin before they start executing tasks. `master` has no barrier — other threads might try to execute tasks before any exist. For linked list processing, `single` is correct.

**Q: `collapse(2)` — why better than just `parallel for` on outer loop?** Without `collapse`: only M threads can be used (one per outer iteration). With `collapse(2)`: M×N iterations distributed across all threads — better utilisation and load balancing, especially when M is small but N is large.

**Q: What's the difference between `final` and `if` clause on tasks?**

* `if(expr)`: if false, this specific task runs immediately (undeferred) but its child tasks can still be parallel
* `final(expr)`: if true, this task AND all its descendants run sequentially on one thread — stops all further parallelism in that branch

***

### 18. Quick Reference — Exam Cheat Sheet

#### Pragma summary

```c
// Parallel region
#pragma omp parallel [clauses]

// Loop parallelism
#pragma omp parallel for [schedule(type,chunk)] [collapse(n)] [clauses]

// Scheduling types
schedule(static)          // equal blocks, assigned upfront
schedule(static, n)       // blocks of n, round-robin
schedule(dynamic)         // first-come-first-served, chunk=1
schedule(dynamic, n)      // first-come-first-served, chunk=n
schedule(guided)          // shrinking chunks
schedule(guided, n)       // shrinking chunks, minimum=n

// Synchronisation
#pragma omp critical       // one thread at a time
#pragma omp atomic         // one thread, simple ops only, faster
#pragma omp barrier        // all threads wait here
#pragma omp master         // thread 0 only, no barrier
#pragma omp single         // any one thread, has barrier
#pragma omp single nowait  // any one thread, no barrier
#pragma omp ordered        // sequential order inside parallel for
#pragma omp flush(x)       // ensure x written to memory

// Tasks
#pragma omp task [clauses]
#pragma omp taskwait       // wait for child tasks
#pragma omp sections       // divide into named blocks
#pragma omp section        // one named block
```

#### Variable scope summary

```
shared      → all threads see same variable (default outside parallel)
private     → each thread has own copy, uninitialised
firstprivate → each thread has own copy, initialised from outside
lastprivate  → each thread has own copy, last iteration's value copied back
reduction    → each thread has own copy (initialised to identity), combined at end
default(none) → must declare everything explicitly
```

#### When to use what

```
fixed iteration count, equal work    → parallel for + static
fixed iteration count, unequal work  → parallel for + dynamic or guided
irregular work (linked list, tree)   → tasks
mutual exclusion (shared variable)   → critical or atomic
accumulation                         → reduction
one-time setup before parallel work  → single (with barrier)
I/O by master thread                 → master (no barrier)
producer/consumer pattern            → sections + semaphores
```

***

_End of OpenMP Study Guide — CSC1141 Concurrent Programming_
