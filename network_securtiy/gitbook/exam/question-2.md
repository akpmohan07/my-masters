# Question 2

<figure><img src="../.gitbook/assets/image (2).png" alt=""><figcaption></figcaption></figure>

## Q2 — ARP Attacks, DHCP, Defences

**CSC1134 — Exam Revision** **Date:** 19 April 2026 **Exam:** Tuesday 21 April 2026 — 9:00 AM

***

### Pattern summary

| Part | Marks | What it asks                                          |
| ---- | ----- | ----------------------------------------------------- |
| (a)  | 10    | ARP cache poisoning attack — **always, every year**   |
| (b)  | 4–6   | DHCP DORA steps OR DAI defence                        |
| (c)  | 4–6   | DHCP spoofing defence OR MAC overflow + port security |

***

### Attack vs Defence — master table

| Attack             | Tool                 | Targets                  | Defence                     | Commands                        |
| ------------------ | -------------------- | ------------------------ | --------------------------- | ------------------------------- |
| MAC table overflow | `macof`              | Switch MAC table         | Port security               | `switchport port-security`      |
| DHCP starvation    | `macof`              | DHCP address pool        | DHCP snooping rate limit    | `ip dhcp snooping limit rate 5` |
| DHCP spoofing      | Rogue DHCP server    | Victim's default gateway | DHCP snooping trust/untrust | `ip dhcp snooping trust`        |
| ARP spoofing       | `arpspoof`, Ettercap | ARP cache                | DHCP snooping + DAI         | `ip arp inspection vlan 1`      |

> Port security does NOT stop ARP spoofing — ARP spoofing spoofs packet **contents**, not source MAC.

***

### Part (a) — ARP cache poisoning \[10 marks]

**Exact question — 2025 \[Q2]\[A], 2024 \[Q2]\[A], 2025R \[Q2]\[A]:**

> ⚠️ Image in question — refer to 2025 \[Q2] topology diagram (Attacker/Victim/Router/DHCP server)
>
> Consider the network depicted above. Explain how the Attacker might implement an ARP cache poisoning attack to intercept all traffic between the Victim and the Internet. Provide example malicious traffic and mention any relevant tools in your explanation.

**2023 \[Q2]\[A] variant — uses Q1 topology:**

> Consider the network depicted in Question 1. Explain how an attacker at PC-B might implement an ARP cache poisoning attack to intercept all traffic between PC-A and PC-C.

#### How to approach — step by step:

**Step 1** — Show BEFORE state: normal ARP caches for Victim and Router

**Step 2** — Describe the 2 unsolicited fake ARP replies attacker sends

**Step 3** — Show exact example malicious traffic (Sender IP, Sender MAC)

**Step 4** — Show AFTER state: poisoned ARP caches

**Step 5** — Explain result: MITM — all traffic flows through attacker

**Step 6** — Name the tool: arpspoof / Ettercap

**Topology:** Attacker (136.206.10.11), Victim (136.206.10.12), Router (136.206.10.254)

#### Before attack — normal ARP caches

```
Victim's ARP cache:
136.206.10.254 → ROUTER-MAC

Router's ARP cache:
136.206.10.12 → VICTIM-MAC
```

#### The attack — 2 unsolicited fake ARP replies

```
ARP Reply 1 → sent to Victim:
Sender IP:  136.206.10.254   (pretending to be router)
Sender MAC: ATTACKER-MAC     (own MAC)
→ Victim learns: 136.206.10.254 → ATTACKER-MAC ❌

ARP Reply 2 → sent to Router:
Sender IP:  136.206.10.12    (pretending to be Victim)
Sender MAC: ATTACKER-MAC     (own MAC)
→ Router learns: 136.206.10.12 → ATTACKER-MAC ❌
```

> Replies are **unsolicited** — attacker does not wait for an ARP request.

#### After attack — poisoned caches

```
Victim's ARP cache:
136.206.10.254 → ATTACKER-MAC ❌

Router's ARP cache:
136.206.10.12 → ATTACKER-MAC ❌
```

#### Result — MITM

```
Victim → thinks sending to Router → actually sends to Attacker
Router → thinks sending to Victim → actually sends to Attacker
Attacker forwards traffic both ways → invisible to both sides
All traffic intercepted ✅
```

#### Tools

```bash
arpspoof -i eth0 -t 136.206.10.12 136.206.10.254
arpspoof -i eth0 -t 136.206.10.254 136.206.10.12
```

Or: **Ettercap** (GUI-based MITM tool)

***

### Part (b) — DHCP DORA steps \[4–6 marks]

**Exact question — 2025R \[Q2]\[B] and 2024 \[Q2]\[B]:**

