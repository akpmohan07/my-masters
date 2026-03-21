# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

Master's degree (MSc Computing, Dublin City University) coursework for Mohankumar Muthusamy (A00049023). Contains work across 8 independent modules.

## Subject Index

Each subject folder has a `CLAUDE.md` with deep context. Read it before working in that area.

| Subject | Context | Key Project |
|---|---|---|
| [Blockchain](blockchain/CLAUDE.md) | Solidity, consensus, security vulnerabilities, exam prep | Smart contracts (Remix IDE) |
| [Concurrent Programming](concurrent_programming/CLAUDE.md) | Java concurrency patterns, benchmarking | [ParallelPrimeFinder](concurrent_programming/ParallelPrimeFinder/CLAUDE.md) |
| [Cryptography](cryptography/CLAUDE.md) | Block ciphers, hash functions, cryptanalysis | [FEAL-4 Linear Attack](cryptography/assignment_cryptanalysis/FEAL4LinearAttack/CLAUDE.md) |
| [Formal Programming](formal_programming/CLAUDE.md) | Event-B, Rodin, refinement proofs | 8 Rodin specifications |
| [Network Security](network_securtiy/CLAUDE.md) | Cisco Packet Tracer labs, DNS | `.pkt` simulation files |
| [Professional Research](professional_research/CLAUDE.md) | Ethics, GDPR, AI regulation, exam prep | Essay-based assessment |
| [Secure Software](secure_software/CLAUDE.md) | Buffer overflows, format strings, GDB, x86 stack | C labs + timed exam |
| [Practicum](Practicum/CLAUDE.md) | LLM security research, Zotero library (81 papers) | Literature review |

## Cross-Subject Connections

- **Security thread:** Blockchain vulnerabilities → Cryptography attack/defence → Secure Software exploitation → Practicum LLM security
- **Formal methods → Secure software:** Event-B correctness proofs relate to preventing the vulnerabilities studied in secure software
- **Ethics → Practicum:** GDPR and AI regulation frameworks from Professional Research inform the Practicum's LLM security research context

## Build & Run Commands

### ParallelPrimeFinder (`concurrent_programming/ParallelPrimeFinder/`) — Java 21, Gradle
```bash
./gradlew jar
java -jar build/libs/ParallelPrimeFinder.jar
# Configure N values and thread percentages before building:
# src/main/resources/config.properties
# Output: logs/report.txt
```

### FEAL4 Cryptanalysis (`cryptography/assignment_cryptanalysis/FEAL4LinearAttack/`) — Java, Gradle
```bash
./gradlew build
./gradlew test
```

### Java Concurrency demos (`concurrent_programming/java-concurrency/`) — Maven
```bash
mvn compile
mvn exec:java -Dexec.mainClass="<ClassName>"
```

### OpenMP (`concurrent_programming/openmp/`)
```bash
gcc -fopenmp hello_openmp.c -o hello_openmp && ./hello_openmp
```

### Secure Software C programs (`secure_software/`)
```bash
gcc <file>.c -o <output>
# Labs involving stack/format-string exploits need:
gcc -fno-stack-protector -z execstack <file>.c -o <output>
```

### Cryptography Python
```bash
python3 cryptography/decrypt.py   # Vigenère breaker
```

## Key Architectural Notes

### ParallelPrimeFinder
- Entry: `src/main/java/dev/mohanverse/Main.java`
- `PrimerFinderExecutor` runs all algorithm × thread-count combinations from config
- Algorithms: Sequential/Parallel Trial Division + Sequential/Parallel Sieve of Eratosthenes
- Results written to `logs/report.txt`; previous run backed up to `logs/report_1.txt`

### FEAL4 Cryptanalysis
- Entry: `FealProgram.java`; attack driver: `FEAL4Cryptanalysis.java`
- Known plaintext/ciphertext pairs loaded via `PlaintextCiphertextLoader`
- Key recovery pipeline: `KeyCandidateGenerator` → `KeyCandidateFilter` → `KeyRecoverySolver`

### Blockchain Smart Contracts
- Solidity contracts deployed via Remix IDE (no Hardhat/Truffle config)
- `week_7_contracts/` contains intentionally vulnerable contracts for security analysis

### Secure Software Labs
- Low-level C: stack layout, format string vulnerabilities, race conditions, dynamic linking/hooking
- Labs include pre-compiled binaries alongside source; GDB used for stack frame analysis
