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

