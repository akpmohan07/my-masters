# OpenCL Past Exam Questions (2019–2025)

## CSC1141 — OpenCL Past Exam Questions (2019–2025)

> **How to use this file:**
>
> * Questions of the **same type** across years → one full answer provided, others marked `📝 See answer above`
> * Question types: (a) Models/Concepts, (b) Synchronisation/Language, (c) Kernel writing

***

### Question Type Map

| Year | (a)                                     | (b)                                        | (c)                                                   |
| ---- | --------------------------------------- | ------------------------------------------ | ----------------------------------------------------- |
| 2019 | Platform Model                          | Basic steps in host program                | Kernel — Sobel edge detection                         |
| 2020 | Execution Model                         | Synchronisation (cross work-group vs same) | Kernel — max of above/below pixel                     |
| 2021 | Memory Model + advantages/disadvantages | Type casting + address space qualifiers    | Kernel — image smoothing (average neighbours)         |
| 2022 | Execution Model                         | Synchronisation                            | Kernel — min of left/right pixel                      |
| 2023 | Memory Model (diagram)                  | Synchronisation (with sample code)         | Kernel — matrix multiply (private A row, local B col) |
| 2024 | Memory Model (diagram)                  | Type casting + address space qualifiers    | Kernel — image smoothing (4 neighbours)               |
| 2025 | Memory Model (diagram)                  | Execution Model                            | Kernel — horizontal edge detection (private rows)     |

***

***

## PART A — Models & Concepts

***

### Platform Model

#### 2019 — 4(a) \[4 Marks]

> Describe, with the aid of a diagram, OpenCL's Platform Model.

**✅ Full Answer**

The OpenCL Platform Model describes how the host and OpenCL devices are physically organised.

**Hierarchy (4 levels):**

```
Host
 └── OpenCL Device (one or more)
      └── Compute Unit (one or more per device)
           └── Processing Element (one or more per compute unit)
```

**Diagram:**

```
┌─────────────────────────────────────────┐
│                  HOST                   │
│         (standard OS computer)          │
└──────────────────┬──────────────────────┘
                   │
       ┌───────────┴───────────┐
       ▼                       ▼
┌─────────────┐         ┌─────────────┐
│  Device 1   │   ...   │  Device N   │
│  (e.g. GPU) │         │  (e.g. CPU) │
│ ┌─────────┐ │         └─────────────┘
│ │Compute  │ │
│ │Unit     │ │
│ │ ┌─────┐ │ │
│ │ │ PE  │ │ │
│ │ │ PE  │ │ │
│ │ └─────┘ │ │
│ └─────────┘ │
└─────────────┘
```

**What each level does:**

* **Host** — a standard computer running a standard OS. Controls everything. Transfers data to/from devices and coordinates execution. Never executes kernels itself.
* **OpenCL Device** — the parallel compute device (GPU, CPU, DSP). Executes kernels.
* **Compute Unit** — a subdivision of the device (like one GPU core cluster). Work-items in the same work-group must run on the same compute unit.
* **Processing Element** — the actual execution unit where one work-item runs. Either SIMD (GPUs — all PEs execute the same instruction) or SPMD (CPUs/DSPs — each PE can be at a different point in the program).

***

***

### Execution Model

#### 2020 — 4(a) \[5 Marks]

> Describe the Execution Model in OpenCL.

**✅ Full Answer**

The OpenCL Execution Model has two components: **kernels** and a **host program**.

**Kernel:** OpenCL performs parallel computation by defining the problem on an N-dimensional index space (NDRange), where N is 1, 2, or 3. Each element in this space is called a **work-item**. The code executed on each work-item is called a **kernel**.

Work-items can be grouped into **work-groups**. All work-items in the same work-group must execute on the same compute device. Synchronisation between work-items can only occur between work-items in the **same work-group**.

Each work-item knows its position via:

* `get_global_id(dim)` — position in the full NDRange
* `get_local_id(dim)` — position within its work-group
* `get_group_id(dim)` — which work-group it belongs to

**Example — 2D NDRange:**

```
32×32 global work-items, organised into 16 work-groups of 8×8.
Work-item at global (28,10):
  - work-group ID = (28/8, 10/8) = (3, 1)
  - local ID      = (28%8, 10%8) = (4, 2)
```

