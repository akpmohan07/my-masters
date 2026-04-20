# Question 3

## Q3 — Subnetting + VLSM

**CSC1134 — Exam Revision** **Date:** 20 April 2026 **Exam:** Tuesday 21 April 2026 — 9:00 AM

***

### Pattern summary

| Year  | Block            | LANs | Links   | Max hosts |
| ----- | ---------------- | ---- | ------- | --------- |
| 2023  | 192.168.203.0/24 | 4    | 1 LINK  | 70        |
| 2023R | 192.168.203.0/24 | 4    | 1 LINK  | 60        |
| 2024  | 136.206.104.0/22 | 4    | 2 LINKs | 500       |
| 2025  | 136.206.128.0/17 | 4    | 3 LINKs | 10000     |
| 2025R | 136.206.104.0/22 | 4    | 2 LINKs | 500       |

**Trend:** Blocks getting bigger, hosts getting larger. Same format every year — most predictable question in the exam.

***

### VLSM method — 5 steps

**Step 1 — Sort LANs by size (largest first)** **Step 2 — Calculate base values (n, block, prefix, mask)** **Step 3 — Assign starting addresses contiguously** **Step 4 — Calculate broadcast for each subnet** **Step 5 — Document assumptions**

***

### Core formulas

#### Step 1 — Base values:

```
n (host bits) = 32 - prefix
block size    = 2^n
usable hosts  = block - 2
prefix        = 32 - n
```

#### Step 2 — Mask formula:

**Concept:** Fill host bits from right to left across octets. Host bits occupy octet 4 first, then octet 3, then octet 2.

```
Mask octet = 256 - 2^(host bits in that octet)
Fully network octet = 255
Fully host octet    = 0
```

**Bit distribution table:**

| n (host bits) | Octet 3 host bits | Octet 4 host bits | Mask            |
| ------------- | ----------------- | ----------------- | --------------- |
| 2             | 0                 | 2                 | 255.255.255.252 |
| 6             | 0                 | 6                 | 255.255.255.192 |
| 7             | 0                 | 7                 | 255.255.255.128 |
| 8             | 0                 | 8                 | 255.255.255.0   |
| 9             | 1                 | 8                 | 255.255.254.0   |
| 10            | 2                 | 8                 | 255.255.252.0   |
| 11            | 3                 | 8                 | 255.255.248.0   |
| 12            | 4                 | 8                 | 255.255.240.0   |
| 13            | 5                 | 8                 | 255.255.224.0   |
| 14            | 6                 | 8                 | 255.255.192.0   |
| 15            | 7                 | 8                 | 255.255.128.0   |

**Example — n=10:**

```
Octet 1  . Octet 2  . Octet 3  . Octet 4
11111111 . 11111111 . 11111100 . 00000000
                          ^^      ^^^^^^^^
                     2 host bits  8 host bits

Octet 4 = 256 - 2^8 = 0
Octet 3 = 256 - 2^2 = 252
Mask = 255.255.252.0 ✅
```

#### Step 3 — Next subnet formula:

```
sum = last octet + block size

Case 1 — sum < 256:
   next subnet = same 3rd octet, last octet = sum

Case 2 — sum ≥ 256:
   3rd octet += sum ÷ 256  (integer quotient)
   last octet = sum mod 256 (remainder)

Special case — block > 256 (large subnets):
   last octet always = 0
   3rd octet += block ÷ 256
```

**Examples:**

```
136.206.106.0  + block 128:
sum = 0 + 128 = 128 < 256
next = 136.206.106.128 ✅

136.206.106.128 + block 128:
sum = 128 + 128 = 256 ≥ 256
quotient = 256÷256 = 1 → 3rd octet +1
remainder = 0 → last octet = 0
next = 136.206.107.0 ✅

136.206.104.0 + block 512:
block > 256 → 3rd octet += 512÷256 = 2
next = 136.206.106.0 ✅
```

#### Step 4 — Broadcast formula:

```
broadcast = next subnet - 1

If next subnet last octet = 0:
   broadcast 3rd octet = next 3rd octet - 1
   broadcast last octet = 255

If next subnet last octet ≠ 0:
   broadcast 3rd octet = same
   broadcast last octet = next last octet - 1
```

