# Revision

Great idea. A **good exam guide** should have **definitions + small diagrams + key bits to memorise**. I rebuilt the guide so it reads like **lecture revision notes**, not just keywords.

***

## Network Security Exam Preparation Guide

_(Weeks 1, 2, 3, 5, 6)_

***

## Week 1 — Network Models & Layered Architecture

### What is a Layered Architecture?

**Definition**

A layered architecture divides networking functionality into **separate layers**, where each layer provides a **service** to the layer above it and hides implementation details.

Purpose:

* Manage complexity
* Allow protocol upgrades
* Standardise communication

***

### OSI Model (Conceptual Model)

```
+---------------------+
| 7 Application       |
+---------------------+
| 6 Presentation      |
+---------------------+
| 5 Session           |
+---------------------+
| 4 Transport         |
+---------------------+
| 3 Network           |
+---------------------+
| 2 Data Link         |
+---------------------+
| 1 Physical          |
+---------------------+
```

#### Key Services

| Layer       | Purpose                           |
| ----------- | --------------------------------- |
| Application | Network services for applications |
| Transport   | Process-to-process communication  |
| Network     | Routing packets across networks   |
| Data Link   | Hop-to-hop delivery               |
| Physical    | Transmission of bits              |

***

### TCP/IP Model (Used in the Internet)

```
+--------------------+
| Application        |
+--------------------+
| Transport          |
+--------------------+
| Network            |
+--------------------+
| Network Access     |
+--------------------+
```

Mapping:

| OSI          | TCP/IP         |
| ------------ | -------------- |
| Application  | Application    |
| Presentation | Application    |
| Session      | Application    |
| Transport    | Transport      |
| Network      | Network        |
| Data Link    | Network Access |
| Physical     | Network Access |

***

### PDU Names (Very Common Exam Question)

| Layer       | PDU     |
| ----------- | ------- |
| Application | Message |
| Transport   | Segment |
| Network     | Packet  |
| Data Link   | Frame   |
| Physical    | Bit     |

***

### TCP Connection

**Definition**

TCP is a **connection-oriented transport protocol** that provides:

* reliable delivery
* ordered data
* congestion control

#### TCP 3-Way Handshake

```
Client                     Server
   | ---- SYN -----------> |
   | <--- SYN-ACK -------- |
   | ---- ACK -----------> |
```

Purpose:

* establish connection
* synchronise sequence numbers

***

## Week 2 — Data Link Layer

### Data Link Layer Definition

The data link layer is responsible for **moving frames across a single link** between two directly connected nodes.

Responsibilities include:

* framing
* MAC addressing
* error detection
* link access control

***

## Ethernet Frame

#### Structure

```
+----------+-------------+------------+------+--------+------+
| Preamble | Destination | Source MAC | Type |  Data  | CRC  |
+----------+-------------+------------+------+--------+------+
```

| Field           | Purpose                |
| --------------- | ---------------------- |
| Preamble        | Synchronises receivers |
| Destination MAC | Target device          |
| Source MAC      | Sender device          |
| Type            | Payload protocol       |
| Data            | Encapsulated packet    |
| CRC             | Error detection        |

***

## MAC Address

#### Definition

A **MAC address** is a **48-bit physical address** assigned to a network interface card.

Example:

```
7e:6a:a1:8c:a1:14
```

Structure:

```
+--------------------+------------------+
| OUI                | Device ID        |
| (Manufacturer)     | Unique Interface |
| 24 bits            | 24 bits          |
+--------------------+------------------+
```

***

## MAC Address Flags (Bit Meaning)

Example:

```
7E = 01111110
```

The **last two bits of the first byte** indicate special properties.

| Bit  | Meaning             |
| ---- | ------------------- |
| bit0 | Unicast / Multicast |
| bit1 | Global / Local      |

Meaning:

```
bit0 = 0 → Unicast
bit0 = 1 → Multicast
```

```
bit1 = 0 → Globally assigned
bit1 = 1 → Locally administered
```

Example analysis:

```
7E = 01111110
Last bits = 10
```

Result:

```
Unicast
Locally administered
```

***

## Ethernet Broadcast

Broadcast address:

```
FF:FF:FF:FF:FF:FF
```

Meaning:

```
Send frame to every device on the LAN
```

***

## Multiple Access Problem

**Definition**

When multiple devices share the same communication medium, a mechanism is needed to determine **who can transmit and when**.

Example analogy:

```
Classroom discussion
Only one person speaks at a time
```

***

### Multiple Access Protocol Types

| Category             | Example     |
| -------------------- | ----------- |
| Channel Partitioning | TDM, FDM    |
| Random Access        | ALOHA, CSMA |
| Taking Turns         | Token Ring  |

***

### CSMA/CD Operation

```
1 Listen to channel
2 If idle → transmit
3 If collision → stop transmitting
4 Wait random backoff
5 Retry transmission
```

***

## Week 3 — Network Layer (IP)

### Internet Protocol (IP)

**Definition**

The Internet Protocol is responsible for:

* logical addressing
* packet forwarding
* routing between networks

***

## IPv4 Address

#### Definition

An IPv4 address is a **32-bit logical address** used to identify a network interface.

Example:

```
192.168.1.10
```

Binary:

```
11000000.10101000.00000001.00001010
```

***

## IPv4 Datagram Structure

```
+---------+-------------+-------------+
| Version | Header Len  | TOS         |
+---------+-------------+-------------+
| Total Length                        |
+-------------------------------------+
| Identification | Flags | Frag Off   |
+-------------------------------------+
| TTL | Protocol | Header Checksum    |
+-------------------------------------+
| Source IP Address                   |
+-------------------------------------+
| Destination IP Address              |
+-------------------------------------+
| Options (optional)                  |
+-------------------------------------+
| Data                                |
+-------------------------------------+
```

***

### Key IPv4 Header Fields

| Field         | Purpose                |
| ------------- | ---------------------- |
| Version       | IP version             |
| Header length | Size of header         |
| TTL           | Prevent infinite loops |
| Protocol      | Next layer protocol    |
| Checksum      | Error detection        |

***

### TTL — Time To Live

**Definition**

TTL limits the number of hops a packet can travel.

Operation:

```
Router receives packet
TTL = TTL − 1
```

If:

```
TTL = 0
```

Packet is dropped and router sends:

```
ICMP TTL exceeded
```

***

## Protocol Field Numbers

These values must be memorised.

| Value | Protocol |
| ----- | -------- |
| 1     | ICMP     |
| 6     | TCP      |
| 17    | UDP      |

***

## ICMP

#### Definition

ICMP is used for **network diagnostics and error reporting**.

Examples:

```
ping
traceroute
```

***

## Week 5 — IPv6

### Why IPv6?

IPv6 was introduced because **IPv4 addresses were exhausted**.

IPv4 capacity:

```
2^32 ≈ 4.3 billion
```

***

## IPv6 Address

#### Definition

An IPv6 address is a **128-bit logical address**.

Example:

```
2001:0db8:000a:0001:c012:9aff:fe9a:19ac
```

Structure:

```
8 groups
16 bits each
```

***

## IPv6 Compression

#### Remove leading zeros

```
0db8 → db8
```

#### Replace longest zero sequence

```
0000:0000:0000 → ::
```

Example:

```
2001:db8:0:1111:0:0:0:200
```

becomes

```
2001:db8:0:1111::200
```

***

## IPv6 Address Types

| Type      | Meaning          |
| --------- | ---------------- |
| Unicast   | One device       |
| Multicast | Group of devices |
| Anycast   | Nearest device   |

***

## IPv6 Global Unicast

Range:

```
2000::/3
```

Meaning:

```
First hextet
2000 – 3FFF
```

***

## Link Local Address

Prefix:

```
fe80::/10
```

Used for:

* router communication
* default gateway

***

## IPv6 Address Structure

```
+------------------+-----------+------------------+
| Global Prefix    | Subnet ID | Interface ID     |
| 48 bits          | 16 bits   | 64 bits          |
+------------------+-----------+------------------+
```

Typical subnet:

```
/64
```

***

## Week 6 — DNS

### DNS Definition

The **Domain Name System** translates **human-readable domain names into IP addresses**.

Example:

```
www.google.com → 142.250.190.78
```

***

## DNS Hierarchy

```
           Root (.)
             |
           .com
             |
          google
             |
            www
```

***

## DNS Message Header

Important flags:

| Flag | Meaning              |
| ---- | -------------------- |
| QR   | Query or response    |
| AA   | Authoritative answer |
| TC   | Message truncated    |
| RD   | Recursion desired    |
| RA   | Recursion available  |

Example:

```
QR = 0 → Query
QR = 1 → Response
```

***

## DNS Resource Records

| Type  | Purpose        |
| ----- | -------------- |
| A     | IPv4 address   |
| AAAA  | IPv6 address   |
| MX    | Mail server    |
| NS    | Name server    |
| CNAME | Alias          |
| SOA   | Zone authority |

***

## Final High-Value Exam Facts

```
IPv4 address = 32 bits
IPv6 address = 128 bits
MAC address = 48 bits

Broadcast MAC
FF:FF:FF:FF:FF:FF

DNS port
53

Protocol numbers
1 = ICMP
6 = TCP
17 = UDP

IPv6 link-local prefix
fe80::/10

IPv6 global unicast
2000::/3
```

***

| Feature             | IPv4                   | IPv6                   |
| ------------------- | ---------------------- | ---------------------- |
| Address size        | **32 bits**            | **128 bits**           |
| Address format      | Decimal                | Hexadecimal            |
| Example address     | 192.168.1.1            | 2001:db8::1            |
| Total address space | 2³² ≈ **4.3 billion**  | 2¹²⁸ ≈ **3.4 × 10³⁸**  |
| Header size         | **20–60 bytes**        | **Fixed 40 bytes**     |
| Header complexity   | Variable               | Simplified             |
| Payload size        | Up to **65,535 bytes** | Up to **65,535 bytes** |
| Fragmentation       | Allowed                | Only done by sender    |
| Checksum            | Present                | Removed                |
| Broadcast support   | Yes                    | No                     |
| Multicast support   | Limited                | Native support         |
| Configuration       | Manual / DHCP          | SLAAC / DHCPv6         |
| NAT needed          | Often required         | Not required           |
| Typical subnet      | Variable               | **/64**                |