**Host Program:** The host program defines contexts for managing kernel execution. It creates and manipulates:

* Devices
* Program Objects (kernel source + compiled executable)
* Kernels
* Memory Objects (buffers shared between host and device)

There are two types of kernels:

* **OpenCL kernels** — written in OpenCL C, compiled at **runtime** for the target device. Portable.
* **Native kernels** — device-specific, not supported on all devices.

***

#### 2022 — 4(a) \[5 Marks]

> Describe, in your own words, the Execution Model in OpenCL.

📝 **Same question as 2020 4(a). See answer above.**

***

#### 2025 — 4(b) \[6 Marks]

> Describe the execution model in OpenCL.

📝 **Same question as 2020 4(a). See answer above.** (Worth 6 marks — add more detail on NDRange dimensions and the two kernel types.)

***

***

### Memory Model

#### 2021 — 4(a) \[6 Marks]

> Describe, using a diagram, the Memory Model used in OpenCL. Describe the advantages and disadvantages of each type of memory in this model.

**✅ Full Answer**

**Diagram:**

```
┌─────────────────────────────────────┐
│            HOST MEMORY              │
│     (normal RAM on the CPU side)    │
└──────────────┬──────────────────────┘
               │  (data transferred via clEnqueueWriteBuffer / ReadBuffer)
               ▼
┌─────────────────────────────────────┐
│       GLOBAL / CONSTANT MEMORY      │
│   Visible to ALL work-items         │
│   Host can read & write at runtime  │
└──────────────┬──────────────────────┘
               ▼
┌─────────────────────────────────────┐
│           LOCAL MEMORY              │
│   Shared within a work-group only   │
│   NOT accessible by host            │
└──────────────┬──────────────────────┘
               ▼
┌─────────────────────────────────────┐
│          PRIVATE MEMORY             │
│   One work-item only                │
│   NOT accessible by host            │
└─────────────────────────────────────┘
```

**Each memory type:**

| Memory       | Visible to                    | Host access  | Advantages                                                       | Disadvantages                                                                 |
| ------------ | ----------------------------- | ------------ | ---------------------------------------------------------------- | ----------------------------------------------------------------------------- |
| **Global**   | All work-items                | Read & Write | Large capacity, flexible                                         | Slowest — high latency, contention when many work-items access simultaneously |
| **Constant** | All work-items (read only)    | Read & Write | Cached — faster than global for read-only data                   | Read only from kernel — cannot be written by work-items                       |
| **Local**    | Work-items in same work-group | None         | Much faster than global, shared within group enables cooperation | Limited size, not accessible from host, requires explicit management          |
| **Private**  | One work-item only            | None         | Fastest — register-level speed                                   | Very limited size, not shared                                                 |

**Key rule:** Default address space is **private**. Variables declared in a kernel without a qualifier go into private memory.

***

#### 2023 — 4(a) \[4 Marks]

> Describe the memory model used in OpenCL using a carefully labeled diagram.

📝 **Same as 2021 4(a) — diagram + 4 memory types. See answer above.** (4 marks — focus on diagram + one line per memory type, skip advantages/disadvantages table.)

***

#### 2024 — 4(a) \[6 Marks]

> Describe, using a diagram, the Memory Model used in OpenCL.

📝 **Same as 2021 4(a). See answer above.**

***

#### 2025 — 4(a) \[4 Marks]

> Describe, using a diagram, the memory model in OpenCL.

📝 **Same as 2021 4(a). See answer above.** (4 marks — diagram + brief description of each memory type.)

***

***

## PART B — Concepts & Language

***

### Basic Steps in a Host Program

#### 2019 — 4(b) \[6 Marks]

> Describe the basic steps in an OpenCL host program.

**✅ Full Answer**

An OpenCL host program follows these steps in order:

1. **Query the host for OpenCL devices** — use `clGetPlatformIDs()` to find available platforms, then `clGetDeviceIDs()` to find devices (GPU, CPU etc.)
2. **Create a context** — use `clCreateContext()` to create a workspace that associates the selected devices. The context manages command queues, program objects, kernels, and memory objects.
3. **Create a command queue** — use `clCreateCommandQueue()` to create a channel for sending commands (write buffer, execute kernel, read buffer) to the device.
4. **Create kernels** — use `clCreateProgramWithSource()` to load the kernel source, `clBuildProgram()` to compile it at runtime, and `clCreateKernel()` to create a kernel object for a specific kernel function.
5. **Create memory objects** — use `clCreateBuffer()` to allocate memory buffers on the device for input and output data.
6. **Copy data to the device** — use `clEnqueueWriteBuffer()` to transfer input data from host memory into device buffers.
7. **Set kernel arguments and submit for execution** — use `clSetKernelArg()` to bind buffers to kernel parameters, then `clEnqueueNDRangeKernel()` to launch the kernel across the NDRange. Call `clFinish()` to block until execution completes.
8. **Copy results back to the host** — use `clEnqueueReadBuffer()` to transfer results from device buffers back to host memory.
9. **Clean up** — release all OpenCL resources with `clReleaseMemObject()`, `clReleaseKernel()`, `clReleaseProgram()`, `clReleaseCommandQueue()`, `clReleaseContext()`.

**Why this order?** Each step depends on the previous — you cannot create a context without a device, cannot compile a kernel without a context, cannot set arguments without a kernel and buffers.

***

***

### Synchronisation

#### 2020 — 4(b) \[5 Marks]

> Describe how synchronisation between work-items in different work-groups is achieved in OpenCL. How does this differ from synchronisation between work-items in the same work-group?

**✅ Full Answer**

**Within the same work-group:** Work-items in the same work-group can synchronise using:

* `barrier(CLK_LOCAL_MEM_FENCE)` — blocks until ALL work-items in the work-group have reached this point, then ensures all local memory reads/writes are visible to all work-items.
* `barrier(CLK_GLOBAL_MEM_FENCE)` — same but for global memory.
* `mem_fence(mem_fence_flag)` — waits until all reads/writes prior to the call are visible to all work-items in the work-group, but does NOT block until all work-items reach the same point.

This is possible because all work-items in the same work-group execute on the **same compute unit** and share local memory.

**Across different work-groups:** Direct synchronisation between work-items in different work-groups is **not possible** within a kernel. Work-items in different work-groups may run on different compute units.

Instead, synchronisation across work-groups is achieved using **events** in the host program:

* All `clEnqueue*` functions accept an event wait list and can produce an event on completion.
* `clWaitForEvents(num_events, event_list)` blocks the host until the specified events complete.
* Kernels submitted to different command queues on different devices can be synchronised by making one kernel wait on the event produced by another.

**Key difference:**

> Same work-group → `barrier()` inside the kernel. Different work-groups → event wait lists managed by the host.

***

#### 2022 — 4(b) \[5 Marks]

> Describe, in your own words, how synchronisation is achieved in OpenCL.

📝 **Same topic as 2020 4(b). See answer above.**

***

#### 2023 — 4(b) \[6 Marks]

> Describe, with the aid of sample code, how synchronisation is accomplished in OpenCL.

📝 **Same topic as 2020 4(b). See answer above.** Add code example:

```c
// Within same work-group — barrier
for (k = ilocal; k < Pdim; k = k + nloc)
{
    Blocal[k] = B[k * Pdim + j];
}
barrier(CLK_LOCAL_MEM_FENCE);  // wait for all work-items to finish loading
// now safe to read Blocal
```

```c
// Host side — cross work-group via events
cl_event kernel_event;
clEnqueueNDRangeKernel(queue1, kernel1, ..., NULL, &kernel_event);
// kernel2 waits for kernel1 to complete
clEnqueueNDRangeKernel(queue2, kernel2, ..., 1, &kernel_event, NULL);
// or block host:
clWaitForEvents(1, &kernel_event);
```

***

***

### Type Casting & Address Space Qualifiers

#### 2021 — 4(b) \[4 Marks]

> Describe type casting and address space qualifiers in OpenCL C.

**✅ Full Answer**

**Address Space Qualifiers:**

The OpenCL memory model has 4 address spaces, each with a corresponding qualifier:

