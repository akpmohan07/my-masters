# ParallelPrimeFinder — Project README

> **Module:** CSC1141 Concurrent Programming | **Type:** Main Assignment | **Language:** Java 21 | **Build:** Gradle

## Purpose

A benchmarking application that finds all prime numbers up to N using four algorithm/execution combinations, comparing performance across different CPU core allocations (25%, 50%, 75%, 100%).

## Architecture

```
src/main/java/dev/mohanverse/
├── Main.java                              # Entry point — loads config, runs executor
└── prime/
    ├── config/
    │   ├── PrimeFinderConfig.java         # Singleton — reads config.properties
    │   │                                  #   Provides test values (N) and thread configs
    │   └── ProcessorConfig.java           # Detects CPU cores via Runtime.availableProcessors()
    │                                      #   Inner class ThreadConfig maps % → thread count
    ├── enums/
    │   ├── Algorithm.java                 # LINEAR_TRIAL_DIVISION | SIEVE_OF_ERATOSTHENES
    │   └── ExecutionType.java             # SEQUENTIAL | PARALLEL
    ├── executor/
    │   └── PrimerFinderExecutor.java      # Orchestrator — runs all algorithm combos
    │                                      #   Iterates N values × algorithms × thread configs
    ├── finder/
    │   ├── PrimeFinderAbstract.java       # Template Method base class
    │   │                                  #   findPrimesUntil() → computePrimesForRange()
    │   │                                  #   Handles timing, metrics, result construction
    │   ├── PrimeFinderResult.java         # Data class: primes list + metrics + config info
    │   ├── trial_division/
    │   │   ├── LinearTrialDivisionFinder.java    # Sequential: test each 2..N for primality
    │   │   └── ParallelTrialDivisionFinder.java  # Parallel: partition range into chunks
    │   │                                         #   ExecutorService fixed pool, Future.get()
    │   └── sieve/
    │       ├── LinearSieveFinder.java            # Sequential Sieve of Eratosthenes
    │       │                                     #   Boolean array, mark composites from p²
    │       └── ParallelSieveFinder.java          # Two-phase parallel sieve:
    │                                             #   Phase 1: sequential base primes to √N
    │                                             #   Phase 2: parallel marking via invokeAll()
    ├── metrics/
    │   └── PrimeFinderMetrics.java        # Tracks: execution time, numbers checked,
    │                                      #   primes found, chunk size per thread
    └── utility/
        ├── PrimeUtils.java                # isPrime(long): checks 2, skips evens, tests to √N
        └── ResultsPrinter.java            # Formatted table output via SLF4J logging
```

## Algorithm Comparison

| Algorithm | Type | Time Complexity | How It Works |
|-----------|------|-----------------|--------------|
| Trial Division | Sequential | O(N√N) | For each number 2..N, test divisibility by all integers up to √N |
| Trial Division | Parallel | O(N√N / threads) | Partition [2, N] into equal chunks, each thread tests its chunk independently |
| Sieve of Eratosthenes | Sequential | O(N log log N) | Create boolean[N], for each prime p mark p², p²+p, p²+2p... as composite |
| Sieve of Eratosthenes | Parallel | O(N log log N / threads) | Sequentially find primes up to √N, then parallel-mark composites in segmented chunks |

**Performance result:** Sieve is ~870× faster than trial division at N=100M.

## Configuration

`src/main/resources/config.properties`:
```properties
prime.test.values=100000,1000000,100000000,1000000000
prime.thread.percentages=25,50,75,100
```

## Design Patterns

- **Template Method** — `PrimeFinderAbstract` defines the algorithm skeleton; subclasses implement `computePrimesForRange()`
- **Singleton** — `PrimeFinderConfig` loaded once, shared across all finders
- **Strategy** — Algorithms are interchangeable via the abstract base class

## Key Concurrency Concepts

- `ExecutorService.newFixedThreadPool()` for bounded parallelism
- `Future.get()` for blocking result collection
- `invokeAll()` for barrier synchronisation in parallel sieve
- Thread-safe shared boolean arrays (non-overlapping index ranges)
- Chunk-based range partitioning for load balancing

## Build & Run

```bash
./gradlew build
java -jar build/libs/ParallelPrimeFinder.jar
```
Requires Java 21. Dependencies: JUnit 5, SLF4J/Logback, Lombok.

---

*Related: [Concurrent Programming](./CONCURRENT_PROGRAMMING.md) | [MASTER.md](./MASTER.md)*