**Examples:**

```
next subnet = 136.206.106.0
broadcast   = 136.206.105.255 ✅

next subnet = 136.206.106.128
broadcast   = 136.206.106.127 ✅
```

***

### Powers of 2 — memorise:

```
2^2  = 4        2^8  = 256
2^3  = 8        2^9  = 512
2^4  = 16       2^10 = 1024
2^5  = 32       2^11 = 2048
2^6  = 64       2^13 = 8192
2^7  = 128      2^14 = 16384
                2^15 = 32768
```

***

### Finding n from hosts needed:

```
Find smallest n where 2^n - 2 ≥ hosts needed

500 hosts:  2^9 - 2 = 510 ✅  n=9
126 hosts:  2^7 - 2 = 126 ✅  n=7
64 hosts:   2^7 - 2 = 126 ✅  n=7 (not n=6, 2^6-2=62 too small)
10000 hosts: 2^14 - 2 = 16382 ✅  n=14
2000 hosts:  2^11 - 2 = 2046 ✅  n=11
256 hosts:   2^9 - 2 = 510 ✅  n=9 (not n=8, 2^8-2=254 too small)
```

***

### LINK subnets — always /30:

```
LINK = connection between two routers
Needs only 2 usable hosts
n=2, block=4, prefix=/30, mask=255.255.255.252
usable = 4-2 = 2 ✅
```

***

### Worked example — 2025 Resit \[Q3]

**Block:** `136.206.104.0/22`

**LANs:**

| LAN      | Hosts |
| -------- | ----- |
| HR       | 64    |
| STUDENTS | 500   |
| FINANCE  | 100   |
| ACADEMIC | 126   |

**Step 1 — Sort by size:**

```
1. STUDENTS  500  → n=9,  block=512,  prefix=/23
2. HR         64  → n=7,  block=128,  prefix=/25
3. FINANCE   100  → n=7,  block=128,  prefix=/25
4. ACADEMIC  126  → n=7,  block=128,  prefix=/25
5. LINK-A      2  → n=2,  block=4,    prefix=/30
6. LINK-B      2  → n=2,  block=4,    prefix=/30
```

**Step 2 — Assign subnets:**

```
! Assumption: starting address = 136.206.104.0
! Assumption: LINK subnets use /30 (most efficient for 2 hosts)
! Assumption: same size subnets assigned in table order (HR before FINANCE before ACADEMIC)
```

| Subnet   | Network/CIDR       | Broadcast       | Mask            | Usable |
| -------- | ------------------ | --------------- | --------------- | ------ |
| STUDENTS | 136.206.104.0/23   | 136.206.105.255 | 255.255.254.0   | 510    |
| HR       | 136.206.106.0/25   | 136.206.106.127 | 255.255.255.128 | 126    |
| FINANCE  | 136.206.106.128/25 | 136.206.106.255 | 255.255.255.128 | 126    |
| ACADEMIC | 136.206.107.0/25   | 136.206.107.127 | 255.255.255.128 | 126    |
| LINK-A   | 136.206.107.128/30 | 136.206.107.131 | 255.255.255.252 | 2      |
| LINK-B   | 136.206.107.132/30 | 136.206.107.135 | 255.255.255.252 | 2      |

**Working:**

```
STUDENTS:
start  = 136.206.104.0
block  = 512 → jump = 512÷256 = 2 in 3rd octet
next   = 136.206.106.0
bcast  = 136.206.105.255

HR:
start  = 136.206.106.0
sum    = 0+128 = 128 < 256
next   = 136.206.106.128
bcast  = 136.206.106.127

FINANCE:
start  = 136.206.106.128
sum    = 128+128 = 256 ≥ 256 → carry!
next   = 136.206.107.0
bcast  = 136.206.106.255

ACADEMIC:
start  = 136.206.107.0
sum    = 0+128 = 128 < 256
next   = 136.206.107.128
bcast  = 136.206.107.127

LINK-A:
start  = 136.206.107.128
sum    = 128+4 = 132 < 256
next   = 136.206.107.132
bcast  = 136.206.107.131

LINK-B:
start  = 136.206.107.132
sum    = 132+4 = 136 < 256
next   = 136.206.107.136
bcast  = 136.206.107.135
```

