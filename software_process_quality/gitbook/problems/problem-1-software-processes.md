# Problem 1 — Software Processes

## Problem 1 — Software Processes

### How do you organise the work?

> **Exam question:** Q1 — 20 marks (Q1a: 10 marks, Q1b: 10 marks) **MediTrack modules:** Patient Intake Module (Q1a) · Drug Interaction Engine (Q1b) **Weeks:** Week 1 + Week 2

***

### The Big Picture

Before software processes existed, development was chaotic — no structure, no phases, no checkpoints. Process models were invented to bring discipline to how software is built.

> Each model was invented because the previous one had a flaw. Understanding that chain is more important than memorising any single model.

***

### Topic 1 — The 3 QA Principles

#### What problem does it solve?

Quality doesn't happen by accident. Without explicit principles, teams build the wrong thing, in the wrong way, with no way to know they've gone wrong. The 3 QA Principles are the foundation of everything else in this module.

#### The 3 Principles

| Principle                                   | Plain English                                    | How you achieve it                                                    |
| ------------------------------------------- | ------------------------------------------------ | --------------------------------------------------------------------- |
| **P1 — Know what you are doing**            | Understand what you're building right now        | Regular meetings, test runs, process milestones, management structure |
| **P2 — Know what you should be doing**      | Have explicit, continuously updated requirements | Use cases, acceptance tests, user feedback, explicit prototypes       |
| **P3 — Know how to measure the difference** | Compare actual vs expected continuously          | Testing + metrics                                                     |

#### The analogy

Think of it like a chef:

* P1 = know what dish you're currently cooking
* P2 = know what the customer actually ordered
* P3 = taste it and compare

#### MediTrack Application — Patient Intake Module

| Principle | Was it violated?         | Why                                                                                                          |
| --------- | ------------------------ | ------------------------------------------------------------------------------------------------------------ |
| P1        | ✅ Not violated           | Team knew what they were building and delivered it on time                                                   |
| P2        | ❌ Most severely violated | Requirements were gathered from written spec, never validated with actual reception staff workflow           |
| P3        | ❌ Also violated          | Testing measured against written spec — not against real user workflow. The measuring stick itself was wrong |

> **Key exam insight:** P3 failed _as a consequence_ of P2 failing. If requirements had been correct, tests would have measured the right thing. P2 is the root cause — always identify it as most severely violated.

#### Failure mode

When P2 is violated, teams build to the wrong target. The system passes all tests but fails in real use — exactly what happened with the Patient Intake Module.

#### Exam answer template

> The most severely violated principle was Principle 2 — Know what you should be doing. The Patient Intake Module met its written specifications but was rejected by hospital reception staff because those specifications never captured the rapid patient-flow workflow reality. Requirements were gathered in meetings, not validated through real use. Principle 3 was also violated as a downstream consequence — the team measured against the wrong specification, so testing passed while real-world use failed.

***

### Topic 2 — Process Models

#### The chain of invention

```
Waterfall ——→ Problem: requirements frozen too early
    ↓ fix
Throwaway Prototyping ——→ Problem: prototype work gets thrown away
    ↓ fix
Evolutionary Prototyping ——→ Problem: poor prototype architecture in production
    ↓ different problem
Spiral ——→ Problem: no explicit risk management
    ↓ different problem
Iterative ——→ Problem: can't deliver large known system fast
```

***

#### Model 1 — Waterfall

**What problem does it solve?**

Chaotic, unstructured development before it existed. Waterfall brought engineering discipline — phases, checkpoints, sign-offs.

**How it works**

6 phases carried out in strict order. Each phase must be signed off before proceeding to the next:

1. Requirements Analysis and Definition
2. System and Software Design
3. Implementation and Unit Testing
4. Integration and System Testing
5. Operation and Maintenance
6. Retirement and Decommissioning

**Why the name?**

The diagram shows phases cascading downward like water falling over steps. Water only flows one way — down. Going back up is unnatural and expensive. The name captures both the structure and the flaw.

**When to use**

* Requirements are fully known and stable upfront
* Safety-critical system needing extensive documentation
* Large team needing clear phase boundaries
* Hardware constraints are fixed and well understood

**When NOT to use**

* Requirements are unclear or likely to change
* Users need to see something before giving feedback
* Fast delivery is needed

**Advantages**

* Clear structure — everyone knows what phase they're in
* Easy to manage and track progress
* Strong documentation at every phase
* Works well with large teams

