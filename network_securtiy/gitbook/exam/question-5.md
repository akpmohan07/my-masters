# Question 5

## Q5 — IPv6 + DNS + WEP

**CSC1134 — Exam Revision** **Date:** 20 April 2026 **Exam:** Tuesday 21 April 2026 — 9:00 AM

***

### Pattern summary

| Year  | Part (a)             | Part (b)                        |
| ----- | -------------------- | ------------------------------- |
| 2019  | Web security         | WEP authentication + encryption |
| 2020  | Keystream role       | Bit-flipping attack             |
| 2021  | Keystream role       | Keystream exploitation          |
| 2022  | DHCP attacks         | Chop-chop attack                |
| 2022R | DNS output interpret | Keystream + exploitation        |
| 2023  | dig explain style    | Cafe latte attack               |
| 2023R | DNS output interpret | Keystream + exploitation        |
| 2024  | dig explain style    | Cafe latte attack               |
| 2025  | dig write style      | SLAAC                           |
| 2025R | dig explain style    | GUA vs LLA + compression        |

**For 2026 — focus on:**

* dig explain or write style — certain
* SLAAC or GUA/LLA — very likely
* WEP cafe latte — possible (dropped 2025 but could return)

***

### Part (a) — DNS dig commands

***

#### Pattern 1 — Explain style \[2023, 2024, 2025R]

**Exact question — 2024 \[Q5]\[A]:**

> Assume each of the commands shown below is executed on a Linux lab machine in DCU's School of Computing (with no firewall restrictions in place). Documenting any assumptions, explain each command and its expected output. Where applicable, also explain any security issues the output of these commands may reveal. i. `dig jp NS` ii. `dig @c.ns.ie www.rte.ie` iii. `dig . NS` iv. `dig +trace A einstein.computing.dcu.ie` v. `dig -x 136.206.1.6`

#### How to approach — step by step:

**Step 1** — Identify what the command is querying (domain + record type)

**Step 2** — Predict flags using the flag prediction rules

**Step 3** — State expected output (status, number of records, examples)

**Step 4** — State security issue if applicable

**Step 5** — Document assumptions

***

#### Flag prediction rules — memorise:

| Command                         | Expected flags | Why                                  |
| ------------------------------- | -------------- | ------------------------------------ |
| `dig jp NS`                     | `qr rd ra`     | Local resolver — not authoritative   |
| `dig @c.ns.ie www.rte.ie`       | `qr rd ra`     | c.ns.ie not authoritative for rte.ie |
| `dig . NS`                      | `qr rd ra`     | Local resolver cache                 |
| `dig +trace` intermediate steps | `qr ra`        | No recursion needed                  |
| `dig +trace` final step         | `qr aa`        | Authoritative server ✅               |
| `dig -x IP`                     | `qr rd ra`     | Local resolver                       |

**One liner:**

```
qr  = always present
rd  = always (unless +norecurse)
ra  = local resolver only
aa  = only when server OWNS the zone
```

***

#### Full model answer — all 5 commands

**General assumptions:**

```
- Commands executed on Linux machine in DCU's School of Computing
- No firewall restrictions in place
- Local recursive resolver available
- DNS uses UDP port 53
- All domains exist and have valid DNS records
```

**i. `dig jp NS`**

**What it does:** Retrieves the name servers responsible for Japan's `.jp` top-level domain.

**Expected output:**

```
status:  NOERROR
flags:   qr rd ra        (no aa — non-authoritative, from local resolver cache)
ANSWER:  8 NS records    (a.dns.JP through h.dns.JP)
TTL:     ~11000 seconds
```

**Security issue:** Exposes Japan's TLD DNS infrastructure. An attacker could DDoS these servers making all `.jp` domains unresolvable. NS queries return large responses from small queries — useful for DNS amplification attacks where attacker spoofs victim's IP address to flood them with large responses.

***

**ii. `dig @c.ns.ie www.rte.ie`**