***

### Worked example — 2025 \[Q3]

**Block:** `136.206.128.0/17`

**LANs:**

| LAN      | Hosts |
| -------- | ----- |
| HR       | 256   |
| STUDENTS | 10000 |
| FINANCE  | 64    |
| ACADEMIC | 2000  |

**Step 1 — Sort by size:**

```
1. STUDENTS  10000 → n=14, block=16384, prefix=/18
2. ACADEMIC   2000 → n=11, block=2048,  prefix=/21
3. HR          256 → n=9,  block=512,   prefix=/23
4. FINANCE      64 → n=7,  block=128,   prefix=/25
5. LINK-A        2 → n=2,  block=4,     prefix=/30
6. LINK-B        2 → n=2,  block=4,     prefix=/30
7. LINK-C        2 → n=2,  block=4,     prefix=/30
```

**Step 2 — Assign subnets:**

```
! Assumption: starting address = 136.206.128.0
! Assumption: LINK subnets use /30
! Assumption: same size subnets assigned in table order
```

| Subnet   | Network/CIDR       | Broadcast       | Mask            | Usable |
| -------- | ------------------ | --------------- | --------------- | ------ |
| STUDENTS | 136.206.128.0/18   | 136.206.191.255 | 255.255.192.0   | 16382  |
| ACADEMIC | 136.206.192.0/21   | 136.206.199.255 | 255.255.248.0   | 2046   |
| HR       | 136.206.200.0/23   | 136.206.201.255 | 255.255.254.0   | 510    |
| FINANCE  | 136.206.202.0/25   | 136.206.202.127 | 255.255.255.128 | 126    |
| LINK-A   | 136.206.202.128/30 | 136.206.202.131 | 255.255.255.252 | 2      |
| LINK-B   | 136.206.202.132/30 | 136.206.202.135 | 255.255.255.252 | 2      |
| LINK-C   | 136.206.202.136/30 | 136.206.202.139 | 255.255.255.252 | 2      |

**Working:**

```
STUDENTS:
start  = 136.206.128.0
block  = 16384 → jump = 16384÷256 = 64
next   = 136.206.(128+64).0 = 136.206.192.0
bcast  = 136.206.191.255

ACADEMIC:
start  = 136.206.192.0
block  = 2048 → jump = 2048÷256 = 8
next   = 136.206.(192+8).0 = 136.206.200.0
bcast  = 136.206.199.255

HR:
start  = 136.206.200.0
block  = 512 → jump = 512÷256 = 2
next   = 136.206.(200+2).0 = 136.206.202.0
bcast  = 136.206.201.255

FINANCE:
start  = 136.206.202.0
sum    = 0+128 = 128 < 256
next   = 136.206.202.128
bcast  = 136.206.202.127

LINK-A:
start  = 136.206.202.128
sum    = 128+4 = 132 < 256
next   = 136.206.202.132
bcast  = 136.206.202.131

LINK-B:
start  = 136.206.202.132
sum    = 132+4 = 136 < 256
next   = 136.206.202.136
bcast  = 136.206.202.135

LINK-C:
start  = 136.206.202.136
sum    = 136+4 = 140 < 256
next   = 136.206.202.140
bcast  = 136.206.202.139
```

***

### Exam traps

| Trap            | Wrong                | Correct                         |
| --------------- | -------------------- | ------------------------------- |
| Sorting order   | Random order         | **Largest first**               |
| Same size order | Any order            | **Table order**                 |
| Usable hosts    | Block size           | **Block - 2**                   |
| LINK subnet     | /24 or /29           | **/30 always**                  |
| Broadcast       | Network + block      | **Next subnet - 1**             |
| Missing LINK    | Forget router links  | **Always include LINK subnets** |
| Mask for n=7    | 255.255.255.64       | **255.255.255.128**             |
| Contiguous      | Gaps between subnets | **No gaps allowed**             |

***

### Assumptions to document in exam:

```
- Subnets assigned in descending size order
- Same size subnets assigned in table order as given
- LINK subnets use /30 — most efficient for 2 usable hosts
- Subnets are contiguous — no unused space between subnets
- Starting address = [given block network address]
```
