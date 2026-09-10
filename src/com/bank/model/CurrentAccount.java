package com.bank.model;

import com.bank.exceptions.InsufficientFundsException;

public class CurrentAccount extends Account {
    private final double overdraftLimit;

    public CurrentAccount(String accountNumber, String accountHolderName, double balance, double overdraftLimit) {
        super(accountNumber, accountHolderName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > (balance + overdraftLimit)) {
            throw new InsufficientFundsException("Withdrawal exceeds overdraft limit of Rs " + overdraftLimit);
        }
        balance -= amount;
        transactionHistory.add(new Transaction("WITHDRAW", amount));
    }

    @Override
    public String getAccountType() {
        return "CURRENT";
    }

    @Override
    public String serialize() {
        return String.join(";", "CURRENT", getAccountNumber(), getAccountHolderName(), 
                String.valueOf(balance), String.valueOf(overdraftLimit));
    }
}