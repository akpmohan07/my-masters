# Formal Programming — Subject Context

> **Module:** Formal Specification | **Programme:** MSc Computing | **Student:** Mohankumar Muthusamy

## Overview

This module covers formal methods using **Event-B** on the **Rodin Platform**. Students model systems as state machines with mathematical invariants, then verify correctness through proof obligations. Two assignments progress from basic specifications to refinement — adding concrete implementation detail while preserving proven properties.

## Folder Structure

```
formal_programming/
├── rodin_workspace/
│   ├── Formal_Specification_Assignment_1/     # Assignment 1: 4 basic systems
│   │   ├── Q1_ReadingTracker/                 # Child book reading system
│   │   ├── Q2_ClubManagement/                 # Membership with capacity & waiting list
│   │   ├── Q3_ParkingSystem/                  # Vehicle permits (partial bijection)
│   │   └── Q4_StudentExam/                    # Exam registration with resits
│   ├── SimpleBankSol.zip_expanded/SimpleBank/ # Banking system demo
│   └── .metadata/                             # (Rodin IDE files — ignore)
├── Solutions/                                 # Assignment 2: Refinement
│   ├── Q1_Progress/                           # Employee career progression
│   ├── Q2_Papers/                             # Property paper delivery
│   ├── Q3_Stack/                              # Abstract stack → concrete array
│   ├── Q4_Buffer/                             # FIFO buffer with head/tail pointers
│   └── Supermarket/                           # Complex retail system
└── Formal Specification Assignment Submission/
```

**Event-B File Types:**
- `.bum` — Machine specifications (state variables, invariants, events)
- `.buc` — Context files (constants, carrier sets, axioms)
- `.bcm/.bcc` — Compiled/cached versions
- `.bpo/.bps/.bpr` — Proof obligations, status, and results

## Assignment 1: Basic System Specifications

### Q1: Reading Tracker
Models a library system tracking which children have read which books.
- **Variables:** `hasread` (child↔book relation), `unread_book`, `read_book_count`
- **Events:** `record` (log a book read), `newbook` (get unread book for child), `books_query` (count via `card()`)
- **Key concept:** Set relations and cardinality queries

### Q2: Club Management
Membership system with capacity limits and waiting list.
- **Context:** `MAX_CLUB_SIZE = 500`, `NAMES` carrier set
- **Variables:** `member`, `waiting` (subsets of NAMES), counters
- **Invariants:** Disjointness (`member ∩ waiting = ∅`), capacity (`card(member) < MAX_CLUB_SIZE`)
- **Events:** `join` (waiting→member), `join_queue`, `remove`, `jump_queue` (skip queue), queries
- **Key concept:** Disjoint sets, capacity constraints via cardinality

### Q3: Parking System
Vehicle registration with one-to-one person↔vehicle mapping.
- **Variables:** `permit ∈ PEOPLE ⤔ VEHICLES` (partial bijection)
- **Events:** `register`, `deregister`, `change_register`, `vehicle` (query), `owner` (inverse via `permit∼`), `drugs_check` (random suspect)
- **Key concept:** Partial bijections, inverse relations, injective functions

### Q4: Student Exam
Exam registration with multiple attempts (exam, resit1, resit2).
- **Variables:** `exam_result`, `resit1_result`, `resit2_result` (partial functions: registered ⇸ mark)
- **Invariants:** Cascading domains (`dom(resit1_result) ⊆ dom(exam_result)`), pass threshold enforcement (`exam_result(s) < pass_mark` required for resit eligibility), resit marks capped at pass_mark
- **Events:** `register`, `exam_marks`, `resit1_mark`, `resit2_mark`, `studentquery` (count attempts), `markquery` (best mark via `max()`)
- **Key concept:** Partial functions, quantified formulas, domain constraints

## Assignment 2: Refinement

### Q1: Progress — Employee Career Progression
- **Abstract:** Three disjoint sets (trainee, sales, manager) with promotion/fire events
- **Refined:** Concrete state transitions preserving disjointness invariants

### Q2: Papers — Property Management
- **Abstract:** `papers ⊆ HOUSE`, boolean query
- **Refined:** Adds concrete implementation details

### Q3: Stack (Most Important Refinement Example)
- **Abstract (Stack.bum):** Stack modelled as relation `stack ⊆ ℕ × ELEMENT`, pointer `top`, push/pop/empty/full events. Invariant: `dom(stack) = 0..top-1`
- **Refined (StackR.bum):** Introduces `stackarr` as concrete array. Refinement invariants connect abstract to concrete: `stackarr = stack`, `card(stackarr) = top`. Push becomes `stackarr ≔ stackarr ∪ {card(stackarr) ↦ e}`. Pop uses `card(stackarr)-1` indexing.
- **Key concept:** Data refinement — replacing abstract relations with concrete arrays while proving equivalence

### Q4: Buffer — Circular Queue
- **Abstract:** `buffer ⊆ ℕ × ELEMENT`, `front`/`back` pointers. Domain = `front..back-1`. Capacity: `back - front < maxsize`
- **Refined:** Concrete array representation
- **Key concept:** FIFO semantics via pointer arithmetic

## Key Event-B Concepts Across All Specifications

| Concept | Examples |
|---------|----------|
| **Invariants** | Type correctness (`x ∈ ℤ`), subset constraints, relations (`x ∈ A ⇸ B`), disjointness |
| **Partial Functions (⇸)** | `exam_result ∈ registered ⇸ mark` — not all students have results |
| **Partial Bijections (⤔)** | `permit ∈ PEOPLE ⤔ VEHICLES` — one-to-one exclusive mapping |
| **Set Operations** | `∪` union, `∖` difference, `∩` intersection, `⊆` subset |
| **Relational Operators** | `dom()` domain, `ran()` range, `∼` inverse, `[]` image |
| **Cardinality** | `card(member) < MAX_CLUB_SIZE`, `card(stackarr) = top` |
| **Quantified Formulas** | `∀s · s ∈ dom(resit1) ⇒ exam_result(s) < pass_mark` |
| **Guards** | Preconditions on events preventing invalid state transitions |
| **Refinement** | Adding implementation detail while preserving proven safety properties |
| **Witness Predicates** | Connecting abstract variables to concrete ones in refinement |

---

*Related: [Root CLAUDE.md](../CLAUDE.md)*
