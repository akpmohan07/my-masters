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