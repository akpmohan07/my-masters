# Question 4

## Q4 — VLANs, Securing Network Devices, ACLs

**CSC1134 — Exam Revision** **Date:** 20 April 2026 **Exam:** Tuesday 21 April 2026 — 9:00 AM

***

### Pattern summary

| Year  | Part (a)         | Part (b)             | Part (c)          |
| ----- | ---------------- | -------------------- | ----------------- |
| 2023  | VLAN benefits 4m | Router-on-a-stick 6m | IOS hardening 10m |
| 2023R | VLAN benefits 4m | Multilayer SVI 6m    | IOS hardening 10m |
| 2024  | VLAN setup 8m    | —                    | ACL DNS+web 10m   |
| 2025  | VLAN setup 8m    | —                    | —                 |
| 2025R | VLAN benefits 4m | Multilayer SVI 6m    | ACL DNS+web 10m   |

**For 2026:** Part (c) ACLs is most likely — appeared 3 years running.

***

### Part (a) — VLAN security benefits \[4 marks]

**Exact question — 2023 \[Q4]\[A], 2023R \[Q4]\[A], 2025R \[Q4]\[A]:**

> Explain two security benefits of using VLANs to partition network traffic.

**Always the same two benefits — 4 free marks:**

**Benefit 1 — Smaller broadcast domains:**

> VLANs reduce broadcast domain size. Attacks like ARP cache poisoning are constrained to the VLAN they originate from — an attacker in VLAN 10 cannot affect ARP caches in VLAN 20.

**Benefit 2 — Traffic isolation + chokepoints:**

> VLANs segregate trusted from untrusted traffic. Inter-VLAN traffic must pass through a router or multilayer switch — a chokepoint where ACLs can be applied to enforce the security policy.

**Key exam word:** Always mention **"chokepoint"** — examiners look for it.

***

### Part (b) — Inter-VLAN routing

#### Method 1 — Router-on-a-stick

**Exact question — 2023 \[Q4]\[B]:**

> Making use of an appropriate example, describe how you would apply the router-on-a-stick approach to inter-VLAN routing. Include a network diagram in your description along with corresponding IP addressing.

**Concept:** One physical trunk cable from switch to router. Router uses subinterfaces — one per VLAN.

```
[PC-A VLAN10]──┐
               ├──[SWITCH]──Gig0/1 trunk──Gig0/0/0──[ROUTER]
[PC-B VLAN20]──┘                           ├── Gig0/0/0.10 (VLAN 10 gateway)
                                            └── Gig0/0/0.20 (VLAN 20 gateway)
```

**On the switch:**

```
enable
configure terminal

! Step 1 — Create VLANs
vlan 10
 name Students
exit
vlan 20
 name Staff
exit

! Step 2 — Assign access ports to VLANs
interface fa0/1
 switchport mode access
 switchport access vlan 10
exit

interface fa0/2
 switchport mode access
 switchport access vlan 20
exit

! Step 3 — Configure trunk port to router
interface gig0/1
 switchport mode trunk
exit

! Step 4 — Save
copy running-config startup-config
```

**On the router:**

```
enable
configure terminal

! Step 1 — Enable physical interface FIRST (subinterfaces inherit its state)
interface gig0/0/0
 no shutdown
exit

! Step 2 — Create subinterface for VLAN 10
interface gig0/0/0.10
 encapsulation dot1Q 10
 ip address 192.168.10.254 255.255.255.0
exit

! Step 3 — Create subinterface for VLAN 20
interface gig0/0/0.20
 encapsulation dot1Q 20
 ip address 192.168.20.254 255.255.255.0
exit

! Step 4 — Save
copy running-config startup-config
```

**Key points:**

* `no shutdown` on physical interface is mandatory — subinterfaces won't come up without it
* `encapsulation dot1Q` tells subinterface which VLAN tag to handle
* No `ip routing` needed — routers route by default

***

#### Method 2 — Multilayer Switch SVI

**Exact question — 2023R \[Q4]\[B] and 2025R \[Q4]\[B]:**

> Making use of an appropriate example, describe how you would configure a multilayer switch to support routing between two VLANs. Include in your description relevant Cisco IOS commands and a simple network diagram with corresponding IP addressing.

**Concept:** Routing happens inside the switch itself using Switch Virtual Interfaces (SVIs). No router needed.

```
[PC-A VLAN10]──┐
               ├──[MULTILAYER SWITCH]
[PC-B VLAN20]──┘    ├── interface vlan 10 (SVI — VLAN 10 gateway)
                     └── interface vlan 20 (SVI — VLAN 20 gateway)
```

**Commands:**