> ⚠️ Image in question — refer to 2025R \[Q2] topology diagram
>
> Consider the network depicted above. With reference to the DHCP protocol, briefly describe the steps involved in the Victim acquiring its configuration from the DHCP server.

#### How to approach — step by step:

**Step 1** — Name the 4 steps: Discover, Offer, Request, Acknowledge

**Step 2** — For each step: who sends it, what it says, broadcast or unicast

**Step 3** — Note all 4 are broadcast — explain why (Victim has no IP until final ACK)

**Step 4** — Mention transaction ID (XID) — how devices ignore messages not for them

#### Model answer:

| Step            | Who sends   | What it says                     | How       |
| --------------- | ----------- | -------------------------------- | --------- |
| **Discover**    | Victim      | "Anyone out there? I need an IP" | Broadcast |
| **Offer**       | DHCP server | "Here, take this IP + config"    | Broadcast |
| **Request**     | Victim      | "I accept that offer"            | Broadcast |
| **Acknowledge** | DHCP server | "Confirmed — IP is yours"        | Broadcast |

> All 4 steps are **broadcast** — Victim has no confirmed IP until final ACK. Devices ignore messages not matching their **transaction ID (XID) + MAC address**.

***

### Part (b/c) — DAI defence \[6 marks]

**Exact question — 2025 \[Q2]\[B]:**

> ⚠️ Image in question — refer to 2025 \[Q2] topology diagram
>
> Consider the network depicted above. Describe the steps you would follow to mitigate the threat of the ARP cache poisoning attack described in part (a). Include any relevant Cisco IOS commands.

#### How to approach — step by step:

**Step 1** — Enable DHCP snooping first (DAI depends on it)

**Step 2** — Mark router port as trusted, untrusted all others

**Step 3** — Enable DAI on the VLAN

**Step 4** — Mark router port as trusted for ARP inspection

**Step 5** — Explain how DAI works (checks ARP against snooping table)

#### Model answer:

#### Step 1 — Enable DHCP snooping first (DAI depends on this)

```
! Step 1 — Enable DHCP snooping globally
ip dhcp snooping
ip dhcp snooping vlan 1

! Step 2 — Trust router port (real DHCP traffic comes from here)
interface gig0/1
 ip dhcp snooping trust
exit

! Step 3 — Rate limit untrusted ports
interface range fa0/1-3
 ip dhcp snooping limit rate 5
exit
```

#### Step 2 — Enable DAI

```
! Step 4 — Enable DAI on VLAN (uses snooping binding table)
ip arp inspection vlan 1
ip arp inspection validate src-mac dst-mac ip

! Step 5 — Trust ARP from router (router has static IP — not in snooping table)
interface gig0/1
 ip arp inspection trust
exit
```

#### How DAI works

```
ARP packet arrives on untrusted port
        ↓
DAI checks: does ARP's IP→MAC match DHCP snooping binding table?
        ↓
Match   → forward ✅
Mismatch → DROP ❌ (attacker's fake ARP blocked)
```

> Router port must be trusted — router has static IP so it is not in snooping table.

***

### Part (b/c) — DHCP spoofing defence \[4–6 marks]

**Exact question — 2024 \[Q2]\[C] and 2025R \[Q2]\[C]:**

> ⚠️ Image in question — refer to 2024 \[Q2] topology diagram
>
> Consider the network depicted above. Describe the steps you would follow to mitigate the threat of a DHCP spoofing attack carried out by the Attacker against Victim. Include any relevant IOS commands.

#### How to approach — step by step:

**Step 1** — Briefly explain how DHCP spoofing works (attacker replies faster)

**Step 2** — Enable DHCP snooping globally

**Step 3** — Enable on the VLAN

**Step 4** — Mark router port as trusted

**Step 5** — Rate limit untrusted ports

**Step 6** — Explain: trusted ports can send DHCP replies, untrusted cannot

#### Model answer:

#### How the attack works

```
Victim sends DHCP Discover (broadcast)
↓
Attacker's rogue DHCP server replies faster than real server
↓
Attacker's Offer: "Your gateway = ATTACKER-IP"
↓
Victim accepts → all traffic routed via attacker → MITM
```

#### Defence — DHCP snooping

```
! Step 1 — Enable DHCP snooping globally
ip dhcp snooping

! Step 2 — Enable on the VLAN
ip dhcp snooping vlan 1

! Step 3 — Trust router port (real DHCP server traffic comes from here)
interface gig0/1
 ip dhcp snooping trust
exit

! Step 4 — Rate limit untrusted ports (blocks rogue offers + starvation)
interface range fa0/1-3
 ip dhcp snooping limit rate 5
exit
```

> DHCP snooping: trusted ports can send DHCP replies. Untrusted ports cannot. Rogue DHCP server on untrusted port → replies **dropped**.