**What it does:** Sends a DNS query for `www.rte.ie` directly to `c.ns.ie` — one of Ireland's `.ie` TLD nameservers — bypassing the local resolver.

**Assumption:** `c.ns.ie` resolves to IP `194.146.106.98`

**Expected output:**

```
status:  NOERROR
flags:   qr rd ra        (no aa — c.ns.ie is NOT authoritative for rte.ie zone)
ANSWER:  2 A records
         www.rte.ie → 104.18.124.28 (assumption)
         www.rte.ie → 104.18.125.28 (assumption)
SERVER:  194.146.106.98 (c.ns.ie)
```

**Security issue:** `c.ns.ie` has `ra` flag set — it performs recursion for any client. A TLD server should only serve its own `.ie` zone, not recurse for arbitrary clients. This open recursive resolver misconfiguration can be exploited for DNS amplification attacks — attacker spoofs victim's IP, sends small queries to `c.ns.ie`, victim receives large responses.

***

**iii. `dig . NS`**

**What it does:** Retrieves the name servers for the DNS root zone (`.`) — the top of the entire DNS hierarchy.

**Expected output:**

```
status:  NOERROR
flags:   qr rd ra        (no aa — non-authoritative, from local resolver cache)
ANSWER:  13 NS records   (a.root-servers.net through m.root-servers.net)
TTL:     ~46000 seconds
```

**Security issue:** Exposes all 13 root name servers — the most critical DNS infrastructure on the internet. A DDoS attack against all root servers would make all DNS resolution globally impossible. NS queries return large responses from small queries — useful for DNS amplification attacks. In practice each root server name maps to hundreds of physical servers using anycast routing — making a complete takedown very difficult.

***

**iv. `dig +trace A einstein.computing.dcu.ie`**

**What it does:** Performs iterative DNS resolution from root to authoritative, showing every step. Bypasses local resolver — queries each server in the chain directly.

**Assumption:** Final A record resolves to `136.206.11.45`

**Expected output:**

```
Step 1 — Root servers:
         . NS → a-m.root-servers.net
         flags: qr ra    (no aa, no rd)

Step 2 — .ie TLD servers:
         ie. NS → a.ns.ie, c.ns.ie...
         flags: qr ra    (no aa, no rd)

Step 3 — DCU nameservers:
         dcu.ie NS → ns1.dcu.ie (assumption)
         flags: qr ra    (no aa, no rd)

Step 4 — computing.dcu.ie nameservers:
         computing.dcu.ie NS → ns1.computing.dcu.ie (assumption)
         flags: qr ra    (no aa, no rd)

Step 5 — Final authoritative answer:
         einstein.computing.dcu.ie A → 136.206.11.45
         flags: qr aa    ← authoritative answer ✅
```

**Security issue:** Reveals DCU's complete DNS delegation chain — nameservers used, hosting providers, and final IP of host. Hostname `einstein` reveals it is a named server — attacker can infer its purpose and target it. Exposes internal DNS architecture useful for network reconnaissance and targeted attacks.

***

**v. `dig -x 136.206.1.6`**

**What it does:** Performs a reverse DNS lookup — maps IP `136.206.1.6` back to its hostname using a PTR record. IP is reversed and `.in-addr.arpa` appended: `6.1.206.136.in-addr.arpa`

**Assumption:** PTR record resolves to `examresults2.dcu.ie`

**Expected output:**

```
status:  NOERROR
flags:   qr rd ra        (no aa — non-authoritative)
ANSWER:  PTR record
         6.1.206.136.in-addr.arpa → examresults2.dcu.ie
TTL:     5 seconds       (very short — changes frequently)
```

**Security issue:** Reverse DNS exposes the purpose of a server from just its IP address. Hostname `examresults2` reveals this is DCU's exam results server — a high-value target containing sensitive student data. An attacker could:

* Target this server specifically for data theft
* Infer `examresults1.dcu.ie` exists (naming convention revealed)
* Map DCU's entire IP range using reverse lookups to identify all servers by purpose

***

#### Pattern 2 — Output interpret style \[2022R, 2023R]

**Exact question — 2023R \[Q5]\[A]:**

> Consider the following excerpt from a DNS server response to a query issued from a machine in DCU's School of Computing:
>
> ⚠️ Image in question — refer to 2023R \[Q5] dig output
>
> i. Explain the role in DNS of each of the three highlighted entries. \[6 marks] ii. Provide the Linux command that would elicit the above response. \[2 marks] iii. Provide the Linux command that would elicit the authoritative answer. \[2 marks]

**The dig output given:**

```
;; flags: qr rd ra
;; ANSWER: 2, AUTHORITY: 4, ADDITIONAL: 9

ANSWER SECTION:
www.rte.ie.  221  IN  A  104.18.142.17
www.rte.ie.  221  IN  A  104.18.143.17

AUTHORITY SECTION:
rte.ie.  29296  IN  NS  ns1.rte.ie.
rte.ie.  29296  IN  NS  ns2.rte.ie.
rte.ie.  29296  IN  NS  ns3.rte.ie.
rte.ie.  29296  IN  NS  ns4.rte.ie.

ADDITIONAL SECTION:
ns1.rte.ie.  743     IN  A     162.159.0.73
ns1.rte.ie.  141051  IN  AAAA  2400:cb00:2049:1::a29f:49
...
```

#### How to approach — step by step:

**Step 1** — Read the first line to find the command:

```
; <<>> DiG 9.16.22-Debian <<>> A www.rte.ie
                                ↑ ──────────
                          This IS the command arguments
→ command was: dig www.rte.ie A
```

**Step 2** — For highlighted entries: identify type → explain role

**Step 3** — For authoritative command: look at AUTHORITY section → use that NS server with `@`

#### Three sections explained:

**ANSWER section:**

> Contains the direct response — two A records mapping `www.rte.ie` to Cloudflare IPs. TTL of 221 seconds indicates a cached non-authoritative answer. No `aa` flag — came from resolver cache not authoritative server.

**AUTHORITY section:**

> Lists the authoritative nameservers for `rte.ie` domain — ns1–ns4.rte.ie. These servers own the zone. Included to guide further authoritative queries. TTL \~8 hours.

**ADDITIONAL section:**

> Glue records — IP addresses (A and AAAA) of the nameservers listed in AUTHORITY section. Included to save the resolver from having to look up nameserver IPs separately.

#### The 3 highlighted entries — 2023R specific:

**`id: 61694`**

> Transaction ID — unique 16-bit number per query. Response must carry same ID as query to be accepted. Prevents mismatched responses. Security: DNS cache poisoning exploits this — attacker who guesses transaction ID can inject fake response.

**`flags: qr rd ra`**

> qr = this is a response. rd = client asked for recursion. ra = resolver can recurse. No aa flag = non-authoritative cached answer.

**`rte.ie. 29296 IN NS ns1.rte.ie.`**

> NS record in AUTHORITY section — identifies authoritative nameservers for rte.ie zone. TTL 29296 seconds. Used to direct further queries to rte.ie's own nameservers for authoritative answer.

#### Model answers ii and iii:

```
ii. Command that produced response:
    dig www.rte.ie A
    (or simply: dig www.rte.ie)

iii. Authoritative answer command:
     Step 1 — AUTHORITY section shows: ns1.rte.ie
     Step 2 — Query it directly:
     dig @ns1.rte.ie www.rte.ie A
     → aa flag in response = authoritative ✅
```

***

#### Pattern 3 — Write style \[2025]

**Exact question — 2025 \[Q5]\[A]:**

