# Concurrent Programming — Subject Context

> **Module:** CSC1141 Concurrent Programming | **Programme:** MSc Computing | **Student:** Mohankumar Muthusamy

## Overview

This module covers Java concurrency patterns, parallel algorithm design, and performance benchmarking. The main deliverable is the **ParallelPrimeFinder** project — a comprehensive multi-threaded application comparing sequential vs parallel prime-finding algorithms across different CPU core allocations.

## Folder Structure

```
concurrent_programming/
├── ParallelPrimeFinder/                   # Main assignment project
│   ├── src/main/java/dev/mohanverse/
│   │   ├── Main.java                     # Entry point
│   │   └── prime/
│   │       ├── config/
│   │       │   ├── PrimeFinderConfig.java    # Singleton config loader
│   │       │   └── ProcessorConfig.java      # CPU core detection & thread mapping
│   │       ├── enums/
│   │       │   ├── Algorithm.java            # LINEAR_TRIAL_DIVISION, SIEVE_OF_ERATOSTHENES
│   │       │   └── ExecutionType.java        # SEQUENTIAL, PARALLEL
│   │       ├── executor/
│   │       │   └── PrimerFinderExecutor.java # Orchestrates all algorithm runs
│   │       ├── finder/
│   │       │   ├── PrimeFinderAbstract.java  # Template method base class
│   │       │   ├── PrimeFinderResult.java    # Result data class
│   │       │   ├── trial_division/
│   │       │   │   ├── LinearTrialDivisionFinder.java   # Sequential O(N√N)
│   │       │   │   └── ParallelTrialDivisionFinder.java # Parallel with ExecutorService
│   │       │   └── sieve/
│   │       │       ├── LinearSieveFinder.java           # Sequential Sieve of Eratosthenes
│   │       │       └── ParallelSieveFinder.java         # Two-phase parallel sieve
│   │       ├── metrics/
│   │       │   └── PrimeFinderMetrics.java   # Execution time, counts, chunk size
│   │       └── utility/
│   │           ├── PrimeUtils.java           # isPrime() with √N optimisation
│   │           └── ResultsPrinter.java       # Formatted table output
│   ├── src/main/resources/config.properties  # Test values & thread percentages
│   ├── build.gradle.kts                      # Java 21, JUnit 5, SLF4J, Lombok
│   ├── docs/ParallelPrimeFinder.pdf          # Assignment report
│   └── logs/report.txt                       # Performance results
├── java-concurrency/                      # Educational examples
│   └── src/main/java/com/mohanverse/dev/
│       ├── Main.java                      # Available processors check
│       ├── ExecutorService.java           # Thread pool demo (10 threads, 20 tasks)
│       └── ForkJoinPool.java              # Divide-and-conquer max finder
```

## Key Project: ParallelPrimeFinder

→ *See [ParallelPrimeFinder README](./ParallelPrimeFinder/CLAUDE.md) for full details*

Finds all primes up to N using four algorithm/execution combinations, benchmarked at 25%, 50%, 75%, 100% CPU allocation:

| Algorithm | Execution | Complexity | Approach |
|-----------|-----------|------------|----------|
| Trial Division | Sequential | O(N√N) | Test each number for divisibility up to √N |
| Trial Division | Parallel | O(N√N)/threads | Range partitioned into chunks per thread |
| Sieve of Eratosthenes | Sequential | O(N log log N) | Boolean array, mark composites |
| Sieve of Eratosthenes | Parallel | O(N log log N)/threads | Phase 1: sequential base primes to √N; Phase 2: parallel composite marking |

**Design Patterns Used:** Template Method (PrimeFinderAbstract), Singleton (PrimeFinderConfig), Strategy (interchangeable algorithms).

**Test configurations:** N = 100K, 1M, 100M, 1B × thread percentages 25%, 50%, 75%, 100%.

## Educational Examples (java-concurrency)

**ExecutorService.java** — Demonstrates fixed thread pool (10 threads handling 20 tasks). Each task simulates work with 10-second sleep. Shows lifecycle: create pool → submit tasks → shutdown → await termination.

**ForkJoinPool.java** — Divide-and-conquer to find max in 1M-element array. Extends `RecursiveTask<Integer>`. Sequential threshold = 5 elements. Recursively splits array, forks left subtask, computes right locally, joins results with `Math.max()`. Includes visual documentation of execution tree.

## Key Concepts Demonstrated

**Concurrency Patterns:** ExecutorService (bounded thread pools), Fork/Join (recursive work-stealing), Future.get() for result collection, invokeAll() for barrier synchronisation.

**Performance Analysis:** Sieve is ~870x faster than trial division at N=100M. Parallel speedup scales with core count but diminishes due to coordination overhead. Chunk-based range partitioning for load balancing.

**Thread Safety:** Shared boolean arrays in ParallelSieveFinder (safe due to non-overlapping index ranges). AtomicLong for concurrent counters. Proper shutdown and termination handling.

---

*Related: [Root CLAUDE.md](../CLAUDE.md) | [ParallelPrimeFinder README](./ParallelPrimeFinder/CLAUDE.md)*