```
enable
configure terminal

! Step 1 — Create VLANs
vlan 10
 name Students
exit
vlan 20
 name Staff
exit

! Step 2 — Assign access ports to VLANs
interface fa0/1
 switchport mode access
 switchport access vlan 10
exit

interface fa0/2
 switchport mode access
 switchport access vlan 20
exit

! Step 3 — Create SVIs (default gateways for each VLAN)
! Assumption: gateway = last usable host (.254)
interface vlan 10
 ip address 192.168.10.254 255.255.255.0
 no shutdown
exit

interface vlan 20
 ip address 192.168.20.254 255.255.255.0
 no shutdown
exit

! Step 4 — Enable routing on the multilayer switch (CRITICAL)
ip routing

! Step 5 — Save
copy running-config startup-config
```

**Key points:**

* `ip routing` is mandatory — switches don't route by default
* `no shutdown` on SVIs — they are off by default
* SVI IP = gateway IP for that VLAN (use last usable host .254)
* VLAN must be created first before SVI can be configured
* No `encapsulation dot1Q` needed — switches know VLAN by number

***

#### Method 3 — VLAN setup without inter-VLAN routing (2024/2025 style)

**Exact question — 2024 \[Q4]\[A] and 2025 \[Q4]\[A]:**

> ⚠️ Image in question — refer to 2024 \[Q4] or 2025 \[Q4] topology diagram
>
> Consider the network depicted above where DCU's Electrical and Mechanical departments share the 136.206.10.128/25 subnet. Without altering the hardware or cabling, describe the steps you would follow to assign the Electrical and Mechanical departments above to separate VLANs. Include with your description:
>
> * Your new IP addressing scheme (making use of the original IP block)
> * Any relevant Cisco IOS commands
>
> Note: Inter-VLAN routing is not required.

**Concept:** Split existing subnet into smaller subnets, assign departments to VLANs. No routing required.

**Example:** 136.206.10.128/25 split into two /26 subnets

```
! IP addressing calculation:
! Original: 136.206.10.128/25 — block size = 2^7 = 128 addresses
! Split into /26 — block size = 2^6 = 64 addresses each

! Subnet 1 — VLAN 10 Electrical:
!   Network:   136.206.10.128/26
!   Broadcast: 136.206.10.191
!   Hosts:     136.206.10.129 → 136.206.10.190

! Subnet 2 — VLAN 20 Mechanical:
!   Network:   136.206.10.192/26  (128 + 64 = 192)
!   Broadcast: 136.206.10.255
!   Hosts:     136.206.10.193 → 136.206.10.254
```

**On S1:**

```
enable
configure terminal

! Step 1 — Create VLANs
vlan 10
 name Electrical
exit
vlan 20
 name Mechanical
exit

! Step 2 — Assign access ports
interface fa0/1
 switchport mode access
 switchport access vlan 10
exit

interface fa0/2
 switchport mode access
 switchport access vlan 20
exit

! Step 3 — Trunk to S2
interface gig0/1
 switchport mode trunk
exit

! Step 4 — Save
copy running-config startup-config
```

**On S2 — same VLAN creation + port assignments + trunk back to S1**

**Assumptions to document:**

* Original /25 block split into two equal /26 subnets
* Hosts reassigned to new subnet ranges
* Inter-VLAN routing not configured as per question requirement
* VLANs created identically on both switches for trunk to carry correctly

***

### Part (c) — IOS Hardening

#### Two password levels on a Cisco router:

```
! Level 1 — Login (user exec mode >)
! Controlled by: username + login local on VTY

! Level 2 — Privileged exec mode (#)
! Controlled by: enable secret

Router> enable          ← needs enable secret password
Router#                 ← full access
Router# configure terminal
Router(config)#         ← global config mode
```

***

#### Full IOS hardening — 2023 \[Q4]\[C] + 2023R \[Q4]\[C] merged

**2023 \[Q4]\[C] exact question:**

> Provide the Cisco IOS commands to accomplish the tasks below on an unconfigured Cisco router. Document any assumptions and do not abbreviate any IOS commands. i. Encrypt all plaintext passwords. ii. Password protect privileged exec mode with strongly encrypted password ca645. iii. Generate 2048-bit SSH keys. iv. Add local user admin with strongly encrypted password cisco. v. Configure all VTY lines for SSH-only access using local user profiles for authentication and with a 5-minute time-out. vi. Save the new configuration.

**2023R \[Q4]\[C] exact question:**

> i. Encrypt all plaintext passwords. ii. Set an appropriate login warning banner. iii. Generate 1024-bit SSH keys. iv. Add user admin with secret password cisco. v. Configure all VTY lines for SSH-only access using local user profiles for authentication and with a 5-minute time-out. vi. Save the new configuration.

**⚠️ Differences between years:**