> Documenting any assumptions, describe the steps and provide the dig commands you would use to retrieve the following information: i. The authoritative A record for `www.computing.dcu.ie` ii. The email servers for the `dcu.ie` domain iii. The name servers for the `fr` (France) domain iv. The IPv6 address of `www.cisco.com` v. The name of the host whose IPv4 address is `136.206.1.14`

#### How to approach — record type mapping:

| Requirement     | Record | Command             |
| --------------- | ------ | ------------------- |
| IPv4 address    | A      | `dig domain A`      |
| IPv6 address    | AAAA   | `dig domain AAAA`   |
| Mail servers    | MX     | `dig domain MX`     |
| Name servers    | NS     | `dig domain NS`     |
| Country TLD     | NS     | `dig cc NS`         |
| Reverse lookup  | PTR    | `dig -x IP`         |
| Authoritative A | A + @  | 2 steps — see below |

#### Authoritative answer — always 2 steps:

```
Step 1 — Find authoritative nameserver:
dig computing.dcu.ie NS
→ Assumption: returns ns1.computing.dcu.ie

Step 2 — Query it directly:
dig @ns1.computing.dcu.ie www.computing.dcu.ie A
→ aa flag = authoritative ✅
```

#### Full model answer:

```
! Assumptions:
! - Recursive resolver available
! - No firewall restrictions
! - DNS uses UDP port 53
! - Nameserver assumed as ns1.computing.dcu.ie

! i — Authoritative A record for www.computing.dcu.ie
!     Step 1: find authoritative nameserver
dig computing.dcu.ie NS
!     Step 2: query it directly
dig @ns1.computing.dcu.ie www.computing.dcu.ie A
!     aa flag in response = authoritative ✅

! ii — Email servers for dcu.ie
dig dcu.ie MX

! iii — Name servers for France
dig fr NS

! iv — IPv6 address of www.cisco.com
dig www.cisco.com AAAA

! v — Hostname for IP 136.206.1.14
dig -x 136.206.1.14
```

***

### Part (b) — SLAAC \[2025]

**Exact question — 2025 \[Q5]\[B]:**

> Describe the operation of SLAAC (stateless address auto-configuration) under IPv6. Include a simple network diagram and make reference to the following in your answer:
>
> * Message types
> * Global unicast addresses
> * Link local addresses

#### How to approach — step by step:

**Step 1** — Draw diagram showing PC ↔ Router exchange

**Step 2** — Explain each step with protocol + destination + content

**Step 3** — Show final configuration table

#### Network diagram:

```
    PC                         Router
    |                             |
    | 1. Creates LLA              |
    |    fe80::abcd (auto)        |
    |                             |
    |-- 2. RS (ICMPv6) ---------> |
    |      dst: ff02::2           |
    |      "Any routers here?"    |
    |                             |
    |<-- 3. RA (ICMPv6) --------- |
    |      dst: ff02::1           |
    |      prefix: 2001:db8::/64  |
    |      gateway: fe80::1       |
    |                             |
    | 4. Builds GUA               |
    |    2001:db8::abcd:1234      |
    |    (prefix + interface ID)  |
    |                             |
    | 5. DAD check (NDP)          |
    |    "Is this address taken?" |
    |    No reply = unique ✅     |
    |                             |
    |-- 6. DHCPv6 -------------> |
    |      dst: ff02::1:2         |
    |      "DNS server?"          |
    |<-- DNS reply --------------|
    |      DNS: 2001:db8::254    |
    |                             |
    | IPv6 ready ✅               |
```

#### Written explanation:

**Step 1 — LLA creation:** As soon as the device connects it automatically generates a Link-Local Address with prefix `fe80::/10`. Used only on the local link — never routed beyond the router. Used to communicate with router for SLAAC.

**Step 2 — Router Solicitation (RS):** Device sends ICMPv6 RS message to `ff02::2` (all routers multicast) asking if any routers are present on the link.

**Step 3 — Router Advertisement (RA):** Router replies with ICMPv6 RA message sent to `ff02::1` (all nodes multicast) containing:

* Network prefix: `2001:db8:1:2::/64`
* Default gateway: router's LLA (`fe80::1`)
* DNS server (if configured)

**Step 4 — GUA construction:** Device builds Global Unicast Address by combining:

* Network prefix from RA: `2001:db8:1:2::`
* Self-generated interface ID: `abcd:1234:5678:9abc`
* Result: `2001:db8:1:2:abcd:1234:5678:9abc`

**Step 5 — DAD check:** Device uses NDP (Neighbour Discovery Protocol) to check if GUA is already in use. Sends Neighbour Solicitation for its own address. No reply = address unique ✅

**Step 6 — DHCPv6 for DNS:** Device sends DHCPv6 request to `ff02::1:2` (all DHCPv6 servers) to obtain DNS server address. SLAAC does not provide DNS — DHCPv6 fills this gap.

#### Final configuration table:

| Item            | Source                                    |
| --------------- | ----------------------------------------- |
| LLA             | Self-generated (`fe80::`)                 |
| GUA             | SLAAC — prefix (RA) + interface ID (self) |
| Default gateway | Router's LLA from RA                      |
| DNS server      | DHCPv6                                    |

#### Multicast addresses — memorise:

| Address     | Meaning            | Used for                       |
| ----------- | ------------------ | ------------------------------ |
| `ff02::2`   | All routers        | RS — device looking for router |
| `ff02::1`   | All nodes          | RA — router broadcasting       |
| `ff02::1:2` | All DHCPv6 servers | DHCPv6 DNS request             |

***

### Part (b) — GUA vs LLA \[2025R]

**Exact question — 2025R \[Q5]\[B]:**

> Using examples, describe the difference between the following kinds of IPv6 address: i. A global unicast address (GUA) ii. A link-local address (LLA)

#### How to approach:

**Step 1** — Define each address type

**Step 2** — State prefix range

**Step 3** — Explain how assigned

**Step 4** — State scope + mandatory/optional

**Step 5** — Give example

#### Model answer:

**i. Global Unicast Address (GUA):**

> A GUA is equivalent to a public IPv4 address — globally unique and routable on the internet. First hextet in range `2000–3fff` (currently assigned block `2000::/3`). Router sends RA containing network prefix — host generates its own interface ID and combines both to form GUA. Optional — device needs GUA only for internet access.
>
> Structure: Network prefix (48 bits) + Subnet ID (16 bits) + Interface ID (64 bits)
>
> Example: `2001:db8:1:2:abcd:1234:5678:9abc`

**ii. Link-Local Address (LLA):**

> An LLA is used only on the local network link — never routed beyond the local router. Always starts with `fe80::/10`. Generated automatically by the host — no router or DHCP needed. Mandatory — every IPv6 device must have one. Used for router discovery (RS/RA), NDP neighbour discovery, and as default gateway address.
>
> Example: `fe80::52:911c:a370:b03f`

#### Key difference table:

|            | GUA                            | LLA                     |
| ---------- | ------------------------------ | ----------------------- |
| Prefix     | `2000–3fff`                    | `fe80::`                |
| Scope      | Global — internet              | Local link only         |
| Mandatory  | No                             | **Yes**                 |
| Created by | Prefix (router RA) + ID (host) | Host only               |
| Routed?    | Yes                            | Never past router       |
| Used for   | Internet traffic               | NDP, gateway, discovery |

***

### Part (c) — IPv6 compression \[2025R]

**Exact question — 2025R \[Q5]\[C]:**

> Write the following IPv6 address in compressed format: `2001:0000:0db8:1111:0000:0000:0000:0200`

#### Two rules:

**Rule 1 — Remove leading zeros per hextet:**

```
0000 → 0
0db8 → db8
0200 → 200
```

**Rule 2 — Replace longest consecutive all-zero hextets with `::`:**

```
Only ONE :: per address
If tie → replace LEFTMOST group
```

#### Worked example — 2025R question:

```
Step 1 — Remove leading zeros:
2001:0000:0db8:1111:0000:0000:0000:0200
→ 2001:0:db8:1111:0:0:0:200

Step 2 — Find longest consecutive zeros:
positions 5-7: 0:0:0 (3 hextets) ← only group
→ replace with ::

Result: 2001:0:db8:1111::200 ✅
```

#### All cases:

| Scenario                          | Rule             | Example                                |
| --------------------------------- | ---------------- | -------------------------------------- |
| One zero group                    | Replace with ::  | `2001:db8:0:0:0:0:0:1` → `2001:db8::1` |
| Multiple groups — different sizes | Replace longest  | `2001:0:0:1:0:0:0:1` → `2001:0:0:1::1` |
| Multiple groups — same size       | Replace leftmost | `0:0:0:1:0:0:0:1` → `::1:0:0:0:1`      |
| :: only once                      | Never use twice  | `2001::1::1` ❌ invalid                 |

***

### Part (b) — WEP Attacks \[2023, 2024]

#### WEP foundation — must know:

```
Encryption:
Plaintext  XOR  Keystream  =  Ciphertext

Keystream = RC4(IV + WEP key)
IV = 24-bit random number sent in plaintext

XOR property:
A XOR B = C
C XOR B = A    ← decryption
C XOR A = B    ← keystream recovery

Key rule: know any two → find the third
```

***

#### Attack 1 — Cafe Latte \[2023, 2024]

**Exact question — 2024 \[Q5]\[B]:**

> Describe in detail how a WEP cafe latte attack functions.

**Setup:**

```
- Attacker sets up rogue access point
- Victim's laptop has saved WEP network
- Victim's laptop auto-connects to rogue AP
- No proximity to real AP required — only to victim's laptop
```

**Step by step:**

```
Step 1 — Capture ARP packet:
Victim's laptop sends ARP broadcast to find gateway
Attacker captures encrypted ARP packet
ARP content is predictable → known plaintext

Step 2 — Bit flip the ARP packet:
Ciphertext XOR known_plaintext = Keystream
Attacker flips bits to change dst IP from broadcast → AP IP
WEP CRC is linear → attacker fixes checksum too

Step 3 — Replay modified packets:
Attacker replays modified ARP packet thousands of times
Victim's laptop responds to each → generates thousands of packets
Each packet has different IV

Step 4 — Crack WEP key:
Collect 60,000-100,000 packets
Run aircrack-ng → statistical analysis on IVs
WEP key revealed ✅
```

**Diagram:**

```
Attacker               Victim Laptop
    |                       |
    |<-- ARP packet --------|   (captured)
    |                       |
    | [bit flip + fix CRC]  |
    |                       |
    |--- modified ARP ----->|   (thousands of times)
    |<-- ARP responses -----|   (collect IVs)
    |                       |
    | [aircrack-ng]         |
    | WEP key cracked ✅    |
```

**Why it works:**

```
1. IV is only 24 bits → reuse inevitable
2. ARP content predictable → known plaintext attack
3. XOR reversible → keystream extractable
4. CRC is linear → modifiable without key
5. aircrack-ng exploits RC4 statistical weaknesses
```

***

#### Attack 2 — Keystream role + exploitation \[2021, 2022R]

**Part i — Role of keystream:**

> The keystream is a pseudo-random bit sequence generated by RC4 using the WEP key and IV. XORed with plaintext to produce ciphertext.
>
> Example:
>
> ```
> Plaintext:  10110011
> Keystream:  11001010
> Ciphertext: 01111001  (XOR)
> ```

**Part ii — Can attacker decrypt with keystream? No — but can exploit:**

> ```
> If attacker knows ciphertext + plaintext (e.g. ARP structure):
> Ciphertext XOR Plaintext = Keystream
>
> With keystream attacker can:
> 1. Decrypt any packet that used same IV
> 2. Inject new encrypted packets
> 3. Modify existing packets (bit flipping)
> ```

