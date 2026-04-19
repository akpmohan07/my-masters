# Question 1



<figure><img src="../.gitbook/assets/image (1).png" alt=""><figcaption></figcaption></figure>



## Q1 — Ethernet, ARP, MAC Tables, Switches

**CSC1134 — Exam Revision** **Date:** 19 April 2026 **Exam:** Tuesday 21 April 2026 — 9:00 AM

***

### Pattern summary

| Part | Marks | What it asks                                                       |
| ---- | ----- | ------------------------------------------------------------------ |
| (a)  | 10    | MAC table trace — fill S1, S2, S3 after 2–3 pings                  |
| (b)  | 4–6   | Switch reaction OR encapsulation diagram                           |
| (c)  | 4–5   | Src/dst MAC across hops OR subnet detection OR Jimmy's MAC routing |

***

### The fixed topology — memorise this

Same diagram every year:

```
[PC-A/AAAA/192.168.10.1]
        |
       Fa0/1
       [S1]
       Gig0/1
        |
       Gig0/1──[R1 Gig0/0/1/FBFB/192.168.10.254]
               [R1 Gig0/0/0/FAFA/192.168.20.254]
        |
       Gig0/1
       [S2]──Fa0/2──[PC-B/BBBB/192.168.20.1]
       Fa0/1
        |
       Fa0/1
       [S3]──Fa0/2──[PC-C/CCCC/192.168.20.2]
           ──Fa0/3──[PC-D/DDDD/192.168.20.3]
           ──Fa0/4──[PC-E/EEEE/192.168.20.4]
```

**2024/2025 variant** adds PC-F (FFFF/192.168.20.4) and moves PC-A/PC-B both to 10.x subnet.

***

### Core concepts

#### Switch MAC learning — 3 rules

1. **Always learn** — record source MAC → port on every frame received
2. **Known destination** → forward out that specific port only
3. **Unknown destination** → flood out ALL ports except incoming

> Switch records: **source MAC → port it came in on**. Always the source. Never the destination.

#### Switch vs Router

| Device | Operates at | Forwards based on | Changes frame?                         |
| ------ | ----------- | ----------------- | -------------------------------------- |
| Switch | Layer 2     | MAC address       | No — frame passes through unchanged    |
| Router | Layer 3     | IP address        | Yes — strips old frame, builds new one |

#### ARP — Address Resolution Protocol

* Maps **IP address → MAC address**
* Only works within the **same subnet**
* ARP request = **broadcast** (FF:FF:FF:FF:FF:FF)
* ARP reply = **unicast** (back to requester only)
* Mappings cached in ARP table — expire after \~20 minutes

#### MAC addresses change at every router hop

```
PC-D → S3 → S2 → R1 → S1 → PC-A

Link          Src MAC   Dst MAC   Src IP          Dst IP
PC-D → R1     DDDD      FAFA      192.168.20.3    192.168.10.1
R1 → PC-A     FBFB      AAAA      192.168.20.3    192.168.10.1
```

> **MAC = current hop. IP = final destination. IP never changes end to end.**

#### Encapsulation — TCP/IP layers

```
Layer           PDU         Addressing
Application     Message     None
Transport       Segment     Src port / Dst port
Network         Packet      Src IP / Dst IP
Data Link       Frame       Src MAC / Dst MAC
Physical        Bits        None
```

Key ports to know:

| Protocol | Transport | Port |
| -------- | --------- | ---- |
| HTTP     | TCP       | 80   |
| HTTPS    | TCP       | 443  |
| DNS      | UDP       | 53   |
| SSH      | TCP       | 22   |

#### Subnet detection — AND operation

```
PC-E (192.168.20.3) pings 192.168.10.1

Step 1: 192.168.20.3 AND 255.255.255.0 = 192.168.20.0  (own network)
Step 2: 192.168.10.1 AND 255.255.255.0 = 192.168.10.0  (target network)
Step 3: 192.168.20.0 ≠ 192.168.10.0 → DIFFERENT network → send to default gateway
```

> PC ANDs both IPs with its own subnet mask. Same result = same network. Different = via router.

***

### Exam traps — what loses marks

