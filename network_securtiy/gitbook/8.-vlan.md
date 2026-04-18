# VLAN

## CSC1134 — Week 8: VLANs Revision Notes

> **Mohan's exam revision — Test 2, 30 March 2026**

***

### 1. Why VLANs Exist

#### The Flat Network Problem

```
WITHOUT VLANs — one big broadcast domain:
─────────────────────────────────────────

  PC-A ──┐
  PC-B ──┤
  PC-C ──┼── SWITCH ── everyone sees everyone's broadcasts
  PC-D ──┤
  PC-E ──┘

  Problem 1: PERFORMANCE
    IPv4 loves broadcasts (ARP, DHCP).
    Every device receives every broadcast → congestion.

  Problem 2: SECURITY
    Staff can ARP-spoof students.
    All users share the same Layer-2 domain.
    Applying ACLs on a flat network is complicated.
```

#### The VLAN Solution

```
WITH VLANs — logical separation on one physical switch:
───────────────────────────────────────────────────────

  PC-A ──┐              ┌── PC-C
  PC-B ──┤  [VLAN 10]  │   [VLAN 10]
         │              │
         └──── SWITCH ──┘
         ┌──── SWITCH ──┐
         │              │
  PC-D ──┤  [VLAN 20]  ├── PC-E
  PC-F ──┘              └── PC-G
                             [VLAN 20]

  VLAN 10 and VLAN 20 are completely isolated.
  One physical switch. Two logical LANs.
```

***

### 2. What is a VLAN?

* A **Virtual LAN** — emulates N logical LANs on one physical switch
* Each VLAN is its own **broadcast domain**
* Broadcasts in VLAN 10 **never** reach VLAN 20
* One switch can support many VLANs → reduces hardware cost

#### Security Benefits (exam favourite)

| Benefit                   | Why                                                                     |
| ------------------------- | ----------------------------------------------------------------------- |
| Smaller broadcast domains | Limits attack scope, reduces congestion                                 |
| Layer-2 attack prevention | ARP spoofing across VLANs is impossible                                 |
| Traffic isolation         | Users only communicate within their VLAN                                |
| Chokepoint enforcement    | Inter-VLAN traffic must pass through a router where ACLs can be applied |
| User grouping             | Students → student server, Staff → staff server                         |

***

### 3. VLAN 1

```
VLAN 1 facts:
─────────────────────────────────────────────────────

  ✓ Default VLAN — all ports belong to it out of the box
  ✓ Default native VLAN — carries untagged trunk traffic
  ✓ Cannot be deleted (IOS will not allow it)
  ✗ Security risk — leaving users in VLAN 1 is dangerous
  ✗ Overloaded — being both default and native is a problem
```

***

### 4. Port Types

```
ACCESS PORT vs TRUNK PORT:
─────────────────────────────────────────────────────

ACCESS PORT:
  ┌─────────────────────────────────────────────┐
  │ Belongs to:  ONE VLAN only                  │
  │ Carries:     Only that VLAN's traffic        │
  │ Used for:    User devices (PCs, printers)    │
  │ Tags frames: NO — traffic is untagged        │
  └─────────────────────────────────────────────┘

TRUNK PORT:
  ┌─────────────────────────────────────────────┐
  │ Belongs to:  ALL VLANs (by default)         │
  │ Carries:     Any VLAN's traffic              │
  │ Used for:    Switch-to-switch, switch-router │
  │ Tags frames: YES — 802.1Q (dot1Q) tag added  │
  └─────────────────────────────────────────────┘
```

***

### 5. Creating VLANs — CLI Commands

