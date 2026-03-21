# Blockchain — Subject Context

> **Module:** Blockchain Technology | **Programme:** MSc Computing | **Student:** Mohankumar Muthusamy

## Overview

This module covers blockchain fundamentals, consensus mechanisms, smart contract development (Solidity/Ethereum), security vulnerabilities, and real-world applications. The coursework includes hands-on Solidity smart contract development and comprehensive exam preparation spanning 5 years of past papers (2020–2025).

## Folder Structure

```
blockchain/
├── week_3/run.py                          # Proof of Work implementation (Python)
├── week_4_token/SimpleToken.sol           # ERC-20 style token contract
├── week_7_contracts/                      # Vulnerability demonstration contracts
│   ├── LotteryV1.sol                      #   Predictable randomness (blockhash)
│   ├── TokenManager.sol                   #   Missing access control on mint()
│   ├── TokenV1.sol                        #   Integer overflow (pre-0.8 Solidity)
│   ├── Forwarder.sol                      #   Unchecked external call return
│   └── VulnerableBank.sol                 #   Secure withdrawal pattern (checks-effects-interactions)
├── smart_contracts/
│   ├── basics/
│   │   ├── voting.sol                     # Yes/No voting with double-vote prevention
│   │   └── SimpleVault.sol                # ETH deposit/withdrawal vault
│   └── role_based_contracts/
│       └── ExtendedVault.sol              # VIP system with rate-limited withdrawals
├── exam/
│   ├── answers/                           # Detailed answers by topic (8 files)
│   ├── GroupByTopicAndYear.md             # Question frequency analysis across 5 years
│   ├── SmarrtContractAndEtherum.md        # Web3, DApps, Ethereum architecture
│   ├── Condensed Revision Sheet.md        # High-yield 2-page summary
│   └── QuestionSimulation.md             # Practice exam (4 questions, 100 marks)
└── Mohankumar_Muthusamy_Defense Mechanisms for LLM Security Vulnerabilities.pdf
```

## Topics Covered

### 1. Blockchain Fundamentals
Decentralization, transparency, immutability. Blockchain types: public-permissionless vs private-permissioned. Distributed databases vs DLT comparison. Benefits (trustless, censorship-resistant) and limitations (scalability, energy, privacy).

### 2. Consensus Mechanisms
Proof of Work (PoW) vs Proof of Stake (PoS) — security, performance, energy trade-offs. Ethereum's transition ("The Merge"). Why consensus is critical for decentralization. Byzantine fault tolerance.

### 3. Bitcoin & Cryptographic Structures
Double spending prevention. Merkle trees for efficient SPV verification. PoW as Sybil attack defence. Block structure: header, transactions, Merkle root.

### 4. Smart Contracts & Ethereum
Smart contract properties: immutable, transparent, deterministic. DApp architecture vs traditional web apps. Web 3.0 evolution (Read → Read+Write → Read+Write+Own). EVM execution model. Gas mechanics.

### 5. Smart Contract Security (Hands-on)
Five vulnerability patterns implemented in Solidity:
- **Reentrancy** — external calls before state updates
- **Oracle Manipulation** — untrusted price feeds
- **Integer Overflow** — pre-0.8 Solidity arithmetic (TokenV1.sol)
- **Insecure Access Control** — missing onlyOwner checks (TokenManager.sol)
- **Unchecked External Calls** — silent failures (Forwarder.sol)

Testing checklist: unit, integration, security, fuzz, formal verification, gas profiling.

### 6. Security & Attacks
DDoS attacks and countermeasures. Stale blocks (valid but not in main chain) vs orphan blocks (disconnected parent). 51% attacks. Blockchain as cybersecurity opportunity and hindrance.

### 7. Real-World Applications
Healthcare (permissioned blockchain, GDPR/HIPAA compliance). Self-sovereign identity with cryptographic keys. Education records and verification. Supply chain transparency.

### 8. Scalability Solutions
Layer 2: Rollups (optimistic, ZK), payment channels, Lightning Network. Sharding for parallel processing. Alternative consensus: PoS, PBFT.

## Key Code Highlights

**run.py** — Proof of Work in Python: SHA256 hashing with configurable difficulty, nonce incrementing until hash prefix matches target zeros.

**SimpleToken.sol** — ERC-20 pattern: `transfer()`, `approve()`, `transferFrom()`, `mint()` with balance mappings and allowance tracking.

**ExtendedVault.sol** — Advanced access control: VIP status system, withdrawal rate limiting (5 min cooldown, 0.01 ETH max for non-VIPs), `onlyOwner` modifier.

**Week 7 contracts** — Intentionally vulnerable contracts used as teaching examples. Each demonstrates one specific vulnerability pattern with comments explaining the flaw.

## Exam Pattern (2020–2025)

**Most frequent questions (asked 3–6 times):** Blockchain limitations with solutions, PoW vs PoS comparison, why consensus matters, PoW defence against Sybil. **Format:** 25-mark written questions, some years include MCQ on Loop platform.

---

*Related: [Root CLAUDE.md](../CLAUDE.md) | [Practicum](../Practicum/CLAUDE.md)*
