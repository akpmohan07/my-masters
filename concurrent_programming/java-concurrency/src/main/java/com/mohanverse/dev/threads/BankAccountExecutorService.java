package com.mohanverse.dev.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class BankAccountExecutorService {
    public static final int USER_COUNT = 1000;
    public static final int ACCOUNT_COUNT = 10;

    int accounts[];
    ExecutorService executor = Executors.newFixedThreadPool(USER_COUNT);

    public BankAccountExecutorService() {
        accounts = new int[ACCOUNT_COUNT];
        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            accounts[i] = 1;
        }
    }

    // Without synchronization there is an concurrency issue and the balance can become negative or incorrect
    public synchronized void deposit(int amount, int account) {
        accounts[account] += amount;
        System.out.println("Thread " + Thread.currentThread().getName() + " deposited " + amount + " to account " + account + ". New balance: " + accounts[account]);
    }

    public synchronized void withdraw(int account, int amount) {
        accounts[account] -= amount;
        System.out.println("Thread " + Thread.currentThread().getName() + " withdrew " + amount + " from account " + account + ". New balance: " + accounts[account]);
    }

    public void query(int account) {
        System.out.println("Thread " + Thread.currentThread().getName() + " queried account " + account + ". Balance: " + accounts[account]);
    }



    public static void main(String[] args) throws InterruptedException {
        BankAccountExecutorService bankAccountExecutorService = new BankAccountExecutorService();

        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            int account = i;
            for(int j = 0; j < USER_COUNT; j++) {
                    bankAccountExecutorService.executor.execute(() -> {
                        bankAccountExecutorService.deposit(100, account);
                        bankAccountExecutorService.withdraw(account, 50);
                        bankAccountExecutorService.query(account);
                    });
            }

        }
        bankAccountExecutorService.executor.shutdown();
        bankAccountExecutorService.executor.awaitTermination(10, TimeUnit.SECONDS);
        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            System.out.println("Final balance of account " + i + ": " + bankAccountExecutorService.accounts[i]);
        }
    }




}
