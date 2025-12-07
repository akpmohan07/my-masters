# Cryptography Final Revision Summary

## 1. Hash Functions  
- Map arbitrary-length input → fixed-length digest.  
- Must satisfy:  
  - **Preimage resistance** (hard to find message from hash)  
  - **Second preimage resistance** (hard to find different message with same hash)  
  - **Collision resistance** (hard to find any two messages with same hash)  
- Digest size = security level (collisions need ~2^(n/2) tries).

### Birthday Paradox & Hashing  
| Concept | Meaning |
|--------|---------|
| n | Number of hash outputs (2^digest_size) |
| k | Number of hash attempts |
| Collision likely when | k ≈ sqrt(n) = 2^(digest/2) |

---

## 2. MD Family (MD2, MD4, MD5)
- Produce 128-bit digests.
- **MD4** → weak.  
- **MD5** → fully broken (collisions, chosen-prefix attacks).  
- Should NOT be used for security.

---

## 3. SHA Family  
### SHA-1  
- 160-bit digest  
- Broken (Google SHAttered collision attack).  

### SHA-2  
- SHA-224, 256, 384, 512.  
- Strong & widely used.

### SHA-3 (Keccak – Sponge Construction)  
- Uses 1600-bit internal state.  
- **Absorb + Squeeze** model.  
- Resistant to length-extension attacks.  
- Steps: θ → ρ → π → χ → ι.

---

## 4. Integrity vs Confidentiality  
| Property | Encryption | MDC | MAC |
|----------|------------|------|------|
| Confidentiality | ✔ | ❌ | ❌ |
| Integrity | ❌ | ✔ | ✔ |
| Authentication | ❌ | ❌ | ✔ |

### MDC (Manipulation Detection Code)
- Uses hash only.  
- Detects modification but **no authentication**.

### MAC (Message Authentication Code)
- Uses **secret key**: MACₖ(m).  
- Provides integrity + authenticity.

---

## 5. Password Hashing  
### Salt  
- Must be **unique per user**.  
- Prevents rainbow-table attacks.  
- Stored alongside hash.

### Rainbow Attack vs Dictionary Attack  
| Attack | Description | Broken by Salt? |
|--------|-------------|------------------|
| Rainbow | Precomputed hash→password tables | ✔ Completely stops |
| Dictionary | Try passwords one by one | ❌ Still works (but slower w/ KDF) |

### KDF (PBKDF2, bcrypt, scrypt, Argon2)  
- Converts weak password → strong key.  
- Uses: **salt + iterations**.  
- Slow on purpose to stop brute force.

---

## 6. Key Generation  
- Best: use **cryptographically secure random generator**.  
- Passphrase-based keys require:  
  - Salt  
  - Many iterations  
  - KDF standard (PKCS#5 / PBKDF2)

---

## 7. PRNG from Hashes  
- Start with secret seed S.  
- S₀ = h(S).  
- S₁ = h(S₀), S₂ = h(S₁)…  
- Extract small bits from each state.  
- Secure if **internal states remain secret**.

---

# FINAL REVISION TABLE

| Topic | Key Idea | Security Concern |
|-------|-----------|------------------|
| Hash Functions | One-way, fixed digest | Collisions, preimages |
| MD5/SHA-1 | Legacy hashes | Broken |
| SHA-2 | Modern secure hash | None known |
| SHA-3 | Sponge-based | Very strong |
| MDC | Hash only | No sender authentication |
| MAC | Hash/cipher + key | Secure integrity & authenticity |
| Salt | Unique random per user | Stops rainbow tables |
| KDF | Slow hashing with salt | Prevent brute force |
| PRNG via Hash | Repeated hashing | Must keep state secret |

---

# END OF REVISION
