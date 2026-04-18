# Subnetting

## CSC1134 — Networking Study Notes

### Session: Subnetting, IP Addressing & Packet Tracer Lab

**Date:** 21 March 2026

***

### 1. IP Address Fundamentals

* IPv4 addresses are **32-bit**, written in dot-decimal notation (e.g. `192.168.1.1`)
* Each octet = 8 bits, value range 0–255
* Two portions: **network** (identifies subnet) and **host** (identifies device)
* Unicast host range: `1.0.0.1` to `223.255.255.254`

#### Reserved ranges

| Range                       | Purpose                             |
| --------------------------- | ----------------------------------- |
| `0.0.0.0/8`                 | This network — reserved             |
| `127.0.0.0/8`               | Loopback — never leaves the machine |
| `169.254.0.0/16`            | Link-local / APIPA — DHCP failed    |
| `224.0.0.0–239.255.255.255` | Multicast (Class D)                 |
| `240.0.0.0–255.255.255.255` | Reserved / experimental (Class E)   |
| `255.255.255.255`           | Limited broadcast                   |

***

### 2. Subnet Mask & CIDR

* Subnet mask = all 1s for network bits, all 0s for host bits
* CIDR notation: `192.168.1.0/24` — the `/24` = 24 network bits
* **Interesting octet** = the octet that is neither 255 nor 0 in the mask

#### Mask derivation (last octet)

| Prefix | Network bits in last octet | Binary   | Mask value |
| ------ | -------------------------- | -------- | ---------- |
| /25    | 1                          | 10000000 | 128        |
| /26    | 2                          | 11000000 | 192        |
| /27    | 3                          | 11100000 | 224        |
| /28    | 4                          | 11110000 | 240        |
| /29    | 5                          | 11111000 | 248        |
| /30    | 6                          | 11111100 | 252        |

***

### 3. Key Formulas

```
Block size       = 256 − mask value of interesting octet
Subnets          = 2^(borrowed bits)
Usable hosts     = 2^(host bits) − 2
Borrowed bits    = new prefix − original prefix
Network address  = IP AND subnet mask (bitwise)
Broadcast        = next network address − 1
First host       = network address + 1
Last host        = broadcast − 1
```

***

### 4. The Four Key Addresses (example: 192.168.1.0/26)

| Address    | Value        | Rule              |
| ---------- | ------------ | ----------------- |
| Network    | 192.168.1.0  | All host bits = 0 |
| First host | 192.168.1.1  | Network + 1       |
| Last host  | 192.168.1.62 | Broadcast − 1     |
| Broadcast  | 192.168.1.63 | All host bits = 1 |

***

### 5. Subnetting on vs within Octet Boundary

* **On boundary** (/8, /16, /24) — mask ends in 255 or 0, easy
* **Within boundary** (/25, /26, /27...) — one octet is partially split, use block size method

***

### 6. VLSM (Variable Length Subnet Masks)

#### Rules (critical for exam)

1. Assign subnets in **descending size order** — largest first
2. Same size subnets → assign in **order listed** in the table
3. Subnets must be **contiguous** — no gaps
4. Use the **most efficient** (smallest) subnet for each requirement
5. Router-to-router links → always use **/30** (2 usable hosts)
6. Always provide: network address, broadcast, subnet mask, usable host count

#### Process

```
1. Sort requirements largest → smallest
2. For each: find smallest prefix where 2^h − 2 ≥ required hosts
3. Assign from start of address block
4. Next subnet starts immediately after previous broadcast
```

***

### 7. Broadcast Types

| Type               | Address             | Scope                  | Crosses router?  |
| ------------------ | ------------------- | ---------------------- | ---------------- |
| Limited broadcast  | 255.255.255.255     | Local segment only     | Never            |
| Directed broadcast | Subnet broadcast IP | Specific remote subnet | Yes (if enabled) |

***

### 8. Transmission Types

| Type      | Packets sent      | Receivers             | Example               |
| --------- | ----------------- | --------------------- | --------------------- |
| Unicast   | 1 per destination | One host              | Normal traffic        |
| Broadcast | 1                 | All hosts on LAN      | ARP, DHCP             |
| Multicast | 1                 | Subscribed hosts only | OSPF, video streaming |

***

### 9. Multicast

* Range: `224.0.0.0` to `239.255.255.255` (Class D)
* Destination-only addresses — never assigned to a device as its own IP
* Hosts subscribe via **IGMP (Internet Group Management Protocol)**

#### IGMP message types

| Message                  | Direction          | Purpose                  |
| ------------------------ | ------------------ | ------------------------ |
| Membership Report (0x16) | Host → Router      | Join a group             |
| General Query (0x11)     | Router → 224.0.0.1 | Anyone still subscribed? |
| Leave Group (0x17)       | Host → 224.0.0.2   | Leave a group            |