* 2023 step ii = `enable secret ca645` (privileged password)
* 2023R step ii = `banner login` (warning banner)
* 2023 step iii = 2048-bit keys
* 2023R step iii = 1024-bit keys (just change the number)

***

**Full merged answer — covers both years:**

```
enable
configure terminal

! Step i — Encrypt all plaintext passwords (weak type 7 encryption)
service password-encryption

! Step ii (2023) — Strong privileged exec password (MD5)
! NEVER use 'enable password' — always use 'enable secret'
enable secret ca645

! Step ii (2023R) — Login warning banner shown BEFORE login prompt
! Use this if question asks for banner instead of privileged password
banner login #
Authorized access only. Unauthorized access is prohibited.
#

! Step iii — Generate SSH keys
! Assumption: hostname and domain name required before key generation
! Order is mandatory: hostname → domain → crypto key
hostname R1
ip domain-name csc1134.dcu.ie
crypto key generate rsa general-keys modulus 2048    ! 2023: 2048-bit
! crypto key generate rsa general-keys modulus 1024  ! 2023R: 1024-bit
ip ssh version 2                                     ! force SSH v2 only

! Step iv — Add local user with strong password
! NEVER use 'username admin password' — always use 'username admin secret'
username admin secret cisco

! Step v — Configure ALL VTY lines (0-15) for SSH only
line vty 0 15
 transport input ssh        ! SSH only — blocks Telnet (plaintext)
 login local                ! authenticate using local username/password
 exec-timeout 5 0           ! auto logout after 5 min 0 sec idle
exit

! Step vi — Save configuration
copy running-config startup-config
```

**Assumptions to document:**

```
- Hostname set to R1 (required before RSA key generation)
- Domain name set to csc1134.dcu.ie (required before RSA key generation)
- exec-timeout 5 0 = 5 minutes, 0 seconds
- enable secret uses MD5 strong encryption
- username admin secret uses MD5 strong encryption
- service password-encryption applies weak type 7 encryption
  to any remaining plaintext passwords
```

#### Command structure explained:

| Command                                             | What it does                           | Why                                  |
| --------------------------------------------------- | -------------------------------------- | ------------------------------------ |
| `service password-encryption`                       | Weakly encrypts plaintext passwords    | Stops shoulder surfing               |
| `enable secret X`                                   | Sets privileged mode password (MD5)    | Strong encryption                    |
| `enable password X`                                 | Sets privileged mode password (type 7) | NEVER USE — weak                     |
| `hostname R1`                                       | Sets router name                       | Required before RSA key generation   |
| `ip domain-name X`                                  | Sets domain name                       | Required before RSA key generation   |
| `crypto key generate rsa general-keys modulus 2048` | Generates RSA key pair + enables SSH   | One key for all purposes             |
| `ip ssh version 2`                                  | Forces SSH v2 only                     | v1 is insecure                       |
| `username admin secret cisco`                       | Creates local user (MD5 password)      | For `login local` authentication     |
| `transport input ssh`                               | SSH only on VTY — blocks Telnet        | Telnet is plaintext                  |
| `login local`                                       | Use local username/password            | Authenticates via `username` command |
| `exec-timeout 5 0`                                  | Auto logout after 5 min idle           | Prevents unattended sessions         |
| `banner login #...#`                                | Warning shown before login             | Legal protection                     |

#### Key ordering rule:

```
hostname → ip domain-name → crypto key generate
```

> IOS won't generate RSA keys without both hostname and domain name set first.

***

### Part (c) — ACLs — permit DNS + web only

#### Exact question — 2024 \[Q4]\[B] and 2025R \[Q4]\[C]:

> ⚠️ Image in question — refer to 2024 \[Q4] or 2025R \[Q4] topology diagram
>
> Consider the network depicted above. You have been asked to secure the 136.206.10.0/24 subnet. Describe the steps you would follow in applying ACLs to ensure only the following are permitted on the subnet:
>
> * DNS lookups to Internet-based DNS servers
> * Web browsing to Internet-based web servers
>
> Include any relevant Cisco IOS commands and document any assumptions.

#### Concept:

```
Inbound extended ACL = "What can hosts in this subnet access?"
Applied on Gig0/0/0 inbound (subnet-facing interface)

[136.206.10.0/24]──Gig0/0/0──[ROUTER]──Gig0/0/1──[Internet]
                       ↑
                  ACL applied here (inbound)
                  Checks ALL traffic from subnet
```

#### Full ACL — named (preferred):

