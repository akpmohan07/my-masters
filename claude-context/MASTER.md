# Master's Programme — Complete Repository Context

> **Student:** Mohankumar Muthusamy (akpmohan07@gmail.com)
> **Programme:** MSc Computing
> **Repository:** College workspace containing all coursework, assignments, and exam preparation materials

## Quick Reference

This knowledge base provides full context for an MSc Computing programme covering 8 modules. Each subject has its own detailed context file, and major projects have individual READMEs. All files are interlinked.

### Subject Index

| # | Subject | Context File | Key Content |
|---|---------|-------------|-------------|
| 1 | [Blockchain](./BLOCKCHAIN.md) | Blockchain technology | Solidity smart contracts, PoW/PoS, security vulnerabilities, 5 years of exam prep |
| 2 | [Concurrent Programming](./CONCURRENT_PROGRAMMING.md) | Java concurrency | ParallelPrimeFinder project, ExecutorService, Fork/Join patterns |
| 3 | [Cryptography](./CRYPTOGRAPHY.md) | Crypto fundamentals + cryptanalysis | FEAL-4 linear attack (Java), Vigenère breaker (Python), block ciphers, hashing |
| 4 | [Formal Programming](./FORMAL_PROGRAMMING.md) | Event-B formal methods | 8+ Rodin specifications, refinement, proof obligations |
| 5 | [Network Security](./NETWORK_SECURITY.md) | Network protocols & security | Cisco Packet Tracer labs, DNS commands |
| 6 | [Professional Research](./PROFESSIONAL_RESEARCH.md) | Ethics & legal frameworks | GDPR, privacy theory, ethical reasoning, AI regulation |
| 7 | [Secure Software](./SECURE_SOFTWARE.md) | Systems security in C | Buffer overflows, format strings, GDB, x86 stack frames |
| 8 | [Practicum](./PRACTICUM.md) | Research project | LLM security — adversarial attacks & defence mechanisms |

### Project READMEs

| Project | Subject | README | Description |
|---------|---------|--------|-------------|
| ParallelPrimeFinder | Concurrent Programming | [README](./README_ParallelPrimeFinder.md) | Multi-threaded prime finder benchmarking 4 algorithm combos across CPU core allocations |
| FEAL-4 Linear Attack | Cryptography | [README](./README_FEAL4LinearAttack.md) | Known-plaintext cryptanalysis recovering 6 round keys from 50 pairs |

## Repository Structure

```
my-masters/
├── claude-context/                        # ← YOU ARE HERE (context files for Claude Projects)
│   ├── MASTER.md                          # This file — master index
│   ├── BLOCKCHAIN.md                      # Subject context
│   ├── CONCURRENT_PROGRAMMING.md          # Subject context
│   ├── CRYPTOGRAPHY.md                    # Subject context
│   ├── FORMAL_PROGRAMMING.md              # Subject context
│   ├── NETWORK_SECURITY.md                # Subject context
│   ├── PROFESSIONAL_RESEARCH.md           # Subject context
│   ├── SECURE_SOFTWARE.md                 # Subject context
│   ├── PRACTICUM.md                       # Subject context
│   ├── README_ParallelPrimeFinder.md      # Project README
│   └── README_FEAL4LinearAttack.md        # Project README
│
├── blockchain/                            # Solidity contracts, Python PoW, exam notes
│   ├── week_3/run.py
│   ├── week_4_token/SimpleToken.sol
│   ├── week_7_contracts/                  # Vulnerability demos (5 contracts)
│   ├── smart_contracts/                   # Voting, Vault, ExtendedVault
│   └── exam/                              # 8 topic answers + revision sheets
│
├── concurrent_programming/
│   ├── ParallelPrimeFinder/               # Main project (Java 21, Gradle)
│   │   └── src/main/java/dev/mohanverse/prime/
│   └── java-concurrency/                  # Educational demos (ExecutorService, ForkJoin)
│       └── src/main/java/com/mohanverse/dev/
│
├── cryptography/
│   ├── decrypt.py                         # Vigenère breaker
│   ├── assignment_cryptanalysis/
│   │   └── FEAL4LinearAttack/             # Main project (Java, Gradle)
│   │       └── src/main/java/com/mohanverse/dev/feal4/
│   ├── hashing/                           # Hash revision + SHAttered demo PDFs
│   └── exam/                              # Topic mind map, past questions, revision
│
├── formal_programming/
│   ├── rodin_workspace/
│   │   └── Formal_Specification_Assignment_1/  # 4 Event-B systems (Q1-Q4)
│   └── Solutions/                         # Assignment 2: Refinement (Q1-Q4 + Supermarket)
│
├── network_securtiy/
│   ├── Commands/DNS_Commands.txt
│   └── Packets/                           # .pkt files (Cisco Packet Tracer, binary)
│
├── professional_research/
│   ├── Revision.md                        # Ethics, GDPR, privacy, legal frameworks
│   ├── StudyPlan.md                       # Exam strategy
│   └── ethics/Privacy.md                  # Privacy deep-dive
│
├── secure_software/
│   ├── format_string_attack.c
│   ├── lab01_multi_threading/             # fork(), pthreads, race conditions
│   ├── lab04_string_linking/              # String ops, function hooking, LD_PRELOAD
│   ├── Sample_Labs/                       # Exam prep + workflow
│   ├── exam/Stack.md                      # Stack frame analysis notes
│   └── lab_env/                           # Dockerfile + cloud setup
│
└── Practicum/
    └── Zotero/                            # Reference library (81 papers on LLM security)
```

## Technology Stack

| Technology | Used In | Purpose |
|------------|---------|---------|
| **Java 21** | Concurrent Programming, Cryptography | ParallelPrimeFinder, FEAL-4 attack |
| **Solidity** | Blockchain | Smart contracts (ERC-20 tokens, vaults, vulnerability demos) |
| **Python** | Blockchain, Cryptography | PoW implementation, Vigenère cryptanalysis |
| **C** | Secure Software | Buffer overflows, format strings, threading, linking |
| **Event-B (Rodin)** | Formal Programming | Formal specifications with mathematical proofs |
| **Cisco Packet Tracer** | Network Security | Network simulation labs |
| **GDB** | Secure Software | Stack frame analysis, exploit development |
| **Gradle/Maven** | Multiple | Java project builds |
| **Zotero** | Practicum | Research reference management |

## Cross-Subject Connections

The modules interconnect in several ways:

- **Security thread:** Blockchain smart contract vulnerabilities → Cryptography attack/defence → Secure Software exploitation → Practicum LLM security research
- **Formal methods → Secure software:** Event-B formal verification relates to proving software correctness, a defence against vulnerabilities
- **Concurrency → Cryptography:** The FEAL-4 Python reference attack uses parallel processing; ParallelPrimeFinder's threading concepts apply to brute-force key search
- **Ethics → Practicum:** Professional Research's GDPR and AI regulation frameworks inform the Practicum's LLM security research context
- **Number theory → Blockchain:** Cryptographic primitives (hashing, digital signatures) underpin both modules

## How to Use This Context

**For Claude Projects:** Upload the entire `claude-context/` folder. The MASTER.md provides the entry point, and each subject/project file gives deep context. Total size: ~60 KB of pure text covering the full programme.

**For specific help:** Reference the relevant subject file. For example, if working on a blockchain exam question, point to BLOCKCHAIN.md. For debugging the ParallelPrimeFinder, point to README_ParallelPrimeFinder.md.

**For the actual source code:** The context files describe what each source file does. When Claude needs to see the actual code, reference the file paths listed in each context document.
