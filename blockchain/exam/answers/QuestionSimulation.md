# 📝 **E — EXAM SIMULATION (Modeled on 2020–2025 Patterns)**

Below is a fully realistic 4-question final exam modeled on historical exam structures.

---

# **📘 BLOCKCHAIN FINAL EXAM — SIMULATED PAPER**

*(4 questions × 25 marks each)*

---

# **QUESTION 1 — Blockchain Fundamentals & Challenges (25 marks)**

### **1(a). (10 marks)**

Explain three major benefits of blockchain technology and discuss one use case where these benefits are essential.

### **1(b). (15 marks)**

Identify two significant challenges faced by blockchain systems today and propose practical solutions for each.
Your answer should reference real consensus or scalability mechanisms.

---

# **QUESTION 2 — Smart Contracts & Security (25 marks)**

### **2(a). (10 marks)**

Define what a smart contract is in Ethereum. Explain why immutability is both a benefit and a risk.

### **2(b). (10 marks)**

Review the smart contract code below and identify the vulnerability. Explain how you would fix it.

```solidity
function withdraw() public {
    uint amount = balances[msg.sender];
    if (msg.sender.call{value: amount}("")) {
        balances[msg.sender] = 0;
    }
}
```

### **2(c). (5 marks)**

Provide two best practices for ensuring smart contract security.

---

# **QUESTION 3 — Consensus, Bitcoin & Attack Prevention (25 marks)**

### **3(a). (15 marks)**

Compare Proof of Work (PoW) and Proof of Stake (PoS) in terms of security, performance, and environmental impact.
Provide examples of blockchains using each.

### **3(b). (10 marks)**

Explain how Bitcoin prevents double spending.
Your answer should reference block confirmations, mining, and transaction structure.

---

# **QUESTION 4 — Applications & Cybersecurity (25 marks)**

### **4(a). (10 marks)**

A multinational healthcare provider wants to share patient data between clinics securely.
Should they use a public or permissioned blockchain?
Justify your answer with respect to privacy, performance, and auditability.

### **4(b). (10 marks)**

Discuss whether blockchain is an opportunity or hindrance for cybersecurity.
Provide at least one example supporting each view.

### **4(c). (5 marks)**

Explain the difference between stale blocks and orphan blocks in blockchain systems.

---

# ⭐ **Your simulation is complete.**

If you want, I can also generate:

* A **marking scheme / model answers** for the simulated exam
* A **multiple-choice quiz**
* A **flashcard deck**
* A **practice coding question for smart contracts**

Just tell me!
