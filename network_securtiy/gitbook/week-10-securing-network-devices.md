# Week 10 — Securing Network Devices

&#x20;CSC1134 Week 10 — Securing Network Devices

#### Revision Notes — Mohan's Exam Cheatsheet

***

### ⚠️ YOUR RECURRING MISTAKES — READ FIRST

| Mistake                | Wrong                                   | Correct                                             |
| ---------------------- | --------------------------------------- | --------------------------------------------------- |
| Multiple interfaces    | `int fa0/1-2`                           | `int range fa0/1-2`                                 |
| Port security prefix   | `port-security maximum 1`               | `switchport port-security maximum 1`                |
| Password command       | `security password min-length`          | `security passwords min-length`                     |
| DHCP rate limit order  | `ip dhcp snooping rate-limit 5`         | `ip dhcp snooping limit rate 5`                     |
| Banner syntax          | `login banner Authorized`               | `banner login #` ... `#`                            |
| Switch management IP   | forgotten entirely                      | `int vlan 1` + `ip default-gateway`                 |
| VTY auth               | missing `login local` or `exec-timeout` | always include both                                 |
| DAI on untrusted ports | `int fa0/1-2` + `ip arp inspection`     | DAI is global — `ip arp inspection vlan 1` only     |
| DHCP vs ARP keyword    | `ip arp snooping`                       | `ip arp inspection` — DHCP=snooping, ARP=inspection |

***

### 1. The Three Planes

Every network device function falls into one of three planes:

| Plane          | What it does                    | Examples                         |
| -------------- | ------------------------------- | -------------------------------- |
| **Management** | How you configure the device    | SSH, console, passwords, banners |
| **Control**    | How the device makes decisions  | Routing tables, ARP, STP         |
| **Data**       | How the device forwards traffic | Actually moving packets/frames   |

> Week 10 secures the **management plane** (hardening) and **data plane** (Layer 2 attacks).

***

### 2. Device Hardening — Router & Switch

#### Full Router Hardening Config

```
enable
conf t

hostname R1                                         ! needed before RSA key generation
ip domain-name csc1134.dcu.ie                       ! needed before RSA key generation

username admin secret cisco                         ! local user with STRONG encryption
security passwords min-length 10                    ! defend against brute force
service password-encryption                         ! weak encryption for remaining plaintext passwords

banner login #                                      ! shown BEFORE login prompt
Authorized access only. Unauthorized access is prohibited.
#

crypto key generate rsa general-keys modulus 2048   ! generate RSA key (needs hostname + domain first)
ip ssh version 2                                    ! always use latest SSH version

line console 0
 login local                                        ! authenticate against local user DB
 exec-timeout 5                                     ! auto logout after 5 min inactivity
exit

line vty 0 15                                       ! all 16 virtual lines
 transport input ssh                                ! SSH only — blocks Telnet (plaintext)
 login local                                        ! authenticate against local user DB
 exec-timeout 5                                     ! auto logout after 5 min inactivity
exit

copy running-config startup-config                  ! save — survives reboot
```

#### Key Command Distinctions

| Command                         | Encryption          | Use                          |
| ------------------------------- | ------------------- | ---------------------------- |
| `enable password cisco`         | Plaintext in config | Never use                    |
| `enable secret cisco`           | Strong (MD5/SHA)    | Always use this              |
| `username admin password cisco` | Weak                | Avoid                        |
| `username admin secret cisco`   | Strong              | Always use this              |
| `service password-encryption`   | Weak (Type 7)       | Shoulder surfer defence only |

> **Rule:** Always `secret` over `password`. `service password-encryption` is a last resort for anything left plaintext.

#### SSH Setup — Why Hostname + Domain First

```
RSA key is generated as: hostname.domainname
e.g. R1.csc1134.dcu.ie

Cisco IOS won't generate the key without both.
Order: hostname → ip domain-name → crypto key generate
```

#### Banner Types

