# OpenCL

{% embed url="https://chatgpt.com/g/g-p-696f5c8a24948191a90f2355411ee6a7-concurrent-programming/c/69a6bd4b-c958-8389-be28-a974ea875d01" %}

Here is a **clean “future revision” summary of the entire OpenCL document** you shared.\
It is designed so that you can **quickly revise it before exams or interviews**.

Source: 📄 _CSC1141 – Concurrent Programming: OpenCL_

***

## OpenCL — Complete Revision Summary

***

## 1. What OpenCL Is

**OpenCL (Open Computing Language)** is an **open standard for parallel programming on heterogeneous systems**.

It allows programs to run on:

* GPUs (NVIDIA RTX, AMD Radeon)
* CPUs (Intel Core, AMD Ryzen)
* Accelerators (DSPs, FPGAs)

It was developed by the **Khronos Group**.

#### Main purpose

Use **massive parallel hardware** for general computation.

Example uses:

* Machine learning
* Image processing
* Physics simulation
* Scientific computing

***

## 2. Core Idea of OpenCL

OpenCL separates the program into:

#### Host

The **CPU program** that controls execution.

#### Device

The **hardware that performs parallel computation**.

Example:

Host → Intel Core i7\
Device → NVIDIA RTX 4090

***

## 3. Three Components of OpenCL

OpenCL has **three major components**.

#### 1. Language Specification

OpenCL kernels are written in **OpenCL C**.

Based on **C99** but with restrictions:

Not allowed:

* recursion
* function pointers
* bit-fields
* variable length arrays

Added features:

* vector types
* memory qualifiers
* built-in parallel functions

***

#### 2. Platform API

Used by host programs to:

* detect OpenCL devices
* send data to devices
* launch kernels

***

#### 3. Runtime API

Used to manage:

* command queues
* memory objects
* kernel execution

***

## 4. OpenCL Architecture

OpenCL uses **three models**.

***

## 4.1 Platform Model

Describes hardware organisation.

```
Host (CPU)
   |
Device (GPU / CPU)
   |
Compute Units
   |
Processing Elements
```

#### Real example

| OpenCL term        | Real hardware            |
| ------------------ | ------------------------ |
| Host               | Intel CPU                |
| Device             | NVIDIA GPU               |
| Compute Unit       | Streaming Multiprocessor |
| Processing Element | CUDA core                |

***

## 4.2 Execution Model

Defines how computation runs.

Two components:

#### Host program

Controls execution.

#### Kernels

Functions executed in parallel on the device.

***

### Work-items

A **work-item** is a single execution instance of a kernel.

Example:\
Each element of an array may be processed by one work-item.

***

### Work-groups

Work-items are grouped into **work-groups**.

Properties:

* Work-items inside a group can synchronise
* They share **local memory**
* Work-groups execute independently

***

### NDRange

OpenCL executes kernels over an **N-dimensional index space**.

Possible dimensions:

* 1D
* 2D
* 3D

Example:

```
Image processing → 2D
Volume data → 3D
Vector operations → 1D
```

***

## 4.3 Memory Model

OpenCL memory is hierarchical.

#### 1 Global Memory

* Accessible by all work-items
* Large but slow
* Example: GPU VRAM

***

#### 2 Constant Memory

* Read-only for work-items
* Written by host

***

#### 3 Local Memory

* Shared inside a work-group
* Faster than global memory

Example: GPU shared memory

***

#### 4 Private Memory

* Exclusive to a work-item
* Typically registers

***

## 5. Kernel Programming

A **kernel** is the function executed on the device.

Example:

Sequential C:

```
for(i=0;i<n;i++)
    r[i] = s[i]*s[i];
```

OpenCL kernel:

```
kernel void square(global float *s, global float *r)
{
    int id = get_global_id(0);
    r[id] = s[id] * s[id];
}
```

Each work-item processes one element.

***

## 6. Types of Kernels

#### OpenCL kernels

* Written in OpenCL C
* Compiled at runtime

#### Native kernels

