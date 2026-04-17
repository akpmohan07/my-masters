# Introduction

## CSC1141 — Introduction: Revision Notes v2

**Date:** 17 April 2026\
**Review:** 20 Apr (recall) | 24 Apr (exam Qs) | 1 May (weak spots)

***

### 1. Concurrent Program

**Definition:** Interleaving of sets of sequential atomic instructions

**Atomic instruction:** Once started → completes without interruption (e.g. `MOV A, B`)

**Key facts:**

* Multiple processes execute at same time on same/different processors
* At any instant → each processor executes exactly ONE instruction
* `i++` = 3 atomics: Read → Add → Write → race condition risk

***

### 2. Correctness

**Variables:** `a` = inputs | `b` = outputs | `P(a)` = pre-condition | `Q(a,b)` = post-condition

|             | Formula                              | Meaning                              | Terminates? |
| ----------- | ------------------------------------ | ------------------------------------ | ----------- |
| **Partial** | `(P(a) ∧ terminates(Prog)) ⟹ Q(a,b)` | IF it terminates, output is correct  | Maybe       |
| **Total**   | `P(a) ⟹ (terminates(Prog) ∧ Q(a,b))` | WILL terminate AND output is correct | Always      |

**Rule:** Totally correct programs always terminate. Deadlock = partial OK, total FAIL.

***

### 3. Safety & Liveness

| Property     | Rule                    | Sub-properties                        |
| ------------ | ----------------------- | ------------------------------------- |
| **Safety**   | Must ALWAYS be true     | Mutual exclusion, Absence of deadlock |
| **Liveness** | Must EVENTUALLY be true | Absence of starvation, Fairness       |

**Safety:**

* **Mutual exclusion** — no two processes interleave on shared resource simultaneously
* **Absence of deadlock** — non-terminating system that cannot respond to any signal

**Liveness:**

* **Absence of starvation** — every process eventually gets resources
* **Fairness** — any contention must eventually be resolved

**4 Fairness levels (weak → strong):**

| Level              | Definition                                              |
| ------------------ | ------------------------------------------------------- |
| **Weak**           | Continuously requesting → eventually granted            |
| **Strong**         | Requesting infinitely often → eventually granted        |
| **Linear waiting** | Granted before any other process gets it more than once |
| **FIFO**           | First request in → first granted                        |

**Memory anchor:** **W**hy **S**o **L**ong **F**riend

***

### 4. Flynn's Taxonomy (1966)

|                   | Single Instruction | Multiple Instructions |
| ----------------- | ------------------ | --------------------- |
| **Single Data**   | **SISD**           | **MISD**              |
| **Multiple Data** | **SIMD**           | **MIMD**              |

| Type     | Description                          | Example                               |
| -------- | ------------------------------------ | ------------------------------------- |
| **SISD** | 1 instruction, 1 data stream         | Single CPU core                       |
| **SIMD** | 1 instruction, many data streams     | GPU, vector machines → OpenCL         |
| **MISD** | Many instructions, 1 data stream     | Fault-tolerant systems (rare)         |
| **MIMD** | Many instructions, many data streams | Multicore CPUs → Java Threads, OpenMP |

**MIMD variants:**

* **Shared memory** — processors share memory (fast, contention risk)
* **Distributed memory** — own local memory, communicate by messages

***

### 5. Past & Today

**Past architectures:**

|         | Shared Memory       | Distributed Memory     |
| ------- | ------------------- | ---------------------- |
| Speed   | Memory speed (fast) | Network speed (slower) |
| Problem | Contention          | Communication overhead |

→ Both expensive. **Cluster computing** = first affordable (standard PCs + Ethernet)

**Today — HSA core types:**

|                   | LCU                  | TCU                     |
| ----------------- | -------------------- | ----------------------- |
| Stands for        | Latency Compute Unit | Throughput Compute Unit |
| Generalisation of | CPU                  | GPU                     |
| Maps to           | Task parallelism     | Data parallelism        |
| Module tool       | Java Threads, OpenMP | OpenCL                  |

***

### 6. Speedup & Efficiency

**Variables:** `t_seq` = sequential time | `t_par` = parallel time | `N` = processors

| Metric         | Formula         | Meaning                       |
| -------------- | --------------- | ----------------------------- |
| **Speedup**    | t\_seq / t\_par | How many times faster?        |
| **Efficiency** | speedup / N     | How useful is each processor? |

**Measurement rules:**

* Same hardware, same load for both programs
* Sequential = fastest known solution
* Measure whole execution, multiple runs, report averages
* Threads must not exceed hardware cores

***

### 7. Amdahl's Law

**Variables:** T = sequential time | α = parallelisable fraction | 1-α = sequential fraction | N = processors

**Assumptions:** Fixed problem size | Even division | No communication overhead

**Derivation:**

