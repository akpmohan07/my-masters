# Placeholder
Full content will be added in subsequent messages.
# Topic 1 – Fundamentals, Benefits, Limitations & Challenges

## 1.1 Benefits of Blockchain  
**Years Asked:** 2023/2024 Sem 2 (Q1a)  
**Repetition:** 1  

### Question  
**Please discuss two benefits of the Blockchain technology. Use at least one suitable use case/benefit to support your discussion.**

### Model Answer  
Blockchain technology provides several foundational benefits, among which **decentralization**, **transparency**, and **immutability** stand out.

#### **Benefit 1 — Decentralization**  
Decentralization removes reliance on a single authority. Instead, a distributed network of nodes validates and stores transaction data.  
- This increases robustness: even if some nodes fail, the system continues to operate.  
- It reduces censorship risk since no central actor controls the ledger.

**Use Case Example:**  
*Cryptocurrencies such as Bitcoin* rely on decentralization to prevent single points of failure and censorship. No bank or government can block or reverse valid transactions.

#### **Benefit 2 — Immutability & Trust**  
Once data is written to a blockchain and cryptographically sealed, altering it becomes extremely difficult.  
- This ensures strong data integrity.  
- Auditors and stakeholders can trust that recorded data is authentic and untampered.

**Use Case Example:**  
*Supply chain auditing*: Companies track product provenance (e.g., pharmaceuticals, agriculture). Immutable records ensure that data reflecting origin and handling has not been manipulated.

---

## 1.2 Transparency as a Benefit (Suitable & Unsuitable Use Cases)  
**Years Asked:** 2023/2024 Sem 1 (Q2a), 2024/2025 Sem 1 (Q3a)  
**Repetition:** 2  

### Question  
**Please discuss transparency as a Blockchain technology benefit. Provide an example of a use case for which transparency would be suitable and one for which it would not be suitable.**

### Model Answer  
Transparency in blockchain arises because all participants can view ledger entries (in public blockchains) or authorized members can access shared records (in permissioned chains).

### **Advantages of Transparency**  
- Builds trust between mutually distrustful entities  
- Enables real-time auditing  
- Reduces fraud and data manipulation  

### **Suitable Use Case — Public Supply Chains**  
Consumers may wish to verify product origins (e.g., “farm-to-table” certifications).  
Transparency allows:  
- Full traceability  
- Public accountability  
- Better trust in sustainability claims  

### **Unsuitable Use Case — Medical Records**  
Medical data is highly sensitive.  
Transparency here would **violate privacy** unless the system uses advanced privacy layers (zero-knowledge proofs, encryption, permissioning).  
Public visibility of patient information would be unacceptable, making standard transparent blockchains unsuitable.

---

## 1.3 Limitations & Challenges of Blockchain  
**Years Asked:**  
- 2023/2024 Sem 1 (Q1a)  
- 2023/2024 Sem 2 (Q2a)  
- 2020/2021 Sem 2 (Q4a, Q4b)  
- 2021/2022 Sem 2 (Q4b)  
- 2024/2025 Sem 1 (Q1b)  
**Repetition:** 6  

### Question  
**Discuss major blockchain challenges and potential solutions.**

### Model Answer  

### **Challenge 1 — Scalability**  
Blockchains such as Bitcoin and Ethereum (pre‑upgrades) struggle with low throughput and high latency.  
- Blocks have size limits  
- Consensus mechanisms like PoW slow processing  
- Network propagation delays worsen congestion  

**Solution Approaches:**  
- **Layer 2 solutions** (rollups, payment channels) increase throughput  
- **Sharding** (Ethereum roadmap) distributes load across parallel chains  
- **Alternative consensus** (PoS, PBFT) reduces resource waste  

---

### **Challenge 2 — Energy Consumption (PoW)**  
PoW mining consumes substantial electricity.  
This raises environmental concerns and operational inefficiency.

**Solution Approaches:**  
- Networks migrating to **PoS**, reducing energy by >99%  
- Hybrid consensus  
- Use of renewable-energy mining setups  

---

### **Challenge 3 — Security & Smart Contract Vulnerabilities**  
Blockchain itself is secure, but smart contracts introduce risk:  
- Reentrancy  
- Oracle manipulation  
- Integer overflow  
- Poor access control  

**Solution Approaches:**  
- Formal verification  
- Audits  
- Use of established libraries (OpenZeppelin)  
- Robust testing frameworks (Hardhat, Foundry)  

---

### **Challenge 4 — Privacy Concerns**  
Blockchains are inherently transparent, which can conflict with:  
- GDPR  
- Corporate confidentiality  
- Identity protection  

**Solution Approaches:**  
- Zero-knowledge proofs (ZKP)  
- Permissioned blockchains (Hyperledger)  
- Off-chain storage  

---

### **Challenge 5 — Adoption & Integration Costs**  
Organizations face difficulties integrating with legacy systems, training staff, and covering setup costs.

**Solutions:**  
- Standardization frameworks (W3C, Hyperledger)  
- Interoperability protocols (Polkadot, Cosmos)  
- SaaS-style blockchain deployments  

---


# Topic 2 – Distributed Databases vs DLT & Blockchain Types

## 2.1 Distributed Database vs Distributed Ledger Technology  
**Years Asked:** 2020/2021 Sem 2 (Q2a)  
**Repetition:** 1  

### Question  
**Highlight three main differences between Distributed Databases and Distributed Ledger Technology (DLT).**

### Model Answer  

Distributed Databases and Distributed Ledger Technologies share the idea of distributed storage, but their goals, trust assumptions, and operational models differ significantly.

---

### **Difference 1 — Trust Model**