* Written for specific devices
* Less portable

***

## 7. Steps in an OpenCL Program

Typical workflow:

1️⃣ Query available platforms\
2️⃣ Query devices\
3️⃣ Create context\
4️⃣ Create command queue\
5️⃣ Create program from source\
6️⃣ Compile program\
7️⃣ Create kernel\
8️⃣ Create memory buffers\
9️⃣ Transfer data to device\
🔟 Set kernel arguments\
11️⃣ Execute kernel\
12️⃣ Read results\
13️⃣ Release resources

***

## 8. Important OpenCL API Functions

#### Platform & device detection

```
clGetPlatformIDs()
clGetDeviceIDs()
```

***

#### Context creation

```
clCreateContext()
```

Context manages:

* devices
* memory objects
* kernels

***

#### Command queue

```
clCreateCommandQueue()
```

Queues commands for device execution.

***

#### Program creation

```
clCreateProgramWithSource()
```

Creates program from OpenCL source code.

***

#### Program compilation

```
clBuildProgram()
```

Compiles kernel code at runtime.

***

#### Kernel creation

```
clCreateKernel()
```

Extracts kernel from compiled program.

***

#### Kernel arguments

```
clSetKernelArg()
```

Assigns memory buffers and parameters.

***

#### Kernel execution

```
clEnqueueNDRangeKernel()
```

Runs the kernel across work-items.

***

#### Memory management

```
clCreateBuffer()
clEnqueueWriteBuffer()
clEnqueueReadBuffer()
```

Transfers data between host and device.

***

## 9. Memory Objects

Two types exist.

#### Buffer objects

Used for:

* arrays
* vectors
* numerical data

Created with:

```
clCreateBuffer()
```

***

#### Image objects

Used for:

* 2D images
* 3D textures

Created with:

```
clCreateImage2D()
clCreateImage3D()
```

***

## 10. Synchronisation

OpenCL synchronisation mechanisms include:

#### Barrier

Ensures all work-items reach the same point.

```
barrier(CLK_LOCAL_MEM_FENCE)
```

***

#### Memory fence

Ensures memory operations are visible.

```
mem_fence()
```

***

#### Event synchronisation

Used to coordinate kernel execution.

Example:

```
clWaitForEvents()
```

***

## 11. OpenCL Data Types

OpenCL supports vector types.

Examples:

```
float2
float4
int8
```

Vector operations execute on multiple values simultaneously.

Example:

```
float4 a,b,c;
c = a + b;
```

***

## 12. Performance Considerations

Performance depends heavily on memory usage.

Major optimisation strategies:

#### Reduce global memory access

Global memory is slow.

***

#### Use local memory

Shared inside work-groups.

***

#### Use private memory

Registers are fastest.

***

## 13. OpenCL vs CUDA

| Feature          | OpenCL       | CUDA         |
| ---------------- | ------------ | ------------ |
| Hardware support | Many vendors | NVIDIA only  |
| Compilation      | Runtime      | Compile-time |
| Portability      | High         | Low          |
| Tools            | Fewer        | Mature       |

***

## 14. Example Performance Comparison

Matrix multiplication results:

| Implementation | Performance |
| -------------- | ----------- |
| Sequential CPU | 167 Mflops  |
| Naive GPU      | 511 Mflops  |
| Optimised GPU  | 2472 Mflops |

Speed improvement achieved by:

* reducing global memory access
* using private memory
* using local shared memory

***

## 15. Key Concepts to Remember

#### Hardware hierarchy

```
Host
Device
Compute Units
Processing Elements
```

***

#### Execution hierarchy

```
NDRange
Work-groups
Work-items
```

***

#### Memory hierarchy

```
Global memory
Local memory
Private memory
Constant memory
```

***

## Final One-Line Concept

OpenCL works by:

**splitting a problem into thousands of small tasks and executing them simultaneously on parallel hardware like GPUs.**

***

If you want, I can also give you a **super-short 1-page “exam cheat sheet” version** that contains **only the 15 most important points to memorise.**
