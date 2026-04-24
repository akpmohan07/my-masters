# Data Parallel Primitive

## CSC1141 Concurrent Programming — Data Parallel Primitives

**Date:** 2026-04-24 **Topics covered:** DPP fundamentals, vector datatype, transformations, reductions, prefix scans, data management, stream compaction, radix sort, Monte Carlo integration, Boost.Compute, Thrust **Review dates:** Day+3: 2026-04-27 | Day+7: 2026-05-01 | Day+14: 2026-05-08

***

### 1. What is DPP?

#### One-liner

DPP lets you describe _what_ you want to do to data — the library figures out _how_ to parallelise it.

#### The Analogy

Like SQL vs a for-loop. With SQL you say `SELECT MAX(salary)` — the engine parallelises internally. DPP is the same approach for parallel computing.

#### How it works

* Frees the developer from partitioning data, deploying kernels, and collecting results
* Like OpenMP, tries to make the transition from sequential to concurrent programming as easy as possible
* Usually a trade-off between ease of use and efficiency — ease of use takes precedence
* Every operation is expressed as a primitive applied to a vector

#### Key rule

> Primitive = basic building block that cannot be broken down further. Complex algorithms are built by composing them.

#### Exam angle

> 📝 Every year — Q5 (20 marks). Always appears. Never skipped.

#### Watch out for

DPP is not about writing kernels — it's about composing primitives. If your answer describes writing OpenCL kernels manually, you're answering the wrong question.

***

### 2. Vector Datatype

#### One-liner

The vector is more than an array — it knows which memory space it lives in and provides iterators that work across all memory spaces.

#### The Analogy

A plain pointer is like a street address — only valid in one city. A DPP vector is like a GPS coordinate — works regardless of which memory space (city) you're in.

#### How it works

* Abstracts over host, global, local, and private memory spaces
* Provides iterators (`transform_iterator`, `permutation_iterator`) that work regardless of memory location
* Without iterators, a plain pointer passed to a GPU would point to invalid host memory
* Makes code portable across CPU and GPU without manual memory management

#### Key rule

> Vector = array + memory-space awareness + iterators

#### Exam angle

> 📝 2019R, 2020, 2023 — 4 marks each "What is the main datatype used in data-parallel primitives? What feature of this datatype effectively handles different memory spaces?"

**Expected answer:** Vector. Iterators handle different memory spaces.

#### Watch out for

Don't say "scan" or "copy" — those are primitives, not features of the vector. The answer is **iterators**.

***

### 3. Transformations

#### One-liner

A transformation applies an operation to each element — output size always equals input size.

#### How it works

| Primitive   | What it does                                  | Example                                       |
| ----------- | --------------------------------------------- | --------------------------------------------- |
| `fill`      | Set every element to same value               | `fill(v, 7)` → `[7,7,7,7]`                    |
| `sequence`  | Fill with incrementing values + optional step | `sequence(v, 0, 2)` → `[0,2,4,6]`             |
| `zip`       | Pair up elements from 2 vectors positionally  | `[1,2,3]` + `[A,B,C]` → `[(1,A),(2,B),(3,C)]` |
| `transform` | Apply a function to each element              | `[1,2,3,4]` → square → `[1,4,9,16]`           |

#### Key rule

> Transformations always preserve vector size. Input size = output size. Always.

#### Key code

```cpp
// apply sqrt to every element in-place
transform(v.begin(), v.end(), v.begin(), sqrt<float>());

// syntax: transform(input_begin, input_end, output_begin, function)
```

#### Exam angle

> 📝 2019, 2021, 2022, 2024, 2025 — 2 marks each "Describe transform / give an example"

**Expected answer:** definition + output size same as input + concrete example with numbers

#### Watch out for

`zip` pairs positionally — it does NOT mathematically combine values. Element 0 with element 0, element 1 with element 1.

***

### 4. Reductions

#### One-liner

A reduction applies a binary operation across a vector to produce a single value.

#### How it works

| Primitive       | Input             | Output         | Key detail                              |
| --------------- | ----------------- | -------------- | --------------------------------------- |
| `reduce`        | 1 vector          | 1 value        | apply binary op with initial value      |
| `inner_product` | 2 vectors         | 1 value        | pair-wise f, then reduce with g         |
| `reduce_by_key` | (key,value) pairs | reduced values | only reduces **consecutive** equal keys |

#### Key rule

> reduce collapses ONE vector → one value. inner\_product combines TWO vectors pair-wise → then collapses → one value. inner\_product = zip + transform + reduce in one step.

#### Key code

```cpp
// reduce: sum all elements, starting from 0
float sum = 0.0f;
reduce(v.begin(), v.end(), &sum, plus<float>(), queue);
// syntax: reduce(begin, end, output, binary_op, queue)

// inner_product: dot product of two vectors
// step 1 - multiply pairs: f(v[i], w[i])
// step 2 - sum all results: g(results)
inner_product(v.begin(), v.end(), w.begin(), w.end(), f, g);
```