```
FULL SEQUENCE:
─────────────────────────────────────────────────────

Switch> enable
Switch# conf t
Switch(config)# no ip domain-lookup        ← stops IOS doing DNS on typos

Switch(config)# show vlan brief            ← check current state

Switch(config)# vlan 10
Switch(config-vlan)# name Students

Switch(config-vlan)# vlan 20
Switch(config-vlan)# name Staff

Switch(config)# show vlan brief            ← verify VLANs created

Switch(config)# int fa0/1
Switch(config-if)# switchport mode access
Switch(config-if)# switchport access vlan 10

Switch(config-if)# int fa0/2
Switch(config-if)# switchport mode access
Switch(config-if)# switchport access vlan 10

Switch(config-if)# int range fa0/3-4      ← configure multiple ports at once
Switch(config-if)# switchport mode access
Switch(config-if)# switchport access vlan 20

Switch(config-if)# end
Switch# copy running-config startup-config ← save config
```

#### Mode Navigation

```
MODE HIERARCHY:
─────────────────────────────────────────────────────

  Switch>              ← User EXEC (view only)
      ↓ enable
  Switch#              ← Privileged EXEC (view + save)
      ↓ conf t
  Switch(config)#      ← Global Config (change device-wide settings)
      ↓ int fa0/1
  Switch(config-if)#   ← Interface Config (change port settings)

  exit    → go back ONE level
  end     → jump straight to Switch#
  Ctrl+Z  → same as end
```

***

### 6. The Multi-Switch Problem and Trunking

#### Problem Setup

```
S1 and S2 both have VLAN 10 and VLAN 20.
How do hosts on the same VLAN but different switches talk?

S1                                    S2
┌────────────────────┐                ┌────────────────────┐
│ fa0/1 [VLAN 10]   │                │ fa0/1 [VLAN 10]   │
│ fa0/2 [VLAN 20]   │                │ fa0/2 [VLAN 20]   │
│ gig0/1 [VLAN 1] ──┼──(default)─────┼─ [VLAN 1] gig0/1 │
└────────────────────┘                └────────────────────┘

Inter-switch port is VLAN 1 by default.
VLAN 10 traffic hits gig0/1 (VLAN 1 port) → DROPPED ✗
```

#### Naive Fix (Q9) — One access port per VLAN

```
COMMANDS (run on BOTH S1 and S2):
─────────────────────────────────────────────────────

int gig 0/1
 switchport mode access
 switchport access vlan 10     ← dedicate cable 1 to VLAN 10

int gig 0/2
 switchport mode access
 switchport access vlan 20     ← dedicate cable 2 to VLAN 20


RESULT:
─────────────────────────────────────────────────────

S1                                    S2
gig0/1 [VLAN 10] ════ cable 1 ════ gig0/1 [VLAN 10]  ✓
gig0/2 [VLAN 20] ──── cable 2 ──── gig0/2 [VLAN 20]  ✓


PROBLEM — DOES NOT SCALE:
─────────────────────────────────────────────────────

  2 VLANs  → 2 cables, 2 ports per switch
  10 VLANs → 10 cables, 10 ports per switch
             ↑ half your switch gone before any users
```

#### Proper Fix (Q11) — Trunk port

```
COMMANDS (run on BOTH S1 and S2):
─────────────────────────────────────────────────────

int gig 0/1
 switchport mode trunk         ← ONE port carries ALL VLANs

show int trunk                 ← verify (not a config command)


RESULT:
─────────────────────────────────────────────────────

S1                                    S2
gig0/1 [TRUNK] ════ ONE cable ════ gig0/1 [TRUNK]

VLAN 10 frame → tag [10] added → travels trunk → tag read → delivered ✓
VLAN 20 frame → tag [20] added → travels trunk → tag read → delivered ✓
Add 18 more VLANs? Still one cable. ✓


HOW 802.1Q TAGGING WORKS:
─────────────────────────────────────────────────────

Normal Ethernet frame:
┌──────┬──────┬──────┬─────────┐
│ Dest │ Src  │ Type │ Payload │
└──────┴──────┴──────┴─────────┘

802.1Q tagged frame (on trunk):
┌──────┬──────┬──────────┬──────┬─────────┐
│ Dest │ Src  │ 4-byte   │ Type │ Payload │
│      │      │ VLAN tag │      │         │
└──────┴──────┴──────────┴──────┴─────────┘
                  ↑
                  contains VLAN ID (e.g. 10 or 20)
                  added by sending switch
                  removed by receiving switch
```

