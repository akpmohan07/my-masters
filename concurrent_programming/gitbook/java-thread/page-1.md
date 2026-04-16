# Page 1

## CSC1141 — Concurrent Programming

### Java Threads — Complete Revision Notes

**Mohan | DCU | April 2026** 8 Topics · 9 Code Patterns · All Past Paper Q\&As · Compare Tables

***

### Table of Contents

1. [Processes & Threads + Thread Creation](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#topic-1)
2. [Scheduling + Cooperative Multitasking](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#topic-2)
3. [Race Conditions + Critical Sections](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#topic-3)
4. [Synchronisation](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#topic-4)
5. [Deadlock + Dining Philosophers](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#topic-5)
6. [Lock Objects](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#topic-6)
7. [wait/notify + Condition Variables + Producer-Consumer](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#topic-7)
8. [Executors + Fork/Join](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#topic-8)
9. [Missing Topics](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#missing-topics)
10. [Master Compare Tables](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#master-compare-tables)
11. [Past Paper Q\&A](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#past-paper-qa)
12. [Weak Spots](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#weak-spots)
13. [Spaced Repetition Schedule](https://claude.ai/chat/e9e81022-de07-4265-a59d-e999ff048ce3#spaced-repetition)

***

\<a name="topic-1">\</a>

### Topic 1 — Processes & Threads + Thread Creation

#### One-liner

A process is a self-contained program with its own memory. A thread is a lightweight execution unit inside a process — all threads share the same memory.

#### Analogy

A process is a kitchen. Threads are chefs working in that kitchen — they share the same fridge (memory) but cook independently.

#### extends Thread vs implements Runnable

|             | extends Thread                 | implements Runnable                  |
| ----------- | ------------------------------ | ------------------------------------ |
| What it is  | The thread IS the task         | The task is separate from the thread |
| Inheritance | Uses your one inheritance slot | Free to extend another class         |
| Reuse       | One instance = one thread      | One instance → many threads          |
| Preferred?  | No                             | Yes — more flexible                  |

#### Key code

```java
// Approach 1 — extends Thread
class BasicThread extends Thread {
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}
new BasicThread().start();

// Approach 2 — implements Runnable (preferred)
class MyTask implements Runnable {
    public void run() {
        try {
            Thread.sleep(1000);   // give up CPU for 1 second
        } catch (InterruptedException e) {
            return;               // stop cleanly on interrupt
        }
        System.out.println(Thread.currentThread().getName());
    }
}
Thread t1 = new Thread(new MyTask());
Thread t2 = new Thread(new MyTask());  // same task, two threads
t1.start();
t2.start();
```

#### Exam angle

> 📝 **2019, 2021, 2025 — 6 marks** "Describe the two approaches to creating a Java Thread. Which is preferred and why?"

**Expected answer:**

* Approach 1: extend Thread — override run(), call start()
* Approach 2: implement Runnable — pass to new Thread(), call start()
* Runnable preferred: separates task from thread, doesn't waste inheritance slot, one Runnable can be shared across multiple threads
* Always call `start()` not `run()` — `run()` executes on the current thread, not a new one

#### Watch out for

> ❌ **Common mistake:** Calling `run()` instead of `start()`. `run()` executes synchronously on the calling thread — no new thread is created.

***

\<a name="topic-2">\</a>

### Topic 2 — Scheduling + Cooperative Multitasking

#### One-liner

The JVM uses preemptive priority-based scheduling. Threads voluntarily yield control via sleep/yield — this voluntary release is called cooperative multitasking.

#### How it works

* **Preemptive:** JVM always runs highest priority thread (1–10). Tie → FIFO. Higher priority unblocks → preempts current thread immediately.
* **Cooperative:** threads voluntarily release CPU via `yield()` or `sleep()` — letting other threads run.

#### yield / sleep / interrupt / join

| Method             | What it does                                                                 | JVM can ignore? |
| ------------------ | ---------------------------------------------------------------------------- | --------------- |
| `Thread.yield()`   | Hint to JVM: I'm not critical, let others run. Thread stays RUNNABLE.        | Yes             |
| `Thread.sleep(ms)` | Suspend for fixed time. Releases CPU. Throws InterruptedException.           | No              |
| `t.interrupt()`    | Signal thread to stop. Sets interrupted flag or throws InterruptedException. | No              |
| `t.join(ms)`       | Current thread waits for t to finish. Timeout optional.                      | No              |

#### Key code

```java
class CoopTask implements Runnable {
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " step " + i);
            Thread.yield();          // cooperative — let others run
        }
    }
}

// join with timeout — patience pattern
Thread t = new Thread(new CoopTask());
t.start();
long start = System.currentTimeMillis();
while (t.isAlive()) {
    t.join(1000);                    // wait max 1 second
    if (System.currentTimeMillis() - start > 5000 && t.isAlive()) {
        t.interrupt();               // tired of waiting — signal to stop
        t.join();
    }
}
```

#### Exam angle

> 📝 **2019, 2021, 2025 — 6 marks** "What is cooperative multitasking? How does this relate to priority? Illustrate using Java Threads."

**Expected answer — three parts:**

1. **Preemptive:** JVM picks highest priority (1–10). FIFO on tie. Preempts lower priority when higher priority unblocks.
2. **Cooperative:** threads voluntarily release CPU via `yield()` or `sleep()` — this is cooperative multitasking.
3. **Code:** show Runnable with `sleep()` or `yield()`, two threads started with different priorities.

```java
Thread t1 = new Thread(new Task(), "High");
Thread t2 = new Thread(new Task(), "Low");
t1.setPriority(8);  // higher priority — runs first
t2.setPriority(3);
t1.start();
t2.start();
```

#### Watch out for

> ⚠️ **Key distinction:** Cooperative = voluntary. Preemptive = forced. The exam wants both named and distinguished.

***

\<a name="topic-3">\</a>

### Topic 3 — Race Conditions + Critical Sections

#### One-liner

A race condition occurs when two threads access shared state simultaneously and the result depends on the order of execution. The critical section is the entire read-modify-write block that must not be interleaved.

#### Real-world analogy (Kafka)

Multiple Kafka consumers reading a partition, each doing: read count from DB → add processed → write back. Two consumers read the same count before either writes → one overwrites the other → count mismatch. Same root cause as the banking example.

#### The banking interleaving — why €1000 disappears

| Thread 1           | Thread 2           | balance                   |
| ------------------ | ------------------ | ------------------------- |
| load balance → 500 |                    | 500                       |
| add 100 → 600      |                    | 500                       |
|                    | load balance → 500 | 500                       |
| store → 600        |                    | 600                       |
|                    | add 1000 → 1500    | 600                       |
|                    | store → 1500       | **1500 ❌ should be 1600** |

#### Key code — demonstrating race condition

```java
// WITHOUT synchronized — race condition
public void deposit(int amount, int account) {
    int current = accounts[account];     // READ
    try { Thread.sleep(10); }           // artificial delay — opens race window
    catch (InterruptedException e) {}
    accounts[account] = current + amount;  // WRITE — race happens here
}

// Fix — add synchronized
public synchronized void deposit(int amount, int account) {
    accounts[account] += amount;         // atomic under lock
}
```

#### Exam angle

> 📝 **2024 — 4 marks** "Using an example, describe critical sections."

**Expected answer:**

* Define: a critical section is a block of code that reads and updates shared state — it must complete atomically without another thread interleaving.
* Show the bad interleaving: load balance → add → store across two threads.
* Name the consequence: corrupted shared state (missing money, wrong count).
* Solution: protect with `synchronized` so only one thread executes the critical section at a time.

#### Watch out for

> ⚠️ **Critical section scope:** It is NOT just the add line. It is the entire read-modify-write block. Partial protection still allows corruption.

***

\<a name="topic-4">\</a>

### Topic 4 — Synchronisation

#### One-liner

Java uses the monitor concept — every object has an intrinsic lock. `synchronized` ensures only one thread executes a protected section at a time on the same object.

#### synchronized method vs statement vs separate locks

| Form                              | What it locks                          | When to use                                                |
| --------------------------------- | -------------------------------------- | ---------------------------------------------------------- |
| `synchronized` method             | Entire object (this)                   | When all methods share same resource                       |
| `synchronized(this)` statement    | Entire object, but only for that block | When some code is non-critical and can run concurrently    |
| `synchronized(lockObj)` statement | Only the specific lock object          | When two independent resources should not block each other |

#### Key code

```java
// Form 1 — synchronized method
public class BankAccount {
    private float balance = 0;

    public synchronized void deposit(float amount) {
        balance += amount;  // only one thread at a time on this object
    }
}

// Form 2 — synchronized statement (finer granularity)
public void deposit(String name, float amount) {
    if (checkOwner(name)) {          // non-critical — can run concurrently
        synchronized(this) {
            balance += amount;       // critical — protected
        }
    }
}

// Form 3 — separate lock objects (independent resources)
private long c1 = 0, c2 = 0;
private Object lock1 = new Object();
private Object lock2 = new Object();

public void inc1() { synchronized(lock1) { c1++; } }  // doesn't block inc2
public void inc2() { synchronized(lock2) { c2++; } }  // doesn't block inc1
```

#### Reentrant synchronization

A thread can acquire a lock it already owns — prevents deadlocking itself when calling synchronized methods recursively. Java counts acquisitions and only releases when count hits 0.

#### Atomic access

| Type                      | Atomic? | Notes                                         |
| ------------------------- | ------- | --------------------------------------------- |
| int, boolean, float, etc. | Yes     | All primitives except long and double         |
| long, double              | No      | 64-bit — two 32-bit operations on 32-bit JVMs |
| volatile long/double      | Yes     | volatile makes all types atomic               |
| references                | Yes     | Reading/writing object references is atomic   |

#### Exam angle

> 📝 **2023 — 4 marks** "Describe synchronized methods and synchronized statements with commented code examples."

> 📝 **2024 — 10 marks** "Describe synchronized methods (i) and synchronized statements (ii) with examples."

#### Watch out for

> ❌ **IllegalMonitorStateException:** Calling `wait()`/`notify()` outside a `synchronized` block. You must own the lock before calling these methods.

***

\<a name="topic-5">\</a>

### Topic 5 — Deadlock + Dining Philosophers

#### One-liner

Deadlock: two or more threads each holding a resource the other needs — circular wait — nobody moves forever.

#### Dining Philosophers setup

* 5 philosophers, 5 forks, need both left and right fork to eat
* Deadlock scenario: all pick up left fork simultaneously → all wait for right → circular wait → deadlock

#### Three solutions

| Solution               | Mechanism                                                            | Weakness                  |
| ---------------------- | -------------------------------------------------------------------- | ------------------------- |
| 1. Asymmetric          | One philosopher picks right before left. Breaks circular dependency. | More complex logic        |
| 2. tryLock + release   | Acquire both or release all. Non-blocking attempt.                   | Starvation still possible |
| 3. Capacity constraint | Only 4 philosophers at table (semaphore). At least one always eats.  | Reduces throughput        |

#### Key code — synchronized solution (asymmetric ordering)

```java
protected void eat() {
    // always acquire lower-id chopstick first — breaks circular wait
    Chopstick first  = id < (id + 1) % chopsticks.length ? getLeft() : getRight();
    Chopstick second = id < (id + 1) % chopsticks.length ? getRight() : getLeft();

    synchronized(first) {              // acquire first chopstick
        synchronized(second) {         // acquire second chopstick
            try { Thread.sleep(1000); } // eat
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }                              // release second
    }                                  // release first
}
```

#### Key code — tryLock solution (GraceousPhilosopher)

```java
protected void eat() {
    Lock firstLock  = chopstickLocks.get(chopstickOrder[0]);
    Lock secondLock = chopstickLocks.get(chopstickOrder[1]);
    boolean gotFirst = false, gotSecond = false;
    try {
        gotFirst  = firstLock.tryLock();   // non-blocking attempt
        gotSecond = secondLock.tryLock();  // non-blocking attempt
    } finally {
        if (gotFirst && gotSecond) {
            try { Thread.sleep(1000); }    // eat — got both forks
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        // ALWAYS release in finally — even if exception thrown
        if (gotFirst)  firstLock.unlock();
        if (gotSecond) secondLock.unlock();
    }
}
```

#### Exam angle

> 📝 **2022 — 10 marks** "Write a symmetric solution to Dining Philosophers where a philosopher releases their fork if unable to secure both."

#### Watch out for

> ⚠️ **Starvation:** `tryLock` solution has no deadlock but a philosopher could keep losing the race and never eat. Mention this in your answer.

***

\<a name="topic-6">\</a>

### Topic 6 — Lock Objects

#### One-liner

Lock objects (`ReentrantLock`) give you everything `synchronized` does plus: non-blocking `tryLock()`, interruptible locking, and per-object Condition Variables.

#### synchronized vs Lock methods

| Method                     | Waits?        | Backs out?                      | Interruptible while waiting?                  |
| -------------------------- | ------------- | ------------------------------- | --------------------------------------------- |
| `synchronized`             | Forever       | No                              | No — interrupt ignored while waiting for lock |
| `lock.lock()`              | Forever       | No                              | No — same as synchronized                     |
| `lock.tryLock()`           | No            | Yes — returns false immediately | N/A                                           |
| `lock.tryLock(t, unit)`    | Up to timeout | Yes — returns false on timeout  | No                                            |
| `lock.lockInterruptibly()` | Yes           | No                              | Yes — throws InterruptedException             |

#### Two advantages over synchronized

1. `tryLock()` — back out if lock not available. `synchronized` has no escape.
2. `lockInterruptibly()` — respond to interrupt **while waiting to acquire the lock**. `synchronized` ignores interrupts at that point.

#### Reentrant counter

```java
lock.lock();       // count = 1
    lock.lock();   // same thread — count = 2 (doesn't block itself)
    lock.unlock(); // count = 1
lock.unlock();     // count = 0 — truly released, others can acquire
```

#### Key code — always use finally

```java
lock.lock();
try {
    // do work — exception may be thrown here
} finally {
    lock.unlock();  // ALWAYS runs — lock never leaked
}

// tryLock pattern
if (lock.tryLock()) {
    try { /* do work */ }
    finally { lock.unlock(); }
} else {
    // couldn't get lock — do something else
}
```

#### Exam angle

> 📝 **2022 — 5 marks** "Describe Lock objects in Java. What advantages do they have over synchronized?"

#### Watch out for

> ❌ **Missing finally:** If you don't put `unlock()` in `finally` and an exception is thrown, the lock leaks. All waiting threads block forever. David looks for this.

***

\<a name="topic-7">\</a>

### Topic 7 — wait/notify + Condition Variables + Producer-Consumer

#### One-liner

`wait/notify` is a thread communication mechanism. Condition Variables extend this with separate wait sets per lock — essential for Producer-Consumer where producers and consumers must wake up independently.

#### wait/notify vs await/signal

| wait/notify (synchronized)    | await/signal (Lock + Condition) | Purpose                                  |
| ----------------------------- | ------------------------------- | ---------------------------------------- |
| `object.wait()`               | `condition.await()`             | Release lock, go to sleep until notified |
| `object.notify()`             | `condition.signal()`            | Wake up ONE waiting thread               |
| `object.notifyAll()`          | `condition.signalAll()`         | Wake up ALL waiting threads              |
| One wait set per object       | Separate wait set per Condition | Condition allows targeted wakeup         |
| Must be inside `synchronized` | Must be inside `lock.lock()`    | Both require owning the lock             |

#### Three queues inside a Lock

* **Queue 1** — threads waiting to acquire the lock (outside the door)
* **Queue 2** — threads in `notFull` waiting room (producers waiting for space)
* **Queue 3** — threads in `notEmpty` waiting room (consumers waiting for items)

`await()` moves thread from inside-lock → waiting room AND releases lock. `signal()` moves ONE thread from waiting room → Queue 1. `unlock()` lets one thread from Queue 1 → inside lock.

#### Producer-Consumer flow

```
Producer — put():                    Consumer — take():
─────────────────────                ──────────────────────
lock()                               lock()
buffer full? → notFull.await()       buffer empty? → notEmpty.await()
  releases lock, sleeps                releases lock, sleeps
  re-acquires on wakeup               re-acquires on wakeup
  rechecks (while loop)               rechecks (while loop)
put item into buffer                 take item from buffer
count++                              count--
notEmpty.signal() → wake consumer    notFull.signal() → wake producer
unlock()                             unlock()
```

#### Key code — BoundedBuffer

```java
class BoundedBuffer {
    final Lock lock          = new ReentrantLock();
    final Condition notFull  = lock.newCondition();  // producers wait here
    final Condition notEmpty = lock.newCondition();  // consumers wait here
    final Object[] items     = new Object[10];
    int putptr, takeptr, count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)  // while not if — spurious wakeups
                notFull.await();           // buffer full — producer waits
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;  // circular buffer
            ++count;
            notEmpty.signal();             // wake ONE consumer
        } finally { lock.unlock(); }       // ALWAYS in finally
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)             // while not if — spurious wakeups
                notEmpty.await();          // buffer empty — consumer waits
            Object x = items[takeptr];
            if (++takeptr == items.length) takeptr = 0;  // circular buffer
            --count;
            notFull.signal();              // wake ONE producer
            return x;
        } finally { lock.unlock(); }
    }
}
```

#### Why while not if

> ⚠️ **Spurious wakeups:** A thread can wake up without being signalled. `while()` rechecks the condition — `if()` assumes it's met and proceeds unsafely.

#### Exam angle

> 📝 **2023 — 10 marks** "Describe the Producer-Consumer problem and implement a solution in Java using Condition Variables."

> 📝 **2023 — 6 marks** "Describe the wait-notify mechanism and Condition Variables."

#### Watch out for

> ❌ **IllegalMonitorStateException:** Calling `await()`/`signal()` without holding the lock, OR calling `wait()`/`notify()` outside `synchronized` block.

***

\<a name="topic-8">\</a>

### Topic 8 — Executors + Fork/Join

#### One-liner

`ExecutorService` manages a thread pool for task execution. Fork/Join extends this with work-stealing for divide-and-conquer parallel algorithms.

#### Executor interfaces

| Interface                  | Adds                                           | Use when                                  |
| -------------------------- | ---------------------------------------------- | ----------------------------------------- |
| `Executor`                 | `execute(Runnable)`                            | Simple fire-and-forget task submission    |
| `ExecutorService`          | `submit()`, `shutdown()`, `awaitTermination()` | Need lifecycle management, Future results |
| `ScheduledExecutorService` | `schedule()`, `scheduleAtFixedRate()`          | Delayed or periodic task execution        |

#### Runnable vs Callable

|                           | Runnable                  | Callable\<V>                |
| ------------------------- | ------------------------- | --------------------------- |
| Method                    | `void run()`              | `V call() throws Exception` |
| Returns value?            | No                        | Yes — via `Future<V>`       |
| Throws checked exception? | No                        | Yes                         |
| Submit with               | `execute()` or `submit()` | `submit()` only             |
| Fork/Join equivalent      | `RecursiveAction`         | `RecursiveTask<V>`          |

#### RecursiveTask vs RecursiveAction

|                | RecursiveTask\<V>           | RecursiveAction              |
| -------------- | --------------------------- | ---------------------------- |
| Returns value? | Yes                         | No                           |
| Method         | `protected V compute()`     | `protected void compute()`   |
| Like           | Callable                    | Runnable                     |
| Example        | MaxFinder — returns Integer | ArraySorter — sorts in place |

#### shutdown vs shutdownNow

| Method          | Accepts new tasks? | Running tasks?   | Queued tasks?                   |
| --------------- | ------------------ | ---------------- | ------------------------------- |
| `shutdown()`    | No                 | Lets them finish | Lets them finish                |
| `shutdownNow()` | No                 | Interrupts them  | Returns as List — never started |

#### Key code — ExecutorService banking program

```java
public class BankAccount {
    private int[] accounts = new int[10];
    private Object[] locks  = new Object[10];

    public BankAccount() {
        for (int i = 0; i < 10; i++) {
            accounts[i] = 100;
            locks[i] = new Object();  // one lock per account — no unnecessary blocking
        }
    }

    public void deposit(int amount, int acc) {
        synchronized(locks[acc]) { accounts[acc] += amount; }
    }
    public void withdraw(int acc, int amount) {
        synchronized(locks[acc]) { accounts[acc] -= amount; }
    }
    public void query(int acc) {
        synchronized(locks[acc]) {
            System.out.println("Account " + acc + ": " + accounts[acc]);
        }
    }
}

// main
BankAccount bank = new BankAccount();
ExecutorService executor = Executors.newFixedThreadPool(3);  // 3 users

for (int user = 0; user < 3; user++) {
    executor.execute(() -> {
        for (int i = 0; i < 20; i++) {
            int acc = (int)(Math.random() * 10);
            int op  = (int)(Math.random() * 3);
            if (op == 0)      bank.deposit(100, acc);
            else if (op == 1) bank.withdraw(acc, 50);
            else              bank.query(acc);
        }
    });
}
executor.shutdown();
executor.awaitTermination(10, TimeUnit.SECONDS);  // wait for all users to finish
```

#### Key code — Fork/Join MaxFinder

```java
public static class MaxFinder extends RecursiveTask<Integer> {  // static — no outer ref
    private static final int THRESHOLD = 5;
    private final int[] data;
    private final int start, end;

    public MaxFinder(int[] data, int start, int end) {
        this.data = data; this.start = start; this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start < THRESHOLD)
            return computeDirectly();       // small enough — do sequentially

        int mid = (start + end) / 2;
        MaxFinder left  = new MaxFinder(data, start, mid);
        MaxFinder right = new MaxFinder(data, mid + 1, end);

        left.fork();                        // left runs on another thread
        return Math.max(right.compute(),    // right runs on THIS thread — no waste
                        left.join());       // wait for left result
    }

    private Integer computeDirectly() {
        int max = Integer.MIN_VALUE;
        for (int i = start; i <= end; i++)
            if (data[i] > max) max = data[i];
        return max;
    }
}

// usage
ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
int max = pool.invoke(new MaxFinder(data, 0, data.length - 1));
```

#### Why fork left, compute right — not fork both?

```java
// WRONG — current thread sits idle
left.fork();
right.fork();
left.join();   // just waiting — wasted thread
right.join();

// RIGHT — current thread stays busy
left.fork();                    // left on another thread
right.compute();                // right on THIS thread — no waste
left.join();                    // by now left may already be done
```

#### Work stealing

When a thread finishes its tasks, it steals tasks from the tail of another busy thread's queue. No idle threads. Key difference between `ForkJoinPool` and `ExecutorService`.

#### Exam angle

> 📝 **2021, 2025 — 14 marks** "Describe the different approaches to executing Java threads on different cores. Give brief examples of each approach."

> 📝 **2022, 2024 — 6 marks** "Describe the Fork-Join Framework. What is its purpose? How does it differ from the Executor Interface?"

> 📝 **2024 — 14 marks** "Write a banking program with 10 accounts and 3 users. Each user managed by a separate thread. Must be thread-safe."

#### Watch out for

> ⚠️ **Static inner class:** ForkJoin tasks should be `static` inner classes. Non-static holds implicit reference to outer instance — memory overhead when millions of tasks created.

***

\<a name="missing-topics">\</a>

### Missing Topics (Low Exam Frequency — Know One-Liners)

#### Thread States

| State          | Meaning                                | How to get there                    |
| -------------- | -------------------------------------- | ----------------------------------- |
| NEW            | Created but not started                | `new Thread()`                      |
| RUNNABLE       | Running or ready to run                | `thread.start()`                    |
| BLOCKED        | Waiting to acquire a synchronized lock | Another thread holds the lock       |
| WAITING        | Waiting indefinitely for notification  | `wait()`, `join()` with no timeout  |
| TIMED\_WAITING | Waiting for specified time             | `sleep(ms)`, `join(ms)`, `wait(ms)` |
| TERMINATED     | Finished execution                     | `run()` returns or throws           |

#### Atomic Access + volatile

* **Atomic by default:** all primitives except `long` and `double`. All reference reads/writes.
* **volatile:** makes `long`/`double` atomic AND guarantees visibility across threads (no CPU cache staleness).

```java
private volatile long counter = 0;  // atomic read/write + visible across all threads
// Note: volatile does NOT make compound operations (counter++) atomic
// For compound atomics use AtomicLong or synchronized
```

#### isAlive() patience pattern

```java
while (t.isAlive()) {
    t.join(1000);                        // wait max 1 second
    if (timeoutExceeded && t.isAlive()) {
        t.interrupt();                   // signal to stop
        t.join();                        // wait for clean stop
    }
}
```

#### ScheduledExecutorService

Subinterface of `ExecutorService`. Adds:

* `schedule(task, delay, unit)` — run once after delay
* `scheduleAtFixedRate(task, initial, period, unit)` — run repeatedly

***

\<a name="master-compare-tables">\</a>

### Master Compare Tables

#### All locking mechanisms

| Mechanism                | Granularity  | tryLock? | Interruptible? | Condition Variables?    |
| ------------------------ | ------------ | -------- | -------------- | ----------------------- |
| `synchronized` method    | Whole object | No       | No             | Via wait/notify only    |
| `synchronized` statement | Block only   | No       | No             | Via wait/notify only    |
| `ReentrantLock`          | Block only   | Yes      | Yes            | Yes — multiple per lock |

#### Thread communication

|                                  | wait/notify              | await/signal (Condition)          |
| -------------------------------- | ------------------------ | --------------------------------- |
| Requires                         | `synchronized` block     | `lock.lock()` / `lock.unlock()`   |
| Wait sets                        | One per object           | Multiple per lock                 |
| Wake specific type?              | No — random thread woken | Yes — separate Condition per type |
| Spurious wakeups?                | Yes — use `while`        | Yes — use `while`                 |
| Preferred for Producer-Consumer? | No                       | Yes                               |

#### Multicore execution

|                  | ExecutorService                           | ForkJoinPool                       |
| ---------------- | ----------------------------------------- | ---------------------------------- |
| Best for         | Independent tasks, I/O                    | Divide-and-conquer, CPU-bound      |
| Work stealing?   | No                                        | Yes — idle threads steal from busy |
| Task type        | Runnable / Callable                       | RecursiveTask / RecursiveAction    |
| Result handling  | `Future.get()`                            | `pool.invoke()` or `task.join()`   |
| Shutdown needed? | Yes — `shutdown()` + `awaitTermination()` | No — self-managing                 |

***

\<a name="past-paper-qa">\</a>

### Past Paper Q\&A — Grouped by Type

#### Type A — Cooperative Multitasking (2019, 2021, 2025 — 6 marks each)

**Question:** "What is cooperative multitasking? How does this relate to priority? Illustrate using Java Threads."

**Full answer:**

* The JVM uses preemptive priority-based scheduling: thread with highest priority (1–10) runs first. Ties broken by FIFO. If a higher priority thread becomes runnable, it immediately preempts the current thread.
* Cooperative multitasking: threads voluntarily release the CPU by calling `yield()` or `sleep()`, allowing other threads to run — rather than waiting to be forcibly preempted.
* `yield()` — hint to JVM that thread is not critical. JVM may ignore it. Thread remains RUNNABLE.
* `sleep(ms)` — suspends thread for fixed duration. Releases CPU. Throws InterruptedException.

```java
class Task implements Runnable {
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " step " + i);
            Thread.yield();     // cooperative — voluntarily give up CPU
        }
    }
}
Thread t1 = new Thread(new Task(), "High");
Thread t2 = new Thread(new Task(), "Low");
t1.setPriority(8);  // higher priority — runs first
t2.setPriority(3);
t1.start();
t2.start();
```

***

#### Type B — Synchronisation (2023 — 4 marks, 2024 — 10 marks)

**Question:** "Describe synchronized methods and synchronized statements with commented code examples."

**Full answer — key points:**

* `synchronized` method: locks entire object. All other synchronized methods on same object block.
* `synchronized` statement: locks only the specified block. Non-critical code runs concurrently.
* happens-before guarantee: when synchronized method exits, changes visible to all threads.
* Separate lock objects: use when two resources are independent and should not block each other.

See Topic 4 key code for full implementation.

***

#### Type C — Deadlock (2022 — 10 marks)

**Question:** "Write a symmetric solution to Dining Philosophers where a philosopher releases their fork if unable to secure both."

**Full answer — key points:**

* Define deadlock: circular wait — each thread holds resource the other needs.
* Symmetric: all philosophers run identical code.
* `tryLock()`: non-blocking attempt. Returns true/false immediately.
* Release both in `finally`: even if exception thrown, locks are never leaked.
* Mention: no deadlock but starvation still possible — a philosopher could keep losing the race.

See Topic 5 `tryLock` code for full implementation.

***

#### Type D — Multicore Execution (2019, 2021, 2022, 2024, 2025)

**Question:** "Describe the different approaches to executing Java threads on different cores."

**Two approaches:**

1. **ExecutorService + Thread Pool:** fixed pool of threads. Tasks queued and executed as threads become available. Thread reuse avoids creation overhead. Use for independent tasks.
2. **Fork/Join Framework:** divide-and-conquer. Tasks split until small enough, then solved directly. Results combined up the tree. Work-stealing keeps all threads busy. Use for recursive parallel algorithms.

**Key difference:** Fork/Join implements work-stealing — idle threads steal tasks from busy threads. ExecutorService does not.

```java
// ExecutorService
ExecutorService exec = Executors.newFixedThreadPool(4);
exec.execute(() -> doWork());
exec.shutdown();
exec.awaitTermination(10, TimeUnit.SECONDS);

// Fork/Join
ForkJoinPool pool = new ForkJoinPool(4);
int result = pool.invoke(new MaxFinder(data, 0, data.length - 1));
```

***

#### Type E — Producer-Consumer (2023 — 10 marks)

**Question:** "Describe the Producer-Consumer problem and implement a solution in Java using Condition Variables."

**Full answer — key points:**

* Producer-Consumer: producers add to bounded buffer, consumers remove. Must wait when full/empty.
* Problem with plain `wait/notify`: single wait set — can't wake only producers or only consumers.
* Condition Variables: separate wait sets — `notFull` for producers, `notEmpty` for consumers.
* `notFull.await()` — producer waits when buffer full.
* `notEmpty.signal()` — producer wakes one consumer after adding.
* `notEmpty.await()` — consumer waits when buffer empty.
* `notFull.signal()` — consumer wakes one producer after removing.
* Always `while` not `if` — spurious wakeups.
* Always `unlock()` in `finally`.

See Topic 7 `BoundedBuffer` code for full implementation.

***

\<a name="weak-spots">\</a>

### Weak Spots Flagged This Session

* **Cooperative multitasking definition** — initially described as "communication between components". Correct: voluntary release of CPU via yield/sleep.
* **Critical section scope** — initially identified as just the add line. Correct: entire read-modify-write block.
* **Deadlock solution order** — listed in reverse to slides. Correct order: asymmetric → tryLock → capacity constraint.
* **synchronized ignores interrupt while waiting** — not initially recalled. `synchronized` blocks interrupt; `lockInterruptibly()` does not.
* **wait/notify vs await/signal** — naming confusion between the two worlds. Use the compare table above.
* **Producer-Consumer** — wrong condition signalled in consumer (`writeLock` instead of `readLock`).
* **Race condition demonstration** — sleep must be between read and write, not after the operation.

***

\<a name="spaced-repetition">\</a>

### Spaced Repetition Schedule

| Review | Date          | Focus                                                         |
| ------ | ------------- | ------------------------------------------------------------- |
| Day 3  | 19 April 2026 | Active recall — explain all 8 topics without notes            |
| Day 7  | 23 April 2026 | Past paper questions only — timed, written answers            |
| Day 14 | 30 April 2026 | Full weak spots drill + write all 9 code patterns from memory |

> 📌 **Exam tip:** David asks you to "clearly describe your code" in almost every question. Always add comments explaining WHY, not just WHAT.