#### Exam angle

> 📝 2019, 2021, 2022, 2024, 2025 — 2-3 marks each "Describe reduce / inner product"

**Expected answer for inner\_product:**

```
v = [1,2,3], w = [4,5,6]
step 1 (transform pairs): [1×4, 2×5, 3×6] = [4,10,18]
step 2 (reduce):           4+10+18 = 32
```

#### Watch out for

`reduce_by_key` only reduces **consecutive** equal keys — not all elements with the same key.

```
keys:   [A, A, B, B, B, C]
values: [1, 2, 3, 4, 5, 6]
result: A→3, B→12, C→6
```

***

### 5. Prefix Scans

#### One-liner

A scan produces a running total — instead of collapsing to one value like reduce, it keeps every intermediate result.

#### The Analogy

Cricket scoreboard:

* Inclusive = total score _at end of_ each over
* Exclusive = total score _before_ each over started

#### How it works

| Primitive        | What it produces                                       | Example input `[1,2,3,4]` |
| ---------------- | ------------------------------------------------------ | ------------------------- |
| `inclusive scan` | Running total including current element                | `[1, 3, 6, 10]`           |
| `exclusive scan` | Running total excluding current element, starts with 0 | `[0, 1, 3, 6]`            |
| `transform scan` | Apply transform first, then exclusive scan             | depends on transform      |

#### Key rule

> Exclusive scan = push inclusive scan one position right, put 0 at front, drop last element. Exclusive at position i = "how many slots are already taken before me" = my output position.

#### Key code

```cpp
// inclusive scan
inclusive_scan(v.begin(), v.end(), output.begin());

// exclusive scan — identity value 0
exclusive_scan(v.begin(), v.end(), output.begin(), 0);
// syntax: exclusive_scan(input_begin, input_end, output_begin, identity)
```

#### Exam angle

> 📝 2019, 2019R, 2020, 2022, 2022R, 2023, 2023R, 2025, 2025R — appears every year "Describe scan / prefix sum"

**Expected answer:** define both inclusive and exclusive with examples. Always include numbers.

#### Watch out for

When input has a 0, the running total **stays the same** — it does not increment. Most common arithmetic error in dry runs.

***

### 6. Data Management

#### One-liner

Primitives for copying, filtering, and reordering elements of a vector.

#### How it works

| Primitive    | What it does                                                    |
| ------------ | --------------------------------------------------------------- |
| `copy`       | Copies vector v into w                                          |
| `copy_if`    | Copies elements where predicate is true                         |
| `remove`     | Removes elements matching a value                               |
| `remove_if`  | Removes elements where predicate is true                        |
| `replace`    | Replaces elements matching a value                              |
| `replace_if` | Replaces elements where predicate is true                       |
| `unique`     | Keeps only first from each group of identical elements          |
| `partition`  | Reorders so predicate-true elements come before predicate-false |
| `scatter`    | Places elements at specific positions based on a map vector     |
| `scatter_if` | Conditionally scatters based on a map                           |

#### Key rule — scatter

> `scatter`: element at position i goes to `output[map[i]]`

```
input:  [A, B, C, D]
map:    [3, 0, 2, 1]
output: [B, D, C, A]
```

#### Key code

```cpp
// scatter: place each element at position given by map
scatter(v.begin(), v.end(), map.begin(), output.begin());
// syntax: scatter(input_begin, input_end, map_begin, output_begin)
```

#### Watch out for

`copy_if` is NOT easily parallelised — each thread needs to know how many elements before it passed the predicate. This creates a sequential dependency. Use scan + scatter instead (see Stream Compaction).

***

### 7. Stream Compaction

#### One-liner

Remove unwanted elements from a large sparse vector, producing a compact dense vector of only the surviving elements.

#### The Analogy

A warehouse shelf with 8 boxes — only 3 contain anything. You want to move only the non-empty boxes to a smaller shelf. Stream compaction = pack the useful ones, discard the rest.

#### How it works — 3 steps

**Recipe:** `transform → exclusive scan → scatter`

Consider: `[6, 2, 9, 8, 1, 5, 7, 3]` with predicate `> 5`

**Step 1 — Transform (apply predicate):**

```
input:  [6, 2, 9, 8, 1, 5, 7, 3]
flags:  [1, 0, 1, 1, 0, 0, 1, 0]   ← 1=keep, 0=discard
```

**Step 2 — Exclusive scan (compute destinations):**

```
flags:    [1, 0, 1, 1, 0, 0, 1, 0]
ex scan:  [0, 1, 1, 2, 3, 3, 3, 4]   ← output position for each element
```

**Step 3 — Scatter:** Place each element at its destination — only where flag = 1:

```
output: [6, 9, 8, 7]
```

#### Key rule

> Why exclusive scan and NOT inclusive scan: Exclusive gives "how many surviving elements came BEFORE me" = my 0-based output index. Inclusive would be off by one — every element would land one position too far right.

#### Why this is efficient

All three steps — transform, exclusive scan, scatter — operate independently on each element. Fully parallelisable. No sequential dependencies.

#### Why not copy\_if

`copy_if` is sequential — each thread must wait to know how many previous elements passed the predicate before it can write its output. Creates a bottleneck.

#### Exam angle

> 📝 2019R, 2020, 2023 — 10 marks "Using data parallel primitives describe an efficient stream compaction algorithm"

**Expected answer:** definition + 3 steps with names + worked example with all vectors shown

#### Watch out for

Always name the primitives: **transform**, **exclusive scan**, **scatter**. Don't just show the numbers — name the step.

***

### 8. Radix Sort

#### One-liner

Sort a vector of integers one bit at a time from MSB to LSB, without ever comparing elements — O(n).

#### The Analogy

Sorting a deck of cards by sorting one property at a time. Each pass splits into two groups — those whose current bit is 0 (go left) and those whose bit is 1 (go right).

#### How it works — 6 steps per pass

**Setup:** Convert all numbers to binary, padding to same width (determined by largest number). Number of passes = number of bits.

**Example input:** `[4, 7, 2, 6, 3, 5, 1, 0]` → `[100, 111, 010, 110, 011, 101, 001, 000]`

**Pass 1 — MSB:**

| Step | Vector         | Formula                         | Result                              |
| ---- | -------------- | ------------------------------- | ----------------------------------- |
| 1    | input (binary) | —                               | `[100,111,010,110,011,101,001,000]` |
| 2    | `e`            | 1 if splitting bit=0, else 0    | `[0,0,1,0,1,0,1,1]`                 |
| 3    | `f`            | exclusive scan of `e`           | `[0,0,0,1,1,2,2,3]`                 |
| 4    | `total_false`  | `e[n-1] + f[n-1]`               | `1 + 3 = 4`                         |
| 5    | `t`            | `t[i] = i - f[i] + total_false` | `[4,5,6,6,7,7,8,8]`                 |
| 6    | `d`            | bit=1 → `t[i]`, bit=0 → `f[i]`  | `[4,5,0,6,1,7,2,3]`                 |
| 7    | output         | scatter input using `d`         | `[010,011,001,000,100,111,110,101]` |

Repeat for bit 1 and bit 0. After 3 passes → `[0,1,2,3,4,5,6,7]` ✅

#### Key rules

| Rule                         | Detail                                              |
| ---------------------------- | --------------------------------------------------- |
| `e` is inverted              | bit=0 → e=1 (false), bit=1 → e=0 (true)             |
| `f` gives false destinations | positions 0 to total\_false-1                       |
| `t` gives true destinations  | positions total\_false to n-1                       |
| `t[i]` out of bounds         | safe — Step 6 filters it out (only used when bit=1) |
| `d` picks from t or f        | based on splitting bit of each element              |
| Pass order                   | MSB → LSB                                           |

#### Why t\[i] can be "out of bounds" safely

`t` is computed for every element in parallel — but only **used** for elements where bit=1. If bit=0, Step 6 picks `f[i]` instead. The GPU computes all values simultaneously; the filter happens at Step 6.

#### Exam angle

> 📝 2019, 2022, 2025 — 12 marks "Describe, in detail with the aid of an example, the operation of radix sort using data parallel primitives"

**Expected answer:** definition + why O(n) + 6 steps with formulas + full worked example + "repeat for remaining bits"

#### Watch out for

* Always include the definition: _"non-comparative, O(n), one bit at a time MSB→LSB"_
* Always show the worked example — "with the aid of an example" = marks lost without it
* Always end with: _"Repeat for remaining bits in decreasing order of significance"_

***

### 9. Monte Carlo Integration

#### One-liner

Approximate a definite integral by randomly sampling N points and averaging the function values — more samples = better accuracy.

#### The Analogy

Throwing darts randomly at a square with a circle inside. The ratio of darts inside the circle to total darts approximates π/4. The more darts, the better the estimate.

#### How it works — estimating π

Quarter circle in first quadrant: `x² + y² ≤ 1`, x: 0→1, y: 0→1

```
π/4 ≈ (points inside circle) / (total points)
π   = 4 × (points inside) / N
```

**DPP recipe:**

| Step | Primitive   | Purpose                                                |
| ---- | ----------- | ------------------------------------------------------ |
| 1    | `sequence`  | Generate thread segment indices `[0,1,2,3...]`         |
| 2    | `transform` | Each thread runs MonteCarlo on its segment in parallel |
| 3    | `reduce`    | Sum all inside counts                                  |
| 4    | arithmetic  | `4 × total / N` → π estimate                           |

