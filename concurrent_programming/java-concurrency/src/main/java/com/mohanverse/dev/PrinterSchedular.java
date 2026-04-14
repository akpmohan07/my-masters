package com.mohanverse.dev;

public class PrinterSchedular {
    Semaphore semaphore = new Semaphore(5);
    int users;

    public PrinterSchedular(int users) {
        this.users = users;
    }

    public void print() {
        for (int i = 0; i < users; i++) {
            new Thread(() -> {
                semaphore.acquirePrinter();
                System.out.println("Printing document for user " + Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                semaphore.releasePrinter();
            }).start();
        }
    }

    class Semaphore {
        private int avialable_printers;
        private Object lock;

        public Semaphore(int avialable_printers) {
            this.avialable_printers = avialable_printers;
            this.lock = new Object();
        }

        public void acquirePrinter() {
            synchronized (lock) {
                while (avialable_printers <= 0) {
                    try {
                        System.out.println("User " + Thread.currentThread().getName() + " is waiting for a printer to become available.");
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                avialable_printers--;
            }
        }

        public void releasePrinter() {
            synchronized (lock) {
                System.out.println("User " + Thread.currentThread().getName() + " has finished printing and released the printer.");
                avialable_printers++;
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        PrinterSchedular printerSchedular = new PrinterSchedular(10);
        printerSchedular.print();
    }
}