> ⚠️ **Exam tip**: Both ends of a trunk link must be configured as trunk. If one end is access, tagged frames get dropped.

***

### 7. Inter-VLAN Routing

VLANs isolate traffic — to allow communication between VLANs you need a **Layer-3 device**.

```
WHY:
─────────────────────────────────────────────────────

Sales-A (192.168.10.1) → Marketing-A (192.168.20.1)

  Switch sees: VLAN 10 traffic trying to reach VLAN 20 port
  Switch thinks: "not my job — I only forward within a VLAN"
  Result: DROPPED ✗

  You need a router to move traffic between networks.
```

#### Q13 — Legacy Approach (one router port per VLAN)

```
TOPOLOGY:
─────────────────────────────────────────────────────

               R1
          ┌─────────┐
          │ gig0/0/0│ 192.168.10.254  ← gateway for VLAN 10
          │ gig0/0/1│ 192.168.20.254  ← gateway for VLAN 20
          └────┬────┘
               │  2 cables, 2 router ports
         gig0/1   gig0/2
        [VLAN10] [VLAN20]
          ┌──────┴──────┐
          │    SWITCH   │
          │ fa0/1 fa0/3 │
          └──┬──────┬───┘
           Sales  Marketing


COMMANDS:
─────────────────────────────────────────────────────

Switch:
  int gig 0/1
   switchport mode access
   switchport access vlan 10

  int gig 0/2
   switchport mode access
   switchport access vlan 20

Router:
  int gig 0/0/0
   ip address 192.168.10.254 255.255.255.0
   no shut

  int gig 0/0/1
   ip address 192.168.20.254 255.255.255.0
   no shut

  show ip route


PROBLEM:
─────────────────────────────────────────────────────

  1 switch  + 2 VLANs = 2 router ports
  2 switches + 2 VLANs = 4 router ports
  2 switches + 5 VLANs = 10 router ports ← router is full
  Router only has 2-4 ports → runs out fast ✗
```

#### Q14 — Router-on-a-Stick (subinterfaces + trunk)

```
TOPOLOGY:
─────────────────────────────────────────────────────

               R1
          ┌─────────┐
          │ gig0/0/0│  ONE physical port
          │  ├ .10  │  192.168.10.254  ← subinterface VLAN 10
          │  └ .20  │  192.168.20.254  ← subinterface VLAN 20
          └────┬────┘
               │  ONE trunk cable
            gig0/1 [TRUNK]
          ┌────┴────────┐
          │   SWITCH    │
          │ fa0/1 fa0/3 │
          └──┬──────┬───┘
           Sales  Marketing


WHAT IS A SUBINTERFACE:
─────────────────────────────────────────────────────

  Physical: gig0/0/0 (one real port)
                │
       ┌────────┴────────┐
  gig0/0/0.10        gig0/0/0.20
  (logical — VLAN 10) (logical — VLAN 20)
  .254 of /24         .254 of /24

  The dot after the port number = subinterface number
  encapsulation dot1Q tells it which VLAN tag to read/write


COMMANDS:
─────────────────────────────────────────────────────

Router:
  int gig 0/0/0.10
   encapsulation dot1Q 10           ← tag traffic for VLAN 10
   ip address 192.168.10.254 255.255.255.0

  int gig 0/0/0.20
   encapsulation dot1Q 20           ← tag traffic for VLAN 20
   ip address 192.168.20.254 255.255.255.0

  int gig 0/0/0
   no shut                          ← bring up physical interface

  show ip route

Switch:
  int gig 0/1
   switchport mode trunk            ← trunk carries all VLANs to router


TRAFFIC FLOW:
─────────────────────────────────────────────────────

Sales-A → Marketing-A

  Sales-A sends to gateway 192.168.10.254
      ↓
  Switch tags [VLAN 10] → sends up trunk
      ↓
  Router gig0/0/0.10 receives (reads tag 10)
      ↓
  Router routes to 192.168.20.0 network
      ↓
  Router sends out gig0/0/0.20 with tag [VLAN 20]
      ↓
  Switch reads tag 20 → delivers to Marketing-A ✓


SCALES TO ~50 VLANs:
─────────────────────────────────────────────────────

  Add VLAN 30? → add subinterface gig0/0/0.30
  Add VLAN 40? → add subinterface gig0/0/0.40
  No new cables. No new router ports. ✓
  Beyond ~50 VLANs → use multilayer switch
```