**Disadvantages**

* Requirements frozen too early
* Users see product for the first time at the end
* Going back is expensive
* Delivered system sometimes doesn't meet real user needs

**Real world examples**

* ✅ **Success:** NASA Space Shuttle — stable physics-based requirements, zero in-flight failures across 135 missions, 420,000 lines of code
* ❌ **Failure:** Healthcare.gov 2013 — requirements frozen without user validation, $1.7B spent, only 6 people enrolled on day one

**Failure mode**

When requirements change after freezing, the team either works around design problems with implementation tricks (messy code) or delivers a system that meets the spec but not real user needs.

**Exam signal words**

Stable requirements · safety-critical · large team · government project · well-defined constraints · documentation heavy

***

#### Model 2 — Throwaway Prototyping

**What problem does it solve?**

Waterfall freezes requirements too early. Users often don't know what they want until they see something. Prototyping lets requirements emerge from real use rather than written guesses.

**How it works**

1. Requirements gathering — loose, not strict
2. Quick design — rough sketch only
3. Build prototype — fast, approximate, shows external features only
4. Customer evaluation — user tries it, gives feedback
5. Design refinement — update based on feedback
6. Full scale development — build the real product from scratch

Steps 3, 4, 5 repeat until requirements are clear. Then the prototype is **discarded** and real development begins.

**The analogy**

Custom suit fitting — tailor makes a rough fitting first, you try it on, give feedback, adjust, try again — _then_ the real suit is made. The fitting is thrown away.

**When to use**

* Requirements genuinely unclear
* Users can't articulate needs without seeing something
* High risk of building the wrong thing

**When NOT to use**

* Requirements already clear — adds unnecessary time
* Small team that can't afford to throw work away
* Tight deadline

**Advantages**

* Requirements emerge from real user feedback
* Reduces risk of building wrong product
* Users feel involved early

**Disadvantages**

* Prototype gets thrown away — wasted work
* Risk of creeping excellence — never finishing
* Prototype quality is poor — shortcuts everywhere
* Full prototypes of complex systems are hard to build quickly

**Failure mode**

Teams either waste significant effort on a prototype that gets discarded, or fall into creeping excellence — endlessly refining the prototype without ever building the real product.

**Exam signal words**

Unclear requirements · user feedback needed · requirements keep changing · users don't know what they want

***

#### Model 3 — Evolutionary Prototyping

**What problem does it solve?**

Throwaway Prototyping wastes the prototype. Evolutionary Prototyping fixes this — the prototype is never discarded. It is continuously evolved until it becomes the final product.

**How it works**

1. Build initial prototype — rough but functional
2. Show user — get feedback
3. Evolve the same prototype based on feedback
4. Repeat steps 2 and 3
5. Prototype gradually becomes the production system

No throwaway. No rebuild from scratch. The work is never wasted.

**The analogy**

Sculpture — start with rough clay, client says "make the nose smaller", refine it, "now the eyes", refine it — keep going until the clay _becomes_ the final piece.

**Key distinction from Throwaway**

|                | Throwaway Prototyping                 | Evolutionary Prototyping        |
| -------------- | ------------------------------------- | ------------------------------- |
| After feedback | Discard prototype, build from scratch | Evolve the same prototype       |
| Work wasted?   | Yes                                   | No                              |
| Risk           | Wasted effort                         | Poor architecture in production |

**When to use**

* Requirements unclear and likely to change
* Can't afford to throw away prototype work
* System can be built incrementally — each version is usable
* Continuous user involvement is possible

**When NOT to use**

* Safety-critical system — evolved prototypes carry accumulated shortcuts
* Large team — hard to coordinate continuous evolution at scale
* Prototype architecture too weak to evolve into production system

**Advantages**

* No wasted work — prototype becomes the product
* Requirements emerge naturally from use
* Users stay involved throughout
* Fixes both the Waterfall requirements problem and the Throwaway waste problem

**Disadvantages**

* Risk of inheriting poor prototype architecture into production
* Hard to know when you're done — creeping excellence still a risk
* Documentation often neglected

**MediTrack Application — Patient Intake Module**

Instead of gathering requirements in a meeting room, build a rough version of the intake interface and let reception staff use it in their actual rapid patient-flow workflow. Requirements emerge from real use. Each iteration brings the interface closer to what staff actually need.

**Failure mode**

