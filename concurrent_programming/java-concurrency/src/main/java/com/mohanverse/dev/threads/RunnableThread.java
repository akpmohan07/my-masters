package com.mohanverse.dev.threads;

import static java.lang.Thread.sleep;

public class RunnableThread implements Runnable {
    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("RunnableThread is running: " + Thread.currentThread().getName());
    }

        public static void main(String[] args) {
            RunnableThread runnableThread = new RunnableThread();
            Thread thread1 = new Thread(runnableThread);
            Thread thread2 = new Thread(runnableThread);
            thread1.start();
            thread2.start();
        }
}
