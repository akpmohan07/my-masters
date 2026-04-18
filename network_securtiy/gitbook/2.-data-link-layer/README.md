# Data Link layer

{% embed url="https://chatgpt.com/g/g-p-69653dfc24f08191a76360c88807a286-network-security/c/69a93c17-0628-838f-a154-767eb3d53091?tab=sources" %}

## Switched Local Area Networks (LANs), Ethernet, ARP, and Network Devices

_(Expanded revision notes following the logical flow of the chapter, with text diagrams to help recall concepts during exams.)_

***

## 1. Link-Layer Addressing and ARP

### 1. Core Concept

In a Local Area Network (LAN), devices communicate using **MAC (Media Access Control) addresses**, which operate at the **data link layer (Layer 2)**. However, higher layers such as the **network layer** use **IP addresses**. Therefore, before a device can send an Ethernet frame to another device in the LAN, it must determine the **MAC address corresponding to the destination IP address**.

This translation is performed by the **Address Resolution Protocol (ARP)**.

***

### 2. Why This Exists / Motivation

Networking involves multiple protocol layers, and each layer uses its own addressing scheme.

Example problem:

* Network layer uses **IP addresses**
* Ethernet frame requires **MAC addresses**

So when Host A wants to send an IP packet to Host B, it must answer:

> “What MAC address corresponds to this IP address?”

Without this mapping, the frame cannot be delivered on the LAN.

ARP solves this problem by dynamically mapping IP addresses to MAC addresses.

***

### 3. MAC Addressing

A **MAC address** is a **48-bit unique identifier** assigned to every network interface card (NIC).

Example MAC address:

```
88:B2:2F:54:1A:0F
```

Breakdown:

```
| Manufacturer ID | Device ID |
|     24 bits     |  24 bits  |
```

Explanation:

* The first 24 bits identify the **manufacturer** (OUI).
* The last 24 bits uniquely identify the **device produced by that manufacturer**.

#### Key Property: Flat Addressing

MAC addresses are called **flat addresses** because they do **not encode network location**.

Example:

```
MAC address: AA:34:9C:2F:11:8B
```

From this address we **cannot determine:**

* which network it belongs to
* which building it is in
* which router connects to it

In contrast, **IP addresses are hierarchical**:

```
IP address: 192.168.1.15
            | network | host |
```

Routers can use this hierarchy to route packets efficiently.

***

### 4. ARP (Address Resolution Protocol)

ARP is used to resolve **IP → MAC address mappings** inside a LAN.

#### ARP Operation

Step-by-step process:

1. Host A wants to send a packet to IP `192.168.1.5`.
2. Host A checks its **ARP table**.
3. If the MAC address is unknown, Host A sends a **broadcast ARP request**.

Text diagram:

```
Host A broadcasts:
"Who has IP 192.168.1.5?"

          Broadcast
    ------------------------
    |        |        |    |
  Host B   Host C   Router Host D
```

4. The device with that IP responds.

```
Host B → Host A
"I am 192.168.1.5
My MAC address is 88:B2:2F:54:1A:0F"
```

5. Host A stores the mapping in its **ARP table**.

Example ARP table:

```
IP Address        MAC Address
---------------------------------
192.168.1.1  →  98:3B:67:7C:94:7B
192.168.1.14 →  52:30:2E:3D:44:49
```

Entries expire after some time to keep the table updated.

***

## 2. Sending a Datagram Off the Subnet

### 1. Core Concept

If a host wants to send data to a device **outside its subnet**, it cannot send the frame directly to that device. Instead, it must send the frame to the **router (default gateway)**.

***

### 2. How a Host Detects the Destination Network

The host uses the **subnet mask** to determine whether the destination is local.

Example:

```
Host IP:        111.111.111.111
Subnet mask:    /24
Network:        111.111.111.0
```

Destination:

```
222.222.222.222
```

Different network → must use router.

***

### 3. Transmission Process

Step-by-step:

1. Host creates an IP datagram.
2. Host determines destination is outside subnet.
3. Host uses ARP to find **router MAC address**.
4. Host sends frame to router.

Text diagram:

```
Host A
  |
  | Ethernet Frame
  v
Switch ----> Router ----> Destination Network
                |
                v
             Host B
```

Important rule:

| Address Type | Role              |
| ------------ | ----------------- |
| IP Address   | Final destination |
| MAC Address  | Next hop          |

MAC addresses change at each hop.

***

## 3. Ethernet

### 1. Core Concept

Ethernet is the most widely used **wired LAN technology**. It defines how frames are transmitted over a LAN using **MAC addresses**.

It was standardized by the **Institute of Electrical and Electronics Engineers** under the **IEEE 802.3** specification.

***

### 2. Evolution of Ethernet

Ethernet speeds evolved dramatically:

```
10 Mbps   → Early Ethernet
100 Mbps  → Fast Ethernet
1 Gbps    → Gigabit Ethernet
10 Gbps+  → Modern Ethernet
```

Competing technologies once included:

* Token Ring
* FDDI
* ATM

However, Ethernet became dominant due to:

* simplicity
* lower cost
* easier installation
* scalability

***

### 3. Ethernet Frame Structure

An Ethernet frame encapsulates data from the network layer.