#### Multicast sub-ranges

| Range                     | Scope                                   |
| ------------------------- | --------------------------------------- |
| 224.0.0.0–224.0.0.255     | Link-local — never forwarded by routers |
| 224.0.1.0–238.255.255.255 | Global — routable across internet       |
| 239.0.0.0–239.255.255.255 | Private / administratively scoped       |

#### Key OSPF addresses

* `224.0.0.5` — all OSPF routers
* `224.0.0.2` — all routers on segment

***

### 10. Default Gateway

* The router interface on the same subnet as the host
* Used when destination is **outside** the host's subnet
* Typically the first or last usable host address by convention

***

### 11. TTL (Time To Live)

* Each router hop decrements TTL by 1
* Default TTL values:
  * **Cisco devices** → 255
  * **Windows** → 128
  * **Linux/macOS** → 64

***

### 12. Packet Tracer Lab — Subnet an IPv4 Network

#### Scenario

Subnet `192.168.0.0/24` for:

* LAN-A: minimum 50 hosts
* LAN-B: minimum 40 hosts
* Minimum 4 subnets (2 LANs + 2 spare)
* Same mask for all (no VLSM)

#### Chosen prefix: /26 (255.255.255.192)

* 4 subnets ✓ meets minimum 4
* 62 usable hosts ✓ meets minimum 50

#### Subnets derived (block size = 64)

| Subnet | Network          | Broadcast     | Use   |
| ------ | ---------------- | ------------- | ----- |
| 1      | 192.168.0.0/26   | 192.168.0.63  | LAN-A |
| 2      | 192.168.0.64/26  | 192.168.0.127 | LAN-B |
| 3      | 192.168.0.128/26 | 192.168.0.191 | Spare |
| 4      | 192.168.0.192/26 | 192.168.0.255 | Spare |

#### Addressing table

| Device         | Interface | IP Address    | Mask            | Gateway      |
| -------------- | --------- | ------------- | --------------- | ------------ |
| CustomerRouter | G0/0      | 192.168.0.1   | 255.255.255.192 | N/A          |
| CustomerRouter | G0/1      | 192.168.0.65  | 255.255.255.192 | N/A          |
| CustomerRouter | S0/1/0    | 209.165.201.2 | 255.255.255.252 | N/A          |
| LAN-A Switch   | VLAN1     | 192.168.0.2   | 255.255.255.192 | 192.168.0.1  |
| LAN-B Switch   | VLAN1     | 192.168.0.66  | 255.255.255.192 | 192.168.0.65 |
| PC-A           | NIC       | 192.168.0.62  | 255.255.255.192 | 192.168.0.1  |
| PC-B           | NIC       | 192.168.0.126 | 255.255.255.192 | 192.168.0.65 |

#### Key Cisco CLI commands

```bash
# Router interface configuration
enable
configure terminal
hostname CustomerRouter
enable secret Class123
line console 0
password Cisco123
login
exit
interface g0/0
ip address 192.168.0.1 255.255.255.192
no shutdown
exit
do write

# Switch management IP
enable
configure terminal
interface vlan 1
ip address 192.168.0.2 255.255.255.192
no shutdown
exit
ip default-gateway 192.168.0.1
exit
write
```

#### Connectivity test results

| Test                | Result | TTL | Explanation                        |
| ------------------- | ------ | --- | ---------------------------------- |
| PC-A → 192.168.0.1  | ✓      | 255 | Same subnet, Cisco device          |
| PC-A → 192.168.0.2  | ✓      | 255 | First ping slow — ARP              |
| PC-B → 192.168.0.65 | ✓      | 255 | Switch needs no IP to forward      |
| PC-B → PC-A         | ✓      | 127 | 1 router crossed, Windows TTL 128  |
| PC-B → ISP Router   | ✓      | 254 | 1 router crossed, Cisco TTL 255    |
| PC-B → ISP Server   | ✓      | 126 | 2 routers crossed, Windows TTL 128 |

#### Lessons from testing

* A switch **does not need an IP** to forward traffic — IP is management only
* First ping always slower due to **ARP resolution**
* TTL decrements by 1 per router hop
* Different OS use different default TTL values

***

### 13. Exam Checklist

* [ ] Given hosts needed → find correct prefix
* [ ] Derive all subnets from a block using block size
* [ ] Identify network, broadcast, first host, last host
* [ ] Apply VLSM rules — largest subnet first, contiguous, /30 for links
* [ ] Use dot-decimal notation for all answers
* [ ] Document assumptions in exam answers
