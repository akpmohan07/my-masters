# Network Layer Model

Great — let’s work through **Week 1: Network Models / Layers** step by step and make it exam-ready.

I’ll structure this clearly so you can use it for revision.

(Primary source: )

***

## 1️⃣ Why Do We Use Layered Network Design?

#### Core Idea

Networks are built as a **stack of layers**, where:

* Each layer performs a specific function.
* Each layer provides services to the one above it.
* Each layer hides its internal implementation.

#### Why?

Because networks are extremely complex.

Layering:

* Reduces complexity
* Allows independent development
* Makes troubleshooting easier
* Allows protocols to evolve without breaking everything

***

### 🔁 Encapsulation (Very Important Concept)

When data moves **down** the stack:

* Each layer adds control information (header/trailer)
* This process is called **encapsulation**

When data moves **up** the stack:

* Each layer removes its header
* This is decapsulation

Example flow:\
Application → Transport → Network → Data Link → Physical\
Then reversed at the destination.

***

## 2️⃣ Network Model Design Goals

The model should:

1. Create a layer only when necessary
2. Give each layer a clearly defined role
3. Minimise information crossing interfaces
4. Avoid too many layers (overly complex) or too few (functions mixed together)

***

## 3️⃣ Key Network Design Issues

Every network must handle:

* Addressing (how do we reach the right process?)
* Error detection/correction
* Message ordering
* Flow control (fast sender vs slow receiver)
* Congestion control
* Fragmentation (MTU limits)
* Routing (choosing a path)

These problems are distributed across layers.

***

## 4️⃣ Connection-Oriented vs Connectionless

#### 📞 Connection-Oriented (like a phone call)

* Setup phase
* Guaranteed order
* Usually reliable
* Example: TCP

#### 📬 Connectionless (like posting letters)

* No setup
* No ordering guarantee
* Usually unreliable
* Example: UDP

This distinction is fundamental for exams.

***

## 5️⃣ Services, Interfaces, and Protocols

Very examinable distinction:

| Term      | Meaning                    |
| --------- | -------------------------- |
| Service   | What a layer does          |
| Interface | How upper layers access it |
| Protocol  | Rules between peer layers  |

This separation is why OSI is still academically useful.

***

## 6️⃣ The OSI Reference Model

Proposed by the **International Organization for Standardization**

It is called the **OSI reference model**.

It is a **top-down, theoretical model** with 7 layers.

***

### OSI Layers (Top → Bottom)

![Image](https://images.openai.com/static-rsc-3/v7CVM4JUpSDnT-VQgLU2IWuvSWhtVk1AmGHVmdwB0xlGhdF8QLRZ3xd9zLrXOFyz4vhuErazenN_QpndJqroC8eFvwq4c-cYpwiQIP4-dOA?purpose=fullsize\&v=1)

![Image](https://images.openai.com/static-rsc-3/X-_KFBBeVMdf7MPPDmlsDAIvrnMPX943jHPPFsZyibqQ_5l3qvJv8dixoI5jVGj9xcu4XxKQiOKgSFMpXT1bovtxHeFyKjdXdvqFb0ET1yk?purpose=fullsize\&v=1)

![Image](https://assets.bytebytego.com/diagrams/0295-osi-model.jpeg)

#### 7. Application

User-level protocols (HTTP, FTP, SMTP)

#### 6. Presentation

Data formatting, encryption, compression

#### 5. Session

Dialogue control, synchronization

#### 4. Transport

End-to-end delivery\
Connection-oriented or connectionless

#### 3. Network

Routing (IP operates here)

#### 2. Data Link

Framing, MAC addressing

#### 1. Physical

Signals, voltages, cables

***

#### Important Concept:

Layers 1–3 = **Communication Subnet**

They provide basic packet delivery.

***

## 7️⃣ TCP/IP Model

The **TCP/IP model** is a 4-layer model.

It originated from ARPANET and underpins today’s Internet.

It is a **bottom-up, protocol-first model**.

***

### TCP/IP Layers

![Image](https://images.openai.com/static-rsc-3/0WQejBCqkoIauD04XRwVXXvBGbrbp53tsxecM7fpalksI0dXtqgEBcHZnBt7XeweVIQVbpN50ARgilxMaqesBWkhLJhvgOdFEFyOpjALOZw?purpose=fullsize\&v=1)

![Image](https://images.openai.com/static-rsc-3/tNnWYVgCNaoXU-HgzPhmpU2W-BcDnNuVIXWgwjunasd-NKKuUsyuTBWNhoVOHk614xWeCufTB6EoPoy9tCVFM_XwFyMkSVueZP3uXTaYlK8?purpose=fullsize\&v=1)

![Image](https://images.openai.com/static-rsc-3/X-_KFBBeVMdf7MPPDmlsDAIvrnMPX943jHPPFsZyibqQ_5l3qvJv8dixoI5jVGj9xcu4XxKQiOKgSFMpXT1bovtxHeFyKjdXdvqFb0ET1yk?purpose=fullsize\&v=1)

![Image](https://www.researchgate.net/publication/327483011/figure/fig2/AS%3A11431281398641971%401745530538265/The-logical-mapping-between-OSI-basic-reference-model-and-the-TCP-IP-stack.tif)

#### 4. Application

HTTP, FTP, DNS, SMTP

#### 3. Transport

TCP (reliable)\
UDP (unreliable)

#### 2. Internet

IP (routing)

#### 1. Host-to-Network

Ethernet, physical media

***

## 8️⃣ OSI vs TCP/IP (Exam Favourite)

| OSI                             | TCP/IP                       |
| ------------------------------- | ---------------------------- |
| 7 layers                        | 4 layers                     |
| Designed first, protocols later | Protocols first, model later |
| Theoretical                     | Practical                    |
| Rarely implemented              | Dominates Internet           |

Key idea:

* OSI = good for analysis
* TCP/IP = real-world implementation

***

## 9️⃣ Service Primitives (Socket Operations)

Server side:

* socket()
* bind()
* listen()
* accept()
* read()/write()
* close()

Client side:

* socket()
* gethostbyname()
* connect()
* read()/write()
* close()

These map directly to TCP connection lifecycle.

***

## 🔟 Big Picture Summary

Week 1 teaches you:

* Why networks use layers
* What encapsulation is
* OSI model structure
* TCP/IP model structure
* Difference between connection-oriented and connectionless
* Services vs protocols vs interfaces
* Why TCP/IP won in practice

***

If you’d like, next we can:

* 🔍 Do an exam-style question on OSI vs TCP/IP
* 🧠 Deep dive into encapsulation with a worked example
* 📦 Walk through a real packet from browser → server
* 🧩 Create a memory trick for all OSI layers

What would you like to tackle next?
