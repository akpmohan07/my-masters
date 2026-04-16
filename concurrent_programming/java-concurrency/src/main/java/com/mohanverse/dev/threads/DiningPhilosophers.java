package com.mohanverse.dev.threads;
/*
Philosopher 0 is thinking
Philosopher 4 is thinking
Philosopher 3 is thinking
Philosopher 2 is thinking
Philosopher 1 is thinking
Philosopher 4 is eating
Philosopher 2 is eating
Philosopher 2 is thinking
Philosopher 1 is eating
Philosopher 3 is eating
Philosopher 4 is thinking
Philosopher 3 is thinking
Philosopher 1 is thinking
Philosopher 0 is eating
Philosopher 2 is eating
Philosopher 0 is thinking
Philosopher 4 is eating
Philosopher 2 is thinking
Philosopher 1 is eating
Philosopher 4 is thinking
Philosopher 3 is eating
Philosopher 1 is thinking
Philosopher 0 is eating
Philosopher 4 is eating
Philosopher 2 is eating
Philosopher 1 is eating
Done



 */


public class DiningPhilosophers {

    // ─── Chopstick ───────────────────────────────────────────────────────────

    static class Chopstick {
        public final int id;
        public Chopstick(int id) { this.id = id; }
    }

    // ─── Philosopher ─────────────────────────────────────────────────────────

    static class Philosopher implements Runnable {
        private final int id;
        private final Chopstick[] chopsticks;

        public Philosopher(int id, Chopstick[] chopsticks) {
            this.id = id;
            this.chopsticks = chopsticks;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                think();
                eat();
            }
        }

        private void think() {
            try {
                System.out.println("Philosopher " + id + " is thinking");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void eat() {
            // always acquire lower-id chopstick first — prevents circular wait
            Chopstick first  = id < (id + 1) % chopsticks.length ? getLeft() : getRight();
            Chopstick second = id < (id + 1) % chopsticks.length ? getRight() : getLeft();

            synchronized (first) {               // acquire first chopstick
                synchronized (second) {          // acquire second chopstick
                    try {
                        System.out.println("Philosopher " + id + " is eating");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }                                // release second chopstick
            }                                    // release first chopstick
        }

        private Chopstick getLeft()  { return chopsticks[id]; }
        private Chopstick getRight() { return chopsticks[(id + 1) % chopsticks.length]; }
    }

    // ─── Main ────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws InterruptedException {

        /*
        main creates: [C0, C1, C2, C3, C4]  ← 5 chopstick objects, shared
              [P0, P1, P2, P3, P4]  ← 5 philosopher threads

        P0 wants C0 + C1  → OrderById says: grab C0 first (lower id)
        P1 wants C1 + C2  → OrderById says: grab C1 first
        P2 wants C2 + C3  → OrderById says: grab C2 first
        P3 wants C3 + C4  → OrderById says: grab C3 first
        P4 wants C4 + C0  → OrderById says: grab C0 first ← THIS is the fix
                                             (not C4, because C0 has lower id)

        Without OrderById: P4 grabs C4, waits for C0
                           P0 grabs C0, waits for C1
                           → circular wait → deadlock

        With OrderById:    P4 and P0 both try C0 first
                           one wins, eats, releases
                           other gets it next → no circle


         */
        final int N = 5;

        Chopstick[] chopsticks = new Chopstick[N];
        for (int i = 0; i < N; i++) {
            chopsticks[i] = new Chopstick(i);
        }

        Thread[] threads = new Thread[N];
        for (int i = 0; i < N; i++) {
            threads[i] = new Thread(new Philosopher(i, chopsticks), "Philosopher-" + i);
            threads[i].start();
        }

        // run for 5 seconds then stop
        Thread.sleep(5000);
        for (Thread t : threads) t.interrupt();
        for (Thread t : threads) t.join();

        System.out.println("Done");
    }
}