| Qualifier  | Address space                              | Default? |
| ---------- | ------------------------------------------ | -------- |
| `global`   | Global memory — visible to all work-items  | No       |
| `constant` | Constant memory — read-only for work-items | No       |
| `local`    | Local memory — shared within work-group    | No       |
| `private`  | Private memory — one work-item only        | **Yes**  |

By default, variables declared in a kernel are in **private** memory.

```c
global float *ptr;   // ptr is in private memory but POINTS to global memory
int4 v;              // private memory by default
local float *shared; // points to local memory
```

Image objects can be qualified as `read_only` or `write_only`.

**Type Casting:**

OpenCL C allows implicit and explicit casting between **scalar** types as per C99:

```c
int i = 5;
float f = (float) i;  // explicit cast
```

Type casting between **vector types is NOT allowed** implicitly or explicitly. Instead, OpenCL provides built-in conversion functions:

```c
convert_<destination_type>(source)
```

Example:

```c
int4 i;
float4 f = convert_float4(i);  // correct way to convert vector types
```

***

#### 2024 — 4(b) \[4 Marks]

> Describe type casting and address space qualifiers in OpenCL C.

📝 **Identical to 2021 4(b). See answer above.**

***

***

## PART C — Kernel Writing (10 marks each)

> **General pattern for every kernel question:**
>
> 1. Write the kernel
> 2. Describe how the host configures the problem space (NDRange dimensions)
> 3. Describe how to improve runtime (move data from global → private/local memory)

***

### Image Processing Kernels

#### 2020 — 4(c) \[10 Marks]

> Write an OpenCL kernel that processes an image by setting the value of a pixel to the maximum of the pixel immediately above it and the pixel immediately below it. Describe how the host program would configure the problem space for your kernel. How might you improve the run-time of your complete OpenCL program, host and kernel?

**✅ Full Answer**

**Kernel:**

```c
kernel void max_above_below(global const float *input,
                             global float *output,
                             int width, int height)
{
    int x = get_global_id(0);  // column
    int y = get_global_id(1);  // row

    // handle border pixels — skip edges
    if (x >= width || y <= 0 || y >= height - 1)
        return;

    float above = input[(y - 1) * width + x];
    float below = input[(y + 1) * width + x];

    output[y * width + x] = (above > below) ? above : below;
}
```

**Problem space configuration:**

* 2D NDRange — one work-item per pixel.
* Global dimensions: `width × height` (e.g. 512 × 512).
* Local dimensions: e.g. `8 × 8` work-group size.

```c
size_t global[2] = {width, height};
size_t local[2]  = {8, 8};
clEnqueueNDRangeKernel(queue, kernel, 2, NULL, global, local, 0, NULL, NULL);
```

**Improving runtime:**

* Each work-item reads 2 pixels from global memory. Adjacent work-items in the same work-group read overlapping rows.
* **Optimisation:** Load the relevant rows into **local memory** first, then all work-items in the work-group read from local memory instead of global. Use `barrier(CLK_LOCAL_MEM_FENCE)` after loading.
* On the host side: use `CL_MEM_COPY_HOST_PTR` when creating buffers to combine allocation and data transfer in one step.

***

#### 2021 — 4(c) \[10 Marks]

> Write an OpenCL kernel that smooths an image by averaging a pixel with its immediate neighbours. Describe how the host program would configure the problem space for your kernel. How might you improve the run-time of your complete OpenCL program, host and kernel?

**✅ Full Answer**

**Kernel (averaging with 4 neighbours — above, below, left, right + self = 5 pixels):**

```c
kernel void smooth(global const float *input,
                   global float *output,
                   int width, int height)
{
    int x = get_global_id(0);  // column
    int y = get_global_id(1);  // row

    // skip border pixels
    if (x <= 0 || x >= width - 1 || y <= 0 || y >= height - 1)
        return;

    float sum = input[y * width + x];           // self
    sum      += input[(y - 1) * width + x];     // above
    sum      += input[(y + 1) * width + x];     // below
    sum      += input[y * width + (x - 1)];     // left
    sum      += input[y * width + (x + 1)];     // right

    output[y * width + x] = sum / 5.0f;
}
```

**Problem space:**

* 2D NDRange, one work-item per pixel.
* `global = {width, height}`, `local = {8, 8}`.

