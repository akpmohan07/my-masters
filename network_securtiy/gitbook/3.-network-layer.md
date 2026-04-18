# Network Layer

{% embed url="https://chatgpt.com/g/g-p-69653dfc24f08191a76360c88807a286-network-security/c/69addfee-1ebc-838d-9f6f-6fade6b19bc9" %}

Below is a **clean exam-ready summary of the Network Layer topics you covered** (IPv4, routing, MAC/IP traversal, DHCP, fragmentation, MTU, etc.). I organized it so you can **revise quickly before an exam**.

***

## 1. Network Layer Overview

The **Network Layer (Layer 3)** is responsible for **moving packets from source host to destination host across multiple networks**.

Main responsibilities:

* Logical addressing (**IP addresses**)
* **Routing** (finding a path)
* **Forwarding** (sending packets to next hop)
* **Fragmentation**
* **Error reporting (ICMP)**

***

## 2. Routing vs Forwarding

#### Routing

Routing is the process of **building routing tables using routing algorithms**.

Example:

```
Router learns routes using:
- OSPF
- RIP
- BGP
```

#### Forwarding

Forwarding is the **actual movement of packets**.

Router checks routing table:

```
Destination IP → Next hop
```

Summary:

| Concept    | Meaning                        |
| ---------- | ------------------------------ |
| Routing    | Builds routing tables          |
| Forwarding | Uses the table to send packets |

***

## 3. Longest Prefix Matching

Routers use **longest prefix matching** to choose the best route.

Example routing table:

| Network          | Route   |
| ---------------- | ------- |
| 136.206.0.0/16   | Route A |
| 136.206.115.0/24 | Route B |

Packet arrives:

```
136.206.115.23
```

Matches both routes, but:

```
/24 > /16
```

So router chooses:

```
136.206.115.0/24
```

Rule:

```
Most specific prefix wins
```

***

## 4. IPv4 Address Structure

IPv4 address size:

```
32 bits
```

Structure:

```
Network part + Host part
```

Example:

```
192.168.1.10
```

```
Network = 192.168.1
Host = 10
```

***

## 5. IPv4 Classes (Legacy System)

| Class | Range   | CIDR | Mask          | Hosts        |
| ----- | ------- | ---- | ------------- | ------------ |
| A     | 0–127   | /8   | 255.0.0.0     | 16M          |
| B     | 128–191 | /16  | 255.255.0.0   | 65K          |
| C     | 192–223 | /24  | 255.255.255.0 | 254          |
| D     | 224–239 | —    | —             | Multicast    |
| E     | 240–255 | —    | —             | Experimental |

Memory trick:

```
/8 → /16 → /24
```

***

## 6. Important IPv4 Special Addresses

| Address                   | Meaning         |
| ------------------------- | --------------- |
| 127.0.0.0/8               | Loopback        |
| 10.0.0.0/8                | Private network |
| 172.16.0.0–172.31.255.255 | Private network |
| 192.168.0.0/16            | Private network |
| 255.255.255.255           | Broadcast       |

***

## 7. IPv4 Header

Minimum size:

```
20 bytes
```

Maximum size:

```
60 bytes
```

Because of the **IHL field**.

#### IPv4 Header Fields

```
+-----------------------------+
| Version | IHL | Type of Service |
+-----------------------------+
| Total Length |
+-----------------------------+
| Identification |
+-----------------------------+
| Flags | Fragment Offset |
+-----------------------------+
| TTL |
+-----------------------------+
| Protocol |
+-----------------------------+
| Header Checksum |
+-----------------------------+
| Source IP |
+-----------------------------+
| Destination IP |
+-----------------------------+
| Options (optional) |
+-----------------------------+
| Data |
+-----------------------------+
```

***

## 8. Fragmentation

Fragmentation occurs when:

```
Packet size > MTU
```

MTU = Maximum Transmission Unit.

Example:

```
Ethernet MTU = 1500 bytes
```

Fragmentation fields:

| Field           | Purpose                  |
| --------------- | ------------------------ |
| Identification  | identifies fragments     |
| More Fragments  | indicates more fragments |
| Fragment Offset | position of fragment     |

Fragments are **reassembled only at destination**.

***

## 9. Fragmentation Problems

1. Extra work for routers
2. Extra work for destination
3. If one fragment is lost → resend entire packet
4. Security attacks can hide in fragments

