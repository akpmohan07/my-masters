# Event B

<figure><img src="../.gitbook/assets/image (1) (1).png" alt=""><figcaption></figcaption></figure>





<figure><img src="../.gitbook/assets/image (2).png" alt=""><figcaption></figcaption></figure>





<figure><img src="../.gitbook/assets/image (3).png" alt=""><figcaption></figcaption></figure>

Perfect — let me explain every symbol with concrete name examples like that.

***

**∈ — is a member of**

> p ∈ PERSONS

Means: p is one specific person who belongs to PERSONS.

Example:

> Mohan ∈ PERSONS ✓ Surekha ∈ PERSONS ✓

***

**∉ — is NOT a member of**

> p ∉ members

Means: p is not in the club yet.

Example:

> { Mohan, Surekha } are in members Deepak ∉ members ✓ — Deepak is not a member yet

***

**⊆ — is a subset of**

> members ⊆ PERSONS

Means: everyone in the club is a person.

Example:

> members = { Mohan, Surekha, Deepak } PERSONS = { Mohan, Surekha, Deepak, Alice, Bob, Carol ... } members ⊆ PERSONS ✓ — every member is a person

***

**∅ — empty set**

> members := ∅

Means: at the start nobody is in the club.

Example:

> Before anyone joins: members = { } — completely empty

***

**∩ — intersection**

> waiting ∩ members = ∅

Means: nobody can be in both sets at the same time.

Example:

> members = { Mohan, Surekha } waiting = { Deepak, Alice } members ∩ waiting = { } = ∅ ✓ — no overlap

> If Deepak was in both: members = { Mohan, Surekha, Deepak } waiting = { Deepak, Alice } members ∩ waiting = { Deepak } ≠ ∅ ✗ — invariant violated!

***

**card() — cardinality**

> card(members) < MaxCapacity

Means: count how many people are in members, check it's below the limit.

Example:

> members = { Mohan, Surekha, Deepak } card(members) = 3 MaxCapacity = 5 3 < 5 ✓ — club has room, event can fire

> If members = { Mohan, Surekha, Deepak, Alice, Bob } card(members) = 5 5 < 5 ✗ — club is full, event cannot fire

***

**↦ — maps to**

> allocation = { Mohan ↦ 101, Surekha ↦ 102, Deepak ↦ 103 }

Means: each person is paired with their membership number.

> allocation := allocation ∪ { Alice ↦ 104 }

Means: "Add Alice with membership number 104 to allocation."

> allocation = { Mohan ↦ 101, Surekha ↦ 102, Deepak ↦ 103, Alice ↦ 104 }

***

**dom() — domain**

> dom(allocation)

Means: the set of all people who have a membership number — left hand side of every pair.

Example:

> allocation = { Mohan ↦ 101, Surekha ↦ 102, Deepak ↦ 103 } dom(allocation) = { Mohan, Surekha, Deepak }

> p ∉ dom(allocation)

Means: p has not been given a membership number yet.

> Alice ∉ dom(allocation) ✓ — Alice has no membership number yet

***

**ran() — range**

> ran(allocation)

Means: the set of all membership numbers currently in use — right hand side of every pair.

Example:

> allocation = { Mohan ↦ 101, Surekha ↦ 102, Deepak ↦ 103 } ran(allocation) = { 101, 102, 103 }

> n ∉ ran(allocation)

Means: membership number n is not taken yet.

> 104 ∉ ran(allocation) ✓ — number 104 is free

***

**⇸ — partial function**

> allocation ∈ PERSONS ⇸ N

Means: allocation maps SOME people to membership numbers — not everyone needs one.

Example:

> PERSONS = { Mohan, Surekha, Deepak, Alice, Bob, Carol } allocation = { Mohan ↦ 101, Surekha ↦ 102, Deepak ↦ 103 }

Alice, Bob, Carol are in PERSONS but have no membership number — that's fine with ⇸.

***

**→ — total function**

> allocation ∈ PERSONS → N

Means: EVERY person in PERSONS must have a membership number — no exceptions.

Example:

> PERSONS = { Mohan, Surekha, Deepak, Alice, Bob, Carol } allocation must contain ALL six: { Mohan ↦ 101, Surekha ↦ 102, Deepak ↦ 103, Alice ↦ 104, Bob ↦ 105, Carol ↦ 106 }

If Alice is missing — invariant violated ✗

***

**∪ {} — add to set**

> members := members ∪ { Deepak }

Means: add Deepak to the club.

Example:

> Before: members = { Mohan, Surekha } After: members = { Mohan, Surekha, Deepak }

***

**\ {} — remove from set**

> members := members \ { Surekha }

Means: remove Surekha from the club.

Example:

> Before: members = { Mohan, Surekha, Deepak } After: members = { Mohan, Deepak }

***

**{x} ⩤ — domain subtraction**

> allocation := { Surekha } ⩤ allocation

Means: remove Surekha's entire record from allocation — delete the pair where Surekha is on the left.

Example:

> Before: allocation = { Mohan ↦ 101, Surekha ↦ 102, Deepak ↦ 103 } After: allocation = { Mohan ↦ 101, Deepak ↦ 103 }

Surekha ↦ 102 is gone. Number 102 is now free.

***

**⩥ {} — range subtraction**

> allocation := allocation ⩥ { 102 }

Means: remove whoever has membership number 102 — delete the pair where 102 is on the right.

Example:

> Before: allocation = { Mohan ↦ 101, Surekha ↦ 102, Deepak ↦ 103 } After: allocation = { Mohan ↦ 101, Deepak ↦ 103 }

Same result as above — but you're searching by number, not by name.

***

**:∈ — non-deterministic assignment**

> free :∈ PERSONS

Means: pick any person from PERSONS — we don't care which one specifically.

Example:

> PERSONS = { Mohan, Surekha, Deepak, Alice, Bob } free could become Mohan, or Surekha, or anyone — the system picks arbitrarily

Used in initialisation when the starting value doesn't matter.

***

That's every symbol with concrete names throughout. Ready for the Q4 template?