#### Q15 — Multilayer Switch with SVIs (modern approach)

```
TOPOLOGY:
─────────────────────────────────────────────────────

  ┌──────────────────────────────────────┐
  │         MULTILAYER SWITCH            │
  │                                      │
  │  [SVI VLAN 10] 192.168.10.254       │ ← virtual interface
  │  [SVI VLAN 20] 192.168.20.254       │ ← virtual interface
  │                                      │
  │  fa0/1 [VLAN 10]   fa0/3 [VLAN 20] │
  └────┬──────────────────────┬──────────┘
       │                      │
    Sales-A              Marketing-A
  192.168.10.1           192.168.20.1

  No router needed. Routing happens INSIDE the switch.


WHAT IS AN SVI:
─────────────────────────────────────────────────────

  Switch Virtual Interface — a virtual port inside the switch.
  Has an IP address. Acts as the default gateway for its VLAN.
  No physical port consumed. No cable to a router.
  The switch pretends to be a router internally.


COMMANDS:
─────────────────────────────────────────────────────

  int vlan 10
   ip address 192.168.10.254 255.255.255.0   ← SVI for VLAN 10

  int vlan 20
   ip address 192.168.20.254 255.255.255.0   ← SVI for VLAN 20

  ip routing                                 ← MUST enable routing
  show ip route
  show ip int brief


TRAFFIC FLOW:
─────────────────────────────────────────────────────

  Sales-A → gateway 192.168.10.254 (SVI VLAN 10 inside switch)
      ↓
  ip routing → looks up table → 192.168.20.0 via SVI VLAN 20
      ↓
  Delivered to Marketing-A ✓
  Everything stays inside the switch. No external router.
```

#### Comparison Table

```
                Legacy Q13    Router-on-stick Q14    SVI Q15
                ──────────    ───────────────────    ───────
Router needed?  YES           YES                    NO
Ports used      1 per VLAN    1 total                0
Cables          1 per VLAN    1 trunk                0
Scales?         NO ✗          ~50 VLANs ✓            50+ VLANs ✓
Modern?         NO            somewhat               YES ✓
Bottleneck?     per-VLAN      one shared cable       none (internal)
```

***

### 8. VLAN Hopping Attacks

#### Q16 — What is a VLAN Hopping Attack?

```
NORMAL BEHAVIOUR:
─────────────────────────────────────────────────────

  Attacker (VLAN 1) → sends traffic → switch → DROPPED
  VLANs are isolated. Switch never forwards
  VLAN 1 traffic to VLAN 10. ✓


VLAN HOPPING:
─────────────────────────────────────────────────────

  Attacker (VLAN 1) ──────────────────► Victim (VLAN 10) ✗
                      bypasses isolation
                      no router involved
                      Layer-2 attack
```

#### Q17 — Double-Tagging Attack

