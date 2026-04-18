# Ethernet, MAC Tables, ARP, and Multicast Mapping

{% embed url="https://chatgpt.com/g/g-p-69653dfc24f08191a76360c88807a286-network-security/c/69adc6eb-5b0c-838e-b882-bd70b31f847a" %}

Below is a **complete, clean study summary** of everything we discussed in this chat. It focuses on **Ethernet, MAC addresses, switches, ARP, multicast mapping, and related link-layer concepts**. I organized it in a way that is good for **revision before exams**.

***

## Networking Summary

### Ethernet, MAC Learning, ARP, and Multicast Mapping

***

## 1. Network Layers (Context)

Computer networking uses a **layered architecture** to manage complexity.

#### TCP/IP Model

| Layer       | PDU     | Example   |
| ----------- | ------- | --------- |
| Application | Message | HTTP, DNS |
| Transport   | Segment | TCP, UDP  |
| Network     | Packet  | IP        |
| Data Link   | Frame   | Ethernet  |
| Physical    | Bit     | Signals   |

Each layer provides a **service to the layer above**.

Example flow:

```
Application Data
      ↓
TCP/UDP Segment
      ↓
IP Packet
      ↓
Ethernet Frame
      ↓
Bits on cable
```

***

## 2. Data Link Layer (Layer 2)

#### Main responsibility

Move data **from one node to the next node on the same link**.

Example path:

```
Host → Switch → Router → Switch → Host
```

Each step is called a **hop**.

#### Services provided

1. **Framing**
2. **Link access**
3. **Error detection**
4. **Flow control**
5. **Reliable delivery (sometimes)**

The network layer determines the route, but the **data link layer delivers frames over each hop**.

***

## 3. Ethernet

Ethernet is the **dominant wired LAN technology**.

Standard: **IEEE 802.3**

#### Ethernet Frame Format

```
+-----------+--------------+-------------+------+------+
| Preamble  | Destination  | Source MAC  |Type  | Data |
| 8 bytes   | 6 bytes      | 6 bytes     |2B    |      |
+-----------+--------------+-------------+------+------+
| CRC |
|4 B |
+----+
```

#### Fields

| Field           | Purpose                    |
| --------------- | -------------------------- |
| Preamble        | Synchronization            |
| Destination MAC | Receiver address           |
| Source MAC      | Sender address             |
| Type            | Protocol (IPv4, IPv6, ARP) |
| Data            | Payload                    |
| CRC             | Error detection            |

Ethernet is **unreliable**:

* No acknowledgements
* Corrupted frames are **dropped**.

***

## 4. MAC Addresses

#### Definition

A **MAC address** is the physical address of a network interface.

Length:

```
48 bits (6 bytes)
```

Example

```
88-B2-2F-54-1A-0F
```

#### Structure

```
| 24 bits | 24 bits |
| Vendor  | Device  |
```

* First 24 bits → manufacturer
* Last 24 bits → unique device ID

The IEEE assigns address blocks to manufacturers.

***

## 5. Types of MAC Addresses

#### 1. Unicast

One device → one device

Example

```
00:1A:2B:3C:4D:5E
```

***

#### 2. Broadcast

All devices receive the frame.

Address:

```
FF:FF:FF:FF:FF:FF
```

Used by **ARP requests**.

***

#### 3. Multicast

One device → group of devices

Example prefix:

```
01:00:5E:XX:XX:XX
```

***

## 6. Switch Operation

A **switch operates at the Data Link layer**.

It forwards frames using **MAC addresses**.

Switches maintain a **MAC Address Table**.

***

## 7. MAC Address Table

Example

| MAC | Port  |
| --- | ----- |
| AA  | Fa0/1 |
| BB  | Fa0/2 |
| CC  | Fa0/3 |
| DD  | Fa0/4 |

***

#### How the switch learns addresses

When a frame arrives:

```
Frame received on port Fa0/1
Source MAC = AA
Destination MAC = EE
```

Step 1 — Learn source

```
AA → Fa0/1
```

Step 2 — Check destination

If destination exists → forward

If unknown → flood.

***

#### Unknown Unicast Flooding

If the switch **does not know the destination MAC**:

```
Send frame to all ports except incoming port
```

Example

```
Fa0/1 → incoming

Switch sends to:
Fa0/2
Fa0/3
Fa0/4
```

***

## 8. Switch Port MAC Addresses

Statement:

**“Each switch port has zero or one associated MAC addresses.”**

❌ False