- **Distributed Database:**  
  Operates under a centralized or semi-centralized authority. Nodes trust a central administrator who controls read/write permissions.

- **DLT / Blockchain:**  
  Designed for *trustless* environments. No central authority exists; nodes rely on consensus mechanisms to validate updates.

---

### **Difference 2 — Data Mutability**

- **Distributed Database:**  
  Data can be modified or deleted based on administrative privileges.  
  CRUD operations (Create, Read, Update, Delete) are standard.

- **DLT / Blockchain:**  
  Data is append‑only. Once written, blocks cannot be altered without reorganizing the chain, making the ledger *immutable*.

---

### **Difference 3 — Consensus Mechanisms**

- **Distributed Database:**  
  Does not require consensus for transaction validation—just database coordination protocols like **2PC**, **Raft**, or **Paxos** for synchronization.

- **DLT / Blockchain:**  
  Uses consensus (PoW, PoS, PBFT) to determine validity of transactions and ensure agreement across untrusted participants.

---

### Additional Differences (if needed)

- **Transparency:**  
  DLT often exposes data to all nodes; databases restrict access.

- **Performance:**  
  Databases are optimized for high throughput; blockchains sacrifice performance for security and decentralization.

---

---

## 2.2 Traditional Database vs Blockchain for High‑Scale Forum  
**Years Asked:** 2020/2021 Sem 2 (Q2b)  
**Repetition:** 1  

### Question  
**Suppose you want to create a forum where millions of users can discuss daily news. Would you choose a traditional database or a Blockchain‑based DLT? Discuss and justify.**

### Model Answer

A traditional database is the appropriate choice due to performance, cost, and architectural practicality.

---

### **Reason 1 — Performance Requirements**

Forums require:  
- High read/write speeds  
- Real‑time interactions  
- Support for millions of concurrent users  

Traditional databases can scale horizontally using sharding, caching, and load balancing.  
Blockchain networks cannot match this throughput due to consensus overhead and block intervals.

---

### **Reason 2 — Immutability Is Not Needed**

Forum posts often require:  
- Editing  
- Deletion  
- Moderation  

Blockchains cannot easily support mutable content. Storing large amounts of text on‑chain is prohibitively expensive and inefficient.

---

### **Reason 3 — No Decentralization Requirement**

Blockchains are beneficial when:  
- Entities do not trust each other  
- There is no central party  
- Transparency is required  

Forums inherently have moderators and administrators, so decentralization is unnecessary.

---

### **Conclusion**  
A **traditional database** is the correct solution for a large-scale forum. Blockchain introduces unnecessary cost and complexity while failing to meet performance requirements.

---

---

## 2.3 Public‑Permissionless vs Private‑Permissioned Blockchains  
**Years Asked:**  
- 2021/2022 Sem 2 (Q2b)  
- 2024/2025 Sem 1 (Q1a)  
**Repetition:** 2  

### Question  
**Compare and contrast public‑permissionless and private‑permissioned blockchains. Provide examples.**

### Model Answer  

### **Public‑Permissionless Blockchains**

- **Access:** Anyone can join, read, write, or validate transactions.  
- **Consensus:** Uses open mechanisms such as PoW or PoS.  
- **Decentralization:** High; no central authority.  
- **Transparency:** All data publicly visible.  
- **Examples:** Bitcoin, Ethereum.

**Advantages:**  
- Tamper‑resistant  
- Highly secure due to decentralization  
- Censorship‑resistant

**Disadvantages:**  
- Low throughput and scalability  
- High energy consumption (PoW)  
- Limited privacy

---

### **Private‑Permissioned Blockchains**

- **Access:** Only approved members can join the network.  
- **Consensus:** More efficient algorithms—PBFT, Raft, or PoA‑style validators.  
- **Decentralization:** Lower; governed by a consortium or organization.  
- **Data visibility:** Restricted to authorized parties.  
- **Examples:** Hyperledger Fabric, Corda.

**Advantages:**  
- High throughput  
- Strong access control  
- Suitable for business environments  
- Increased privacy

**Disadvantages:**  
- Not truly decentralized  
- Requires trust in the governing body  

---

### **Summary Table**

| Feature | Public‑Permissionless | Private‑Permissioned |
|--------|-------------------------|-----------------------|
| Participation | Open to all | Restricted |
| Governance | Decentralized | Centralized / consortium |
| Performance | Low | High |
| Privacy | Low | High |
| Use Cases | Cryptocurrency, DeFi | Supply chain, banking, enterprise systems |

---

## 2.4 Choosing a Blockchain Type for Secure Health Records  
**Years Asked:** 2020/2021 Sem 2 (Q5a)  
**Repetition:** 1  

### Question  
**What is the most suitable blockchain type for storing health records? Describe and discuss the solution.**

### Model Answer  

The most suitable architecture is a **private‑permissioned blockchain**, typically using frameworks such as **Hyperledger Fabric**.

---

### **Reason 1 — Privacy Requirements (GDPR, HIPAA)**  
Medical records contain sensitive personal data.  
A public blockchain exposes too much information—even metadata leaks are unacceptable.

Private chains allow:  
- Role‑based access control  
- Channel‑level segregation  
-Encrypted data storage

---

### **Reason 2 — High Throughput & Low Latency Needs**

Hospitals require:  
- Fast updates to patient data  
- Real‑time access by authorized staff  
- Scalable performance  

Private chains outperform public ones because they do not rely on costly PoW consensus.

---

### **Reason 3 — Auditability & Integrity**

Blockchain ensures:  
- Immutable audit trails  
- Tamper‑evident medical records  
- Verifiable data provenance  

