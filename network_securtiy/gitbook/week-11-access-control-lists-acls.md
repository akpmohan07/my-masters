# Week 11 — Access Control Lists (ACLs)

## CSC1134 Week 11 — Access Control Lists (ACLs)

#### Revision Notes — Mohan's Exam Cheatsheet

***

### 1. What is an ACL?

* **Access Control List** — a series of rules on a **router** that filters traffic based on packet headers
* Implements a **stateless firewall** — considers each packet in isolation, no connection tracking
* Each rule inside an ACL is called an **ACE (Access Control Entry)**

#### How ACL processing works:

```
Packet arrives at interface
        ↓
Compare to ACE 1 → match? → permit or deny → STOP
        ↓ no match
Compare to ACE 2 → match? → permit or deny → STOP
        ↓ no match
Compare to ACE 3 → match? → permit or deny → STOP
        ↓ no match
Implicit DENY ALL → packet dropped ❌

IMPORTANT:
- First match wins — remaining ACEs are ignored
- Every ACL ends with implicit deny all (not shown in config)
- Must have at least ONE permit statement or all traffic is dropped
```

***

### 2. Two Types of ACL

|              | Standard ACL         | Extended ACL                                 |
| ------------ | -------------------- | -------------------------------------------- |
| Number range | 1–99, 1300–1999      | 100–199, 2000–2699                           |
| Filters on   | Source IP only       | Source IP + Destination IP + Protocol + Port |
| Placement    | Near **destination** | Near **source**                              |
| Layer        | Layer 3 (Network)    | Layer 3 + Layer 4 (Transport)                |

***

### 3. Wildcard Masks

Wildcard mask = inverse of subnet mask:

```
Subnet mask:   255.255.255.0
Wildcard mask:   0.0.0.255
Formula: 255.255.255.255 − subnet mask = wildcard mask
```

**How wildcard bits work:**

```
0 = fixed — this bit MUST match
1 = wild  — this bit can be anything
```

**Common wildcard masks:**

| Wildcard          | Meaning                       | Matches                |
| ----------------- | ----------------------------- | ---------------------- |
| `0.0.0.0`         | Match exact IP — nothing wild | Single host only       |
| `0.0.0.255`       | Last octet wild               | Any host in /24 subnet |
| `0.0.0.63`        | Last 6 bits wild              | Range of 64 hosts      |
| `0.0.0.15`        | Last 4 bits wild              | Range of 16 hosts      |
| `255.255.255.255` | Everything wild               | Any IP address         |

**Shortcuts:**

```
host 192.168.1.1        = 192.168.1.1 0.0.0.0    (single host)
any                     = 0.0.0.0 255.255.255.255  (any IP)
```

***

### 4. Standard ACL — Syntax

```
access-list [1-99] [permit|deny] [source] [source-wildcard]
```

**Examples:**

```
access-list 10 permit 192.168.1.0 0.0.0.255   ! permit entire /24 subnet
access-list 10 permit host 192.168.1.1         ! permit single host
access-list 10 permit 192.168.1.1 0.0.0.0     ! same as above
access-list 10 deny any                        ! deny everything else (explicit)
```

**Named standard ACL:**

```
ip access-list standard PRINTER-ONLY
 permit host 192.168.2.101
 deny any
```

***

### 5. Extended ACL — Syntax

```
access-list [100-199] [permit|deny] [protocol] [source] [src-wildcard] [destination] [dst-wildcard] [eq port] [established]
```

**Protocol options:**

```
ip    = all IP traffic
tcp   = TCP only
udp   = UDP only
icmp  = ping traffic
```

**Operator options:**

```
eq      = equal to (exact port match)
neq     = not equal to
gt      = greater than
lt      = less than
range   = port range
```

**Examples:**

```
access-list 100 permit tcp 192.168.1.0 0.0.0.255 any eq 80    ! HTTP
access-list 100 permit tcp 192.168.1.0 0.0.0.255 any eq 443   ! HTTPS
access-list 100 permit udp 192.168.1.0 0.0.0.255 any eq 53    ! DNS
access-list 100 deny ip any any                                ! deny rest
```

**Named extended ACL:**

```
ip access-list extended WEB_DNS_ONLY
 permit tcp 192.168.1.0 0.0.0.255 any eq 80
 permit tcp 192.168.1.0 0.0.0.255 any eq 443
 permit udp 192.168.1.0 0.0.0.255 any eq 53
 deny ip any any
```

***

### 6. Common Port Numbers — Must Know

| Service | Protocol | Port | IOS keyword |
| ------- | -------- | ---- | ----------- |
| HTTP    | TCP      | 80   | `www`       |
| HTTPS   | TCP      | 443  | `443`       |
| DNS     | UDP      | 53   | `domain`    |
| SSH     | TCP      | 22   | `22`        |
| Telnet  | TCP      | 23   | `telnet`    |
| FTP     | TCP      | 21   | `ftp`       |