#### The random number problem

The pseudo-random number generator must NOT become a sequential bottleneck.

**Two solutions:**

1. **Different seed per thread** — efficient time-wise but can produce common subsequences between threads
2. **Same seed, partition the sequence** — all threads use the same seed; each thread discards the numbers belonging to previous threads before generating its own. Removes common subsequences but at cost of possibly duplicated computation.

#### Pseudo-code

```cpp
struct MonteCarlo
{
    int seed;
    int points_per_thread;

    MonteCarlo(int s, int p) : seed(s), points_per_thread(p) {}

    // operator() is called by transform for each thread segment index
    long operator()(int segment)
    {
        float x, y, distance;
        long inside = 0;

        urnq.set_seed(seed);  // same seed for all threads

        // skip this thread's predecessors' random numbers
        // segment * 2 because each point needs x AND y
        for (int i = 0; i < segment * 2 * points_per_thread; i++)
        {
            x = urnq.get();  // discard — these belong to other threads
        }

        // generate this thread's actual points
        for (int i = 0; i < points_per_thread; i++)
        {
            x = urnq.get();
            y = urnq.get();
            distance = x*x + y*y;
            if (distance <= 1.0)  // inside unit circle?
                inside++;
        }

        return inside;  // count for this segment
    }
};

float integrate(int points, int points_per_thread)
{
    int N = points;

    // sequence: generate thread indices [0, 1, 2, ...]
    vector v[N / points_per_thread];
    v.sequence(v.begin(), v.end(), 0, 1);

    // transform: run MonteCarlo on each segment in parallel
    // each element becomes the inside count for that segment
    transform(v.begin(), v.end(), v.begin(),
              MonteCarlo(0, points_per_thread));

    // reduce: sum all inside counts into one total
    long result = reduce(v.begin(), v.end(), 0, plus());

    // arithmetic: π ≈ 4 × (points inside quarter circle) / total points
    return 4 * (float)result / N;
}
```

#### Exam angle

> 📝 2022R, 2023R, 2025R — 14 marks "Using data-parallel primitives, write a pseudo-code implementation of Monte Carlo integration. Pay special attention to the pseudo-random number generator"

**Expected answer:** definition + formula + pseudo-code with comments + RNG problem explained + two solutions

#### Watch out for

The RNG problem is **explicitly asked** in the question — always worth 3 marks. Never skip it. Two solutions must be named.

***

### 10. Boost.Compute

#### One-liner

A C++ library providing a high-level DPP interface to OpenCL for CPU and GPU computing.

#### How it works

* Based on OpenCL — works on any GPU or CPU
* Provides containers: `vector<T>`, `array<T,N>` for host and device
* Provides DPP algorithms: `transform()`, `sort()`, `reduce()`
* Provides iterators: `transform_iterator`, `permutation_iterator`
* Provides lambda expressions and asynchronous memory transfers
* Pattern: get device → create context → create queue → copy to device → compute → copy back

#### Key code — mean square root of 100 floats

```cpp
// Step 1 — setup: get device, context, command queue
compute::device device = compute::system::default_device();
compute::context context(device);
compute::command_queue queue(context, device);

// Step 2 — create host vector with 100 floats and fill with data
std::vector<float> host_vector(100);
std::generate(host_vector.begin(), host_vector.end(), rand);

// Step 3 — create device vector and copy host → device
compute::vector<float> device_vector(100, context);
compute::copy(
    host_vector.begin(), host_vector.end(),
    device_vector.begin(),
    queue);  // queue sends the copy command to the GPU

// Step 4 — transform: apply sqrt to every element in parallel on device
compute::transform(
    device_vector.begin(),   // input start
    device_vector.end(),     // input end
    device_vector.begin(),   // output (in-place)
    compute::sqrt<float>(),  // function to apply
    queue);

// Step 5 — reduce: sum all sqrt values into one float
float sum = 0.0f;
compute::reduce(
    device_vector.begin(),
    device_vector.end(),
    &sum,                       // output destination
    compute::plus<float>(),     // binary operation — MUST specify
    queue);

// Step 6 — arithmetic: divide by N to get mean
float mean = sum / 100.0f;

// Step 7 — copy results back to host (if needed)
compute::copy(
    device_vector.begin(), device_vector.end(),
    host_vector.begin(),
    queue);
```

#### Exam angle

> 📝 2021, 2024 — 4 marks (outline) + 10 marks (code) 5(b): "Outline the Boost.compute library" 5(c): "Sketch the code to calculate the mean square root of a vector of 100 floats"

**Expected answer for outline:** OpenCL-based, C++ library, containers, algorithms, iterators, lambda, async transfers. **Expected answer for code:** setup + copy to device + transform(sqrt) + reduce(plus) + divide by N + copy back.

#### Watch out for

