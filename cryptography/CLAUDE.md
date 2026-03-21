# Cryptography — Subject Context

> **Module:** Cryptography | **Programme:** MSc Computing | **Student:** Mohankumar Muthusamy

## Overview

This module covers symmetric and public-key cryptography, hash functions, number theory, and hands-on cryptanalysis. The main assignment is a **FEAL-4 Linear Cryptanalysis Attack** — a complete Java implementation that recovers all 6 round keys from 50 known plaintext/ciphertext pairs.

## Folder Structure

```
cryptography/
├── decrypt.py                             # Vigenère cipher cryptanalysis (IoC + chi-squared)
├── assignment_cryptanalysis/
│   ├── FEAL4LinearAttack/                 # Main assignment
│   │   ├── src/main/java/com/mohanverse/dev/feal4/
│   │   │   ├── FEAL4Cryptanalysis.java    # Main orchestrator
│   │   │   ├── FealProgram.java           # FEAL-4 cipher implementation
│   │   │   ├── FealOperations.java        # Bit extraction & XOR helpers
│   │   │   ├── KeyCandidateFilter.java    # Linear approximation equations (~295 lines)
│   │   │   ├── KeyCandidateGenerator.java # Key space partitioning (inner/outer bytes)
│   │   │   ├── KeyRecoverySolver.java     # Sequential key recovery with validation
│   │   │   └── PlaintextCiphertextLoader.java  # Hex pair parser
│   │   ├── known.txt                      # 50 plaintext/ciphertext pairs for attack
│   │   └── build.gradle.kts               # Java build config
│   └── Reference/
│       ├── FEAL.java                      # Reference cipher implementation
│       ├── feal4attack.py                 # Python attack variant (parallel processing)
│       └── BitUnderstand.py               # Educational bit manipulation visualiser
├── hashing/
│   ├── Hasing_Revision.md                 # Hash function cheat sheet
│   ├── a.txt, b.txt                       # Files for hash collision demos
│   ├── shattered-1.pdf, shattered-2.pdf   # SHA-1 collision proof (Google's SHAttered)
├── practical/leaf.crt                     # Certificate for PKI exercises
└── exam/
    ├── CryptoTopicsMindMap.md             # Full course outline (5 topic areas)
    ├── QuestionGroupByTopicAndYear.md     # Verbatim exam questions 2022–2024
    ├── ImportantQuestions.md               # Priority ranking with frequency stars
    └── symmetry/
        ├── BlockCiphers.md                # Comparison of 7 ciphers (DES, AES, FEAL, etc.)
        └── ModeOfOperation.md             # ECB/CBC/OFB/CFB/CTR comparison table
```

## Key Project: FEAL-4 Linear Cryptanalysis

→ *See [FEAL4 README](./assignment_cryptanalysis/FEAL4LinearAttack/CLAUDE.md) for full details*

A known-plaintext attack on the FEAL-4 block cipher using linear cryptanalysis:

**Attack Pipeline:**
1. Load 50 plaintext/ciphertext pairs from `known.txt`
2. Generate key candidates using inner/outer byte partitioning (4,096 inner × ~1M outer)
3. Filter candidates using linear approximation equations — XOR of specific bit positions across plaintext halves, ciphertext halves, and F-function outputs must be constant across all pairs
4. Recover keys sequentially: Key0 → Key1 → Key2 → Key3 (each depends on previous)
5. Derive Key4 and Key5 algebraically from the Feistel structure
6. Validate by decrypting all 50 pairs

**Key insight:** The `KeyCandidateFilter` implements bit-level linear equations for each round. For example, Key0 approximation checks whether `bit5(L0⊕F(L4⊕R4⊕K0)) ⊕ bit5(L4⊕R4) ⊕ bit5(R0)` is constant across all pairs.

## Topics Covered

### 1. Symmetric Cryptography
Block ciphers: DES (56-bit, broken), AES (128/192/256-bit, current standard), FEAL (academic, broken), IDEA, Blowfish, SAFER, RC5. Feistel structure vs SPN. Confusion and diffusion principles. Modes of operation: ECB (pattern leakage), CBC, OFB, CFB, CTR — compared by error propagation, parallelisability, and self-synchronisation.

### 2. Hash Functions
Properties: pre-image resistance, second pre-image resistance, collision resistance. Birthday paradox: ~2^(n/2) attempts for n-bit digest. Algorithms: MD5 (broken), SHA-1 (broken, SHAttered demo), SHA-2 (strong), SHA-3 (sponge construction). MDC vs MAC. Password hashing: salt + KDF.

### 3. Number Theory
Modular arithmetic, GCD (Euclidean algorithm), Euler's totient φ(n), modular exponentiation, Chinese Remainder Theorem, quadratic residues, primitive roots. Primality testing. Factorisation methods: Pollard ρ, Pollard p-1.

### 4. Public Key Cryptography
RSA (key generation, encryption, decryption, CRT optimisation). Rabin cryptosystem. Discrete logarithms. Digital signatures. Certificates and PKI (leaf.crt practical).

### 5. Cryptanalysis
Differential cryptanalysis (attack paths through block ciphers). Linear cryptanalysis (FEAL-4 assignment). Classical: Vigenère breaking via Index of Coincidence + chi-squared frequency analysis (decrypt.py).

## Exam Pattern (2022–2024)

**Every year (⭐⭐):** Block ciphers (DES vs AES), confusion & diffusion, hash properties + birthday paradox, number theory basics, factorisation methods, FEAL-4/TinyDES differential paths, RSA + CRT. **Some years (⭐):** Linear cryptanalysis, padding schemes. **Format:** 20 marks per question, 5 questions.

---

*Related: [Root CLAUDE.md](../CLAUDE.md) | [FEAL4 README](./assignment_cryptanalysis/FEAL4LinearAttack/CLAUDE.md)*