**Improving runtime:**

* Many pixels are read multiple times by neighbouring work-items from global memory.
* **Optimisation:** Load a tile of pixels (including halo border) into **local memory**. All work-items in the work-group then read from local memory. Use `barrier(CLK_LOCAL_MEM_FENCE)` after loading.

***

#### 2022 — 4(c) \[10 Marks]

> Write an OpenCL kernel that processes an image by setting the value of a pixel to the minimum of the pixel immediately to the left of it and the pixel immediately to the right of it. Describe how the host program would configure the problem space. How might you improve the run-time?

📝 **Same pattern as 2020 4(c) — just change max/above/below to min/left/right. See 2020 answer above.**

```c
kernel void min_left_right(global const float *input,
                            global float *output,
                            int width, int height)
{
    int x = get_global_id(0);
    int y = get_global_id(1);

    if (x <= 0 || x >= width - 1 || y >= height)
        return;

    float left  = input[y * width + (x - 1)];
    float right = input[y * width + (x + 1)];

    output[y * width + x] = (left < right) ? left : right;
}
```

***

#### 2024 — 4(c) \[10 Marks]

> Write an OpenCL kernel that smooths an image by averaging a pixel with the immediate neighbours that are above, below, left and right of the pixel. Describe how the host program would configure the problem space. How might you improve the run-time?

📝 **Identical to 2021 4(c). See answer above.**

***

#### 2019 — 4(c) \[10 Marks]

> Write an OpenCL kernel that implements the Sobel edge detection algorithm.
>
> v(i,j) is white if value1² + value2² > THRESHOLD where:
>
> * value1 = v(i−1,j+1) − v(i−1,j−1) + 2\*(v(i,j+1) − v(i,j−1)) + v(i+1,j+1) − v(i+1,j−1)
> * value2 = v(i−1,j+1) + &#x32;_&#x76;(i−1,j) + v(i−1,j−1) − v(i+1,j+1) − &#x32;_&#x76;(i+1,j) − v(i+1,j−1)

**✅ Full Answer**

```c
#define THRESHOLD 128

kernel void sobel(global const float *input,
                  global float *output,
                  int width, int height)
{
    int x = get_global_id(0);  // j = column
    int y = get_global_id(1);  // i = row

    // skip border pixels
    if (x <= 0 || x >= width - 1 || y <= 0 || y >= height - 1)
    {
        output[y * width + x] = 0;
        return;
    }

    float v_im1_jm1 = input[(y-1) * width + (x-1)];
    float v_im1_j   = input[(y-1) * width + x];
    float v_im1_jp1 = input[(y-1) * width + (x+1)];
    float v_i_jm1   = input[y * width + (x-1)];
    float v_i_jp1   = input[y * width + (x+1)];
    float v_ip1_jm1 = input[(y+1) * width + (x-1)];
    float v_ip1_j   = input[(y+1) * width + x];
    float v_ip1_jp1 = input[(y+1) * width + (x+1)];

    float value1 = v_im1_jp1 - v_im1_jm1
                 + 2.0f * (v_i_jp1 - v_i_jm1)
                 + v_ip1_jp1 - v_ip1_jm1;

    float value2 = v_im1_jp1 + 2.0f * v_im1_j + v_im1_jm1
                 - v_ip1_jp1 - 2.0f * v_ip1_j - v_ip1_jm1;

    output[y * width + x] = (value1 * value1 + value2 * value2 > THRESHOLD)
                            ? 255.0f : 0.0f;
}
```

**Problem space:** 2D NDRange `{width, height}`, one work-item per pixel.

***

#### 2025 — 4(c) \[10 Marks]

> Write an OpenCL kernel for emphasising horizontal edges in a 256x256 image A. Each element (i,j) of output image B is calculated by work item (i,j). Work item (i,j) should have a private copy of the appropriate rows from image A. What is the advantage?
>
> Edge formula: `|((A(i, j+1) − A(i, j))) − ((A(i, j) − A(i, j−1)))|`

**✅ Full Answer**

