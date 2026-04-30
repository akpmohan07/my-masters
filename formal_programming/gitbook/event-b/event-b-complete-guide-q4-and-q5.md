# Event-B Complete Guide — Q4 and Q5

### Sum:

<figure><img src="../.gitbook/assets/image (6).png" alt=""><figcaption></figcaption></figure>

### STEP 1 — Read and Extract

Read the problem once slowly. Extract:

| What to look for              | Where it goes |
| ----------------------------- | ------------- |
| Nouns with unique identifiers | SETS          |
| Fixed numbers / maximums      | CONSTANTS     |
| Rules about constants         | AXIOMS        |
| What changes over time        | VARIABLES     |
| Always true rules             | INVARIANTS    |
| Each listed operation         | EVENT         |

#### Clue words to watch for

| Clue word in problem               | What it means                |
| ---------------------------------- | ---------------------------- |
| "each X has a unique identifier"   | → SETS                       |
| "a number of" / "maximum"          | → CONSTANTS                  |
| "is allocated to" / "maps to"      | → function variable          |
| "maximum of one" / "unique"        | → partial injection          |
| "every X has a Y"                  | → total function             |
| "set of" / "collection of"         | → set variable               |
| "count of" / "number of currently" | → integer variable           |
| "only if"                          | → guard                      |
| "return" / "give"                  | → output event, no parameter |

***

### STEP 2 — Write the CONTEXT

```
CONTEXT name_ctx
SETS
  THINGIDS        ← one per type of unique thing
CONSTANTS
  MaxSomething    ← one per fixed number
AXIOMS
  axm1 : MaxSomething ∈ ℕ₁
END
```

#### SETS rules

* Declare each type of unique thing as a SET
* No axioms needed — just declaring it is enough
* It is not a number or string — it is an abstract collection of unique identifiers

#### CONSTANTS rules

* One constant per fixed number mentioned in the problem
* Every constant needs exactly one axiom

#### AXIOMS rules

* Always write: `MaxSomething ∈ ℕ₁`
* ℕ₁ means positive natural numbers — 1, 2, 3 ... (never 0)
* Axioms are ONLY for constants, never for sets

***

### STEP 3 — Write VARIABLES

Ask for each thing that changes over time:

| Question                                   | Variable type | Example                             |
| ------------------------------------------ | ------------- | ----------------------------------- |
| Is it a set of things?                     | Set           | members, waiting, inStation, logged |
| Is it a mapping from one thing to another? | Function      | allocation, session, votes, ballot  |
| Is it a count?                             | Integer       | numprocs, number, free              |

***

### STEP 4 — Write INVARIANTS

For each variable ask three questions:

#### Question 1 — What type is it?

| Variable type     | Invariant                  | When to use                                |
| ----------------- | -------------------------- | ------------------------------------------ |
| Set of things     | `var ⊆ SETNAME`            | Simple collection                          |
| Partial function  | `var ∈ A ↠ B`              | Some inputs mapped, outputs can repeat     |
| Total function    | `var ∈ A → B`              | Every input must have a mapping            |
| Partial injection | `var ∈ A ↣ B`              | Some inputs mapped, outputs must be unique |
| Total injection   | `var ∈ A ↣ B (full arrow)` | All inputs mapped, outputs unique          |
| Count             | `var ∈ 0..Maximum`         | Numeric range                              |

#### Choosing the right function type

| Problem says                | Use                 |
| --------------------------- | ------------------- |
| "maximum of one" / "unique" | Partial injection ↣ |
| "every X has a Y"           | Total function →    |
| "some X have a Y"           | Partial function ↠  |
| "every X has a unique Y"    | Total injection     |

#### Question 2 — What constraints does it have?

| Problem says                 | Invariant             |
| ---------------------------- | --------------------- |
| "never exceeds maximum"      | `card(var) ≤ Maximum` |
| "cannot be in both X and Y"  | `X ∩ Y = ∅`           |
| "X must have been allocated" | `X ⊆ dom(allocation)` |
| "count equals size of set"   | `num = card(set)`     |