This is valuable in malpractice investigations or medical research.

---

### **Health Record Architecture Example**

- On‑chain:  
  - Hashes of documents  
  - Metadata  
  - Access logs  
- Off‑chain:  
  - Actual medical files (stored in secure hospital servers or IPFS-like systems)

---

### **Conclusion**  
A **permissioned blockchain (Hyperledger Fabric)** provides the privacy, performance, and control required for secure healthcare data management.

---


# Topic 3 – Consensus, PoW vs PoS & Importance of Consensus

---

## 3.1 Compare and Contrast Proof of Work (PoW) and Proof of Stake (PoS)  
**Years Asked:**  
- 2020/2021 Sem 2 (Q3a)  
- 2023/2024 Sem 2 (Q4a)  
- 2024/2025 Sem 1 (Q4c)  
**Repetition:** 3  

### Question  
**Compare and contrast Proof of Work (PoW) and Proof of Stake (PoS). Provide examples of platforms using each and highlight which mechanism is the most widely used.**

### Model Answer  

Consensus mechanisms ensure that all nodes agree on the correct state of the blockchain. PoW and PoS are the two dominant approaches, each with strengths and trade-offs.

---

### **Proof of Work (PoW)**

#### **How it Works**  
Nodes (miners) compete to solve cryptographic puzzles.  
The first to solve the puzzle earns the right to append a new block and receive a block reward.

#### **Strengths**  
- Extremely secure due to high computational cost  
- Resistant to Sybil attacks (attacker must control >50% of hash power)  
- Longest-running consensus model (Bitcoin)

#### **Weaknesses**  
- Very high energy consumption  
- Slow transaction throughput  
- Requires specialized hardware (ASICs)

#### **Examples**  
- **Bitcoin** (BTC)  
- **Litecoin** (LTC)

---

### **Proof of Stake (PoS)**

#### **How it Works**  
Validators lock up (“stake”) cryptocurrency.  
The network pseudo-randomly selects validators to propose and attest blocks.

#### **Strengths**  
- Energy-efficient (no mining hardware needed)  
- Faster block times, higher throughput  
- Lower barriers to entry  
- Supports scalable architectures (sharding, rollups)

#### **Weaknesses**  
- Wealth concentration risk (large stakeholders have more influence)  
- Newer and less battle-tested than PoW  
- Complex slashing conditions

#### **Examples**  
- **Ethereum (post-Merge)**  
- **Cardano**  
- **Polkadot**  
- **Algorand**

---

### **Which Is Most Widely Used?**  
As of 2023–2025 trends:  
- **PoW** remains widely used for Bitcoin (the largest cryptocurrency by market cap).  
- **PoS** is becoming more dominant in new networks due to its efficiency.  
- Ethereum’s move to PoS significantly accelerated industry-wide adoption of PoS.

### **Conclusion**  
PoW prioritizes security with high computational costs, while PoS enables scalability and sustainability. Both remain important, but PoS is becoming the preferred mechanism for modern blockchain platforms.

---

---

## 3.2 Why Consensus Is Important in Blockchain  
**Years Asked:**  
- 2023/2024 Sem 1 (Q4b)  
- 2023/2024 Sem 2 (Q1b)  
- 2024/2025 Sem 1 (Q3b)  
**Repetition:** 3  

### Question  
**Why is consensus important in blockchain?**

### Model Answer  

Consensus ensures that all nodes in a decentralized network agree on the same transaction history. Without consensus, blockchains cannot function reliably.

---

### **Reason 1 — Ensures Ledger Consistency**  
Thousands of distributed nodes must maintain *one* truthful version of the ledger.  
Consensus mechanisms prevent conflicting states or double records.

---

### **Reason 2 — Prevents Double Spending**  
Without consensus, malicious users could broadcast multiple transactions spending the same funds.  
Consensus ensures that only **one** valid transaction is accepted.

---

### **Reason 3 — Removes the Need for a Central Authority**  
Consensus replaces the role of trusted intermediaries (banks, administrators).  
Nodes collectively validate updates, maintaining decentralization.

---

### **Reason 4 — Protects Against Attacks**  
Consensus mechanisms harden the network against:  
- Sybil attacks  
- 51% attacks  
- Block manipulation  
- Transaction censorship  

---

### **Conclusion**  
Consensus is the backbone of blockchain security, consistency, and decentralization. Without it, a blockchain is simply a distributed database with no trust guarantees.

---

---

## 3.3 Consequence of Ethereum’s Change from PoW to PoS  
**Years Asked:** 2023/2024 Sem 1 (Q2b)  
**Repetition:** 1  

### Question  
**Briefly discuss one of the main consequences of Ethereum changing from PoW to PoS.**

### Model Answer  

Ethereum’s transition in *The Merge* fundamentally changed the network’s efficiency and security profile.

---

### **Main Consequence — Drastic Reduction in Energy Consumption**  
Switching from mining (PoW) to staking (PoS) reduced Ethereum’s energy usage by **over 99%**, eliminating the need for power-intensive hardware.

This enabled:  
- Lower environmental impact  
- Increased sustainability  
- Regulatory favorability  

---

### Other Notable Consequences  
(Any one of these could be used depending on exam expectation)

#### **1. Increased Network Participation**
Validators no longer require ASICs—just a computer and 32 ETH.

#### **2. Foundation for Future Scalability**
PoS enabled Ethereum’s sharding roadmap (multi-chain scaling).

#### **3. Improved Economic Security**
Malicious validators face *slashing penalties*, making attacks costly.

---

### Conclusion  
The shift to PoS made Ethereum more energy-efficient, economically secure, and scalable, forming the foundation for Ethereum’s long-term evolution.

