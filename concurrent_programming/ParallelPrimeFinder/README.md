# ParallelPrimeFinder

A thread-based Java program that finds all prime numbers up to N
using multiple algorithms and execution strategies on multiple cores.
Report: https://github.com/akpmohan07/my-masters/blob/main/concurrent_programming/ParallelPrimeFinder/docs/ParallelPrimeFinder.pdf


## Algorithms Implemented
- Sequential Trial Division
- Parallel Trial Division
- Sequential Sieve of Eratosthenes
- Parallel Sieve of Eratosthenes

## Requirements
- Java 21 (Eclipse Temurin 21 recommended)

## Configuration
Edit before building:
`src/main/resources/config.properties`
```properties
# Values of N to test
prime.test.values=100000,1000000,100000000

# Percentage of CPU cores to use (25% = 2 threads on 8-core machine)
prime.thread.percentages=25,50,75,100
```

## Build
```bash
./gradlew jar
```

JAR will be generated at:
```
build/libs/ParallelPrimeFinder.jar
```

## Run
```bash
java -jar build/libs/ParallelPrimeFinder.jar
```

## Output
Results are written to:
```
logs/report.txt      ← main results table
logs/report_1.txt    ← previous run backup
logs/application.log ← full application logs
```

## Project Structure
```
src/main/java/dev/mohanverse/
├── Main.java
└── prime/
    ├── config/
    │   ├── PrimeFinderConfig.java
    │   └── ProcessorConfig.java
    ├── enums/
    │   ├── Algorithm.java
    │   └── ExecutionType.java
    ├── executor/
    │   └── PrimerFinderExecutor.java
    ├── finder/
    │   ├── PrimeFinderAbstract.java
    │   ├── PrimeFinderResult.java
    │   ├── sieve/
    │   │   ├── LinearSieveFinder.java
    │   │   └── ParallelSieveFinder.java
    │   └── trial_division/
    │       ├── LinearTrialDivisionFinder.java
    │       └── ParallelTrialDivisionFinder.java
    ├── metrics/
    │   └── PrimeFinderMetrics.java
    └── utility/
        ├── PrimeUtils.java
        └── ResultsPrinter.java
```

## Results Sample
```
N               | Algorithm                 | Type         | Threads | Time         | Primes
100,000         | Linear Trial Division     | Sequential   | 1       | 8 ms         | 9,592
100,000         | Sieve of Eratosthenes     | Sequential   | 1       | 4 ms         | 9,592
100,000,000     | Linear Trial Division     | Sequential   | 1       | 25.93 sec    | 5,761,455
100,000,000     | Sieve of Eratosthenes     | Sequential   | 1       | 874 ms       | 5,761,455
100,000,000     | Sieve of Eratosthenes     | Parallel     | 4       | 1.51 sec     | 5,761,455
```

## Author
- Name: Mohankumar Muthusamy
- Student Number: A00049023
- Course: CSC1141 Concurrent Programming
- Institution: Dublin City University