```c
kernel void horizontal_edges(global const float *A,
                              global float *B,
                              int width)
{
    int x = get_global_id(0);  // j = column
    int y = get_global_id(1);  // i = row

    // skip border pixels
    if (x <= 0 || x >= width - 1)
    {
        B[y * width + x] = 0;
        return;
    }

    // private copy of the relevant row
    float row_left   = A[y * width + (x - 1)];  // A(i, j-1)
    float row_centre = A[y * width + x];          // A(i, j)
    float row_right  = A[y * width + (x + 1)];   // A(i, j+1)

    float diff = (row_right - row_centre) - (row_centre - row_left);
    B[y * width + x] = diff < 0 ? -diff : diff;  // absolute value
}
```

**Advantage of private copy:** Reading from global memory is slow and causes contention when many work-items access it simultaneously. By copying the relevant values into private variables (`row_left`, `row_centre`, `row_right`), each work-item reads from global memory only once per value, then works entirely from its private (register-level) memory for the calculation. This eliminates repeated global memory accesses and reduces contention.

***

***

### Matrix Multiply Kernel

#### 2023 — 4(c) \[10 Marks]

> Write an OpenCL kernel for multiplying two matrices \[C]=\[A]\[B], where each element C\[i,j] is calculated by work item (i,j). Work item (i,j) should have a private copy of the appropriate A row and store the appropriate B column in local memory. Explain the operation. Why is it an improvement over global-only?

**✅ Full Answer**

```c
kernel void matrix_mul(int Mdim, int Ndim, int Pdim,
                       global float *A,
                       global float *B,
                       global float *C,
                       local float *Blocal)
{
    int i      = get_global_id(0);   // row of C
    int j      = get_global_id(1);   // col of C
    int ilocal = get_local_id(0);
    int nloc   = get_local_size(0);
    int k;

    // copy row i of A into private memory
    float Apriv[1000];
    for (k = 0; k < Pdim; k++)
    {
        Apriv[k] = A[i * Ndim + k];
    }

    float tmp = 0.0f;

    // for each column j of B
    // cooperatively load column j of B into local memory
    for (k = ilocal; k < Pdim; k = k + nloc)
    {
        Blocal[k] = B[k * Pdim + j];
    }
    barrier(CLK_LOCAL_MEM_FENCE);  // wait for all work-items to finish loading

    // compute C[i,j] using private A row and local B column
    for (k = 0; k < Pdim; k++)
    {
        tmp += Apriv[k] * Blocal[k];
    }

    C[i * Ndim + j] = tmp;
}
```

**Explanation of operation:**

* Each work-item is responsible for one element C\[i,j].
* It first copies its entire row of A into **private memory** — this removes all contention on A since each work-item reads a different row.
* Work-items in the same work-group **cooperatively** load the column of B into **local memory** (each work-item loads a strided subset).
* A `barrier` ensures all of B's column is loaded before any work-item starts the dot product.
* The dot product is then computed entirely from private and local memory.

**Why it is an improvement over global-only:**

* In a global-only kernel, every access to A and B goes through slow global memory which can only be read by one work-item at a time — causing contention.
* Moving A's row to **private** memory: each work-item reads its A row once from global, then all subsequent accesses are register-speed.
* Moving B's column to **local** memory: the column is read from global once per work-group (shared), not once per work-item. All work-items in the group then read from fast local memory.
* Performance improvement from slides: Global-only = 511 Mflops → private A row = 873 Mflops → private A + local B = 2472 Mflops (15× speedup over sequential).

***

***

### Watch Out For

* **Border pixels** — always check bounds in image kernels. Pixels at the edge have no above/below/left/right neighbour.
* **`barrier()` placement** — must come AFTER loading into local memory, BEFORE reading from it. Wrong placement = race condition.
* **`kernel` qualifier** — always required on the function, always returns `void`.
* **`global` on pointer parameters** — always needed for buffer arguments.
* **NDRange is set by host, not kernel** — the kernel just calls `get_global_id()`.
* **OpenCL (x,y) vs matrix (row,col)** — `get_global_id(0)` = x = column, `get_global_id(1)` = y = row. Index as `image[y * width + x]`.
* **Kernels compiled at runtime** — OpenCL kernels are NOT compiled at build time. This is the key difference from CUDA.

***

_Generated from CSC1141 past papers 2019–2025 | Study session April 2026_