| Banner         | When shown          | Use                         |
| -------------- | ------------------- | --------------------------- |
| `banner login` | Before login prompt | Security warning — use this |
| `banner motd`  | After login         | Planned maintenance notices |

> Always use `banner login` for security warnings — it appears before credentials are entered.

#### Why `exec-timeout 5`?

```
[Admin logs in] → [walks away] → [5 minutes of inactivity] → [auto logout]
Prevents: attacker sitting down at an unattended terminal
```

#### VTY — Why `transport input ssh` Matters

```
transport input all  → allows Telnet + SSH
                        Telnet = PLAINTEXT — attacker sniffs username/password

transport input ssh  → SSH only
                        SSH = ENCRYPTED — safe for remote management
```

***

### 3. Switch-Specific Config

#### Switch Management IP (SVI)

```
enable
conf t

int vlan 1                                          ! SVI — Switch Virtual Interface
 ip address 192.168.1.253 255.255.255.0             ! management IP
 no shutdown                                        ! SVI is off by default
exit

ip default-gateway 192.168.1.254                    ! needed to reach other networks
```

> **Security note:** SVI on VLAN 1 means ALL devices on VLAN 1 can reach the switch management IP. Better practice: create a dedicated management VLAN. But for this module — VLAN 1 is used.

#### Close Unused Ports

```
int range fa0/4-24                                  ! all unused ports
 shutdown                                           ! close them — physical security
exit
```

> Open physical ports = anyone can plug in and connect. Always close unused ports.

***

### 4. Layer 2 Attacks & Defences

#### The Summary Table — Exam Gold

| Attack             | Defence                     |
| ------------------ | --------------------------- |
| MAC table overflow | Port security               |
| DHCP starvation    | DHCP snooping rate limit    |
| Rogue DHCP server  | DHCP snooping trust/untrust |
| ARP spoofing       | DHCP snooping + DAI         |

***

#### Attack 1 — MAC Table Overflow

**How it works:**

```
                    [SWITCH]
                    MAC Table:
[Attacker] ──────── fa0/1    Normal state:
using macof tool             PC-A → fa0/2
sends 100,000s of            PC-B → fa0/3
random source MACs           PC-C → fa0/4

                    After attack:
                    Table FULL of fake MACs
                    No room for real MACs
                    Switch can't learn legitimate devices

                    Switch reverts to HUB behaviour:
                    ALL frames flooded out ALL ports
                    Attacker receives everyone's traffic ❌
```

**Defence: Port Security**

```
int range fa0/1-2
 switchport port-security                           ! enable port security
 switchport port-security maximum 1                 ! max 1 MAC per port
 switchport port-security mac-address sticky        ! learn and remember first MAC seen
 switchport port-security violation shutdown        ! disable port on violation
exit
```

**Violation modes:**

| Mode       | Drops traffic | Port stays up  | Logs     | Recovery                   |
| ---------- | ------------- | -------------- | -------- | -------------------------- |
| `shutdown` | ✅             | ❌ err-disabled | ✅        | `shutdown` + `no shutdown` |
| `restrict` | ✅             | ✅              | ✅        | automatic                  |
| `protect`  | ✅             | ✅              | ❌ silent | automatic                  |

**Recovering err-disabled port:**

```
int fa0/1
 shutdown
 no shutdown
```

***

#### Attack 2 — DHCP Spoofing (Rogue DHCP Server)

**How it works:**

```
                         [SWITCH]
                             |
              _______________↓_______________
              |                             |
         Gig0/1                          Fa0/3
       TRUSTED ✅                      UNTRUSTED ❌
              |                             |
           [ROUTER]                    [ATTACKER]
        Real DHCP server            Rogue DHCP server

PC asks: "Anyone give me an IP?" (broadcast)

Without snooping:
→ Router replies "You are 192.168.1.10, gateway 192.168.1.254"
→ Attacker replies "You are 192.168.1.10, gateway ATTACKER-IP"
→ Whoever replies first wins → man-in-the-middle ❌

With DHCP snooping:
→ Router reply arrives on Gig0/1 (TRUSTED) → forwarded ✅
→ Attacker reply arrives on Fa0/3 (UNTRUSTED) → DROPPED ❌
```