Prototype shortcuts get baked into the production system — accumulating technical debt and making the system fragile as it grows.

**Exam signal words**

Requirements unclear · continuous user feedback · can't afford waste · evolving system · user involvement throughout

***

#### Model 4 — Spiral Model

**What problem does it solve?**

Neither Waterfall nor Prototyping explicitly manages **risk**. The Spiral model puts risk assessment and mitigation at the centre of every single phase. For safety-critical systems, this is not optional.

**How it works**

The spiral moves outward in layers — each loop bigger than the last. Every single layer goes through the same **4-step cycle**:

1. **Determine Objectives** — what are we achieving in this layer? What are the constraints?
2. **Assess and Reduce Risks** — what could go wrong? How likely? How do we reduce it?
3. **Develop and Validate** — choose a development approach, build, test
4. **Review and Plan** — review what was done, plan the next layer

> Risk assessment is not an add-on. It is the core of every cycle.

**The analogy**

Mountaineering expedition — before every stage of the climb, stop and ask: what could kill us here? Avalanche? Weather? Equipment failure? Assess each risk, reduce it, then proceed. Repeat at every camp on the way up.

**The doctor analogy for the 4 steps**

1. Determine objectives — what are we operating on?
2. Assess risks — allergies? Heart condition? Blood type?
3. Perform surgery
4. Review — debrief, plan follow-up

**When to use**

* Large, complex, long-term projects
* Safety-critical systems where failure is catastrophic
* Government or defence projects
* Requirements partially known but risks are high
* Experienced team available

**When NOT to use**

* Small projects — overhead too heavy
* Simple well-understood requirements
* Inexperienced team — risk analysis requires expertise
* Tight budget — lots of documentation and meetings at every layer

**Advantages**

* Risk explicitly identified and reduced at every stage
* Combines best of Waterfall and Prototyping
* Early identification of problems before they become catastrophic
* Strong documentation — good audit trail

**Disadvantages**

* Heavyweight process — lots of meetings, documentation, overhead
* Slow — not suitable for fast delivery
* Depends heavily on quality of risk analysis — bad analysis = bad outcome
* Meta-model — describes _how to manage_ phases, not _how to build_
* Requires experienced team — not for novices

**MediTrack Application — Drug Interaction Engine**

The Drug Interaction Engine is safety-critical — a decimal error in dosage calculation could cause patient harm or fatal overdose. Spiral forces the team to stop at every layer and ask: what could go wrong with this dosage calculation? Wrong renal function input? Edge cases for paediatric patients? Formula errors? These risks are identified and reduced before the feature ships — not discovered after a patient is harmed.

**Why Spiral is superior to Iterative for Drug Interaction Engine**

Pure Iterative Development has no mechanism to stop and ask "what could kill a patient here?" It would simply continue adding features. Spiral's four-step cycle ensures safety is never compromised for delivery speed.

**Failure mode**

Without risk analysis at each layer, critical failure modes are discovered only after deployment — at maximum cost and maximum harm.

**Exam signal words**

Safety-critical · risk management · large government project · experienced team · catastrophic failure possible · long timeline

***

#### Model 5 — Iterative Development

**What problem does it solve?**

When requirements are known but the system is massive — how do you deliver value to users without making them wait years for the whole thing?

**How it works**

1. Design overall architecture upfront — this never changes
2. Identify most critical subset of features
3. Build and deliver that subset — it is a real, working product
4. Get feedback, identify next most critical features
5. Build and deliver next version
6. Repeat — each version bigger than the last

> Every version is production-quality. Nothing is thrown away. You are not building prototypes — you are building real software in prioritised chunks.

**The analogy**

Railway network — open Line 1 first, people start using it, open Line 2, add more stations, keep expanding. The system grows while people are already using it.

**Key distinction from Prototyping**

|              | Prototyping           | Iterative Development              |
| ------------ | --------------------- | ---------------------------------- |
| Each version | Rough prototype       | Real production software           |
| Requirements | Unclear — emerging    | Known upfront                      |
| Purpose      | Discover requirements | Deliver large system incrementally |

**When to use**

* Requirements are known but system is large
* Need to deliver value to users quickly
* Small to medium team
* Architecture can be defined clearly upfront
* Features can be prioritised and delivered independently

**When NOT to use**

* Requirements are unclear — use prototyping first
* Architecture can't be defined early
* Large team needing parallel development
* Safety-critical — no explicit risk management