```
eq www    = eq 80     (HTTP)
eq domain = eq 53     (DNS)
eq telnet = eq 23     (Telnet)
```

***

### 7. Applying ACL to Interface

**Physical interface:**

```
int gig0/0/0
 ip access-group [ACL name or number] [in|out]
```

**VTY lines (SSH/Telnet access to router itself):**

```
line vty 0 15
 access-class [ACL name or number] in
```

> ⚠️ `ip access-group` = physical interfaces ⚠️ `access-class` = VTY/console lines These are DIFFERENT commands — easy exam trap

**in vs out:**

```
in  = filter traffic ARRIVING at interface (before routing decision)
out = filter traffic LEAVING interface (after routing decision)
```

**How many ACLs per interface?**

```
4 total:
- Inbound IPv4
- Outbound IPv4
- Inbound IPv6
- Outbound IPv6
```

***

### 8. ACL Placement Rules

#### Extended ACL → near SOURCE

```
[PC subnet] → [R1 ← ACL HERE] → network → [R2] → [Server]

Why: Extended ACL knows source + destination + protocol + port
     Can make decision immediately at R1
     Stops unwanted traffic early — saves bandwidth
```

#### Standard ACL → near DESTINATION

```
[PC subnet] → [R1] → network → [R2] → [R3 ← ACL HERE] → [Server]

Why: Standard ACL only knows source IP
     If placed near source — might accidentally block
     that source from reaching ALL destinations
     Wait until near destination to apply the rule
```

***

### 9. The `established` Keyword

Used in extended ACLs for **TCP only**:

```
permit tcp any eq 80 192.168.1.0 0.0.0.255 established
```

**What it does:**

* Only permits TCP packets with **ACK or RST flag** set
* These flags = packet is a **reply** to a connection initiated from inside
* Blocks unsolicited inbound TCP connections from outside

**Why DNS doesn't use `established`:**

```
TCP → has flags (SYN, ACK, RST) → established works
UDP → no flags → established not available → DNS uses UDP → no established
```

**Visual:**

```
WITHOUT established:
Internet → sends unsolicited TCP connection → reaches your subnet ❌

WITH established:
Internet → sends unsolicited TCP SYN → no ACK flag → DROPPED ❌
Your PC → initiates connection → server replies with ACK → ALLOWED ✅
```

***

### 10. The Exam Scenario — DNS + Web Only

This appears in **2024, 2025, and 2025 Resit** papers. Must know cold.

**Network:**

```
[Internet]
     |
  Se0/1/0  ← internet-facing interface
  [ROUTER]
  Gig0/0/0 ← subnet-facing interface
     |
[136.206.10.0/24 subnet]
```

**Requirement:** Only permit DNS lookups and web browsing from subnet to internet.

#### Outbound ACL — what subnet can send OUT:

```
ip access-list extended WEB_DNS_ONLY
 permit tcp 136.206.10.0 0.0.0.255 any eq 80     ! HTTP outbound
 permit tcp 136.206.10.0 0.0.0.255 any eq 443    ! HTTPS outbound
 permit udp 136.206.10.0 0.0.0.255 any eq 53     ! DNS outbound
 deny ip any any                                  ! block everything else

int gig0/0/0
 ip access-group WEB_DNS_ONLY in                 ! IN = traffic arriving from subnet
```

#### Inbound ACL — what can come back IN from internet:

```
ip access-list extended INBOUND_ONLY
 permit tcp any eq 80 136.206.10.0 0.0.0.255 established   ! HTTP replies
 permit tcp any eq 443 136.206.10.0 0.0.0.255 established  ! HTTPS replies
 permit udp any eq 53 136.206.10.0 0.0.0.255               ! DNS replies (UDP - no established)
 deny ip any any                                            ! block unsolicited traffic

int gig0/0/0
 ip access-group INBOUND_ONLY out                ! OUT = traffic leaving towards subnet
```

**Or apply inbound ACL on internet-facing interface:**

```
int se0/1/0
 ip access-group INBOUND_ONLY in                 ! IN = traffic arriving from internet
```

> **Assumption to document in exam:** DNS uses UDP port 53. HTTP uses TCP port 80. HTTPS uses TCP port 443. The `established` keyword is used for TCP return traffic but not UDP.

***

### 11. Visual — Full ACL Traffic Flow