* Always include `compute::plus<float>()` in reduce — without it, compiler doesn't know HOW to reduce
* Mean square root ≠ square root of the mean. Sqrt first, then average.
* Don't confuse Boost.Compute (OpenCL) with Thrust (CUDA)

***

### 11. Thrust

#### One-liner

A C++ template library for CUDA based on the STL — same idea as Boost.Compute but for NVIDIA GPUs.

#### How it works

|             | Boost.Compute                  | Thrust                         |
| ----------- | ------------------------------ | ------------------------------ |
| Backend     | OpenCL                         | CUDA                           |
| Vectors     | `host_vector`, `device_vector` | `host_vector`, `device_vector` |
| Algorithms  | transform, sort, reduce        | transform, sort, reduce        |
| Key feature | standard DPP calls             | `transform_reduce` (combined)  |

#### Key code — norm of a vector

```cpp
// transfer to device
thrust::device_vector<float> dx(x, x+4);

// define operations
square<float> unary_op;         // custom transform: x → x*x
thrust::plus<float> binary_op;  // reduce operation: sum
float init = 0;

// transform_reduce: combines transform + reduce in ONE call (more efficient)
float norm = std::sqrt(
    thrust::transform_reduce(
        dx.begin(), dx.end(),
        unary_op,   // apply square to each element
        init,       // initial value for reduction
        binary_op   // sum all squared values
    )
);
```

#### Exam angle

> 📝 Not asked as a code question — description only (4 marks max) Know: CUDA-based, STL-based, host\_vector + device\_vector, transform\_reduce

#### Watch out for

Thrust uses `transform_reduce` as a single combined call — unlike Boost.Compute which calls them separately.

***

### 12. Past Paper Q\&A

***

#### Type A — describe primitives + Radix Sort

**Years:** 2019, 2022, 2025 **Marks:** 8 + 12 = 20

**5(a) Question:** "Describe the following data-parallel primitives: transform, reduce, prefix sum" \[8 marks]

**Full answer:**

**Transform:** Transform applies a user-supplied function to each element of an input vector and stores the results in an output vector. The size of the output vector is always the same as the input vector.

Example: `[1,2,3,4]` → square each element → `[1,4,9,16]`

Syntax: `transform(v.begin(), v.end(), v.begin(), f)`

**Reduce:** Reduce applies a binary operation across all elements of a vector to produce a single value. An initial value is supplied as the starting point for the operation.

Example: `[1,2,3,4]` → sum → `10`

Syntax: `reduce(v.begin(), v.end(), 0, plus())`

**Prefix Sum (Scan):** A scan produces a running total of a vector using a binary associative operator. There are two types:

* **Inclusive scan** — each output element includes the current element in the running total.

Example: `[1,2,3,4]` → `[1,3,6,10]`

* **Exclusive scan** — each output element excludes the current element. It starts with the identity value 0.

Example: `[1,2,3,4]` → `[0,1,3,6]`

***

**5(b) Question:** "Describe, in detail with the aid of an example, the operation of radix sort using data parallel primitives" \[12 marks]

**Full answer:**

Radix sort is a non-comparative sorting algorithm that sorts a vector of integers one bit at a time, from the most significant bit (MSB) to the least significant bit (LSB). Because it never compares elements against each other it achieves O(n) time complexity, breaking the O(n log n) barrier of comparison-based sorting. Each pass splits the vector into two groups — elements whose splitting bit is 0 (false group, go left) and elements whose splitting bit is 1 (true group, go right) — preserving relative ordering within each group.

**The 6-step recipe per pass:**

**Step 1 — Convert to binary:** Convert all numbers to binary, padding to the same number of bits determined by the largest number. Number of passes = number of bits.

**Step 2 — Generate vector `e`:** Apply transform to extract the splitting bit. Set `e[i] = 1` if splitting bit = 0 (false element), `e[i] = 0` otherwise.

**Step 3 — Generate vector `f`:** Apply exclusive scan to `e`. Gives each false element its destination index.

**Step 4 — Compute `total_false`:** `total_false = e[n-1] + f[n-1]` — total false elements and starting position for true group.

**Step 5 — Generate vector `t`:** `t[i] = i - f[i] + total_false` — true destination for each element.

**Step 6 — Generate destination map `d`:** If splitting bit = 1 (true) → `d[i] = t[i]`. If splitting bit = 0 (false) → `d[i] = f[i]`.

**Step 7 — Scatter:** Scatter input using `d`. False elements → positions 0 to total\_false-1. True elements → positions total\_false to n-1.

**Worked example — Pass 1 (MSB):**