A port may have **many MAC addresses** because:

* Multiple devices may connect via a hub
* Virtual machines may exist

Example

```
Port Fa0/1
 ├ Device A
 ├ Device B
 └ Device C
```

Switch table

```
A → Fa0/1
B → Fa0/1
C → Fa0/1
```

***

## 9. ARP (Address Resolution Protocol)

Problem:

IP uses **IP addresses**, but Ethernet needs **MAC addresses**.

Solution:

ARP converts:

```
IP address → MAC address
```

ARP operates within the **same subnet**.

***

## 10. ARP Table

Every host keeps an **ARP table**.

Example

| IP Address  | MAC Address       |
| ----------- | ----------------- |
| 192.168.1.1 | 00:AA:BB:CC:DD:EE |
| 192.168.1.5 | 22:33:44:55:66:77 |

Entries expire after some time.

***

## 11. ARP Process

#### Step 1 — Host needs MAC address

Example

```
Send packet to
IP = 192.168.1.5
```

But MAC unknown.

***

#### Step 2 — ARP Request

Broadcast to network.

```
Who has 192.168.1.5 ?
```

Destination MAC:

```
FF:FF:FF:FF:FF:FF
```

Diagram

```
Host A
  |
  |  ARP Request (broadcast)
  v
Switch → all hosts
```

***

#### Step 3 — ARP Reply

The correct device responds.

```
192.168.1.5 is at 22:33:44:55:66:77
```

Diagram

```
Host B → Host A
(unicast reply)
```

***

#### Step 4 — Table updated

```
192.168.1.5 → 22:33:44:55:66:77
```

Host can now send frames.

***

## 12. ARP Table Full

If ARP table becomes full:

Possible results

* Old entries are removed
* New ARP requests may fail
* Network communication may fail

Usually entries expire automatically.

***

## 13. IPv4 Multicast

IPv4 multicast range

```
224.0.0.0 → 239.255.255.255
```

Binary prefix:

```
1110
```

***

#### Why 28 variable bits?

IPv4 address = 32 bits

First 4 bits fixed:

```
1110
```

So:

```
32 - 4 = 28 variable bits
```

***

## 14. Mapping IPv4 Multicast to Ethernet

IPv4 multicast → Ethernet multicast

But Ethernet only uses **23 bits**.

```
IPv4 variable bits = 28
Ethernet bits = 23
```

Lost bits

```
28 - 23 = 5
```

***

#### Number of addresses mapping to same MAC

```
2^5 = 32
```

So:

**32 IPv4 multicast addresses map to the same Ethernet MAC.**

***

## 15. Example Conversion

IPv4

```
224.0.0.254
```

Binary

```
224 = 11100000
0   = 00000000
0   = 00000000
254 = 11111110
```

Result

```
11100000.00000000.00000000.11111110
```

***

## 16. IPv4 Multicast → Ethernet Multicast Mapping

Ethernet multicast prefix:

```
01:00:5E
```

Example mapping

```
IPv4 multicast
↓
01:00:5E:XX:XX:XX
```

Only **23 bits of the IPv4 address are used**.

***

## 17. Key Rules to Remember

#### Rule 1

Switches learn addresses from **source MAC**.

#### Rule 2

Unknown destination → **flood**.

#### Rule 3

ARP converts

```
IP → MAC
```

#### Rule 4

Broadcast MAC

```
FF:FF:FF:FF:FF:FF
```

#### Rule 5

Multicast prefix

```
01:00:5E
```

***

## 18. Full Communication Example

```
Host A → Host B
```

Step-by-step

```
1. Host A checks ARP table
2. MAC not found
3. Send ARP request (broadcast)
4. Host B replies
5. Host A stores MAC
6. Ethernet frame sent
7. Switch forwards frame
8. Host B receives frame
```

Diagram

```
Host A
  |
  | ARP Request (broadcast)
  v
Switch → All devices

Host B
  |
  | ARP Reply
  v
Host A

Then:
Host A → Switch → Host B
```

***

## Final Concept Map

```
Application Layer
      ↓
Transport Layer
      ↓
Network Layer (IP)
      ↓
Data Link Layer (Ethernet)
      ↓
MAC Addressing
      ↓
Switch Learning
      ↓
ARP (IP → MAC)
      ↓
Frame Forwarding
```

***

If you want, I can also create a **SUPER EXAM CHEAT SHEET (1 page)** for this topic.\
It will include **all formulas, rules, and diagrams professors usually test.**
