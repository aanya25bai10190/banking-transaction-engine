package com.bank.service;

public class InterestAccrualTask implements Runnable {
    private final BankService bankService;
    private final long intervalMillis;
    private volatile boolean running = true;

    public InterestAccrualTask(BankService bankService, long intervalMillis) {
        this.bankService = bankService;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(intervalMillis);
                bankService.applyInterestToSavings();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }
}