#### Question 3 — How do variables relate to each other?

Always ask: does any variable depend on another?

Examples:

* `inStation ⊆ dom(allocation)` — trains in station must have a platform
* `logged = dom(session)` — logged processes must have a session
* `gates = dom(allocation)` — every gate in use must have an allocation
* `waiting ∩ members = ∅` — nobody can be in both sets

***

### STEP 5 — Write INITIALISATION

For each variable:

| Variable type     | Initialisation         |
| ----------------- | ---------------------- |
| Set               | `:= ∅`                 |
| Partial function  | `:= ∅`                 |
| Partial injection | `:= ∅`                 |
| Total function    | `:= (λx.x ∈ SET \| 0)` |
| Count             | `:= 0`                 |
| Any value fine    | `:∈ valid range`       |

#### := vs :∈

| Symbol | Meaning                   | Use when                          |
| ------ | ------------------------- | --------------------------------- |
| `:=`   | Assign one specific value | You know the exact starting value |
| `:∈`   | Pick any value from a set | Any value from a range is fine    |

***

### STEP 6 — Write EVENTS

```
Event name =̂
any
  parameters       ← things the event needs as input
where
  grd1 : ...       ← one guard per "only if" clause
  grd2 : ...
then
  act1 : ...       ← what changes
end
```

#### Finding parameters

* What inputs does this event need?
* Usually: the thing being added, removed, or allocated

#### Finding guards — read every "only if"

| "only if" clause         | Guard                         |
| ------------------------ | ----------------------------- |
| "not already a member"   | `p ∉ members`                 |
| "has been allocated"     | `p ∈ dom(allocation)`         |
| "platform is free"       | `platform ∉ ran(allocation)`  |
| "there is room"          | `card(members) < MaxCapacity` |
| "is currently logged in" | `p ∈ dom(session)`            |
| "not in the station"     | `train ∉ inStation`           |
| "at least one exists"    | `set ≠ ∅`                     |

#### Finding actions — what changes

| What happens                                      | Action                        |
| ------------------------------------------------- | ----------------------------- |
| Add p to a set                                    | `set := set ∪ {p}`            |
| Remove p from a set                               | `set := set \ {p}`            |
| Add a mapping                                     | `func := func ∪ {a ↦ b}`      |
| Remove by left side (domain subtraction)          | `func := {a} ⩤ func`          |
| Remove by right side (range subtraction)          | `func := func ⩥ {b}`          |
| Keep only left side in set S (domain restriction) | `func := S ◁ func`            |
| Keep only right side in set S (range restriction) | `func := func ▷ S`            |
| Increment count                                   | `num := num + 1`              |
| Decrement count                                   | `num := num - 1`              |
| Return any value from range                       | `var :∈ valid range`          |
| Update function value                             | `func(x) := func(x) + amount` |

#### Domain and range operations — explained

| Symbol       | Name               | Meaning                                  | Example                                                   |
| ------------ | ------------------ | ---------------------------------------- | --------------------------------------------------------- |
| `{a} ⩤ func` | Domain subtraction | Remove ALL pairs where left side = a     | `{train} ⩤ allocation` — remove train and its platform    |
| `func ⩥ {b}` | Range subtraction  | Remove ALL pairs where right side = b    | `allocated ⩥ {u}` — remove all blocks belonging to user u |
| `S ◁ func`   | Domain restriction | KEEP only pairs where left side is in S  | `{p} ◁ session` — keep only p's session                   |
| `func ▷ S`   | Range restriction  | KEEP only pairs where right side is in S | `ballot ▷ {c}` — keep only votes FOR candidate c          |

#### Special case — no parameters

If the event just returns a value or computes something:

```
Event name =̂
when
  grd1 : ...       ← condition must hold
then
  act1 : var :∈ ...
end
```

***

### STEP 7 — Write the REFINEMENT (Q5)

#### Part (a) — Abstract machine

Write a simple abstract machine with:

* Abstract variables — usually just sets
* 2 events: Initialisation + one main event

#### Part (b) — Linking invariant