```
+----------+-------------+-----------+-------+----------+------+
| Preamble | Dest MAC    | Src MAC   | Type  | Data     | CRC  |
| 8 bytes  | 6 bytes     | 6 bytes   | 2 B   | 46-1500B | 4 B  |
+----------+-------------+-----------+-------+----------+------+
```

Explanation of fields:

| Field           | Purpose                   |
| --------------- | ------------------------- |
| Preamble        | Synchronizes clocks       |
| Destination MAC | Receiver address          |
| Source MAC      | Sender address            |
| Type            | Identifies upper protocol |
| Data            | Payload                   |
| CRC             | Error detection           |

***

## 4. Error Detection – CRC

### 1. Core Concept

Ethernet uses **Cyclic Redundancy Check (CRC)** to detect transmission errors.

CRC stands for:

| Term       | Meaning                           |
| ---------- | --------------------------------- |
| Cyclic     | Uses cyclic polynomial operations |
| Redundancy | Extra bits added                  |
| Check      | Receiver verifies correctness     |

***

### 2. CRC Process

```
Sender
  |
  | Calculate CRC
  v
Frame + CRC
  |
  v
Transmission
  |
  v
Receiver recalculates CRC
  |
  +-- Match → Accept frame
  |
  +-- Mismatch → Drop frame
```

Ethernet uses **CRC-32 (32 bits)**.

CRC detects:

* single-bit errors
* burst errors
* multiple bit errors

***

## 5. Ethernet Switching

### 1. Core Concept

A **switch** is a device that forwards Ethernet frames based on **MAC addresses**.

Switches operate at **Layer 2**.

***

### 2. Switch Learning

Switches automatically learn which MAC address is connected to which port.

Example table:

```
MAC Address          Port
---------------------------
AA-AA-AA-AA-AA-AA →   1
BB-BB-BB-BB-BB-BB →   2
CC-CC-CC-CC-CC-CC →   3
```

Learning process:

```
Frame arrives
      |
Switch records:
Source MAC → Incoming port
```

***

### 3. Frame Forwarding Cases

When a frame arrives, the switch checks its table.

#### Case 1 – Known destination

```
Switch table contains MAC
→ Forward frame to correct port
```

Diagram:

```
Host A → Switch → Host B
```

***

#### Case 2 – Unknown destination

Switch floods frame.

```
Switch sends frame to all ports except incoming
```

Diagram:

```
        Host B
          |
Host A → Switch → Host C
          |
        Host D
```

***

#### Case 3 – Same port

If destination is on the same port:

```
Frame is filtered (dropped)
```

Example scenario:

```
Host A --- Hub --- Host B
        |
      Switch Port 1
```

Both hosts already share the same segment.

***

## 6. Collision-Free Ethernet

Early Ethernet used **shared cables**, which caused collisions.

Example (hub network):

```
PC1
   \
    Hub --- PC2
   /
 PC3
```

If two PCs transmit simultaneously → **collision occurs**.

Modern Ethernet uses **switches**.

```
PC1 ──\
PC2 ─── Switch ─── PC3
PC4 ──/
```

Each device has a **dedicated link**.

Therefore:

```
No shared medium
→ No collisions
```

***

## 7. Duplex Communication

Duplex refers to communication direction.

| Type        | Explanation                            |
| ----------- | -------------------------------------- |
| Simplex     | One-way communication                  |
| Half-duplex | Both directions but not simultaneously |
| Full-duplex | Both directions simultaneously         |

Example comparison:

Walkie-talkie (half-duplex):

```
A → B
(wait)
B → A
```

Telephone (full-duplex):

```
A ⇄ B
```

Modern switches support **full-duplex communication**, eliminating collisions.

***

## 8. Switches vs Routers

### Switch

Operates at **Layer 2**.

Advantages:

* Plug-and-play
* High forwarding speed
* Simple operation

Disadvantages:

* Broadcast storms possible
* ARP traffic increases in large LANs
* Spanning tree restrictions

***

### Router

Operates at **Layer 3**.

Advantages:

* Supports hierarchical addressing
* Blocks broadcast traffic
* Multiple routing paths

Disadvantages:

* Slower processing
* Requires configuration

***

### Comparison

| Feature | Switch | Router        |
| ------- | ------ | ------------- |
| Layer   | 2      | 3             |
| Address | MAC    | IP            |
| Scope   | LAN    | Inter-network |

***

## Condensed 1-Page Revision Summary

#### MAC Address

```
48-bit hardware address
Flat addressing
Used inside LAN
```

#### ARP

```
Maps IP → MAC
Uses broadcast requests
```

#### Ethernet Frame

```
Preamble | Dest MAC | Src MAC | Type | Data | CRC
```

#### CRC

```
Error detection using polynomial division
Ethernet uses CRC-32
```

#### Switching

```
Learn MAC addresses
Forward frames
Flood unknown addresses
Filter same-port traffic
```

#### Datagram Off Subnet

```
Host → Router → Destination
IP = final destination
MAC = next hop
```

#### Duplex

```
Full-duplex eliminates collisions
```

#### Switch vs Router

| Switch      | Router          |
| ----------- | --------------- |
| Layer 2     | Layer 3         |
| MAC         | IP              |
| LAN traffic | Network routing |

***

