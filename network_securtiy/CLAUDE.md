# Network Security — Subject Context

> **Module:** Network Security | **Programme:** MSc Computing | **Student:** Mohankumar Muthusamy

## Overview

This module is primarily hands-on, focused on network simulation using **Cisco Packet Tracer** and command-line networking tools. The coursework consists of practical lab exercises covering network protocols, topology design, and security configurations.

## Folder Structure

```
network_securtiy/                          # (note: original spelling preserved)
├── Commands/
│   └── DNS_Commands.txt                   # Practical DNS lookup session
├── Packets/
│   ├── 01-models.pkt                      # Network models lab
│   ├── 02-arp.pkt                         # ARP protocol lab
│   ├── 05-ipv6.pkt                        # IPv6 configuration lab
│   ├── 10-02-student.pkt                  # Security-focused lab
│   ├── packet-tracer/                     # Extended security labs (08-06 to 08-18)
│   │   ├── 08-06-student.pkt through 08-18-student.pkt
│   ├── 08-pt-student.zip                  # Packet Tracer lab package
│   └── packet-tracer-security.zip         # Security-specific labs
```

**Note:** `.pkt` files are binary Cisco Packet Tracer simulations and cannot be read as text. The meaningful textual content is in the DNS commands file.

## Content Details

### DNS Commands (Practical Lab)
A documented terminal session demonstrating DNS resolution hierarchy:
- `dig` commands for recursive queries, NS lookups, MX record queries
- Root nameserver interactions showing the hierarchical delegation chain
- Demonstrates iterative vs recursive DNS resolution
- Practical understanding of DNS as attack surface (DNS spoofing, cache poisoning context)

### Packet Tracer Labs (Topics Inferred from Filenames)
- **Lab 01:** Network models and topologies
- **Lab 02:** ARP protocol — address resolution, ARP spoofing concepts
- **Lab 05:** IPv6 configuration and transition mechanisms
- **Labs 08-06 through 08-18:** Security-focused configurations (ACLs, firewalls, VPN, port security)
- **Lab 10-02:** Advanced security lab

## Key Concepts

- DNS hierarchy and resolution (recursive/iterative queries)
- Network protocol security (ARP, IPv6, DNS)
- Network topology design and simulation
- Security configurations: ACLs, firewalls, port security
- Protocol-level vulnerabilities and countermeasures

---

*Related: [Root CLAUDE.md](../CLAUDE.md)*