```
REQUIREMENTS:
─────────────────────────────────────────────────────

  1. Attacker must be in the NATIVE VLAN (VLAN 1 default)
  2. Native VLAN traffic travels trunk links UNTAGGED
  3. Switch must not drop tagged frames on access ports


THE DOUBLE-TAGGED PACKET:
─────────────────────────────────────────────────────

  Normal packet:
  ┌─────────────────────────────┐
  │  no tag  │    Payload       │
  └─────────────────────────────┘

  Attacker crafts:
  ┌────────────┬────────────┬─────────┐
  │ Outer tag  │ Inner tag  │ Payload │
  │  VLAN 1    │  VLAN 10   │         │
  │ (native)   │ (target)   │         │
  └────────────┴────────────┴─────────┘
       ↑              ↑
    stripped       exposed after
    by S1          outer tag removed


STEP BY STEP:
─────────────────────────────────────────────────────

Attacker        S1                    S2          Victim
(VLAN 1)   (access port)         (trunk port)   (VLAN 10)

STEP 1: Attacker sends double-tagged packet to S1
[V1│V10│data] ──►

STEP 2: S1 sees outer tag = VLAN 1 (native VLAN)
        Native VLAN on trunk = untagged
        S1 strips the outer VLAN 1 tag
        Result: [V10│data]

STEP 3: S1 forwards to S2 via trunk (as native/untagged)
        But inner VLAN 10 tag is still there
        [V10│data] ══════ trunk ══════►

STEP 4: S2 reads inner tag = VLAN 10
        S2 thinks it's legitimate VLAN 10 traffic
        S2 delivers to Victim ✗

FULL FLOW:
[V1│V10│data] ──► strip V1 ──► [V10│data] ══trunk══► delivered to VLAN 10 ✗


IMPORTANT LIMITATION:
─────────────────────────────────────────────────────

  ONE-WAY attack only.
  Attacker ──► Victim  ✓ (packet reaches)
  Attacker ◄── Victim  ✗ (reply cannot come back)
  Attacker can send but cannot receive replies.
```

#### Q18 — Preventing VLAN Hopping

```
ROOT CAUSE:
─────────────────────────────────────────────────────

  VLAN 1 = default VLAN + native VLAN (overloaded)
  Attacker sits in VLAN 1 (native) → attack works

  Fixing access ports does NOT fix trunk behaviour.
  The vulnerability is in the TRUNK CONFIGURATION.

  ┌─────────────────────┬──────────────────────────────┐
  │ Shutting ports      │ stops devices connecting     │
  ├─────────────────────┼──────────────────────────────┤
  │ native vlan command │ changes trunk behaviour      │
  │                     │ (this is what you need)      │
  └─────────────────────┴──────────────────────────────┘


THE FIX — MOVE NATIVE VLAN TO AN UNUSED VLAN:
─────────────────────────────────────────────────────

Step 1: Create dedicated unused VLAN 999

  vlan 999
   name NATIVE_VLAN

Step 2: Change native VLAN on ALL trunk ports

  int gig 0/1
   switchport mode trunk
   switchport trunk native vlan 999   ← trunk now uses VLAN 999 as native

Step 3: Move all users out of VLAN 1

  int fa0/1
   switchport mode access
   switchport access vlan 10          ← users in VLAN 10, NOT VLAN 1

Step 4: Shut down unused ports in VLAN 999

  int range fa0/20-24
   switchport access vlan 999
   shutdown                           ← physically disabled, nobody can connect


AFTER FIX — VLAN 1 STATUS:
─────────────────────────────────────────────────────

  VLAN 1 still exists (cannot delete it)
  VLAN 1 is no longer the native VLAN
  VLAN 1 has no users
  VLAN 1 carries no meaningful traffic
  VLAN 1 = empty ghost VLAN — harmless

  VLAN 999 = native VLAN, all ports shutdown
  Nobody can physically connect to VLAN 999
  Attack has no starting point ✓


BEFORE vs AFTER:
─────────────────────────────────────────────────────

  BEFORE:
  VLAN 1   │ attacker here │ native VLAN │ all defaults │ → DANGEROUS
  VLAN 10  │ Sales                                      │
  VLAN 20  │ Marketing                                  │

  AFTER:
  VLAN 1   │ EMPTY                                      │ → harmless
  VLAN 10  │ Sales                                      │
  VLAN 20  │ Marketing                                  │
  VLAN 999 │ native VLAN, all ports shutdown            │ → harmless
```

