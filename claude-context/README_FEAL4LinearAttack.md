# FEAL-4 Linear Cryptanalysis Attack — Project README

> **Module:** Cryptography | **Type:** Main Assignment | **Language:** Java | **Build:** Gradle

## Purpose

A complete implementation of a **known-plaintext linear cryptanalysis attack** on the FEAL-4 block cipher. Recovers all 6 round keys from 50 plaintext/ciphertext pairs by exploiting linear approximations of the cipher's internal operations.

## Architecture

```
src/main/java/com/mohanverse/dev/feal4/
├── FEAL4Cryptanalysis.java        # Main entry point — orchestrates the full attack
│                                  #   1. Load pairs → 2. Init components → 3. Recover keys → 4. Validate
├── FealProgram.java               # Reference FEAL-4 cipher implementation
│                                  #   pack/unpack, F-function (g0/g1 mixing + ROL2), encrypt/decrypt
├── FealOperations.java            # Bit manipulation wrapper
│                                  #   extractBitAtPosition(), computeXorOfBits(), xorThree/xorFour
├── KeyCandidateFilter.java        # Core: linear approximation equations (~295 lines)
│                                  #   Separate methods for Key0/Key1/Key2/Key3 approximations
│                                  #   Tests if XOR of specific bit positions is constant across all pairs
├── KeyCandidateGenerator.java     # Key space partitioning strategy
│                                  #   Inner bytes: 2^12 = 4,096 candidates
│                                  #   Outer bytes: 2^20 per inner candidate
│                                  #   Reconstructs 32-bit key from inner + outer parts
├── KeyRecoverySolver.java         # Sequential recovery engine
│                                  #   tryAllKey0/1/2/3Candidates() — nested filtering with callbacks
│                                  #   deriveFinalKeys() — algebraic computation of Key4, Key5
│                                  #   validateFullKey() — decrypt all 50 pairs to confirm
└── PlaintextCiphertextLoader.java # Parses known.txt (hex pairs into L0/R0/L4/R4 values)
```

## Attack Pipeline

```
Step 1: Load Data
    known.txt → 50 pairs of (Plaintext, Ciphertext) in hex
    Each split into 32-bit left/right halves: (L0, R0) and (L4, R4)

Step 2: Key Space Partitioning
    For each round key (32 bits total):
    ├── Inner bytes (bits 5,13,21): 2^12 = 4,096 candidates
    └── Outer bytes (bits 7,15,23,31): ~2^20 per inner candidate
    Total search per key: ~4 billion, but filtering reduces drastically

Step 3: Linear Approximation Filtering
    For each key candidate, compute:
        bit_i(L0 ⊕ F(L4 ⊕ R4 ⊕ K_candidate)) ⊕ bit_j(L4 ⊕ R4) ⊕ bit_k(R0)
    If this XOR is the SAME value (0 or 1) across all 50 pairs → candidate survives
    Most wrong candidates produce inconsistent XOR → eliminated

Step 4: Sequential Key Recovery
    Key0 recovered first (depends only on plaintext/ciphertext)
    Key1 recovered next (uses Key0 + F-function outputs)
    Key2 recovered (chains Key0 → Key1)
    Key3 recovered (chains Key0 → Key1 → Key2)

Step 5: Derive Final Keys
    Key4 and Key5 computed algebraically from:
    Key4 = L0 ⊕ R0 (first pair, using Feistel structure equations)
    Key5 = L4 ⊕ R4 ⊕ ... (derived from round structure)

Step 6: Validation
    Decrypt all 50 ciphertext pairs using recovered 6-key set
    Verify each decryption matches the original plaintext
```

## FEAL-4 Cipher Structure

FEAL-4 is a 4-round Feistel cipher:
- **Block size:** 64 bits (32-bit left + 32-bit right halves)
- **Key schedule:** 6 subkeys (Key0–Key5), each 32 bits
- **F-function:** Uses g0/g1 mixing functions with ROL2 (rotate left 2 bits)
- **Rounds:** Standard Feistel — `R_i+1 = L_i ⊕ F(R_i ⊕ K_i)`, `L_i+1 = R_i`

## Reference Implementations

```
Reference/
├── FEAL.java              # Academic reference cipher implementation
├── feal4attack.py         # Python attack variant using parallel processing + JSON input
└── BitUnderstand.py       # Educational script: step-by-step bit manipulation with binary visualisation
```

## Key Cryptanalysis Concepts

- **Known-plaintext attack** — attacker has access to both plaintext and ciphertext (but not the key)
- **Linear approximation** — finding linear equations that hold with high probability through the cipher's nonlinear components
- **Key partitioning** — dividing the 32-bit key search space into independent inner/outer byte searches to reduce complexity
- **Sequential recovery** — exploiting the Feistel structure to recover keys one round at a time

## Build & Run

```bash
cd cryptography/assignment_cryptanalysis/FEAL4LinearAttack
./gradlew build
./gradlew run
```

Input: `known.txt` (50 plaintext/ciphertext pairs in hex)
Output: All 6 recovered round keys + validation confirmation

---

*Related: [Cryptography](./CRYPTOGRAPHY.md) | [MASTER.md](./MASTER.md)*