```
! Step 1 — Define ACL rules
ip access-list extended WEB_DNS_ONLY
 permit udp 136.206.10.0 0.0.0.255 any eq 53   ! DNS queries (UDP port 53)
 permit tcp 136.206.10.0 0.0.0.255 any eq 53   ! DNS over TCP (large responses)
 permit tcp 136.206.10.0 0.0.0.255 any eq 80   ! HTTP web browsing
 permit tcp 136.206.10.0 0.0.0.255 any eq 443  ! HTTPS web browsing
 deny ip any any                                ! block everything else (explicit)
exit

! Step 2 — Apply ACL to interface inbound
interface gig0/0/0
 ip access-group WEB_DNS_ONLY in
exit
```

#### ACL rule structure:

```
permit/deny  [protocol]  [src-network] [src-wildcard]  [dst]  eq [port]

permit       udp          136.206.10.0   0.0.0.255      any    eq 53
```

#### Wildcard mask calculation:

```
Wildcard = 255.255.255.255 - subnet mask
/24 → 255.255.255.0 → wildcard = 0.0.0.255
```

#### Port numbers — must know:

| Protocol | Transport | Port |
| -------- | --------- | ---- |
| DNS      | UDP + TCP | 53   |
| HTTP     | TCP       | 80   |
| HTTPS    | TCP       | 443  |
| SSH      | TCP       | 22   |
| Telnet   | TCP       | 23   |

#### Standard vs Extended:

|              | Standard         | Extended                    |
| ------------ | ---------------- | --------------------------- |
| Filters      | Source IP only   | Src + Dst + Protocol + Port |
| Number range | 1–99             | 100–199                     |
| Placement    | Near destination | Near source                 |
| Direction    | Outbound         | Inbound                     |

#### `established` keyword (bonus knowledge):

```
! Permits TCP return traffic only (ACK or RST flag set)
! Blocks unsolicited inbound connections from internet
permit tcp any eq 80 136.206.10.0 0.0.0.255 established

! Note: DNS uses UDP — no established keyword available for UDP
```

#### Assumptions to document in exam:

```
- DNS uses UDP port 53 and TCP port 53
- HTTP uses TCP port 80
- HTTPS uses TCP port 443
- ACL applied inbound on Gig0/0/0 (subnet-facing interface)
- Implicit deny ip any any at end blocks all other traffic
- Named ACL used for readability
```

#### `ip access-group` vs `access-class`:

```
ip access-group WEB_DNS_ONLY in  → physical interfaces
access-class RESTRICT in         → VTY/console lines
```

***

### Exam traps — what loses marks

| Trap                            | Wrong                          | Correct                                       |
| ------------------------------- | ------------------------------ | --------------------------------------------- |
| SVI IP                          | Network address                | **Gateway IP** (.254)                         |
| SVI state                       | Forget no shutdown             | SVIs are **off by default**                   |
| Multilayer routing              | Forget ip routing              | `ip routing` is **mandatory**                 |
| Router subinterface             | Forget no shutdown on physical | Physical must be up first                     |
| encapsulation                   | On switch SVI                  | Only on **router subinterfaces**              |
| Trunk                           | One end only                   | **Both ends** must be trunk                   |
| enable password                 | Use it                         | **Never** — always use `enable secret`        |
| username password               | Use it                         | **Never** — always use `username x secret y`  |
| crypto key order                | domain before hostname         | **hostname first**, then domain               |
| ACL wildcard                    | Subnet mask 255.255.255.0      | **Wildcard 0.0.0.255**                        |
| ACL destination                 | Missing `any`                  | `permit udp src wildcard **any** eq 53`       |
| ACL direction                   | Outbound extended              | Extended = **inbound** on subnet interface    |
| deny syntax                     | `permit deny any`              | `deny ip any any`                             |
| ip access-group vs access-class | Mixed up                       | access-group = interfaces, access-class = VTY |
| DNS TCP                         | Forgot                         | DNS uses **both UDP and TCP** port 53         |
| VTY range                       | `line vty 0`                   | `line vty **0 15**` — all 16 lines            |
| exec-timeout                    | `exec-timeout 5`               | `exec-timeout 5 **0**` — minutes AND seconds  |

***

### Weak spots flagged — Mohan's mistakes this session

1. Skipped `vlan 10` / `vlan 20` creation — went straight to SVI
2. Used network address (.0) as SVI IP instead of gateway (.254)
3. Forgot `no shutdown` on SVIs
4. Forgot `ip routing` on multilayer switch
5. Used `encapsulation dot1Q` on switch SVI (only for router subinterfaces)
6. Used subnet mask instead of wildcard mask in ACL
7. Missing `any` as destination in ACL rules
8. Said "outbound extended" — extended ACL is **inbound** on subnet interface
9. Wrote `permit deny any` — should be `deny ip any any`
10. Forgot `login local` and `exec-timeout 5 0` under VTY lines
11. Used `ip domain-name` without `ip` prefix
12. Used `rsa ssh` instead of `rsa general-keys` in crypto command
