# DNS

{% embed url="https://chatgpt.com/g/g-p-69653dfc24f08191a76360c88807a286-network-security/c/69aeb97b-c980-838b-9af6-1aaca12c16fe" %}

Below is a **clear, structured summary of the DNS topic (including dig commands, records, flags, cache poisoning, and authoritative queries)**. This is designed as a **study sheet for revision**.

***

## DNS (Domain Name System) – Complete Summary

### 1. What DNS Is

DNS stands for **Domain Name System**.

It is called a **domain name system** because it organizes names of hosts in a hierarchical **domain structure** and provides a **system to translate those names into IP addresses**.

Humans prefer domain names:

```
www.google.com
```

But networks use IP addresses:

```
142.250.74.14
```

DNS translates:

```
Domain Name → IP Address
```

***

## 2. DNS Architecture

DNS works as a **distributed hierarchical database**.

#### Structure of DNS hierarchy

```
                 (Root)
                   .
         -----------------------
         |         |          |
        .com       .ie       .org
         |          |
     nytimes.com   dcu.ie
         |
     www.nytimes.com
```

#### DNS server levels

1. **Root DNS servers**
2. **Top Level Domain (TLD) servers**
3. **Authoritative DNS servers**

***

## 3. DNS Resolution Process

Example: resolving

```
www.example.com
```

#### Step-by-step process

```
User → Local Resolver → Root Server → TLD Server → Authoritative Server
```

#### Diagram

```
User
  |
  v
Local DNS Resolver
  |
  v
Root Server (.)
  |
  v
TLD Server (.com)
  |
  v
Authoritative Server (example.com)
  |
  v
IP Address returned
```

The resolver then **caches the result**.

***

## 4. DNS Cache

DNS servers store responses temporarily in **cache**.

Purpose:

* Faster future queries
* Reduced DNS traffic

Example cached record:

```
www.example.com → 93.184.216.34
TTL: 3600 seconds
```

***

## 5. DNS Cache Poisoning

DNS cache poisoning is called that because attackers **poison the cached DNS records with fake data**.

#### Normal DNS response

```
www.bank.com → 192.0.2.10
```

#### Poisoned DNS cache

```
www.bank.com → 203.0.113.5 (attacker site)
```

Users are redirected to a malicious server.

#### Attack process

```
Resolver sends DNS query
        |
Attacker sends fake response
        |
Fake record stored in cache
        |
Users redirected to attacker site
```

#### Prevention

1. DNSSEC
2. Source port randomization
3. Random transaction IDs
4. Short TTL values

***

## 6. Source Port Randomization

DNS queries use UDP.

Example outgoing query:

```
Source Port: 53128
Destination Port: 53
```

Response must match:

```
Transaction ID
Source Port
```

If ports are random, attackers must guess:

```
Transaction ID × Port
```

Which makes cache poisoning much harder.

***

## 7. DNS Resource Records

DNS records store information about domains.

Structure:

```
NAME | TYPE | CLASS | TTL | DATA
```

Example:

```
example.com   IN   A   3600   192.0.2.1
```

***

## 8. Important DNS Record Types

### A Record

Maps domain → IPv4 address

```
www.example.com → 192.168.1.1
```

***

### AAAA Record

Maps domain → IPv6 address

```
www.example.com → 2001:db8::1
```

***

### MX Record

Mail server for a domain.

```
example.com MX mail.example.com
```

Used for email delivery.

***

### NS Record

Specifies authoritative name servers.

```
example.com NS ns1.example.com
```

***

### CNAME Record

Alias for another domain.

```
www.example.com → server.example.com
```

***

### SOA Record

Start of Authority record.

Contains:

* primary DNS server
* admin email
* serial number
* refresh time
* retry time

***

### TXT Record

Stores text information.

Example uses:

* SPF
* domain verification

***

### HINFO Record

Host information.

Contains:

* CPU
* Operating system

***

## 9. DNS Header Flags

DNS responses contain flags.

Example from dig:

```
flags: qr rd ra
```

#### Important flags

| Flag | Meaning                  |
| ---- | ------------------------ |
| QR   | Query/Response indicator |
| RD   | Recursion Desired        |
| RA   | Recursion Available      |
| AA   | Authoritative Answer     |

***

### QR Flag

QR indicates whether a DNS message is:

```
0 → Query
1 → Response
```

Example:

```
flags: qr rd ra
```

means this is a **DNS response**.

***

## 10. Authoritative DNS Answer

Authoritative responses come from the **official DNS server for a domain**.

Example flag:

```
flags: qr aa
```

Meaning:

```
AA = Authoritative Answer
```

***

## 11. Using DIG Command

`dig` is a DNS lookup tool.

#### Basic syntax

```
dig domain
```

Example:

```
dig google.com
```

***

### Query specific record

```
dig domain A
dig domain AAAA
dig domain MX
dig domain NS
```

Example:

```
dig nytimes.com MX
```

***

### Query specific DNS server

```
dig @server domain
```

Example:

```
dig @8.8.8.8 google.com
```

***

### Query authoritative server

```
dig @nameserver domain A
```

Example:

```
dig @ns1.example.com www.example.com A
```

Look for:

```
flags: qr aa
```

***

## 12. Common DNS Response Status Codes

| Status   | Meaning                  |
| -------- | ------------------------ |
| NOERROR  | Query successful         |
| NXDOMAIN | Domain does not exist    |
| REFUSED  | Server refused to answer |
| SERVFAIL | Server failure           |

***

## Example

```
dig @a.ns.ie AAAA www.nyimes.com
```

Explanation:

* query sent to `.ie` server
* asking about `.com` domain
* server refuses recursion

Expected result:

```
status: REFUSED
```

***

## 13. DNS Root Servers

Root DNS servers are the top of the DNS hierarchy.

There are **13 root server clusters**:

```
a.root-servers.net
b.root-servers.net
...
m.root-servers.net
```

You can query them:

```
dig NS .
```

***

## 14. Full DNS Query Flow Example

Resolving:

```
www.rte.ie
```

#### Step 1

Query root server

```
dig @b.root-servers.net www.rte.ie
```

Returns `.ie` servers.

#### Step 2

Query TLD server

```
dig @a.ns.ie www.rte.ie
```

Returns authoritative servers.

#### Step 3

Query authoritative server

```
dig @ns2.rte.ie www.rte.ie
```

Returns:

```
www.rte.ie → IP address
```

***

## 15. DNS Resolution Diagram

```
User
 |
 v
Local Resolver
 |
 v
Root Server (.)
 |
 v
TLD Server (.ie)
 |
 v
Authoritative Server (rte.ie)
 |
 v
IP Address returned
```

***

## Final Quick Revision Points

* DNS translates **domain names → IP addresses**
* DNS is a **hierarchical distributed system**
* Key DNS records:

```
A
AAAA
MX
NS
CNAME
SOA
TXT
```

* DNS responses contain **flags**
* **AA flag** indicates authoritative answer
* `dig` is used to query DNS servers
* DNS cache speeds up lookups
* DNS cache poisoning inserts **fake records into cache**

***

If you want, I can also give you a **1-page ultra-compact exam cheat sheet** (the exact points that usually appear in **network security/DNS exam questions**).