| Trap                | Wrong               | Correct                                                       |
| ------------------- | ------------------- | ------------------------------------------------------------- |
| Ping = request only | Trace only outgoing | Trace **request AND reply** — both directions build MAC table |
| Switch terminology  | "broadcast"         | "**flood**" — unknown unicast flooding                        |
| Dest MAC off-subnet | Final host MAC      | **Router MAC** (gateway facing your subnet)                   |
| IP addresses        | Change at router    | **Never change** end to end                                   |
| MAC table column    | "Switch 3"          | Actual **MAC address**                                        |
| Proxy ARP           | Not mentioned       | Always state "**Proxy ARP disabled**" as assumption           |
| Switches have MAC   | They don't          | Switches are transparent — no MAC in table                    |

***

### Part (a) — MAC table trace \[10 marks]

**Exact question — 2023 \[Q1]\[A]:**

> ⚠️ Image in question — refer to 2023 \[Q1] topology diagram
>
> Assume all MAC address tables and ARP caches are empty. PC-A successfully pings PC-C. Next, PC-A successfully pings PC-D. Provide the resulting updated MAC address table contents for switches S1, S2 and S3. Document any assumptions. Note: A successful ping is one that receives a reply.

**Other years — same format, different pings:**

* 2024 \[Q1]\[A]: PC-B pings 192.168.10.255, then PC-B pings PC-F, then PC-D pings PC-A
* 2025 \[Q1]\[A]: PC-E pings 192.168.20.255, then PC-F pings PC-D, then R1 pings PC-F
* 2025R \[Q1]\[A]: PC-D pings PC-A, then PC-E pings PC-A

#### How to approach — step by step:

**Step 1** — Read topology, note which PC is on which subnet and port

**Step 2** — For each ping: same subnet or different subnet?

* Same subnet → ARP within subnet, no router
* Different subnet → ARP to router gateway first

**Step 3** — Trace ARP request (broadcast → all switches on path learn source MAC)

**Step 4** — Trace ARP reply (unicast → switches learn reply source MAC)

**Step 5** — Trace ICMP request + reply (same path)

**Step 6** — Fill S1, S2, S3 tables — only record what each switch saw

**Step 7** — Document assumptions:

* All ARP caches and MAC tables start empty
* Proxy ARP disabled on R1
* Successful ping = request + reply (both directions)

***

### Part (b) — Switch reaction \[4–5 marks]

**Exact question — 2023 \[Q1]\[B]:**

> ⚠️ Image in question — partial MAC table given in question
>
> Given above are the contents of S3's MAC address table when a ping arrives at S3 from PC-C:
>
> * Source MAC: CCCC, Destination MAC: BBBB
> * Source IP: 192.168.20.2, Destination IP: 192.168.20.1
>
> How does S3 react? Explain your reasoning.

#### How to approach — step by step:

**Step 1** — Learn: record source MAC → incoming port in table

**Step 2** — Look up destination MAC in table

**Step 3** — Known? → forward to that port. Unknown? → flood all except incoming port

#### Model answer:

**Step 1 — Learn:** S3 records CCCC → Fa0/2 (source MAC → incoming port)

**Step 2 — Forward:** S3 looks up BBBB in table. No entry found. S3 **floods** the frame out all ports except Fa0/2 (i.e. Fa0/1, Fa0/3, Fa0/4).

***

### Part (b) — Encapsulation diagram \[6 marks]

**Exact question — 2025 \[Q1]\[B]:**

> Assume a user logged into PC-F, through their web browser, issues a successful request for: http://192.168.10.1/index.html With reference to TCP/IP model, provide:
>
> * A diagram depicting how the request has been encapsulated when it reaches the physical layer on PC-F
> * The addressing information (if any) added at each layer

**2025R \[Q1]\[B] variant — dig command:**

> Assume a user logged into PC-B, runs the following command in their terminal: dig @192.168.20.1 www.cisco.com With reference to TCP/IP model, provide:
>
> * A diagram depicting how the request has been encapsulated at the physical layer
> * The addressing information (if any) added at each layer

#### How to approach — step by step:

**Step 1** — Identify protocol: HTTP → TCP port 80. DNS (dig) → UDP port 53

**Step 2** — Identify src/dst IPs from the topology

**Step 3** — Identify dst MAC: same subnet? → dst MAC = destination host. Different subnet? → dst MAC = router (FAFA)

**Step 4** — Draw 5 layers with addressing at each

#### Model answer — HTTP (2025 \[Q1]\[B]):

