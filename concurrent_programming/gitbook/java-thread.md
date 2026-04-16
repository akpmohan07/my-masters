# Java Thread

Here you go — every code pattern you need to write from memory, grouped by topic.

***

**1 — Runnable thread** _(every year)_

```java
class MyTask implements Runnable {
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return; // stop cleanly on interrupt
        }
        System.out.println(Thread.currentThread().getName());
    }
}

// usage
Thread t1 = new Thread(new MyTask());
Thread t2 = new Thread(new MyTask());
t1.start();
t2.start();
```

***

**2 — yield / sleep / interrupt / join** _(2019, 2021, 2025)_

```java
Thread.yield();          // hint to JVM — may be ignored
Thread.sleep(1000);      // give up CPU for 1 second
t.interrupt();           // signal thread to stop
t.join(1000);            // wait max 1s for thread to finish

// checking interrupt in a loop
if (Thread.interrupted()) {
    throw new InterruptedException();
}
```

***

**3 — Synchronized method** _(2023, 2024)_

```java
public class BankAccount {
    private float balance = 0;

    // only one thread can execute this at a time
    public synchronized void deposit(float amount) {
        balance = balance + amount;
    }
}
```

***

**4 — Synchronized statement + separate lock objects** _(2023, 2024)_

```java
public class Counter {
    private long c1 = 0, c2 = 0;
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public void inc1() {
        synchronized(lock1) { c1++; } // lock1 and lock2 are independent
    }

    public void inc2() {
        synchronized(lock2) { c2++; } // inc1 and inc2 can run concurrently
    }
}
```

***

**5 — Dining Philosophers with tryLock** _(2022)_

```java
Lock firstLock  = chopstickLocks.get(chopstickOrder[0]);
Lock secondLock = chopstickLocks.get(chopstickOrder[1]);

boolean left = false, right = false;
try {
    left  = firstLock.tryLock();   // try to acquire — non-blocking
    right = secondLock.tryLock();
} finally {
    if (left && right) {
        sleep(1000); // eat
    }
    if (left)  firstLock.unlock();
    if (right) secondLock.unlock();
}
```

***

**6 — wait / notify** _(2023)_

```java
// Thread A — waits for Thread B to finish
synchronized(b) {
    b.wait();   // releases lock and sleeps until notify()
    System.out.println(b.total);
}

// Thread B — does work then notifies
synchronized(this) {
    for (int i = 0; i < 100; i++) { total += i; }
    notify(); // wake up whoever is waiting on this object
}
```

***

**7 — Producer-Consumer with Condition Variables** _(2023)_

```java
class BoundedBuffer {
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();         // wait if buffer full
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;
            ++count;
            notEmpty.signal();           // tell consumer item is ready
        } finally { lock.unlock(); }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();        // wait if buffer empty
            Object x = items[takeptr];
            if (++takeptr == items.length) takeptr = 0;
            --count;
            notFull.signal();            // tell producer space is free
            return x;
        } finally { lock.unlock(); }
    }
}
```

***

**8 — Executors + Thread Pool** _(every year)_

```java
ExecutorService executor = Executors.newFixedThreadPool(10);

for (int i = 0; i < 20; i++) {
    executor.execute(new WorkerThread("task-" + i));
}

executor.shutdown();
while (!executor.isTerminated()) {}
System.out.println("All threads finished");
```

***

**9 — Fork/Join** _(2022, 2024, 2025)_

```java
public class MaxFinder extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 5;
    private final int[] data;
    private final int start, end;

    public MaxFinder(int[] data, int start, int end) {
        this.data = data; this.start = start; this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start < THRESHOLD)
            return computeDirectly();

        int split = (end - start) / 2;
        MaxFinder left  = new MaxFinder(data, start, start + split);
        MaxFinder right = new MaxFinder(data, start + split, end);

        left.fork();                          // async — run in another thread
        return Math.max(right.compute(),      // run right in current thread
                        left.join());         // wait for left result
    }
}

// usage
ForkJoinPool pool = new ForkJoinPool(4);
int max = pool.invoke(new MaxFinder(data, 0, data.length));
```

***

That's everything. Patterns 1–5 are the highest priority — they cover the questions that appear every single year. Want to do a quick recall drill on any of these?