***

## 10. Path MTU Discovery

Path MTU discovery avoids fragmentation.

Steps:

1. Sender sends packet with **DF flag**
2. Router cannot fragment
3. Router sends ICMP message
4. Sender reduces packet size

Diagram:

```
Sender
  |
1500 byte packet (DF=1)
  |
Router (MTU 1400)
  |
ICMP: Fragmentation needed
  |
Sender retries with 1400
```

***

## 11. ICMP (Internet Control Message Protocol)

Used for **network diagnostics and errors**.

Examples:

| Type                    | Purpose         |
| ----------------------- | --------------- |
| Echo Request            | ping            |
| Echo Reply              | ping response   |
| Destination unreachable | routing problem |
| TTL exceeded            | traceroute      |

ICMP operates in:

```
Network Layer (Layer 3)
```

***

## 12. TTL (Time To Live)

TTL prevents packets from looping forever.

Process:

```
Router receives packet
TTL = TTL - 1
```

If:

```
TTL = 0
```

Router drops packet and sends:

```
ICMP TTL exceeded
```

***

## 13. Traceroute

Traceroute discovers the path to a destination.

Process:

```
TTL = 1
TTL = 2
TTL = 3
```

Each router sends:

```
ICMP TTL exceeded
```

This reveals the route.

***

## 14. MAC vs IP When Routing

Important rule:

```
IP addresses stay the same
MAC addresses change at every hop
```

Example network:

```
PC-A ---- Switch ---- Router ---- Switch ---- PC-C
```

Packet fields:

| Hop           | Source MAC | Destination MAC |
| ------------- | ---------- | --------------- |
| PC-A → Router | PC-A       | Router          |
| Router → PC-C | Router     | PC-C            |

IP addresses remain:

```
Source IP = PC-A
Destination IP = PC-C
```

***

## 15. Switch vs Router

| Device | Layer   | Function                 |
| ------ | ------- | ------------------------ |
| Switch | Layer 2 | Forward frames using MAC |
| Router | Layer 3 | Forward packets using IP |

Switch:

```
Does NOT change MAC
```

Router:

```
Creates new Ethernet frame
```

***

## 16. DHCP (Dynamic Host Configuration Protocol)

DHCP automatically assigns:

* IP address
* Subnet mask
* Default gateway
* DNS server

***

## 17. DHCP DORA Process

```
Client                Server
  |                      |
  |---- Discover ------->|
  |                      |
  |<---- Offer ----------|
  |                      |
  |---- Request -------->|
  |                      |
  |<---- ACK ------------|
```

DORA =

```
Discover
Offer
Request
Acknowledge
```

***

## DHCP Packet Behaviour

| Message  | Destination          |
| -------- | -------------------- |
| Discover | Broadcast            |
| Offer    | Usually broadcast    |
| Request  | Broadcast            |
| ACK      | Broadcast or Unicast |

Broadcast addresses:

```
MAC = FF:FF:FF:FF:FF:FF
IP  = 255.255.255.255
```

***

## 18. DHCP Example Frame

```
Source MAC: DHCP Server
Destination MAC: FF:FF:FF:FF:FF:FF
Source IP: DHCP Server IP
Destination IP: 255.255.255.255
```

Broadcast ensures the client receives the offer.

***

## 19. Quick Exam Rules

#### Rule 1

```
MAC changes at every router
```

#### Rule 2

```
IP addresses stay constant
```

#### Rule 3

```
Switch = Layer 2
Router = Layer 3
```

#### Rule 4

```
Longest prefix match decides route
```

#### Rule 5

```
Fragmentation occurs if packet > MTU
```

***

## Final Concept Map

```
Network Layer
│
├─ IPv4 Addressing
│   ├─ Classes
│   ├─ CIDR
│   └─ Subnet masks
│
├─ Routing
│   ├─ Routing tables
│   └─ Longest prefix match
│
├─ Packet Handling
│   ├─ TTL
│   ├─ Fragmentation
│   └─ Path MTU discovery
│
├─ ICMP
│   ├─ Ping
│   └─ Traceroute
│
└─ DHCP
    └─ DORA process
```

***

✅ If you want, I can also create a **one-page “Network Layer exam cheat sheet”** that includes **all formulas, tricks, and diagrams used in networking exams**. It will make revision much faster.