```
Layer           PDU         Addressing
Application     HTTP GET    /index.html
Transport       Segment     Src: ephemeral   Dst: 80 (TCP)
Network         Packet      Src: 192.168.20.4   Dst: 192.168.10.1
Data Link       Frame       Src MAC: FFFF    Dst MAC: FAFA (router — diff subnet)
Physical        Bits        010101...
```

> Destination MAC = FAFA (router), NOT the web server's MAC — because PC-F and web server are on different subnets.

***

### Part (c) — Src/dst MAC across hops \[4–5 marks]

**Exact question — 2023 \[Q1]\[C]:**

> PC-D sends an ICMP echo (ping) request to PC-A. Provide the packet's source MAC, destination MAC, source IP, destination IP as it travels: i. Along the link from S3 to S2 ii. Along the link from S1 to PC-A

**2024 \[Q1]\[C] and 2025R \[Q1]\[C] variant:**

> PC-A sends an ICMP echo (ping) request to PC-F. Provide the packet's source MAC, destination MAC, source IP, destination IP as it travels: i. Along the link from S1 to R1 ii. Along the link from S3 to PC-F

#### How to approach — step by step:

**Step 1** — Identify sender and receiver — who is src IP, who is dst IP? They never change.

**Step 2** — For each link, ask: has traffic crossed a router yet?

* Before router → src MAC = sender's MAC, dst MAC = router's MAC (FAFA or FBFB)
* After router → src MAC = router's outgoing interface MAC, dst MAC = final destination MAC

**Step 3** — Fill in the 4 values for each link

#### Model answer — PC-D pings PC-A (2023 \[Q1]\[C]):

| Link      | Src MAC | Dst MAC | Src IP       | Dst IP       |
| --------- | ------- | ------- | ------------ | ------------ |
| S3 → S2   | DDDD    | FAFA    | 192.168.20.3 | 192.168.10.1 |
| S1 → PC-A | FBFB    | AAAA    | 192.168.20.3 | 192.168.10.1 |

***

### Part (c) — Subnet detection \[4 marks]

**Exact question — 2025 \[Q1]\[C]:**

> Assume a user logged into PC-E pings address 192.168.20.1. Describe the steps followed by PC-E in determining whether the target host resides on the same LAN as PC-E or on a different network.

#### How to approach — step by step:

**Step 1** — State that PC-E applies its own subnet mask to both IPs using bitwise AND

**Step 2** — Show the AND calculation for own IP

**Step 3** — Show the AND calculation for destination IP

**Step 4** — Compare results → same = direct ARP, different = send to gateway

#### Model answer:

PC-E applies its subnet mask (255.255.255.0) to both IPs using a bitwise AND:

```
192.168.20.3 AND 255.255.255.0 = 192.168.20.0  (PC-E's network)
192.168.20.1 AND 255.255.255.0 = 192.168.20.0  (target's network)
Results match → same network → PC-E ARPs for 192.168.20.1 directly
```

***

### Part (c) — Jimmy's MAC routing \[5 marks]

**Exact question — 2023R \[Q1]\[C]:**

> Jimmy has an idea. Since MAC addresses are unique, he proposes abandoning IP addresses and routing Internet traffic solely based on MAC addresses. Is Jimmy's idea practical? Provide evidence to support your opinion.

#### How to approach — step by step:

**Step 1** — State your answer clearly: No, not practical

**Step 2** — Give the core reason: MAC addresses are flat and non-hierarchical

**Step 3** — Explain implications: routing table explosion, no aggregation possible

**Step 4** — Add supporting points (2–3 more reasons)

#### Model answer:

1. **MAC addresses are flat** — no hierarchical structure, cannot be summarised or aggregated
2. **Routing table explosion** — every router would need an entry for every device on the Internet (billions of entries)
3. **No subnet structure** — impossible to group devices geographically or by network
4. **MACs can be changed** — spoofed via software, unreliable as global identifier
5. **No geographic routing** — IP addresses encode location via ISP allocation; MAC addresses do not

> Core argument: MAC addressing is flat and non-hierarchical — Internet-scale routing is impossible without aggregation.

***

### Weak spots flagged — Mohan's mistakes this session

1. Wrote "Switch 3" in MAC table column instead of actual MAC address
2. Said "broadcast" instead of "flood" for unknown unicast
3. Swapped src/dst IP in the MAC-across-hops question (PC-D was sender, not PC-A)
4. Used port 80 for DNS — DNS uses **UDP port 53**
5. Forgot to mention proxy ARP disabled as an assumption