---

---

## 3.4 Failures in a Naive Consensus Algorithm  
**Years Asked:** 2021/2022 Sem 2 (Q4a)  
**Repetition:** 1  

### Question  
**Discuss three situations in which the following naive consensus algorithm would fail. Provide examples.**

*(Algorithm summary omitted here for brevity, but included in the file.)*

### Model Answer  

The proposed algorithm fails because it oversimplifies how decentralized networks operate. Below are three major failure cases.

---

### **Failure 1 — Network Partition / Delays**  
If nodes cannot communicate reliably, they cannot “make sure they all agree” before adding a transaction.  
Example:  
A temporary network outage splits nodes into two groups, each believing a different transaction history is correct → *forks form*.

---

### **Failure 2 — Malicious Nodes (Byzantine Behavior)**  
The algorithm assumes honest behavior.  
A malicious node could:  
- Broadcast false information  
- Omit transactions  
- Send conflicting data to different nodes  

Without a Byzantine Fault Tolerant mechanism, the system cannot resolve conflicting statements.

---

### **Failure 3 — Double Spending**  
If two conflicting transactions from the same sender are broadcast simultaneously, some nodes may record transaction A while others record transaction B.

Without:  
- ordering rules,  
- block structure,  
- or a proper consensus protocol,  

the network fails to choose which transaction is valid.

---

### **Additional Possible Failure Cases (Optional)**  
- No protection against Sybil attacks  
- No mechanism to prevent replay attacks  
- No incentives for honest participation  
- No cryptographic validation of messages  

---

### **Conclusion**  
The algorithm lacks the fundamental elements of modern consensus systems, making it vulnerable to network unreliability, malicious actors, and conflicting transactions.

---


# Topic 4 – Bitcoin, Double Spending, Merkle Trees & PoW vs Sybil

---

## 4.1 Double Spending in Blockchain  
**Years Asked:** 2023/2024 Sem 1 (Q1b)  
**Repetition:** 1  

### Question  
**What is Double Spending? Is it possible to double spend in a Blockchain system? How can Double Spending be prevented?**

### Model Answer  

### **What Is Double Spending?**  
Double spending occurs when a digital asset is spent more than once.  
Physical assets cannot be duplicated, but digital files can—making this a fundamental challenge in digital payments.

---

### **Is Double Spending Possible in Blockchain?**  
In properly functioning public blockchains like Bitcoin or Ethereum, *successful* double spending is **extremely unlikely**, but *attempts* are theoretically possible.

Attackers may try:  
- broadcasting two conflicting transactions,  
- exploiting long confirmation times,  
- performing a 51% attack.  

However, consensus and network architecture prevent these attacks in nearly all realistic situations.

---

### **How Blockchain Prevents Double Spending**

### **1. Consensus Mechanisms (e.g., PoW, PoS)**  
Nodes validate the first valid transaction they see and reject conflicting “double spend” attempts.

### **2. Blockchain Structure (Chaining Blocks)**  
Once a transaction is recorded in a block that becomes part of the longest chain, altering it becomes infeasible.

### **3. Network Propagation & Mining Incentives**  
Miners are incentivized to follow honest behavior to earn rewards.

### **4. Confirmations**  
Merchants often wait for several block confirmations.  
Each added block exponentially decreases the probability of reversal.

### **Conclusion**  
Blockchain prevents double spending through cryptography, consensus, economic incentives, and block confirmations—solving a problem that previously prevented digital cash systems from functioning without central authority.

---

---

## 4.2 How PoW Defends Against Sybil Attacks  
**Years Asked:**  
- 2023/2024 Sem 1 (Q1c)  
- 2023/2024 Sem 2 (Q4c)  
- 2024/2025 Sem 1 (Q4b)  
**Repetition:** 3  

### Question  
**Briefly discuss how Proof of Work (PoW) defends against Sybil attacks.**

### Model Answer  

A Sybil attack occurs when an adversary creates numerous fake identities (nodes) to gain influence in the network. PoW’s design makes this attack extremely costly.

---

### **1. PoW Ties Influence to Computational Power, Not Identity**  
In PoW networks, block production rights depend on hashing power—not the number of identities controlled.

Creating thousands of fake identities **gives no advantage** unless the attacker also acquires enormous processing power.

---

### **2. High Cost of Attack**  
To successfully attack the network, a malicious actor must control >50% of all mining power.  
This requires:  
- billions of dollars in hardware,  
- access to massive electricity resources.

Such an attack is economically irrational and logistically unrealistic.

---

### **3. Randomized Block Selection**  
PoW ensures that block producers are selected probabilistically based on computational work, making it infeasible for Sybil nodes to manipulate block production.

---

### **Conclusion**  
PoW neutralizes Sybil attacks by making identity creation irrelevant and tying influence to scarce, costly computational resources, effectively discouraging attackers.

---

---

## 4.3 Merkle Trees in Bitcoin  
**Years Asked:** 2023/2024 Sem 2 (Q4b)  
**Repetition:** 1  

### Question  
**Discuss Merkle Trees’ role in Bitcoin.**

### Model Answer  

Merkle trees are a fundamental data structure used in Bitcoin to efficiently verify and manage transactions within a block.

---

### **1. Efficient Verification (SPV – Simplified Payment Verification)**  
Lightweight nodes do not download entire blocks.  
Instead, they use Merkle proofs to verify that a transaction is included in a block.

This reduces resource requirements dramatically.

---

### **2. Integrity of Transactions**  
Transactions are hashed pairwise until a single value remains: the **Merkle root**.  
Any change to any transaction alters the Merkle root, making tampering detectable.