**Defence: DHCP Snooping**

```
ip dhcp snooping                                    ! enable globally
ip dhcp snooping vlan 1                             ! enable on VLAN 1

int gig0/1
 ip dhcp snooping trust                             ! real DHCP server/router port
exit

int range fa0/1-3
 ip dhcp snooping limit rate 5                      ! max 5 DHCP packets/sec
exit                                                ! defends against DHCP starvation too
```

> **Side effect:** DHCP snooping builds a binding table: IP → MAC → Port → VLAN. This is used by DAI.

***

#### Attack 3 — ARP Spoofing (Cache Poisoning)

**How it works:**

```
Normal ARP:
[PC] asks "Who has gateway 192.168.1.254?"
[Router] replies "I do, my MAC is XXXX"
[PC] caches: 192.168.1.254 → XXXX ✅

ARP Spoofing attack:
[Attacker] sends fake ARP to PC:
"192.168.1.254 (gateway) is at ATTACKER-MAC"
[PC] caches: 192.168.1.254 → ATTACKER-MAC ❌

[Attacker] sends fake ARP to Router:
"192.168.1.10 (PC) is at ATTACKER-MAC"
[Router] caches: 192.168.1.10 → ATTACKER-MAC ❌

Result:
PC → sends to gateway → goes to attacker → attacker forwards → router
Router → sends to PC → goes to attacker → attacker forwards → PC

Attacker sees ALL traffic = man-in-the-middle ❌
```

> **Why port security doesn't stop this:** ARP spoofing spoofs the **contents** of the ARP packet, not the source MAC. Port security only checks the Ethernet frame source MAC — the outer layer. The fake IP→MAC mapping is inside the packet.

**Defence: Dynamic ARP Inspection (DAI)**

Requires DHCP snooping to be configured first — DAI uses the snooping binding table as its source of truth.

```
ip arp inspection vlan 1                            ! enable DAI on VLAN 1
ip arp inspection validate src-mac dst-mac ip       ! validate source MAC, dest MAC, IP

int gig0/1
 ip arp inspection trust                            ! trust ARP from router
exit                                                ! router has static IP — not in snooping table
```

**How DAI works:**

```
ARP packet arrives on untrusted port
            ↓
DAI intercepts it
            ↓
Checks: does ARP's IP→MAC match DHCP snooping table?
            ↓
Snooping table: 192.168.1.254 → ROUTER-MAC
ARP says:       192.168.1.254 → ATTACKER-MAC
            ↓
MISMATCH → packet DROPPED ❌

Snooping table: 192.168.1.10 → PC-MAC
ARP says:       192.168.1.10 → PC-MAC
            ↓
MATCH → packet FORWARDED ✅
```

**Why router port is trusted:**

```
Router has a static IP → not assigned by DHCP
Therefore → not in DHCP snooping table
DAI would drop router's legitimate ARP without trust
Solution → mark gig0/1 as trusted → bypass DAI inspection
```

***

### 5. Full Switch Security Config (Everything Together)

```
enable
conf t

! ── DHCP SNOOPING ──────────────────────────────────────────
ip dhcp snooping                                    ! enable globally
ip dhcp snooping vlan 1                             ! enable on VLAN 1

! ── DYNAMIC ARP INSPECTION ─────────────────────────────────
ip arp inspection vlan 1                            ! enable DAI on VLAN 1
ip arp inspection validate src-mac dst-mac ip       ! full validation

! ── END DEVICE PORTS ────────────────────────────────────────
int range fa0/1-2
 switchport port-security                           ! enable port security
 switchport port-security maximum 1                 ! max 1 MAC per port
 switchport port-security mac-address sticky        ! learn and remember first MAC
 switchport port-security violation shutdown        ! shut port on violation
 ip dhcp snooping limit rate 5                      ! rate limit DHCP
exit

! ── ROUTER PORT ─────────────────────────────────────────────
int gig0/1
 ip dhcp snooping trust                             ! trust DHCP from router
 ip arp inspection trust                            ! trust ARP from router
exit

! ── MANAGEMENT IP ───────────────────────────────────────────
int vlan 1
 ip address 192.168.1.253 255.255.255.0             ! switch management IP
 no shutdown                                        ! bring SVI up
exit

ip default-gateway 192.168.1.254                    ! reach other networks

! ── UNUSED PORTS ────────────────────────────────────────────
int range fa0/3-24
 shutdown                                           ! close unused ports
exit

copy running-config startup-config                  ! save everything
```