***

### Part (c) — MAC overflow attack \[4 marks]

**Exact question — 2025 \[Q2]\[C] and 2023 \[Q2]\[C]:**

> Describe how a MAC address table overflow attack works, the implications of a successful attack and how a switch can be defended against such attacks. Include any relevant Cisco IOS commands.

#### How to approach — step by step:

**Step 1** — Explain the attack: macof sends random source MACs → table fills

**Step 2** — Explain the implication: switch reverts to hub → attacker sees all traffic

**Step 3** — Give the defence: port security

**Step 4** — Write port security commands

#### Model answer:

#### How the attack works

```
Attacker runs macof
→ Generates 100,000s of Ethernet frames with random source MACs
→ Switch MAC table fills with fake entries
→ No room for legitimate MACs
→ Switch reverts to HUB behaviour
→ ALL frames flooded out ALL ports
→ Attacker receives everyone's traffic ❌
```

> `macof` sends random **source MACs in Ethernet frames** — NOT ARP packets.

#### Defence — port security

```
! Step 1 — Enter access ports (not trunk or router ports)
interface range fa0/1-3

! Step 2 — Enable port security on the interface
 switchport port-security

! Step 3 — Limit to 1 MAC address per port
 switchport port-security maximum 1

! Step 4 — Sticky learning — remember first MAC seen
 switchport port-security mac-address sticky

! Step 5 — Shutdown port if violation occurs
 switchport port-security violation shutdown
exit
```

#### Violation modes

| Mode       | Drops traffic | Port stays up  | Logs     | Recovery                   |
| ---------- | ------------- | -------------- | -------- | -------------------------- |
| `shutdown` | ✅             | ❌ err-disabled | ✅        | `shutdown` + `no shutdown` |
| `restrict` | ✅             | ✅              | ✅        | automatic                  |
| `protect`  | ✅             | ✅              | ❌ silent | automatic                  |

***

### IOS commands — quick reference

#### Port security

```
! Enter access ports
interface range fa0/1-3
 switchport port-security                           ! enable port security
 switchport port-security maximum 1                 ! max 1 MAC per port
 switchport port-security mac-address sticky        ! remember first MAC seen
 switchport port-security violation shutdown        ! shutdown port on violation
exit
```

#### DHCP snooping

```
ip dhcp snooping                                    ! enable globally
ip dhcp snooping vlan 1                             ! enable on VLAN 1
interface gig0/1
 ip dhcp snooping trust                             ! router port — trust DHCP
exit
interface range fa0/1-3
 ip dhcp snooping limit rate 5                      ! rate limit untrusted ports
exit
```

#### DAI

```
ip arp inspection vlan 1                            ! enable DAI on VLAN
ip arp inspection validate src-mac dst-mac ip       ! full validation
interface gig0/1
 ip arp inspection trust                            ! trust ARP from router
exit
```

***

### Exam traps — what loses marks

| Trap                    | Wrong                           | Correct                                     |
| ----------------------- | ------------------------------- | ------------------------------------------- |
| ARP spoofing defence    | Port security                   | DHCP snooping + DAI                         |
| Port security stops ARP | Yes it does                     | No — ARP spoofs **contents** not source MAC |
| DAI without snooping    | Enable DAI first                | DHCP snooping **must come first**           |
| DHCP keyword confusion  | `ip arp snooping`               | `ip dhcp snooping`                          |
| ARP keyword confusion   | `ip dhcp inspection`            | `ip arp inspection`                         |
| Rate limit syntax       | `ip dhcp snooping rate-limit 5` | `ip dhcp snooping limit rate 5`             |
| switchport prefix       | `port-security maximum 1`       | `switchport port-security maximum 1`        |
| violation keyword       | `port-security violate`         | `switchport port-security violation`        |
| range keyword           | `interface fa0/1-3`             | `interface range fa0/1-3`                   |
| Trust DAI on all ports  | Globally applied                | Only trusted on **router port** (gig0/1)    |
| DORA last step          | Authenticate                    | **Acknowledge**                             |
| DORA broadcast/unicast  | Offer/ACK unicast               | All 4 steps are **broadcast**               |

***

### Weak spots flagged — Mohan's mistakes this session

1. Said ARP replies are responses to requests — they are **unsolicited**
2. Forgot before/after ARP cache states in poisoning answer
3. Forgot to name tools (arpspoof / Ettercap / macof)
4. Said macof sends random ARP responses — it sends random **Ethernet frames**
5. Said DORA last step = Authenticate — it is **Acknowledge**
6. Said Offer/ACK are unicast — all 4 DORA steps are **broadcast**
7. Missing `switchport` prefix on port security commands
8. Forgot DHCP snooping must be configured before DAI