```
input:   [4,   7,   2,   6,   3,   5,   1,   0  ]
binary:  [100, 111, 010, 110, 011, 101, 001, 000]
MSB:     [1,   1,   0,   1,   0,   1,   0,   0  ]
e:       [0,   0,   1,   0,   1,   0,   1,   1  ]
f:       [0,   0,   0,   1,   1,   2,   2,   3  ]
total_false = 4
t:       [4,   5,   6,   6,   7,   7,   8,   8  ]
d:       [4,   5,   0,   6,   1,   7,   2,   3  ]
output:  [2,   3,   1,   0,   4,   7,   6,   5  ]
```

Repeat for bit 1 and bit 0. After 3 passes → `[0,1,2,3,4,5,6,7]` ✅

***

#### Type B — vector datatype + scan/scatter + Stream Compaction

**Years:** 2019R, 2020, 2023 **Marks:** 4 + 6 + 10 = 20

**5(a) Question:** "What is the main datatype used in data-parallel primitives? What feature effectively handles different memory spaces?" \[4 marks]

**Full answer:** The main datatype is the **vector**. The feature that effectively handles different memory spaces is its **iterators** — they allow the developer to process elements of the vector regardless of which memory space it resides in (host, global, local, or private memory). A plain pointer would be invalid across memory spaces, but iterators abstract over this complexity.

***

**5(b) Question:** "In the context of data-parallel primitives, describe: scan, scatter" \[6 marks]

**Full answer:**

**Scan (Prefix Sum):** A scan produces a running total of a vector using a binary associative operator. There are two types:

* **Inclusive scan** — produces a running total including the current element.

Example: `[1,2,3,4]` → `[1,3,6,10]`

* **Exclusive scan** — produces a running total excluding the current element, starting with identity value 0.

Example: `[1,2,3,4]` → `[0,1,3,6]`

Syntax: `exclusive_scan(v.begin(), v.end(), output.begin(), 0)`

**Scatter:** Scatter copies elements of a vector to specific positions in an output vector based on a supplied map vector. For each element at position `i`, it is placed at `output[map[i]]`.

Example:

```
input:  [A, B, C, D]
map:    [3, 0, 2, 1]
output: [B, D, C, A]
```

Syntax: `scatter(v.begin(), v.end(), map.begin(), output.begin())`

***

**5(c) Question:** "Using data parallel primitives describe an efficient stream compaction algorithm" \[10 marks]

**Full answer:**

Stream compaction removes unwanted elements from a large sparse vector based on a predicate, compacting it into a smaller dense vector containing only the surviving elements. The `copy_if` primitive provides this functionality but is not easily parallelised — each thread must wait to know how many previous elements passed the predicate before writing output, creating a sequential dependency. Instead we use transform, exclusive scan, and scatter which have efficient parallel implementations.

**Algorithm — 3 steps:**

Consider `[6,2,9,8,1,5,7,3]` with predicate `> 5`.

**Step 1 — Transform:** Apply transform with predicate to generate a flag vector. Set `flag[i] = 1` if predicate true, `0` otherwise.

```
input:  [6, 2, 9, 8, 1, 5, 7, 3]
flags:  [1, 0, 1, 1, 0, 0, 1, 0]
```

**Step 2 — Exclusive scan:** Apply exclusive scan to flags. Each surviving element's value = number of surviving elements before it = its output position.

```
flags:   [1, 0, 1, 1, 0, 0, 1, 0]
exscan:  [0, 1, 1, 2, 3, 3, 3, 4]
```

**Step 3 — Scatter:** Scatter original vector using exclusive scan as destination map, only where flag = 1.

```
output: [6, 9, 8, 7]
```

**Why exclusive and not inclusive scan:** Exclusive scan gives the position based on how many surviving elements came _before_ the current element — the correct 0-based output index for scatter. Inclusive scan would be off by one.

**Why this is efficient:** All three steps operate independently on each element and run fully in parallel with no sequential dependencies.

***

#### Type C — describe primitives + Monte Carlo

**Years:** 2022R, 2023R, 2025R **Marks:** 6 + 14 = 20

**5(a) Question:** "Describe: transform, reduce, scan" \[6 marks]

_(Same as Type A — see above)_

***

**5(b) Question:** "Using data-parallel primitives, write a pseudo-code implementation of Monte Carlo integration. Pay special attention to the pseudo-random number generator to ensure that it can run in parallel and clearly describe how it avoids any adverse side effects." \[14 marks]

**Full answer:**

Monte Carlo integration approximates a definite integral by randomly sampling N points across a region and averaging the function values. The approximation `I = Vf̄ ± V√((f²-f̄²)/(N-1))` improves as N increases — the error term reduces as N grows.

**The random number problem:** The pseudo-random number generator must not become a sequential bottleneck — it must run concurrently. Two solutions:

1. **Different seed per thread** — each thread gets a different seed. Efficient time-wise but can still produce common subsequences between threads.
2. **Same seed, partition the sequence** — all threads use the same seed but each thread discards the random numbers belonging to previous threads before generating its own. Removes common subsequences but at the cost of possibly duplicated computation.

