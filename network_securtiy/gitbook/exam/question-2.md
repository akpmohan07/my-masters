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

### Part (a) — ARP cache poisoning — model answer \[10 marks]

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

### Part (b) — DHCP DORA steps — model answer \[4 marks]

| Step            | Who sends   | What it says                     | How       |
| --------------- | ----------- | -------------------------------- | --------- |
| **Discover**    | Victim      | "Anyone out there? I need an IP" | Broadcast |
| **Offer**       | DHCP server | "Here, take this IP + config"    | Broadcast |
| **Request**     | Victim      | "I accept that offer"            | Broadcast |
| **Acknowledge** | DHCP server | "Confirmed — IP is yours"        | Broadcast |

> All 4 steps are **broadcast** — Victim has no confirmed IP until final ACK. Devices ignore messages not matching their **transaction ID (XID) + MAC address**.

***

### Part (b/c) — DAI defence — model answer \[6 marks]

#### Step 1 — Enable DHCP snooping first (DAI depends on this)

```
ip dhcp snooping
ip dhcp snooping vlan 1

interface gig0/1
 ip dhcp snooping trust        ! router port
exit

interface range fa0/1-3
 ip dhcp snooping limit rate 5
exit
```

#### Step 2 — Enable DAI

```
ip arp inspection vlan 1
ip arp inspection validate src-mac dst-mac ip

interface gig0/1
 ip arp inspection trust        ! trust ARP from router
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

### Part (b/c) — DHCP spoofing defence — model answer \[4–6 marks]

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
ip dhcp snooping
ip dhcp snooping vlan 1

interface gig0/1
 ip dhcp snooping trust        ! real DHCP server/router
exit

interface range fa0/1-3
 ip dhcp snooping limit rate 5 ! rate limit + blocks rogue offers
exit
```

> DHCP snooping: trusted ports can send DHCP replies. Untrusted ports cannot. Rogue DHCP server on untrusted port → replies **dropped**.

***

### Part (c) — MAC overflow attack — model answer \[4 marks]

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
interface range fa0/1-3
 switchport port-security
 switchport port-security maximum 1
 switchport port-security mac-address sticky
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
interface range fa0/1-3
 switchport port-security
 switchport port-security maximum 1
 switchport port-security mac-address sticky
 switchport port-security violation shutdown
exit
```

#### DHCP snooping

```
ip dhcp snooping
ip dhcp snooping vlan 1
interface gig0/1
 ip dhcp snooping trust
exit
interface range fa0/1-3
 ip dhcp snooping limit rate 5
exit
```

#### DAI

```
ip arp inspection vlan 1
ip arp inspection validate src-mac dst-mac ip
interface gig0/1
 ip arp inspection trust
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