***

### 6. How Everything Connects — The Big Picture

```
DHCP snooping builds:        DAI uses:
┌─────────────────────┐      ┌─────────────────────┐
│ IP  → MAC  → Port   │ ───→ │ Validate ARP packets │
│ .10 → AAAA → fa0/1  │      │ against this table   │
│ .11 → BBBB → fa0/2  │      └─────────────────────┘
└─────────────────────┘
         ↑
Built by watching DHCP
exchange on trusted port


Port security protects:      DHCP snooping rate limit:
┌─────────────────────┐      ┌─────────────────────┐
│ MAC table overflow  │      │ DHCP starvation      │
│ Max 1 MAC per port  │      │ Max 5 DHCP pkts/sec  │
└─────────────────────┘      └─────────────────────┘
```

***

### 7. Quick Reference — All Commands

#### Passwords

```
enable secret cisco                     ! strong privileged mode password
username admin secret cisco             ! strong local user
security passwords min-length 10        ! minimum length
service password-encryption             ! weak encrypt remaining plaintext
```

#### SSH

```
hostname R1
ip domain-name csc1134.dcu.ie
crypto key generate rsa general-keys modulus 2048
ip ssh version 2
```

#### Lines

```
line console 0
 login local
 exec-timeout 5

line vty 0 15
 transport input ssh
 login local
 exec-timeout 5
```

#### Banner

```
banner login #
Authorized access only.
#
```

#### Switch Management

```
int vlan 1
 ip address 192.168.1.253 255.255.255.0
 no shutdown
ip default-gateway 192.168.1.254
```

#### Port Security

```
int range fa0/1-2
 switchport port-security
 switchport port-security maximum 1
 switchport port-security mac-address sticky
 switchport port-security violation [shutdown|restrict|protect]
```

#### DHCP Snooping

```
ip dhcp snooping
ip dhcp snooping vlan 1
int gig0/1
 ip dhcp snooping trust
int range fa0/1-3
 ip dhcp snooping limit rate 5
```

#### DAI

```
ip arp inspection vlan 1
ip arp inspection validate src-mac dst-mac ip
int gig0/1
 ip arp inspection trust
```

#### Unused Ports

```
int range fa0/4-24
 shutdown
```

#### Save

```
copy running-config startup-config
```

***

### 8. Exam Tips — Based on Past Papers

> 2024 and 2025 papers both tested ARP spoofing + MAC flooding heavily.

**Q: Describe a MAC table overflow attack and defence**

* Attack: `macof` sends random source MACs → table fills → switch acts as hub → attacker sniffs traffic
* Defence: `switchport port-security maximum 1`

**Q: Describe ARP spoofing and defence**

* Attack: fake ARP replies poison gateway and victim caches → MITM
* Defence: DHCP snooping (builds IP→MAC table) + DAI (validates ARP against that table)
* Remember: port security does NOT stop ARP spoofing — it spoofs packet contents not source MAC

**Q: Write commands to secure SSH access**

* hostname → domain → crypto key → ssh version 2 → line vty → transport input ssh → login local → exec-timeout

**Q: Difference between `enable password` and `enable secret`**

* `password` = weak encryption
* `secret` = strong encryption (MD5/SHA)
* Always use `secret`

***

_CSC1134 — Week 10 Revision — Good luck Mohan! 🎯_