**This is the key mark earner — 4 to 8 marks.**

Ask: how does each concrete variable relate to each abstract variable?

| Pattern                                    | Linking invariant                        |
| ------------------------------------------ | ---------------------------------------- |
| Concrete set = abstract set                | `concreteVar = abstractVar`              |
| Concrete count = size of abstract set      | `num = card(abstractSet)`                |
| Abstract set = domain of concrete function | `abstractSet = dom(concreteFunc)`        |
| Abstract set = range of concrete function  | `abstractSet = ran(concreteFunc)`        |
| Vote count recoverable from ballot         | `∀c.c ∈ S ⇒ card(func ▷ {c}) = count(c)` |

**The key question to ask:**

> "If I only had the concrete variables, how would I recover each abstract variable?"

That recovery formula IS the linking invariant.

#### Part (c) — Refined machine

```
MACHINE nameR
REFINES name
SEES name_ctx
VARIABLES
  ...           ← concrete variables
INVARIANTS
  ...           ← type invariants + linking invariants
EVENTS
  Initialisation
  Event1        ← same events as abstract but using concrete variables
  ...
END
```

**Key rule:** Every abstract event must have a corresponding refined event with the **same name**. Guards and actions change to use the concrete variables.

***

### Symbol Quick Reference

#### Set symbols

| Symbol   | Name          | Meaning                            | Example             |
| -------- | ------------- | ---------------------------------- | ------------------- |
| `∈`      | member of     | p belongs to a set                 | `p ∈ PERSONS`       |
| `∉`      | not member of | p does not belong                  | `p ∉ members`       |
| `⊆`      | subset of     | every element is in the larger set | `members ⊆ PERSONS` |
| `∅`      | empty set     | nothing in it                      | `members := ∅`      |
| `∪`      | union         | combine two sets                   | `members ∪ {p}`     |
| `\`      | set minus     | remove from set                    | `members \ {p}`     |
| `∩`      | intersection  | elements in both sets              | `waiting ∩ members` |
| `card()` | cardinality   | count elements                     | `card(members)`     |

#### Function symbols

| Symbol  | Name              | Meaning                                |
| ------- | ----------------- | -------------------------------------- |
| `↦`     | maplet            | one thing maps to another              |
| `dom()` | domain            | set of all left-hand sides             |
| `ran()` | range             | set of all right-hand sides            |
| `↠`     | partial function  | some inputs mapped, outputs can repeat |
| `→`     | total function    | all inputs mapped                      |
| `↣`     | partial injection | some inputs mapped, outputs unique     |
| `f(x)`  | application       | look up x in function f                |
| `f⁻¹`   | inverse           | flip the function around               |
| `f[S]`  | relational image  | find all outputs matching inputs in S  |

#### Logic symbols

| Symbol | Name         | Meaning                                 |
| ------ | ------------ | --------------------------------------- |
| `∧`    | AND          | both must be true                       |
| `∨`    | OR           | at least one must be true               |
| `¬`    | NOT          | opposite                                |
| `⇒`    | implies      | if left is true then right must be true |
| `∀`    | for all      | must hold for every element             |
| `∃`    | there exists | at least one element satisfies          |

***

### Complete Mental Checklist

```
Reading the problem:
  □ What types of things exist?              → SETS
  □ What is fixed / maximum?                 → CONSTANTS + AXIOMS
  □ What changes over time?                  → VARIABLES
  □ What must always hold?                   → INVARIANTS
  □ What is the starting state?              → INITIALISATION
  □ What operations are listed?              → EVENTS

For each variable:
  □ What type is it?                         → type invariant
  □ What constraints does it have?           → constraint invariant
  □ How does it relate to other variables?   → relationship invariant

For each event:
  □ What inputs does it need?                → parameters
  □ What "only if" conditions?               → guards
  □ What variables change?                   → actions
  □ Add/remove set? Union or set minus?
  □ Add/remove mapping? Domain or range subtraction?

For refinement:
  □ What are the concrete variables?
  □ How does each recover the abstract?      → linking invariant
  □ Same event names, new variables          → refined events
```
