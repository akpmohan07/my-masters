package com.mohanverse.dev;

import java.util.concurrent.Executors;

public class ExecutorService implements Runnable {
    private String command;

    public ExecutorService(String s) {
        this.command = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start. Command = " + command);

        processCommand();

        System.out.println(Thread.currentThread().getName() + " End.");
    }

    private void processCommand() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.command;
    }

    public static void main(String[] args) {
        java.util.concurrent.ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 20; i++) {
            Runnable worker = new ExecutorService("" + i);
            executor.execute(worker);
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
            // Wait for all threads to finish
        }

        System.out.println("Finished all threads");
    }
}