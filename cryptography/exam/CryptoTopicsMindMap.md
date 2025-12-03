---

# **🧠 CRYPTOGRAPHY – FULL COURSE MIND MAP**

---

# **1. Symmetric Cryptography**

### **1.1 Block Ciphers**

* Definition
* Block sizes
* Padding
* Confusion & diffusion
* Substitution-permutation networks
* Iterated block ciphers

#### **DES**

* Feistel structure
* S-boxes
* P-boxes
* Key schedule
* Triple DES
* Security limits

#### **AES**

* SubBytes
* ShiftRows
* MixColumns
* AddRoundKey
* Key schedule

#### **Other Block Ciphers**

* FEAL
* IDEA
* Blowfish
* SAFER
* RC5

---

### **1.2 Stream Ciphers**

* Bit/byte stream encryption
* Synchronous vs asynchronous
* Error propagation
* Keystream properties

#### **PRGs**

* LCG
* Blum Blum Shub
* Real random numbers

#### **LFSRs & NLFSRs**

* Linear feedback shift registers
* Combining functions (e.g., A5/1)
* Trivium

#### **RC4**

* Key scheduling (KSA)
* Keystream generation (PRGA)
* WEP weaknesses

---

# **2. Modes of Operation**

* ECB
* CBC
* CFB
* OFB
* CTR
* Why modes exist (pattern leak in ECB)

---

# **3. Number Theory**

### **3.1 Basics**

* Division, remainder
* GCD, LCM
* Euclid & Extended Euclid

### **3.2 Modular Arithmetic**

* Zn
* Addition, multiplication
* Inverses
* Modular exponentiation

### **3.3 CRT (Chinese Remainder Theorem)**

* Reconstruction
* CRT exponentiation

### **3.4 Quadratic Residues**

* Definition
* Euler’s criterion
* Legendre symbol
* Jacobi symbol
* Tonelli–Shanks
* Square roots mod n = pq

### **3.5 Groups, Rings, Fields**

* Group properties
* Rings (Zn,+,×)
* Fields (GF(p), GF(2⁸))
* Polynomial arithmetic

### **3.6 Euler Totient & Fermat**

* φ(n)
* Euler’s theorem
* Fermat’s Little Theorem
* Primitive roots
* Discrete logarithms

### **3.7 Primality Tests**

* Fermat
* Solovay–Strassen
* Miller–Rabin
* AKS

---

# **4. Public Key Cryptography**

### **4.1 RSA**

* Key generation
* Encryption/decryption
* CRT optimization
* Attacks

### **4.2 Rabin**

* Encryption/decryption
* Square root problem

### **4.3 ElGamal**

* Based on discrete logs
* Encryption
* Signatures

### **4.4 DH Key Exchange**

* Shared secrets
* Man-in-the-middle issues

### **4.5 Signatures**

* Hash-and-sign
* RSA signature
* DSA

### **4.6 Certificates / PKI**

* CA
* Digital certificates
* Trust chains

---

# **5. Cryptanalysis**

### **5.1 Structural Attacks**

* Confusion/diffusion failures
* XOR weaknesses

### **5.2 Differential Cryptanalysis**

* Input difference → output difference
* Characteristics
* DES differential tables

### **5.3 Linear Cryptanalysis**

* Linear approximations
* Attack equations
* Known plaintext approach

### **5.4 TinyDES Attacks**

* Differential
* Linear
* Key extraction

### **5.5 FEAL-4 Attacks**

* Differential
* Linear
* Meet-in-the-middle optimizations

---

# **6. Hash Functions**

### **6.1 Hash Properties**

* Pre-image resistance
* Second pre-image
* Collision resistance

### **6.2 Birthday Paradox**

* Collision probability
* 2^(n/2) attacks

### **6.3 Merkle–Damgård Construction**

* Padding
* Compression
* Chaining

### **6.4 Specific Hash Functions**

* MD2 / MD4 / MD5
* SHA-1
* SHA-256 / SHA-384 / SHA-512
* RIPEMD-160
* SHA-3 (Keccak sponge)

### **6.5 Applications**

* MDC
* MAC
* HMAC
* CBC-MAC
* Password hashing
* Digital signatures
* Integrity checking

---
