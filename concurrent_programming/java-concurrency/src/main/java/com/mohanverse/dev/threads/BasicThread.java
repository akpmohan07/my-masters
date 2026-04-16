package com.mohanverse.dev.threads;

public class BasicThread extends Thread{
    @Override
    public void run() {
        System.out.println("BasicThread is running: " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        BasicThread thread1 = new BasicThread();
        BasicThread thread2 = new BasicThread();

        thread1.start();
        thread2.start();
    }

}
