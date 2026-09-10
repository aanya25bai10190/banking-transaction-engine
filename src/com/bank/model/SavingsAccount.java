package com.bank.model;

import com.bank.exceptions.InsufficientFundsException;

public class SavingsAccount extends Account {
    private final double interestRate;

    public SavingsAccount(String accountNumber, String accountHolderName, double balance, double interestRate) {
        super(accountNumber, accountHolderName, balance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient balance. Current balance: Rs " + balance);
        }
        balance -= amount;
        transactionHistory.add(new Transaction("WITHDRAW", amount));
    }

    public synchronized void applyInterest() {
        double interest = balance * interestRate;
        balance += interest;
        transactionHistory.add(new Transaction("INTEREST", interest));
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }

    @Override
    public String serialize() {
        return String.join(";", "SAVINGS", getAccountNumber(), getAccountHolderName(), 
                String.valueOf(balance), String.valueOf(interestRate));
    }
}