| Step               | Formula                               |
| ------------------ | ------------------------------------- |
| Parallel time      | (1-α)T + αT/N                         |
| Speedup            | T / ((1-α)T + αT/N)                   |
| Speedup (final)    | **1 / ((1-α) + α/N)**                 |
| Max speedup (N→∞)  | **1 / (1-α)**                         |
| Efficiency         | speedup / N = T / (N×((1-α)T + αT/N)) |
| Efficiency (final) | **1 / (N(1-α) + α)**                  |

**Efficiency derivation steps:**

1. efficiency = speedup / N
2. \= T / (N × ((1-α)T + αT/N))
3. Expand: N(1-α)T + αT
4. Cancel T: **1 / (N(1-α) + α)**

**Implication:** Sequential remainder (1-α) = permanent ceiling. More cores → efficiency → 0.

***

### 8. Gustafson-Barsis Law

**Variables:** T = parallel time | α = parallel fraction | 1-α = sequential fraction | N = machines

**Assumption:** Problem size grows with N (unlike Amdahl's fixed size)

**Derivation:**

| Step                  | Formula            |
| --------------------- | ------------------ |
| Sequential equivalent | (1-α)T + NαT       |
| Speedup               | ((1-α)T + NαT) / T |
| Speedup (final)       | **(1-α) + Nα**     |
| Efficiency            | speedup / N        |
| Efficiency (final)    | **(1-α)/N + α**    |

**Efficiency derivation steps:**

1. efficiency = ((1-α) + Nα) / N
2. Split: (1-α)/N + Nα/N
3. Cancel N: **(1-α)/N + α**

**Why correct:** Amdahl = fixed problem (unrealistic). Gustafson = more cores → bigger problem in same time (realistic). No ceiling.

***

### 9. Amdahl vs Gustafson

|                       | **Amdahl**               | **Gustafson**            |
| --------------------- | ------------------------ | ------------------------ |
| Year                  | 1967                     | 1988                     |
| Perspective           | Sequential program       | Parallel program         |
| Problem size          | Fixed                    | Grows with N             |
| Starting time         | t\_seq = T               | t\_par = T               |
| Parallel time         | (1-α)T + αT/N            | T                        |
| Sequential equivalent | T                        | (1-α)T + NαT             |
| Speedup (final)       | 1/((1-α)+α/N)            | (1-α)+Nα                 |
| Efficiency (final)    | 1/(N(1-α)+α)             | (1-α)/N+α                |
| Ceiling?              | Yes — 1/(1-α)            | No                       |
| As N→∞                | Efficiency → 0           | Efficiency stays healthy |
| Verdict               | Pessimistic, unrealistic | Optimistic, realistic    |

***

### 10. Scaling Metrics

| Metric             | Formula               | Meaning                        | Connected to |
| ------------------ | --------------------- | ------------------------------ | ------------ |
| **Strong Scaling** | t\_seq / (t\_par × N) | Fixed problem, more processors | Amdahl       |
| **Weak Scaling**   | t\_seq / t'\_par      | Problem grows with N           | Gustafson    |

`t'_par` = time to solve problem **N times larger** on N processors. Ideal = 1.0

***

### 11. Guidelines

**Developing:**

1. Sequential variant first (most naturally parallelisable algorithm)
2. Profile → find time-consuming parts
3. Estimate benefit (Amdahl's Law)

**Measuring:**

1. Whole execution duration
2. Multiple runs → averages (+ std deviations)
3. Exclude outliers (with valid explanation only)
4. Report for various input sizes
5. Threads ≤ hardware cores

***

Here's the exam Q\&A section rewritten — showing how to approach and write each answer:

***

### 📝 Exam Q1 — Full Answers

#### Q1(a) — Amdahl's Law \[6 marks]

> _"Derive Amdahl's Law for speedup and from that derive efficiency. What is the implication?"_

**How to approach:** Build from first principles. State assumptions → build parallel time → derive speedup → derive efficiency → state implication.

***

Assume a sequential program takes time **T**. Let **α** be the fraction that can be parallelised across **N** processors. The remaining fraction **(1-α)** must stay sequential.

The parallel execution time is:

> t\_par = (1-α)T + αT/N

Speedup is defined as t\_seq / t\_par:

> speedup = T / ((1-α)T + αT/N)

Cancelling T:

> **speedup = 1 / ((1-α) + α/N)**

As N → ∞, α/N → 0, giving the upper bound:

> **max speedup = 1 / (1-α)**

Efficiency = speedup / N:

> efficiency = T / (N × ((1-α)T + αT/N)) = T / (N(1-α)T + αT)

Cancelling T:

> **efficiency = 1 / (N(1-α) + α)**

**Implication:** The sequential fraction (1-α) permanently limits speedup regardless of how many processors are added. As N increases, efficiency approaches zero — additional processors contribute diminishing returns.

***

#### Q1(b) — Gustafson-Barsis Law \[8 marks]

> _"Derive Gustafson-Barsis' Law for speedup and from that derive efficiency. Why is it the correct interpretation?"_

**How to approach:** Flip the perspective — start from parallel time, work out what the sequential equivalent would be → derive speedup → derive efficiency → explain why Amdahl is wrong.

***

Assume the parallel program runs in time **T**. The fraction **(1-α)** runs sequentially, taking **(1-α)T**. The fraction **α** runs across all N machines, taking **αT**. If this same work were done on one machine sequentially, it would take **NαT**.

The sequential equivalent time is therefore:

> t\_seq = (1-α)T + NαT

Speedup = t\_seq / t\_par:

> speedup = ((1-α)T + NαT) / T

Cancelling T:

> **speedup = (1-α) + Nα**

Efficiency = speedup / N:

> efficiency = ((1-α) + Nα) / N = (1-α)/N + Nα/N

Cancelling N in the second term:

> **efficiency = (1-α)/N + α**

**Why Gustafson-Barsis is the correct interpretation:**

Amdahl's Law assumes a fixed problem size — as more processors are added, the same amount of work is divided among them. This is unrealistic. In practice, when more processors are available, parallel programs use that extra power to solve proportionally larger problems in the same time.

Gustafson-Barsis measures from the parallel program's perspective, allowing problem size to grow with N. This gives linear speedup growth with no hard ceiling, which matches real-world experience — supercomputers with millions of processors continue to deliver useful speedup on large-scale problems.

***

#### Q1(c) — Calculation \[6 marks]

> _"Using Amdahl's Law, efficiency on 4 cores = 0.8. What would Gustafson-Barsis give for the same program on 4 cores?"_

**How to approach:** Two steps. First use Amdahl's efficiency formula to find α. Then plug α into Gustafson's efficiency formula.

***

**Step 1 — Find α using Amdahl's efficiency:**

efficiency = 1 / (N(1-α) + α) = 0.8, N = 4

> 1 / (4(1-α) + α) = 0.8

Rearrange:

> 4(1-α) + α = 1/0.8 = 1.25 4 - 4α + α = 1.25 4 - 3α = 1.25 3α = 2.75 **α = 0.917**

**Step 2 — Apply Gustafson-Barsis efficiency:**

> efficiency = (1-α)/N + α = (1 - 0.917)/4 + 0.917 = 0.083/4 + 0.917 = 0.021 + 0.917 **= 0.938**

Gustafson-Barsis gives a higher efficiency (0.938 vs 0.8) because it assumes the problem grows with the number of processors, giving a more optimistic and realistic measure of parallel performance.

***

Want me to update v2 with this exam section replaced?

###

### 📝 Exam Q1 — Skeleton Answers

#### Q1(a) — Amdahl derivation + implication \[6 marks]

1. State parallel time: **(1-α)T + αT/N**
2. speedup = T / ((1-α)T + αT/N) = **1/((1-α)+α/N)**
3. efficiency = speedup/N → expand → cancel T → **1/(N(1-α)+α)**
4. As N→∞ → ceiling = **1/(1-α)**
5. Implication: sequential remainder permanently limits speedup. Efficiency → 0 as N grows.

#### Q1(b) — Gustafson derivation + why correct \[8 marks]

1. State sequential equivalent: **(1-α)T + NαT**
2. speedup = ((1-α)T + NαT)/T = **(1-α)+Nα**
3. efficiency = speedup/N → split → cancel N → **(1-α)/N+α**
4. Why correct: Amdahl assumes fixed problem size (unrealistic). Real programs tackle bigger problems with more cores. Gustafson grows problem with N → no ceiling → linear speedup growth.

#### Q1(c) — Calculation \[6 marks]

**Given:** Amdahl efficiency = 0.8, N = 4. Find Gustafson efficiency.

**Step 1 — Find α:**

* 1/(4-3α) = 0.8
* 4-3α = 1.25
* 3α = 2.75
* **α = 0.917**

**Step 2 — Gustafson efficiency:**

* (1-0.917)/4 + 0.917
* \= 0.021 + 0.917
* **= 0.938**

***

### ❓ Weak Spots

* Efficiency derivation — remember: expand N×(...), then cancel T
* Part (c) — find α first from Amdahl, then plug into Gustafson
* Fairness levels — remember order: Weak → Strong → Linear → FIFO

***

### 📅 Spaced Repetition

| Review | Date        | Focus                                    |
| ------ | ----------- | ---------------------------------------- |
| Day 3  | 20 Apr 2026 | Derive both laws from scratch — no notes |
| Day 7  | 24 Apr 2026 | Full Q1 timed — 30 mins                  |
| Day 14 | 1 May 2026  | Weak spots drill                         |