The implementation below uses solution 2.

**Pseudo-code:**

```cpp
struct MonteCarlo
{
    int seed;
    int points_per_thread;

    MonteCarlo(int s, int p) : seed(s), points_per_thread(p) {}

    // called by transform for each thread segment index
    long operator()(int segment)
    {
        float x, y, distance;
        long inside = 0;

        urnq.set_seed(seed);  // same seed for all threads

        // discard previous threads' random numbers
        // multiply by 2 because each point needs x AND y
        for (int i = 0; i < segment * 2 * points_per_thread; i++)
        {
            x = urnq.get();  // throw away
        }

        // generate this thread's points
        for (int i = 0; i < points_per_thread; i++)
        {
            x = urnq.get();
            y = urnq.get();
            distance = x*x + y*y;
            if (distance <= 1.0)  // inside unit circle?
                inside++;
        }

        return inside;
    }
};

float integrate(int points, int points_per_thread)
{
    int N = points;

    // sequence: generate thread indices [0, 1, 2, ...]
    vector v[N / points_per_thread];
    v.sequence(v.begin(), v.end(), 0, 1);

    // transform: each thread runs MonteCarlo on its own segment
    transform(v.begin(), v.end(), v.begin(),
              MonteCarlo(0, points_per_thread));

    // reduce: sum all inside counts
    long result = reduce(v.begin(), v.end(), 0, plus());

    // π ≈ 4 × (inside quarter circle) / total points
    return 4 * (float)result / N;
}
```

**Why this is parallelisable:** Each thread operates on its own independent segment of random numbers — no shared state, no locks, no sequential dependencies. Transform runs all threads simultaneously. ✅

***

#### Type D — zip/transform/inner product + Boost.Compute

**Years:** 2021, 2024 **Marks:** 6 + 4 + 10 = 20

**5(a) Question:** "Describe: zip, transform, inner product. Give an example of each." \[6 marks]

**Full answer:**

**zip:** Zip takes two vectors and creates a vector of pairs where elements are matched positionally — element 0 from v with element 0 from w, element 1 with element 1, and so on. Output size equals input size.

Example: `v=[1,2,3]`, `w=[A,B,C]` → `[(1,A),(2,B),(3,C)]`

**transform:** Transform applies a user-supplied function to each element of an input vector and stores results in an output vector. Output size always equals input size.

Example: `[1,2,3,4]` → square each → `[1,4,9,16]`

**inner product:** Inner product operates on two vectors. It applies a "multiplication" function `f` to each pair of elements positionally, then reduces all results using an "addition" function `g`. Custom `f` and `g` can be supplied.

Example:

```
v = [1,2,3], w = [4,5,6]
step 1 (transform pairs): [1×4, 2×5, 3×6] = [4,10,18]
step 2 (reduce):           4+10+18 = 32
```

***

**5(b) Question:** "Outline the Boost.compute library." \[4 marks]

**Full answer:**

Boost.Compute is a C++ library that provides a high-level interface to multi-core CPU and GPGPU computing platforms based on OpenCL. It is a component of the official Boost library.

It provides:

* **Containers** — `vector<T>`, `array<T,N>` for both host and device memory
* **DPP algorithms** — `transform()`, `sort()`, `reduce()`
* **Iterators** — `transform_iterator`, `permutation_iterator`
* **Lambda expressions** — custom functions on device
* **Asynchronous memory transfers** — between host and device

***

**5(c) Question:** "Using the Boost.compute library, sketch the code to calculate the mean square root of each element of a vector of 100 floating point numbers." \[10 marks]

**Full answer:**

The mean square root is computed in three steps — apply sqrt to every element using transform, sum all results using reduce, then divide by 100 to get the mean.

```cpp
// setup: get device, context, queue
compute::device device = compute::system::default_device();
compute::context context(device);
compute::command_queue queue(context, device);

// create and fill host vector
std::vector<float> host_vector(100);
std::generate(host_vector.begin(), host_vector.end(), rand);

// create device vector and copy host → device
compute::vector<float> device_vector(100, context);
compute::copy(
    host_vector.begin(), host_vector.end(),
    device_vector.begin(), queue);

// transform: apply sqrt to every element in parallel on device
compute::transform(
    device_vector.begin(),
    device_vector.end(),
    device_vector.begin(),   // in-place
    compute::sqrt<float>(),  // function
    queue);

// reduce: sum all sqrt values — MUST specify plus() as binary op
float sum = 0.0f;
compute::reduce(
    device_vector.begin(),
    device_vector.end(),
    &sum,
    compute::plus<float>(),  // how to combine — do not omit
    queue);

// mean = sum of square roots / N
float mean = sum / 100.0f;

// copy back to host
compute::copy(
    device_vector.begin(), device_vector.end(),
    host_vector.begin(), queue);
```

**Code description:**

