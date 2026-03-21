---
description: >-
  https://chatgpt.com/g/g-p-69653dfc24f08191a76360c88807a286-network-security/c/69a63391-7c68-8394-93b8-b359d5d525a6
---

# Quiz 1

## 🔷 1️⃣ OSI Model – What Each Layer Does

### Layer 7 – Application

* User-level protocols
* HTTP, FTP, SMTP, DNS
* Connects **application ↔ application**

### Layer 6 – Presentation

* Data formatting
* Encryption / decryption
* Compression
* Example: TLS/SSL
* Connects **data representation ↔ data representation**

### Layer 5 – Session

* Session setup/management
* Synchronization
* Controls dialogue
* Connects **session ↔ session**

### Layer 4 – Transport

* End-to-end communication
* TCP (reliable) / UDP (unreliable)
* Uses **port numbers**
* Connects **process ↔ process**

### Layer 3 – Network

* Routing
* IP addressing
* Protocols: IP, ICMP
* Connects **host ↔ host**

### Layer 2 – Data Link

* MAC addresses
* Ethernet
* ARP
* Frame delivery on LAN
* Connects **node ↔ node (same LAN)**

### Layer 1 – Physical

* Bits
* Signals
* Cables, voltages
* Connects **hardware ↔ hardware**

***

## 🔷 2️⃣ Protocol Layer Placement (Very Exam Important)

| Protocol | Layer        |
| -------- | ------------ |
| HTTP     | Application  |
| TLS/SSL  | Presentation |
| TCP      | Transport    |
| UDP      | Transport    |
| IP       | Network      |
| ICMP     | Network      |
| ARP      | Data Link    |
| Ethernet | Data Link    |
| MAC      | Data Link    |

***

## 🔷 3️⃣ Key Concepts You Must Remember

### ✅ Transport Session = 5-Tuple

To uniquely identify a transport connection:

* Source IP
* Destination IP
* Source Port
* Destination Port
* Transport Protocol (TCP/UDP)

This is called:

> The **5-tuple**

***

### ✅ Socket Connects Which Layers?

> Application ↔ Transport

A socket is the interface between your program and TCP/UDP.

***

### ✅ TCP vs UDP

#### TCP

* Reliable
* Sequenced
* Connection-oriented
* 3-way handshake
* Flow control (window)
* Congestion control

#### UDP

* Connectionless
* No sequencing
* No retransmission
* Fast
* Used for DNS, streaming

***

### ✅ TCP 3-Way Handshake

1. SYN
2. SYN + ACK
3. ACK

Connection established.

***

### ✅ TCP Connection Close

FIN → ACK\
FIN → ACK

(Two channels close independently)

***

### ✅ ARP

Maps:

> IP address → MAC address

* Works only on local LAN
* Data Link layer

***

### ✅ ICMP

* Error reporting
* Ping (Echo request/reply)
* TTL exceeded
* Network layer

***

### ✅ TLS

* Encryption
* Authentication
* Data integrity
* Runs above TCP
* OSI: Presentation layer

Called “transport security” because it **secures transport**, not because it is transport.

***

## 🔷 4️⃣ Server vs Client System Calls

### TCP Server Order

1. socket()
2. bind()
3. listen()
4. accept()
5. fork() (optional)
6. read()/write()
7. close()

Think:

> Open shop → put address → listen → accept → serve → close

***

### TCP Client Order

1. socket()
2. gethostbyname()
3. connect()
4. read()/write()
5. close()

Think:

> Get phone → look up number → dial → talk → hang up

***

## 🔷 5️⃣ OSI vs TCP/IP Model

### OSI (7 layers)

* Theoretical
* Clean separation
* Used for learning

### TCP/IP (4 layers)

* Practical Internet model
* Application
* Transport
* Internet
* Network Access

Mapping:

| OSI          | TCP/IP         |
| ------------ | -------------- |
| Application  | Application    |
| Presentation | Application    |
| Session      | Application    |
| Transport    | Transport      |
| Network      | Internet       |
| Data link    | Network Access |
| Physical     | Network Access |

***

## 🔷 6️⃣ Frequently Tested Distinctions

#### Host-to-host → Network layer

#### Process-to-process → Transport layer

#### Local delivery → Data Link layer

#### Routing → Network layer

#### Encryption → Presentation layer

#### Port numbers → Transport layer

#### MAC addresses → Data Link layer

#### TTL → Network layer

#### Sequence number → Transport layer

***

## 🔷 7️⃣ High-Yield Exam Memory Triggers

If question mentions:

* Encryption → Presentation
* Ports → Transport
* Routing → Network
* MAC → Data Link
* Cables/Signals → Physical
* DNS/HTTP → Application
* ARP → Data Link
* ICMP → Network
* 5-tuple → Transport session
* Socket → Application ↔ Transport

***

## 🎯 Final Exam Strategy

If stuck, ask:

1. Is it about routing? → Network
2. Is it about reliability/ports? → Transport
3. Is it about local MAC delivery? → Data Link
4. Is it about encryption/format? → Presentation
5. Is it user protocol? → Application