***

### 9. Quick Reference — All Commands

```
CREATE VLANs:
  vlan 10 ; name Students
  vlan 20 ; name Staff

ASSIGN ACCESS PORT:
  int fa0/1
   switchport mode access
   switchport access vlan 10

CONFIGURE TRUNK:
  int gig 0/1
   switchport mode trunk

LEGACY INTER-VLAN (router):
  int gig 0/0/0
   ip address 192.168.10.254 255.255.255.0
   no shut

ROUTER-ON-A-STICK (subinterfaces):
  int gig 0/0/0.10
   encapsulation dot1Q 10
   ip address 192.168.10.254 255.255.255.0
  int gig 0/0/0
   no shut

MULTILAYER SWITCH (SVI):
  int vlan 10
   ip address 192.168.10.254 255.255.255.0
  ip routing

PREVENT VLAN HOPPING:
  vlan 999 ; name NATIVE_VLAN
  int gig 0/1
   switchport trunk native vlan 999
  int range fa0/20-24
   switchport access vlan 999
   shutdown

VERIFY:
  show vlan brief
  show int trunk
  show ip route
  show ip int brief
```

***

### 10. Exam Revision — Key Points to Remember

> These are the things most likely to catch you out.

#### Must-Know Facts

* VLAN 1 = default VLAN + default native VLAN. **Cannot be deleted.**
* Access port = one VLAN. Trunk port = all VLANs.
* 802.1Q (dot1Q) = the tagging protocol used on trunk links. 4-byte tag added to Ethernet frame.
* To route between VLANs you **always** need a Layer-3 device (router or multilayer switch).
* `ip routing` command is **required** on multilayer switch — without it, the SVIs exist but routing does not happen.
* `no shut` is **required** on router physical interface for router-on-a-stick — subinterfaces won't come up without it.
* Both ends of a trunk link must be configured as trunk — mismatched ends drop tagged frames.
* Double-tagging is **one-way only** — attacker can send to victim but cannot receive replies.

#### Common Exam Mistakes

| Mistake                                           | Correct                                                                       |
| ------------------------------------------------- | ----------------------------------------------------------------------------- |
| Thinking shutting VLAN 1 ports prevents hopping   | The fix is `switchport trunk native vlan 999` — trunk config, not port config |
| Forgetting `ip routing` on multilayer switch      | Always needed — switches don't route by default                               |
| Forgetting `no shut` on physical router interface | Subinterfaces inherit state from physical interface                           |
| Forgetting `encapsulation dot1Q` on subinterface  | Without this the subinterface doesn't know which VLAN to handle               |
| Thinking VLAN 999 needs active ports              | VLAN 999 is intentionally empty — all ports shutdown                          |

#### Three Inter-VLAN Methods — One-Line Summary

| Method            | One line                                                     |
| ----------------- | ------------------------------------------------------------ |
| Legacy            | One router port per VLAN — simple but doesn't scale          |
| Router-on-a-stick | One trunk to router, subinterfaces per VLAN — scales to \~50 |
| Multilayer SVI    | Routing inside the switch, no router needed — modern, best   |

#### Security — Two-Line Summary

* **Why VLANs help**: Smaller broadcast domains, ARP spoofing across VLANs impossible, chokepoints for ACLs.
* **VLAN hopping fix**: Move native VLAN to unused VLAN 999, shutdown all its ports, never put users in VLAN 1.

***

_CSC1134 Network Security — Week 8 VLANs_ _Revision notes compiled: 30 March 2026_