**Advantages**

* Users get working software early
* Feedback from real use improves each iteration
* Risk reduced — most critical features built first
* Works well with small focused teams

**Disadvantages**

* Needs early architecture — hard to change later
* Doesn't scale to large parallel teams
* No explicit risk management unlike Spiral
* Requirements must be reasonably clear upfront

**Real world example**

Gmail 2004 — started with 3 developers, basic email only for 10 Google employees. Each iteration added most critical missing feature. Architecture (web-based email) set early and never changed. Now 1.8 billion active users.

**Failure mode**

If architecture is wrong at the start, every subsequent iteration builds on a flawed foundation — increasingly expensive to fix as the system grows.

**Exam signal words**

Large known system · early delivery needed · small team · clear architecture · feature prioritisation · working software fast

***

### Complete Model Comparison Table

| Model                        | Core problem solved             | Use when                                          | Don't use when                     | Key weakness                    | Exam signal words                          |
| ---------------------------- | ------------------------------- | ------------------------------------------------- | ---------------------------------- | ------------------------------- | ------------------------------------------ |
| **Waterfall**                | No structure                    | Stable requirements, large team, safety-critical  | Requirements unclear               | Frozen requirements             | Stable · documented · government           |
| **Throwaway Prototyping**    | Wrong requirements              | Requirements unclear                              | Requirements known, tight deadline | Work thrown away                | Unclear requirements · user feedback       |
| **Evolutionary Prototyping** | Wasted prototype work           | Requirements unclear, continuous user involvement | Safety-critical, large team        | Poor architecture in production | Evolving · continuous feedback · no waste  |
| **Spiral**                   | No risk management              | Safety-critical, large complex projects           | Small projects, inexperienced team | Heavy, slow, needs experts      | Safety-critical · risk · experienced team  |
| **Iterative**                | Can't deliver large system fast | Large known system, need early delivery           | Requirements unclear, large team   | Needs early architecture        | Large system · early delivery · small team |

***

### Q1 Model Exam Answers

#### Q1a — 10 marks

_Evaluate the Requirements Gap in the Patient Intake Module using the 3 QA Principles. Identify which was most severely violated and explain how Evolutionary Prototyping acts as a corrective mechanism._

> The most severely violated principle was Principle 2 — Know what you should be doing. MediTrack's Patient Intake Module met its written specifications but was rejected by hospital reception staff because the specifications failed to capture their rapid patient-flow workflow. Requirements were gathered through documentation rather than validated with actual users. Principle 3 was also violated as a downstream consequence — the team measured the system against the wrong specification, so testing passed while real-world use failed. Principle 1 was not violated — the team knew what they were building and delivered it.
>
> Evolutionary Prototyping corrects this by replacing the requirements-gathering meeting with a working prototype deployed directly to reception staff. Instead of writing specifications and hoping they are correct, requirements emerge from real use. Each iteration exposes what the written spec missed — for example, that emergency admissions cannot support lengthy mandatory intake forms. The prototype is never discarded; it is continuously evolved until it matches the actual rapid patient-flow workflow, not just the original written guess.

#### Q1b — 10 marks

_Critically analyse why a Spiral Model approach is superior to pure Iterative Development for the Drug Interaction Engine, focusing on Risk Analysis and Planning phases._

> The Drug Interaction Engine is safety-critical — a single decimal error in dosage calculation based on Serum Creatinine levels could cause patient harm or fatal overdose. This distinguishes it from modules where a bug causes inconvenience rather than death.
>
> The Spiral Model is superior because risk assessment and mitigation are the core of every development cycle, not an afterthought. Before each phase, the team explicitly identifies what could go wrong — incorrect renal function inputs, edge cases in paediatric dosing, formula errors at boundary values — and reduces those risks before proceeding. The four-step cycle of Determine Objectives, Assess and Reduce Risks, Develop and Validate, and Review and Plan ensures no feature ships without explicit safety validation.
>
> Pure Iterative Development has no such mechanism. It would continue adding dosage calculation features iteration by iteration without formally stopping to ask what could cause a fatal error. The Spiral's Planning phase also documents every risk decision — creating an audit trail essential for medical regulatory compliance. For a system where failure costs lives, the overhead of Spiral's heavyweight process is not a disadvantage — it is a requirement.

***

_Problem 1 Complete — Ready for Problem 2: Agile_