***

#### Attack 3 — Bit flipping \[2020]

> Modifies encrypted traffic WITHOUT decryption or knowing the key.
>
> XOR property: flipping a bit in ciphertext flips the same bit in decrypted plaintext.
>
> ```
> Step 1 — Capture encrypted packet
> Step 2 — Calculate which bits to flip:
>          delta = original_value XOR target_value
> Step 3 — Flip those bits in ciphertext
> Step 4 — Fix WEP CRC checksum (linear → easy to update)
> Step 5 — Inject modified packet → victim decrypts modified plaintext
> ```
>
> Security implication: WEP provides NO integrity protection — encrypted traffic modifiable without decryption.

***

#### Attack 4 — Chop-chop \[2022]

> Decrypts WEP packets one byte at a time without knowing the WEP key.
>
> ```
> Step 1 — Capture encrypted packet
> Step 2 — Remove LAST byte
> Step 3 — Try all 256 possible values for that byte
> Step 4 — Fix CRC for each guess → send to AP
> Step 5 — AP accepts correct guess → byte value known
> Step 6 — Repeat for each byte → full plaintext revealed
> Step 7 — Plaintext XOR Ciphertext = Keystream
> Step 8 — Use keystream to inject own encrypted packets
> ```

***

#### All WEP attacks — master summary:

| Attack          | Requires key? | Exploits                         | Result                      |
| --------------- | ------------- | -------------------------------- | --------------------------- |
| Cafe latte      | No            | ARP predictability + IV weakness | WEP key cracked             |
| Keystream reuse | No            | Same IV used twice               | Plaintext revealed          |
| Bit flipping    | No            | XOR reversibility + linear CRC   | Traffic modified undetected |
| Chop-chop       | No            | AP confirms byte guesses         | Full plaintext + keystream  |

#### Why ALL WEP attacks work:

```
1. IV only 24 bits → reuse inevitable (~5000 packets)
2. RC4 stream cipher → XOR reversible
3. CRC checksum linear → easy to fix after modification
4. ARP content predictable → known plaintext attacks
5. No replay protection → captured packets can be resent
```

***

### Exam traps

| Trap                             | Wrong              | Correct                             |
| -------------------------------- | ------------------ | ----------------------------------- |
| aa flag always present           | Always shown       | Only when server **owns** the zone  |
| +trace all steps have aa         | All steps          | **Final step only**                 |
| c.ns.ie authoritative for rte.ie | Yes                | No — owns .ie zone, not rte.ie zone |
| dig -x uppercase                 | `-X`               | `-x` lowercase                      |
| Authoritative = one command      | `dig www.domain A` | **Two steps** — find NS first       |
| Root servers                     | 12                 | **13** (a–m)                        |
| IV size                          | 32 bits            | **24 bits**                         |
| WEP encryption                   | AES                | **RC4** stream cipher               |
| Cafe latte needs AP proximity    | Yes                | **No** — only victim's laptop       |
| Tool for cafe latte              | Wireshark          | **aircrack-ng**                     |
| LLA mandatory                    | Optional           | **Mandatory** — every IPv6 device   |
| GUA prefix                       | fe80::             | **2000–3fff**                       |
| DHCPv6 destination               | ff02::1            | **ff02::1:2**                       |
| RS destination                   | ff02::1            | **ff02::2**                         |

***

### Weak spots flagged

1. Predicted `aa` flag for `@c.ns.ie` — c.ns.ie is NOT authoritative for rte.ie
2. Used capital `-X` for reverse lookup — must be lowercase `-x`
3. Forgot 2-step process for authoritative answer
4. Said GUA prefix is `2000-3999` — hexadecimal: `2000-3fff`
5. Confused RS destination (`ff02::2`) with RA destination (`ff02::1`)
6. Said cafe latte requires proximity to AP — only needs proximity to victim laptop
