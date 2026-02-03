package com.mohanverse.dev;

import java.util.Random;
import java.util.concurrent.RecursiveTask;

/*
Here's a visual example of how the Fork/Join algorithm works with a small dataset:

## Sample Execution Flow

**Input Array:** `[23, 67, 45, 12, 89, 34, 56, 78, 91, 15]` (10 elements)

### Fork/Join Tree Structure

```
                    [0-10] → max(89, 91) = 91
                   /                        \
            [0-5] = 89                    [5-10] = 91
           /          \                   /           \
      [0-2]=67     [2-5]=89          [5-7]=78      [7-10]=91
      /    \       /    \            /    \        /      \
  [0-1]  [2-3]  [3-5]  [5-6]    [6-7]  [7-8]  [8-10]
   67     45     89     56        78     91     (direct)
(direct)(direct)(direct)(direct)(direct)(direct)  91
  23,67  45,12  12,89   34,56    56,78  78,91   91,15
```

### Step-by-Step Breakdown

```
Step 1: Initial split at index 5
├─ LEFT [0-5]:  [23, 67, 45, 12, 89]  → fork()
└─ RIGHT [5-10]: [34, 56, 78, 91, 15] → compute()

Step 2: LEFT [0-5] splits at index 2
├─ LEFT [0-2]:  [23, 67] (< threshold) → computeDirectly() = 67
└─ RIGHT [2-5]: [45, 12, 89] → computeDirectly() = 89

Step 3: RIGHT [5-10] splits at index 7
├─ LEFT [5-7]:  [34, 56] → computeDirectly() = 56
└─ RIGHT [7-10]: [78, 91, 15] → computeDirectly() = 91

Result: max(89, 91) = 91
```

### Thread Activity (with 4 threads)

```
Thread-1: [0-2]   → 67
Thread-2: [2-5]   → 89
Thread-3: [5-7]   → 56
Thread-4: [7-10]  → 91

Final comparison: max(67, 89, 56, 91) = 91
```

### Console Output Example

```
Thread[ForkJoinPool-1-worker-1] computing: 0 to 2
Thread[ForkJoinPool-1-worker-2] computing: 2 to 5
Thread[ForkJoinPool-1-worker-3] computing: 5 to 7
Thread[ForkJoinPool-1-worker-4] computing: 7 to 10
Maximum value found: 91
```

The algorithm recursively divides the array until segments are smaller than the threshold (5), then processes them in parallel and combines results using `Math.max()`.
 */

public class ForkJoinPool extends RecursiveTask<Integer> {
    private static final int SEQUENTIAL_THRESHOLD = 5;
    private final int[] data;
    private final int start;
    private final int end;

    public ForkJoinPool(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    public ForkJoinPool(int[] data) {
        this(data, 0, data.length);
    }

    @Override
    protected Integer compute() {
        final int length = end - start;
        if (length < SEQUENTIAL_THRESHOLD) {
            return computeDirectly();
        }

        final int split = length / 2;
        final ForkJoinPool left = new ForkJoinPool(data, start, start + split);
        left.fork(); // Asynchronously execute the left task

        final ForkJoinPool right = new ForkJoinPool(data, start + split, end);

        // Compute right task and then join the result of the left task
        return Math.max(right.compute(), left.join());
    }

    private Integer computeDirectly() {
        System.out.println(Thread.currentThread() + " computing: " + start + " to " + end);
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        return max;
    }

    public static void main(String[] args) {
        // Create a random dataset
        final int[] data = new int[1000000]; // Reduced size slightly for readable console output
        final Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(100);
        }

        // Submit the task to the pool
        final java.util.concurrent.ForkJoinPool pool = new java.util.concurrent.ForkJoinPool(4);
        final ForkJoinPool finder = new ForkJoinPool(data);

        System.out.println("Maximum value found: " + pool.invoke(finder));
    }
}


