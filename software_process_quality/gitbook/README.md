# Overview

## CSC1137 — Software Process Quality

### Exam Overview & Strategy

> **Important:** You are the first batch taking this module under a revised curriculum. No past papers exist. Only one model question paper is available. This makes the model paper your single most valuable study resource — treat it as gospel.

***

### 1. Exam Structure

| Item                   | Detail                                           |
| ---------------------- | ------------------------------------------------ |
| Total Questions        | 6 questions                                      |
| Marks per Question     | 20 marks                                         |
| Structure per Question | (a) 10 marks + (b) 10 marks                      |
| Total Marks            | 120 marks                                        |
| Format                 | Case study — all questions anchored to MediTrack |
| Answer style           | Essay / application — not MCQ or short answers   |

#### What the examiner is looking for

The exam is **not** a memory test. It is an **application** test.

Every question gives you a MediTrack scenario and asks you to use a concept to solve a real problem. You cannot just define things — you must land every answer inside the case study.

The examiner rewards answers that:

* Show understanding of the concept
* Apply it specifically to the right MediTrack module
* Explain why it works (the mechanism)
* Illustrate with a concrete example

***

### 2. MediTrack Case Study — Quick Reference

> **Exam day rule:** Read the appendix first, then the questions, then annotate. Never pre-generate answers before reading the questions.

#### What is MediTrack?

MediTrack is a cloud-based hospital management platform developed by MediTrack Solutions. It manages patient records, clinical decision support, medication administration, and data migration from legacy hospital systems.

#### The 4 Modules

| Module                               | Core Problem                                                                                                                                                                                                     | Key Exam Relevance                                         |
| ------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------- |
| **Patient Intake Module**            | Requirement gaps — met written spec but rejected by reception staff because it didn't align with rapid patient-flow workflow                                                                                     | Q1 — QA Principles, Evolutionary Prototyping               |
| **Drug Interaction Engine**          | Safety-critical — single decimal error in dosage calculation causes patient harm or fatal overdose. Calculates dosage based on renal function (Serum Creatinine levels 0.6–4.0 mg/dL)                            | Q1 — Spiral Model, Q4 — EP + BVA                           |
| **Legacy Data Migration Module**     | Extracts patient history from legacy systems using outdated data formats and protocols. Burdened by Technical Debt from rushed iterations causing data format mismatches and schema drift                        | Q2 — Pair Programming, Q4 — V\&V                           |
| **Clinical Decision Support Module** | Frequent drug formulary updates (sometimes 1 week notice). Technical Debt — quick poorly documented fixes break existing alert logic. Small team handles emergency bug fixes AND planned features simultaneously | Q2 — XP Practices, Q3 — Scrum vs Kanban, Q6 — Cost of Bugs |

#### Key phrases to use in answers

> _"A single decimal error in dosage calculation could result in patient harm or fatal overdose."_

> _"Errors found after deployment are significantly more expensive to fix than those caught during the initial coding phase."_

> _"The system met the original written specifications but was rejected because it didn't align with the rapid patient-flow workflow."_

***

### 3. The Answer Formula

> **Apply this to every single sub-question across all 6 questions.**

#### Define → Apply → Reason → Example

| Step        | What it means                                                                      | Common trap                                                       |
| ----------- | ---------------------------------------------------------------------------------- | ----------------------------------------------------------------- |
| **Define**  | State the concept cleanly in 1–2 sentences. Your own words, precise and confident  | Writing 5 sentences when 2 would score full marks                 |
| **Apply**   | Connect directly to MediTrack. Name the specific module. Name the specific problem | "This would help a software team" scores nothing. Name the module |
| **Reason**  | Explain _why_ it works — the mechanism. Not just what it does                      | Skipping this and jumping to example — loses depth marks          |
| **Example** | One specific, vivid example from MediTrack or real world                           | Generic examples — "a bank could use this" scores less            |

#### Bad vs Good Answer Example

**Question:** Explain how Evolutionary Prototyping would have helped the Patient Intake Module.

***

**❌ Bad answer — no formula:**

> Evolutionary Prototyping is when you build a prototype and evolve it. It helps get user feedback. The Patient Intake Module had requirement gaps. Prototyping would have helped get better requirements. Users would have been happier with the result.

_Why it fails: vague, circular, gives the examiner nothing to mark._

***

**✅ Good answer — with formula:**

> **Define:** Evolutionary Prototyping is a development approach where an initial rough implementation is continuously refined based on user feedback until it becomes the final production system — unlike Throwaway Prototyping, the prototype is never discarded.

> **Apply:** The Patient Intake Module at MediTrack was rejected by hospital reception staff because it didn't align with their rapid patient-flow workflow, despite meeting the written specification. This is a classic Principle 2 violation — the requirements captured what was written, not what users actually needed.

> **Reason:** Evolutionary Prototyping would have fixed this by replacing the requirements-gathering meeting with a working prototype shown to reception staff in their actual workflow. Requirements would have emerged from real use rather than written guesses — each iteration bringing the interface closer to what staff actually needed.

> **Example:** In the first iteration, staff might have flagged that the intake form had too many mandatory fields for emergency admissions. That feedback would have been incorporated into the next version — evolving the system until it matched the rapid patient-flow reality, not just the original spec.

***

### 4. Exam Day Strategy

#### Reading order