```
OUTBOUND TRAFFIC (subnet → internet):
======================================

[PC 136.206.10.1]
        |
        | sends HTTP request to google.com
        ↓
[ROUTER Gig0/0/0] ← WEB_DNS_ONLY ACL applied IN
        |
        | ACL checks:
        | permit tcp 136.206.10.0 any eq 80 → MATCH ✅
        ↓
[Internet] → google.com receives request



INBOUND TRAFFIC (internet → subnet):
======================================

[google.com]
        |
        | sends HTTP reply (ACK flag set)
        ↓
[ROUTER Gig0/0/0] ← INBOUND_ONLY ACL applied OUT
        |
        | ACL checks:
        | permit tcp any eq 80 136.206.10.0 established
        | ACK flag set? YES → MATCH ✅
        ↓
[PC 136.206.10.1] receives reply



BLOCKED UNSOLICITED INBOUND:
======================================

[Attacker on internet]
        |
        | sends unsolicited TCP SYN to 136.206.10.1
        ↓
[ROUTER Gig0/0/0] ← INBOUND_ONLY ACL applied OUT
        |
        | ACL checks:
        | permit tcp any eq 80 ... established
        | ACK flag set? NO (it's a SYN) → NO MATCH
        | deny ip any any → MATCH → DROPPED ❌
```

***

### 12. Named vs Numbered ACLs

|                      | Numbered                    | Named                                  |
| -------------------- | --------------------------- | -------------------------------------- |
| Identification       | Number (10, 100)            | Descriptive name (WEB\_DNS\_ONLY)      |
| Recommended          | No                          | Yes — easier to manage                 |
| Edit individual ACEs | Harder                      | Easier                                 |
| Example              | `access-list 100 permit...` | `ip access-list extended WEB_DNS_ONLY` |

***

### 13. ACL Best Practices

* Base ACLs on the organisation's **security policy**
* Use a **text editor** to write, edit and save ACLs before applying
* **Document** ACLs using the `remark` command
* **Test** on a development network before production
* Use **named ACLs** — more readable than numbers
* Always have at least **one permit statement** or all traffic drops
* Place **extended near source**, **standard near destination**

***

### 14. Quick Reference — All Commands

#### Create numbered standard ACL:

```
access-list 10 permit 192.168.1.0 0.0.0.255
access-list 10 permit host 192.168.1.1
access-list 10 deny any
```

#### Create numbered extended ACL:

```
access-list 100 permit tcp 192.168.1.0 0.0.0.255 any eq 80
access-list 100 permit tcp 192.168.1.0 0.0.0.255 any eq 443
access-list 100 permit udp 192.168.1.0 0.0.0.255 any eq 53
access-list 100 deny ip any any
```

#### Create named standard ACL:

```
ip access-list standard PRINTER-ONLY
 permit host 192.168.2.101
 deny any
```

#### Create named extended ACL:

```
ip access-list extended WEB_DNS_ONLY
 permit tcp 192.168.1.0 0.0.0.255 any eq 80
 permit tcp 192.168.1.0 0.0.0.255 any eq 443
 permit udp 192.168.1.0 0.0.0.255 any eq 53
 deny ip any any
```

#### Apply to physical interface:

```
int gig0/0/0
 ip access-group WEB_DNS_ONLY in
 ip access-group INBOUND_ONLY out
```

#### Apply to VTY lines:

```
line vty 0 15
 access-class RESTRICT_VTY in
```

#### Verify ACLs:

```
show access-lists                ! show all ACLs and match counts
show ip interface gig0/0/0      ! show which ACLs applied to interface
show running-config | section access-list
```

***

### 15. Exam Tips — Based on Past Papers

> ACL question appeared in **ALL three past papers** (2024, 2025, 2025 Resit). Always the same scenario — permit DNS + web browsing only.

**Q: Write ACL to permit only DNS and web browsing**

* HTTP → `permit tcp [subnet] [wildcard] any eq 80`
* HTTPS → `permit tcp [subnet] [wildcard] any eq 443`
* DNS → `permit udp [subnet] [wildcard] any eq 53`
* Block rest → `deny ip any any`
* Document assumption: DNS=UDP/53, HTTP=TCP/80, HTTPS=TCP/443

**Q: What is the implicit deny?**

* Every ACL ends with an invisible `deny ip any any`
* If no ACE matches — packet is dropped
* This is fail-safe by design

**Q: Standard vs Extended — what's the difference?**

* Standard = source IP only = near destination
* Extended = src + dst + protocol + port = near source

**Q: What does `established` do?**

* Permits TCP return traffic (replies) only
* Checks for ACK or RST flag
* Not stateful — just checks packet header flags
* Only works for TCP — not UDP

**Q: `ip access-group` vs `access-class`**

* `ip access-group` = physical interfaces
* `access-class` = VTY/console lines

**Q: How many ACLs per interface?**

* 4 — inbound IPv4, outbound IPv4, inbound IPv6, outbound IPv6

***

_CSC1134 — Week 11 Revision — You've got this Mohan! 🎯_
