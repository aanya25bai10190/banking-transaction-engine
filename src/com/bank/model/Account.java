package com.bank.model;

import com.bank.exceptions.InsufficientFundsException;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private final String accountNumber;
    private final String accountHolderName;
    protected double balance;
    protected List<Transaction> transactionHistory;

    public Account(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        this.transactionHistory.add(new Transaction("INITIAL_DEPOSIT", initialBalance));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        this.balance += amount;
        this.transactionHistory.add(new Transaction("DEPOSIT", amount));
    }

    public abstract void withdraw(double amount) throws InsufficientFundsException;

    public synchronized List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public abstract String getAccountType();

    public abstract String serialize();
}