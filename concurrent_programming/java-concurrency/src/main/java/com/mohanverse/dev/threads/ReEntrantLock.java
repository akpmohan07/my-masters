package com.mohanverse.dev.threads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReEntrantLock {
    public static final int PRINTER_TIME_OUT = 5000; // 5 seconds
    public static final int MAX_BUFFER = 10;
    public static final int PRINTER_COUNT = 5;
    public static final int TOTAL_JOBS = 100;

    public class Semaphore {
        ReentrantLock reEntrantLock ;
        Condition readLock;
        Condition writeLock;

        public Semaphore() {
            reEntrantLock = new ReentrantLock();
            readLock = reEntrantLock.newCondition();
            writeLock = reEntrantLock.newCondition();
        }
    }

    public class PrinterSchedular{
        int printers;
        int writerIndex = 0;
        int readerIndex = 0;
        String jobs[] = new String[MAX_BUFFER];
        int bufferSize;
        public PrinterSchedular(int printers) {
            this.printers = printers;
            bufferSize = 0;
        }

        public void addPrintJob(String job, Semaphore s) {
            try {
                s.reEntrantLock.lock();
                try {
                    // Buffer is full, wait for the reader to consume some jobs
                    while(bufferSize == MAX_BUFFER) {
                        s.writeLock.await();
                    }
                    jobs[writerIndex] = job;
                    writerIndex = (writerIndex + 1) % MAX_BUFFER;
                    bufferSize++;
                    s.readLock.signalAll();
                } finally {
                    s.reEntrantLock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        public void print(Semaphore s) {
            try {

                while(true) {
                    s.reEntrantLock.lock();
                    try {
                        // Buffer is empty, wait for the writer to produce some jobs

                        long startTime = System.currentTimeMillis();
                        while(bufferSize  ==  0) {
                            System.out.println(Thread.currentThread().getName() + " is waiting for a job to print...");
                            // Recheck the job availability  every second to avoid waiting indefinitely
                            s.readLock.await(1, TimeUnit.SECONDS);
                            if (System.currentTimeMillis() - startTime > PRINTER_TIME_OUT) {
                                System.out.println(Thread.currentThread().getName() + " timed out waiting for a job to print. Exiting.");
                                return;
                            }
                        }
                        String job = jobs[readerIndex];
                        System.out.println(Thread.currentThread().getName() + " is printing: " + job);
                        readerIndex = (readerIndex + 1) % MAX_BUFFER;
                        bufferSize--;
                        s.writeLock.signalAll();
                    } finally {
                        s.reEntrantLock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
        }
    }
        }
    public static void main(String[] args) {
        ReEntrantLock reEntrantLock = new ReEntrantLock();
        Semaphore semaphore = reEntrantLock.new Semaphore();
        PrinterSchedular printerSchedular = reEntrantLock.new PrinterSchedular(PRINTER_COUNT);
        new Thread(() -> {
            for (int i = 0; i < TOTAL_JOBS; i++) {
                int jobId = i + 1;
                System.out.println("Adding print job: Document " + jobId);
                printerSchedular.addPrintJob("Document " + jobId, semaphore);
            }
        }).start();

        for (int i = 0; i < printerSchedular.printers; i++) {
            System.out.println("Starting printer thread " + (i + 1));
            new Thread(() -> {
                printerSchedular.print(semaphore);
            }, "Printer-" + (i + 1)).start();
        }
    }
}