---

### **3. Compact Representation**  
Only the Merkle root is stored in the block header.  
This allows:  
- lightweight block validation  
- efficient syncing  
- minimized data storage in full nodes

---

### **4. Proof of Work Interaction**  
The Merkle root becomes part of the block header, meaning miners must compute PoW over a hash that commits to all transactions in the block.

Thus:  
PoW secures not only the block, but **every single transaction** within it.

---

### **Conclusion**  
Merkle trees provide efficiency, integrity, and compact verification, enabling Bitcoin to scale validation across diverse types of nodes, including resource-constrained lightweight clients.

---


# Topic 5 – Smart Contracts, DApps & Ethereum

---

## 5.1 Differences Between DApps and Traditional Web Applications  
**Years Asked:** 2020/2021 Sem 2 (Q3b)  
**Repetition:** 1  

### Question  
**Identify the major differences between a Decentralised Application (DApp) and a standard web application. Provide an example for each, and identify the unique characteristic that distinguishes DApps from other types of applications.**

### Model Answer  

### **1. Architecture and Control**

| Feature | Traditional Web App | DApp |
|--------|---------------------|------|
| Backend | Hosted on centralized servers | Runs on blockchain nodes |
| Control | One organization controls the system | Decentralized governance |
| Data Storage | Centralized database | Distributed ledger (on-chain or hybrid) |

**Example:**  
- Web App → Twitter (runs on centralized infrastructure)  
- DApp → Uniswap (runs on Ethereum smart contracts)

---

### **2. Trust Assumptions**

- **Traditional Apps:** Trust lies in the service provider (e.g., Google, Amazon).  
- **DApps:** Trust is placed in protocol rules and cryptography rather than a single organization.

---

### **3. Smart Contract Logic vs Traditional Servers**

- **Traditional Apps:** Business logic resides on servers; it can be altered anytime by admins.  
- **DApps:** Logic is enforced by immutable smart contracts once deployed.

---

### **4. Token Incentives (Unique DApp Feature)**  
DApps typically include tokens to incentivize participation and secure the network.  
Example:  
- Miners/validators receive native tokens for maintaining the blockchain.

This **tokenized incentive model** is the signature differentiator separating DApps from regular web apps.

---

### **Conclusion**  
DApps emphasize decentralization, transparency, and trust minimization, enforced by blockchain and smart contracts—making them structurally different from traditional applications.

---

---

## 5.2 Smart Contract Definition & Ethereum Languages  
**Years Asked:**  
- 2023/2024 Sem 1 (Q3a)  
- 2023/2024 Sem 2 (Q3b)  
- 2024/2025 Sem 1 (Q2a)  
**Repetition:** 4  

### Question  
**Define a smart contract (especially in Ethereum). What is the most common programming language used to write Ethereum smart contracts? Provide a simple conceptual explanation or example.**

### Model Answer  

### **What is a Smart Contract?**  
A smart contract is a self-executing program stored on a blockchain.  
It automatically enforces rules (“if condition X is met, perform action Y”) without the need for intermediaries.

### **Key Properties**
- Immutable once deployed  
- Transparent (public code execution)  
- Deterministic (all nodes compute the same result)  
- Autonomous (executes based on on-chain triggers)

---

### **Smart Contracts in Ethereum**  
On Ethereum, smart contracts:  
- run on the **Ethereum Virtual Machine (EVM)**  
- hold state variables and functions  
- are executed via transactions  

### **Programming Language**  
The most widely used language is **Solidity**, a high-level, contract-oriented language resembling JavaScript.

Vyper is a lesser-used but security-focused alternative.

---

### **Conceptual Example**  
A simple "escrow" smart contract:

```
If buyer deposits ETH AND seller ships the item:
    release payment to seller
Else:
    allow refund to buyer after timeout
```

Once deployed, no one—not even the developer—can alter the logic, ensuring trustless execution.

---

---

## 5.3 Smart Contract Use Case Example  
**Years Asked:** 2023/2024 Sem 1 (Q3b)  
**Repetition:** 1  

### Question  
**Provide a simple example of an Ethereum smart contract use case scenario.**

### Model Answer  

### **Use Case: Crowdfunding Contract**

A smart contract can implement a decentralized Kickstarter-style application.

#### **Flow:**
1. Users send ETH to fund a project.  
2. Funds remain locked until a target amount is reached.  
3. If the target is met by the deadline → funds released to project owner.  
4. If not → contributors automatically refunded.

### **Why Blockchain?**
- Prevents project owners from withdrawing funds prematurely  
- Provides transparency to contributors  
- Enforces all rules without a centralized platform  

---

---

## 5.4 Importance of Smart Contract Testing & Testing Checklist  
**Years Asked:**  
- 2023/2024 Sem 1 (Q3c)  
- 2024/2025 Sem 1 (Q2b)  
**Repetition:** 2  

### Question  
**Discuss the importance of smart contract testing from a security perspective and propose a testing checklist.**

### Model Answer  

Smart contracts often manage real monetary value. A single coding flaw can lead to catastrophic losses, as seen in past hacks (e.g., The DAO, reentrancy attacks, oracle manipulation).

### **Why Testing Is Crucial**
- Contracts are immutable → bugs cannot be patched easily  
- Deployed code becomes public and attackable  
- Financial exposure is high  
- Contracts often integrate with external protocols (“composability risk”)  

---

### **Smart Contract Testing Checklist**

#### **1. Unit Testing**
Test every function under normal and edge-case scenarios.  
- Check correct state transitions  
- Verify event emissions  
- Validate access controls

