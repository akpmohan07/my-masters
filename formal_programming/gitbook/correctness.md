# Correctness

Analyzing these patterns is key to mastering Hoare Logic and formal verification for your MSc at DCU. By looking at these examples, we can categorize them into three main "intuition patterns" to find the invariant ($$ $I$ $$) every time. test

***

#### Pattern 1: The Accumulator (Counting Up)

Applied to: `image_966979.png` (Square), `image_966997.png` (Sum), `image_9669b9.png` (Cubic sum)

In these, a counter (like $Z$, $J$, or $Y$) starts at $0$ and moves toward $N$.

* Logic: The invariant is simply the Post-condition where you replace the final goal ($$ $N$ $$) with the current counter.
* Intuition: "The variable holds the correct result for the steps I have finished so far."
* Examples:
  * Square (`image_966979.png`): Goal is $$ $X = N^2$ $$. Counter is $$ $Z$ $$.
    * Invariant: $$ $X = Z^2 \wedge Z \leq N$ $$.
  * Sum (`image_966997.png`): Goal is $$ $SUM = N(N+1)$ $$. Counter is $$ $J$ $$.
    * Invariant: $$ $SUM = J(J+1) \wedge J \leq N$ $$.
  * Cubic Sum (`image_9669b9.png`): Goal is a complex formula for $$ $N$ $$. Counter is $$ $Y$ $$.
    * Invariant: $$ $X = Y^3/3 + Y^2/2 + Y/6 \wedge Y \leq N$ $$.

***

#### Pattern 2: The "Distance Remaining" (Counting Down)

Applied to: `image_9669da.png` (Square), `image_9669d4.png` (Power)

In these, a variable starts at $$ $N$ $$ and moves toward $$ $0$ $$.

* Logic: Use $$ $(N - \text{current\_counter})$ $$ to represent "steps completed."
* Intuition: "I have finished $$ $(N-X)$ $$ work, so my variable equals the goal function applied to $$ $(N-X)$ $$."
* Example (Square `image_9669da.png`): \* Goal is $$ $Y = N^2$ $$. Counter $$ $X$ $$ goes from $$ $N \to 0$ $$.
  * Steps finished = $$ $(N - X)$ $$.
  * Invariant: $$ $Y = (N - X)^2 \wedge X \geq 0$ $$.
  * _Alternative (The Balance View):_ $$ $Y + X^2 = N^2$ $$.

***

#### Pattern 3: The Multi-Variable Link

Applied to: `image_966979.png` (where $$ $X$ $$ and $$ $Y$ $$ change)

Sometimes two variables change together, and you must find the link between them first.

* Logic: Find the "companion" relationship before plugging it into the main goal.
* Example: In `image_966979.png`, $$ $Y$ $$ is always the "next odd number."
  * Link: $$ $Y = 2Z + 1$ $$.
  * Main Invariant: $$ $X = Z^2$ $$.
  * Combined: $$ $(X = Z^2 \wedge Y = 2Z + 1 \wedge Z \leq N)$ $$.

***

#### Cheat Sheet for Your Exam

| **If the loop...**                | **Then use this mental model:** | **Invariant Structure**                           |
| --------------------------------- | ------------------------------- | ------------------------------------------------- |
| Increments ($$ $i: 0 \to N$ $$)   | "Work already done."            | $$ $Var = f(i)$ $$                                |
| Decrements ($$ $X: N \to 0$ $$)   | "Steps completed so far."       | $$ $Var = f(N - X)$ $$                            |
| Multiplies ($$ $P: 1 \to Y^N$ $$) | "Balance what's left."          | $$ $P \times Y^X = Y^N$ $$ or $$ $P = Y^{N-X}$ $$ |

#### Summary of finding any Invariant:

1. Identify the counter and its direction ($$ $0 \to N$ $$ or $$ $N \to 0$ $$).
2. Write the "Steps Completed" expression ($$ $i$ $$ or $$ $N-X$ $$).
3. Plug that expression into the Post-condition formula.
4. Add the boundary check (e.g., $$ $Z \leq N$ $$ or $$ $X \geq 0$ $$).

Which of these three patterns do you find most difficult to visualize during a timed exam?
