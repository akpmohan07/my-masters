# Page

Now let me extract all the past papers systematically.Excellent — all 6 years extracted cleanly. Now let me analyse the patterns systematically.Now I have everything I need. Let me build the analysis widget.Here's the full picture. Let me summarise the key takeaways:

**The structure is rock-solid across 6 years.** Every paper follows the exact same 5-question layout:

* **Q1 + Q2** are always paired — same program, Q1 is partial correctness, Q2 adds a variant for total correctness. You will see a while loop computing some polynomial result, and you need to: annotate with invariant, list VCs, prove VCs (Q1), then do the same with termination (Q2).
* **Q3** is the one wild card. 4 out of 6 years it's a full step-by-step refinement derivation. 2023 and 2024 switched to asking you to _define a law_ + shorter derivation. 2025 went back to full derivation — so 2026 is unpredictable. You need to prepare both styles.
* **Q4** is always a fresh Event-B specification — context, variables, invariants, events. Always 20 marks split roughly 2–4 / 6 / 10–12.
* **Q5** is Event-B refinement — abstract spec → linking invariant → refined machine. From 2023 onwards they also ask you to write a concrete program. **The linking invariant is now worth up to 8 marks** — it's the crux.

What do you want to tackle first? I'd suggest starting with Q1/Q2 since they're the most predictable and high-confidence marks. Or jump straight to Q5 since the linking invariant is the thing most students drop marks on.



<figure><img src=".gitbook/assets/image (4).png" alt=""><figcaption></figcaption></figure>