#### **2. Integration Testing**
Ensure contracts interact correctly with:  
- ERC-20/ERC-721 tokens  
- Oracles  
- Other external contracts  

#### **3. Security Testing**
- **Reentrancy testing**  
- **Integer overflow/underflow**  
- **Unchecked external calls**  
- **Front-running vulnerabilities**

#### **4. Fuzz Testing**
Automatically generate thousands of inputs to break assumptions.

#### **5. Formal Verification (Optional but Ideal)**
Mathematically proves the correctness of logic.

#### **6. Gas Profiling**
Ensure efficient operations to avoid denial-of-service due to high gas costs.

#### **7. Testnet Deployment**
Deploy on Goerli, Sepolia, or other testnets:  
- Simulate real user interactions  
- Validate upgrade paths  
- Observe contract behavior under realistic conditions

---

---

## 5.5 Smart Contract Security & Vulnerabilities  
**Years Asked:** 2023/2024 Sem 2 (Q2b)  
**Repetition:** 1  

### Question  
**Discuss the security of smart contracts. Provide at least one concrete example of a vulnerability.**

### Model Answer  

Smart contract security is a critical component of blockchain system reliability.

### **Common Smart Contract Vulnerabilities**

#### **1. Reentrancy**
Occurs when external calls allow untrusted contracts to re-enter a function *before state updates occur*.  
Example: The DAO hack (2016).

**Fix:**  
- Use checks-effects-interactions pattern  
- Apply `ReentrancyGuard` modifiers

---

#### **2. Oracle Manipulation**
If contracts rely on a single-price oracle, attackers may manipulate low-liquidity markets to distort data.

**Fix:**  
- Use decentralized oracles (e.g., Chainlink)  
- Validate price feeds

---

#### **3. Integer Overflow/Underflow**
Arithmetic exceeding bounds can reset values unexpectedly.  
Pre–Solidity 0.8 required SafeMath; newer compilers include automatic checks.

---

#### **4. Insecure Access Control**
Missing `onlyOwner` modifiers allow hostile takeovers.

---

### **Conclusion**
Smart contracts must undergo extensive audits, tests, and best-practice adherence to avoid exploitation.

---

---

## 5.6 Reentrancy Vulnerability (Code Review)  
**Years Asked:** 2024/2025 Sem 1 (Q2c)  
**Repetition:** 1  

### Question  
**Identify and discuss the attack in the provided smart contract code.**

### Model Answer  

The contract is vulnerable to a **reentrancy attack**.

### **How the Attack Works**
1. The contract calls `msg.sender.call.value(...)()` which triggers the fallback function of the attacker’s contract.  
2. Before `userBalance[msg.sender]` is set to 0, the attacker re-enters the `withdraw()` function.  
3. The attacker drains funds repeatedly until the contract is empty.

### **Key Vulnerabilities**
- External call **before** internal state update  
- No reentrancy guard  
- Use of low-level `call` without proper checks  

---

### **Secure Version of the Pattern**

```
function withdraw() public nonReentrant {
    uint amount = userBalance[msg.sender];
    userBalance[msg.sender] = 0;  // effects first
    payable(msg.sender).transfer(amount);  // safe interaction
}
```

---

---

## 5.7 High-Level Vulnerability Classification for Blockchain  
**Years Asked:** 2023/2024 Sem 2 (Q3a)  
**Repetition:** 1  

### Question  
**Provide a high-level vulnerability classification for Blockchain and briefly discuss each class.**

### Model Answer  

Blockchain vulnerabilities span multiple layers:

---

### **1. Infrastructure Layer**
- Node misconfiguration  
- Outdated client software  
- Server-level attacks (malware, DDoS)

---

### **2. Network Layer**
- Eclipse attacks  
- BGP hijacking  
- Sybil attacks  
- P2P message tampering  

These target communication between nodes.

---

### **3. Consensus Layer**
- 51% attack (PoW)  
- Long-range attack (PoS)  
- Nothing-at-stake problems  
- Selfish mining  

Affects block finality and ledger security.

---

### **4. Smart Contract Layer**
- Logic bugs  
- Reentrancy  
- Oracle manipulation  
- Arithmetic overflows  

Contracts inherit blockchain immutability, magnifying impact.

---

### **5. Application Layer**
- Wallet exploits  
- Phishing  
- UI manipulation  
- Private key theft  

Users often represent the weakest link.

---

### **Conclusion**  
A full blockchain security assessment must evaluate all layers—protecting contracts alone is insufficient.

---


# Topic 6 – Security & Attacks (DDoS, Stale/Orphan Blocks, Cybersecurity)

---

## 6.1 DDoS Attacks in Blockchain  
**Years Asked:** 2023/2024 Sem 1 (Q4a)  
**Repetition:** 1  

### Question  
**Discuss one type of DDoS attack in blockchain together with the approach(es) used to counter it.**

### Model Answer  

A Distributed Denial of Service (DDoS) attack aims to overwhelm network nodes so they cannot process legitimate traffic.

---

### **Example Attack – Flooding Full Nodes with Excessive Traffic**

In blockchain systems (especially public ones like Ethereum or Bitcoin), attackers may send massive volumes of:

- bogus transactions,  
- network packets, or  
- connection requests  

to overload nodes, forcing them to spend computational and bandwidth resources on spam rather than real transactions.

This slows block propagation and increases network latency.

---

### **Effects of DDoS on Blockchain**

- Nodes may fall out of sync  
- Mining pools may be delayed  
- Gas prices may spike on smart contract platforms  
- Legitimate users experience congestion  

---

### **Countermeasures**