* `default_device()` gets the default OpenCL device (GPU or CPU)
* `context` and `command_queue` set up the OpenCL execution environment
* `compute::copy` host→device transfers data to GPU memory
* `compute::transform` applies `sqrt` to every element in parallel on device
* `compute::reduce` sums all sqrt values into a single float — `plus<float>()` must be specified
* Dividing by 100 gives the mean square root
* Final `compute::copy` transfers the result back to the host

***

### 13. Code Patterns

#### Pattern 1 — Stream Compaction

```cpp
// input vector and predicate
vector<float> input = {6, 2, 9, 8, 1, 5, 7, 3};

// step 1: transform — apply predicate to get flags
vector<int> flags(input.size());
transform(input.begin(), input.end(), flags.begin(), [](float x){ return x > 5 ? 1 : 0; });
// flags: [1, 0, 1, 1, 0, 0, 1, 0]

// step 2: exclusive scan — compute output positions
vector<int> positions(flags.size());
exclusive_scan(flags.begin(), flags.end(), positions.begin(), 0);
// positions: [0, 1, 1, 2, 3, 3, 3, 4]

// step 3: scatter — place surviving elements at their positions
int output_size = positions.back() + flags.back();  // total survivors
vector<float> output(output_size);
scatter_if(input.begin(), input.end(), positions.begin(), flags.begin(), output.begin());
// output: [6, 9, 8, 7]
```

***

#### Pattern 2 — Radix Sort (one pass)

```cpp
// Given: binary input, splitting on MSB

// step 1: generate e — flag false elements (bit=0 → e=1)
vector<int> e = {0,0,1,0,1,0,1,1};

// step 2: exclusive scan → f (false destinations)
vector<int> f(e.size());
exclusive_scan(e.begin(), e.end(), f.begin(), 0);
// f: [0,0,0,1,1,2,2,3]

// step 3: total_false
int total_false = e.back() + f.back();  // = 4

// step 4: compute t (true destinations)
// t[i] = i - f[i] + total_false
vector<int> t(e.size());
for (int i = 0; i < t.size(); i++)
    t[i] = i - f[i] + total_false;
// t: [4,5,6,6,7,7,8,8]

// step 5: generate d (destination map)
// bit=1 → use t[i], bit=0 → use f[i]
vector<int> d(e.size());
for (int i = 0; i < d.size(); i++)
    d[i] = (splitting_bit[i] == 1) ? t[i] : f[i];
// d: [4,5,0,6,1,7,2,3]

// step 6: scatter using d
scatter(input.begin(), input.end(), d.begin(), output.begin());
```

***

#### Pattern 3 — Monte Carlo Integration (DPP structure)

```cpp
// Monte Carlo to estimate π using DPP

// step 1: sequence — generate thread segment indices
int num_segments = N / points_per_thread;
vector<int> v(num_segments);
v.sequence(v.begin(), v.end(), 0, 1);
// v: [0, 1, 2, 3, ...]

// step 2: transform — each thread runs its segment independently
transform(v.begin(), v.end(), v.begin(),
          MonteCarlo(seed, points_per_thread));
// v: [count0, count1, count2, ...]

// step 3: reduce — sum all inside counts
long total = reduce(v.begin(), v.end(), 0, plus());

// step 4: arithmetic — estimate π
float pi = 4 * (float)total / N;
```

***

#### Pattern 4 — Boost.Compute Core Pattern

```cpp
// The always-the-same Boost.Compute pattern:

// 1. setup
compute::device device = compute::system::default_device();
compute::context context(device);
compute::command_queue queue(context, device);

// 2. host → device
compute::copy(host.begin(), host.end(), device_vec.begin(), queue);

// 3. transform on device (in-place)
compute::transform(
    device_vec.begin(), device_vec.end(),
    device_vec.begin(),
    compute::sqrt<float>(),  // or any function
    queue);

// 4. reduce on device
float result = 0.0f;
compute::reduce(
    device_vec.begin(), device_vec.end(),
    &result,
    compute::plus<float>(),  // NEVER forget this
    queue);

// 5. device → host
compute::copy(device_vec.begin(), device_vec.end(), host.begin(), queue);
```

***

### 14. Weak Spots Flagged This Session

* **Prefix sum blanked initially** — use cricket scoreboard analogy to recall: inclusive = score after over, exclusive = score before over
* **Radix Sort missing definition + example** — always open with "non-comparative, O(n), MSB→LSB" and always show the full vector table
* **Boost.Compute missing `plus<float>()` in reduce** — reduce MUST have a binary operation specified
* **Monte Carlo RNG problem** — always worth 3 marks, always explicitly asked — never skip it
* **Scatter rule confusion** — `d[i]`: bit=1 uses `t`, bit=0 uses `f` (not the other way round)
* **Exclusive scan arithmetic** — when input has 0, running total stays same — don't increment
