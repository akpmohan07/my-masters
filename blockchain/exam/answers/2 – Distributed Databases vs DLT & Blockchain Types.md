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