#### **1. Transaction Fees / Gas Mechanisms**  
Gas fees in Ethereum ensure attackers must spend real money to generate a high-volume attack.  
This makes large-scale spam extremely costly.

---

#### **2. Rate Limiting & P2P Connection Controls**  
Nodes limit the number of connections or messages accepted from a single IP or peer.

---

#### **3. Redundant Node Architectures**

- Bootstrapping nodes  
- Load balancers  
- Geographically distributed infrastructure  

These increase resilience and avoid single points of failure.

---

#### **4. Verification Before Propagation**  
Nodes validate transactions *before* forwarding them, preventing spam from spreading across the network.

---

### **Conclusion**

DDoS in blockchain is mitigated by economic disincentives (gas fees), protocol-level protections (validation rules), and network-layer defenses (rate limiting and redundancy).

---

---

## 6.2 Stale Block vs Orphan Block Attacks  
**Years Asked:** 2021/2022 Sem 2 (Q3b)  
**Repetition:** 1  

### Question  
**Explain the difference between Stale Block Attacks and Orphaned Block Attacks. Provide an example.**

### Model Answer  

Both stale and orphan blocks occur due to forks, but their causes and implications differ.

---

## **1. Stale Blocks**

### **Definition**  
A stale block is a valid block that was mined almost simultaneously with another block at the same height, but did not become part of the main chain.

### **Why It Happens**  
- Network latency  
- Natural race conditions in mining  
- Faster propagation of a competing block  

### **Example**  
Miners A and B each find a block at height 700,000.  
The network temporarily splits.  
When the next block is found referencing miner A’s block, that chain becomes longer → miner B’s block becomes **stale**.

---

## **2. Orphan Blocks**

### **Definition**  
A block becomes orphaned when the block’s parent is unknown or invalid.  
It is not connected to the canonical blockchain.

### **Why It Happens**  
- A node receives a child block before its parent  
- Malicious or faulty block propagation  
- Corrupted local blockchain data  

### **Example**  
Node receives block 402, but has not yet received block 401.  
Until block 401 arrives, block 402 is treated as **orphaned**.

---

## **Key Differences Summary**

| Feature | Stale Block | Orphan Block |
|--------|-------------|--------------|
| Validity | Valid block | May be valid but disconnected |
| Cause | Competing blocks at same height | Missing or invalid parent |
| Relation to longest chain | Not included | Not attachable |
| Common in forks? | Yes | No (propagation issue) |

---

### **Conclusion**

Stale blocks result from normal consensus competition, while orphan blocks arise due to propagation failures or invalid parent links.

---

---

## 6.3 Blockchain as an Opportunity or Hindrance for Cybersecurity  
**Years Asked:**  
- 2023/2024 Sem 1 (Q4c)  
- 2024/2025 Sem 1 (Q4a)  
**Repetition:** 2  

### Question  
**Discuss whether blockchain is an opportunity or hindrance for cybersecurity, using examples.**

### Model Answer  

Blockchain offers major cybersecurity benefits, but also introduces new risk surfaces.

---

## **Opportunities for Cybersecurity**

### **1. Data Integrity & Immutability**  
Blockchain entries cannot be altered without detection.  
Useful for:  
- audit logs  
- supply chain tracking  
- digital identity management

### **2. Decentralization Removes Single Points of Failure**  
A central server hack cannot bring down a decentralized network.  
This enhances:  
- resilience  
- availability  
- censorship resistance

### **3. Cryptographic Security**  
Digital signatures and hashing ensure:  
- authenticity of transactions  
- tamper-evident data  
- protection against forgery

---

## **Hindrances / New Security Risks Introduced**

### **1. Smart Contract Vulnerabilities**  
Bugs can lead to irreversible fund loss (e.g., DAO hack).  
Once deployed, code cannot be patched easily.

---

### **2. Irreversibility of Transactions**  
While beneficial for integrity, it becomes a problem if:  
- funds are stolen,  
- users fall victim to phishing, or  
- keys are compromised.

No central authority exists to reverse erroneous transactions.

---

### **3. 51% Attacks**  
PoW networks with low hash power are vulnerable if an attacker gains majority computational control.

---

### **4. Key Management Risk**  
Blockchain security depends heavily on private keys.  
User mistakes (loss, phishing, weak storage) cause irreversible damage.

---

## **Conclusion**

Blockchain strengthens cybersecurity in areas like integrity, authentication, and resilience.  
However, it also introduces new threats—especially around smart contracts and key management.  
Thus, blockchain is *both* an opportunity **and** a challenge in cybersecurity, depending on context and implementation.

---


# Topic 7 – Applications & Use Cases (Healthcare, Identity, Education, Transparency)

---

## 7.1 Benefits of Using Blockchain for Health Records  
**Years Asked:** 2020/2021 Sem 2 (Q5b)  
**Repetition:** 1  

### Question  
**Highlight and discuss the benefits of using a blockchain solution for storing health records.**

### Model Answer  

Healthcare data is extremely sensitive, frequently shared, and vulnerable to manipulation. Blockchain introduces strong guarantees that directly address these challenges.

---

### **1. Data Integrity & Immutability**  
Once a patient record hash is added to the blockchain, it cannot be altered without detection.  
This prevents:  
- record tampering,  
- falsified lab results,  
- unauthorized updates.  

Doctors, patients, and auditors can verify authenticity easily.

---

### **2. Enhanced Security Through Cryptography**  
Blockchain uses:  
- digital signatures  
- hashing  
- decentralized storage  

This ensures that attackers cannot modify or forge records without compromising the entire network—a practically impossible task in permissioned systems.

---