1. **Read the appendix once** — actively, not passively. Absorb the MediTrack story. Understand what's broken and why. (\~3–4 minutes)
2. **Read all 6 questions** — now they make sense because you have context. (\~5 minutes)
3. **Annotate the appendix** — go back and mark which module connects to which question. Quick margin notes like "Q1 — req gap", "Q4 — 0.6–4.0", "Q6 — tech debt". (\~3 minutes)
4. **Answer in order** — skip anything you're stuck on, come back later.

#### Time allocation

| Task                        | Time           |
| --------------------------- | -------------- |
| Read appendix               | 4 mins         |
| Read all questions          | 5 mins         |
| Annotate appendix           | 3 mins         |
| Per sub-question (10 marks) | \~10–12 mins   |
| Review                      | remaining time |

#### What to annotate in the appendix

* Which module maps to which question
* Specific numbers (0.6–4.0 mg/dL for Q4)
* Key failure descriptions ("requirement gap", "technical debt", "safety-critical")

#### Golden rule

**Read each question twice before writing anything.**

* First read — understand what topic is being tested
* Second read — underline the specific constraints

> The marks are hiding in the specific words. Most students read once and answer the general topic instead of the actual question.

***

### 5. The 4 Parent Problems — Map

> Everything in this module is an answer to one question: _How do you build software that doesn't fail?_ Each parent problem attacks it from a different angle.

| Problem                            | Plain English                         | Exam Questions | Marks | Weeks             |
| ---------------------------------- | ------------------------------------- | -------------- | ----- | ----------------- |
| **Problem 1** — Software Processes | How do you organise the work?         | Q1             | 20    | Wk 1 + 2          |
| **Problem 2** — Agile              | How do you work as a team, fast?      | Q2 + Q3        | 40    | Wk 3 + 4          |
| **Problem 3** — Software Testing   | How do you know the software works?   | Q4 + Q5        | 40    | Wk 5, 6, 8, 9, 11 |
| **Problem 4** — Verification       | How do you catch what testing misses? | Q6             | 20    | Wk 10             |

> **MediTrack is a company that failed at all 4 simultaneously.** Each question asks you to diagnose one failure and prescribe one solution.

***

### 6. Quick Reference — All 6 Questions

| Q       | Topic                                    | Key Concepts                                                          | MediTrack Module                         |
| ------- | ---------------------------------------- | --------------------------------------------------------------------- | ---------------------------------------- |
| **Q1a** | QA Principles + Evolutionary Prototyping | 3 QA Principles, Requirements Gap, Evolutionary Prototyping           | Patient Intake Module                    |
| **Q1b** | Spiral vs Iterative                      | Spiral Model, Risk Analysis, Safety-Critical, Planning phases         | Drug Interaction Engine                  |
| **Q2a** | XP Practices                             | Simple Design, Refactoring, Technical Debt                            | Clinical Decision Support Module         |
| **Q2b** | Pair Programming                         | Continuous Informal Review, Collective Ownership, Technical Debt      | Legacy Data Migration Module             |
| **Q3a** | Scrum vs Kanban                          | WIP Limits, Kanban flow, Scrum sprints, Team burnout                  | Compliance Reports vs Emergency Alerts   |
| **Q3b** | Scrum Ceremonies                         | Daily Standups, Sprint Retrospectives, Scrum Master role              | Prescription Engine integration failures |
| **Q4a** | Black Box Testing                        | Equivalence Partitioning, Boundary Value Analysis, 0.6–4.0 mg/dL      | Drug Interaction Engine                  |
| **Q4b** | V\&V + Testing Levels                    | Validation, Verification, Unit Testing, System Testing                | Legacy Data Migration Module             |
| **Q5a** | White Box Testing                        | Statement Coverage, Path Coverage, reliability risks                  | Clinical Decision Support Module         |
| **Q5b** | Test Automation                          | Regression Testing, consistency, scalability, bi-weekly release cycle | MediTrack release cycle                  |
| **Q6a** | Cost of Bugs + Formal Inspection         | IBM cost model, €400 × multiplier, Moderator/Reader/Recorder          | Dosage Adjustment Logic                  |
| **Q6b** | Fagan vs Walkthrough                     | Fagan Inspection, Code Walkthrough, checklist-directed approach       | Patient Data Privacy Logic               |

***

### 7. Priority Study Order

> Tackle what you **can't fake** first. Leave what you can reason through for later.

| Priority  | Topic                           | Question | Why                                           |
| --------- | ------------------------------- | -------- | --------------------------------------------- |
| 🔴 High   | Fagan Inspection + Cost of Bugs | Q6       | Specific roles + calculation — can't bluff    |
| 🔴 High   | Equivalence Partitioning + BVA  | Q4a      | Technical, right/wrong answer, lockable marks |
| 🔴 High   | Spiral Model + QA Principles    | Q1       | Root of everything, heavily applied           |
| 🟡 Medium | Technical Debt + XP Practices   | Q2       | Cross-cutting theme, appears in Q2 + Q6       |
| 🟡 Medium | Scrum + Kanban + WIP Limits     | Q3       | Familiar but needs precise application        |
| 🟡 Medium | Statement vs Path Coverage      | Q5a      | Technical, needs practice                     |
| 🟢 Lower  | Regression Testing + Automation | Q5b      | Reasonably intuitive                          |
| 🟢 Lower  | V\&V + Testing Levels           | Q4b      | Conceptually straightforward                  |

***

_Generated during study session — CSC1137 First Batch_ _Study approach: Learn concept → Apply to MediTrack → Practice exam answer_