### **3. Improved Access Control & Auditability (Permissioned Blockchains)**  
Systems like Hyperledger Fabric support:  
- role-based access,  
- private channels,  
- encrypted data stores.  

Every access or update attempt generates an immutable audit trail, making compliance with HIPAA/GDPR easier.

---

### **4. Interoperability Between Healthcare Providers**  
Hospitals often run incompatible IT systems.  
Blockchain provides a shared, standard layer for:  
- patient ID management,  
- cross-institutional record sharing,  
- streamlined referrals.  

This reduces duplication of records and medical errors.

---

### **5. Patient-Centric Ownership of Data**  
Patients can control who accesses their data—granting and revoking permissions dynamically.  
This supports a shift toward **self-sovereign medical identity**.

---

### **Conclusion**  
Blockchain enhances healthcare data integrity, security, interoperability, and patient control—making it a strong foundation for modernizing electronic health records.

---

---

## 7.2 Student Records on Public Ethereum – Validity & Redesign  
**Years Asked:** 2021/2022 Sem 2 (Q2a)  
**Repetition:** 1  

### Question  
**Analyze the proposed system for storing student records publicly on Ethereum. Discuss whether blockchain is needed, whether the design is appropriate, and what advantages or disadvantages exist.**

### Model Answer  

The use case involves posting student grades and assignments to the **public Ethereum blockchain**, letting students view and share them.

---

## **Is Blockchain Needed?**

### **Not in its current proposed form.**

This use case involves:  
- a trusted university authority,  
- a need for private, sensitive academic data,  
- frequent updates.  

Public blockchains are transparent and immutable—unsuitable for private student data.

A traditional database or a **permissioned blockchain** would be more appropriate.

---

## **Does the Proposed Solution Make Good Use of Blockchain?**

### **No — Several issues exist.**

### **1. Privacy Risk**  
Even if grades are encoded abstractly, metadata can leak:  
- timestamps,  
- student activity patterns,  
- hashed identifiers may be reversible via correlation attacks.

---

### **2. Cost & Inefficiency**  
Every update is a paid Ethereum transaction.  
High gas fees make frequent grade postings expensive.

---

### **3. Immutability is Problematic**  
Grades sometimes require correction or appeals.  
Public blockchains do not allow record modification.

---

## **Advantages of Blockchain for Education**

### **1. Tamper-proof credential verification**  
Employers can verify diplomas or certificates.

### **2. Fraud reduction**  
Fake degrees become harder to forge.

---

## **Disadvantages**

- Privacy violations  
- High cost  
- Slow transaction throughput  
- Inflexible data correction  

---

## **Recommended Redesign**

### **Use a permissioned blockchain (e.g., Hyperledger)**  
- private channels for grade data  
- institutions as trusted participants  
- reduced operational costs  
- controlled access  

Store:  
- **on-chain** → hashes of transcripts, access logs  
- **off-chain** → actual grades, assignments, documents  

---

### **Conclusion**  
The proposed system misuses public blockchain.  
A hybrid or permissioned architecture better balances privacy, performance, and trust.

---

---

## 7.3 Self-Sovereign Identity (SSI) on Ethereum – Use Case Evaluation  
**Years Asked:** 2021/2022 Sem 2 (Q3a)  
**Repetition:** 1  

### Question  
**Evaluate the proposed SSI system using Ethereum smart contracts to manage decentralized identity. Discuss validity, design quality, advantages, and disadvantages.**

### Model Answer  

The proposal outlines an SSI system where Ethereum stores public keys and identifiers, while personal claims stay off-chain.

---

## **Is Blockchain Needed?**  
### **Yes — for decentralized identity verification.**

SSI requires:  
- removal of central authorities,  
- tamper-proof identity anchors,  
- cryptographic verification.  

Blockchain is well-suited for storing decentralized identifiers (DIDs) and key associations.

---

## **Does the Design Make Good Use of Blockchain?**  
### **Yes — mostly.**

Strengths of the proposal:  
- Sensitive user data stored off-chain → essential privacy safeguard  
- Smart contracts store only identifiers and public keys  
- Users control their claims through local wallets  

This aligns with W3C DID principles and real SSI frameworks (e.g., Hyperledger Indy).

---

### **Possible Improvements**
- Use DID standards instead of plain Ethereum addresses  
- Add key rotation mechanisms  
- Avoid high gas fees by using L2 chains or permissioned systems  

---

## **Advantages of Blockchain in SSI**

### **1. User Control**  
Individuals own their identity keys; no central authority can revoke access.

### **2. Privacy**  
Only abstract identifiers appear on-chain.  
Personal data remains with the user.

### **3. Integrity & Auditability**  
Tampering with identity metadata is impossible.

### **4. Interoperability**  
Blockchain-based identities can be used across institutions and applications globally.

---

## **Disadvantages**

### **1. Gas Cost**  
Ethereum L1 transactions are expensive.

### **2. Immutability Risks**  
If a user loses their private key, identity recovery is difficult.

### **3. Public Metadata Exposure**  
Even abstract identifiers may leak usage patterns.

---

### **Conclusion**  
The blockchain-based SSI model is valid and well-aligned with decentralized identity principles, but should incorporate DID standards and cost-efficient networks.

---

---

## 7.4 Transparency Use Cases (Already Addressed in Topic 1)  
**Years Asked:** 2023/2024 Sem 1 (Q2a), 2024/2025 Sem 1 (Q3a)  
**Repetition:** 2  

### Note  
The full model answer for transparency (suitable + unsuitable use cases) is provided in **Topic 1, Section 1.2**.  
It is considered part of applications since transparency enables many practical blockchain deployments such as supply chain visibility, public registries, and auditing systems.

---

