package com.bank.service;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.CurrentAccount;
import com.bank.model.SavingsAccount;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankServiceImpl implements BankService {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final String dataFilePath = "data/accounts.txt";

    public BankServiceImpl() {
        loadData();
    }

    @Override
    public void createAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
        persistData();
    }

    @Override
    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account " + accountNumber + " does not exist.");
        }
        return account;
    }

    @Override
    public Collection<Account> getAllAccounts() {
        return accounts.values();
    }

    @Override
    public void deposit(String accountNumber, double amount) throws AccountNotFoundException {
        Account acc = getAccount(accountNumber);
        acc.deposit(amount);
        persistData();
    }

    @Override
    public void withdraw(String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException {
        Account acc = getAccount(accountNumber);
        acc.withdraw(amount);
        persistData();
    }

    @Override
    public void transfer(String fromAccNum, String toAccNum, double amount) 
            throws AccountNotFoundException, InsufficientFundsException {
        Account sender = getAccount(fromAccNum);
        Account receiver = getAccount(toAccNum);

        // Lock ordering to prevent deadlocks
        Account firstLock = sender.getAccountNumber().compareTo(receiver.getAccountNumber()) < 0 ? sender : receiver;
        Account secondLock = firstLock == sender ? receiver : sender;

        synchronized (firstLock) {
            synchronized (secondLock) {
                sender.withdraw(amount);
                receiver.deposit(amount);
            }
        }
        persistData();
    }

    @Override
    public void applyInterestToSavings() {
        for (Account acc : accounts.values()) {
            if (acc instanceof SavingsAccount sa) {
                sa.applyInterest();
            }
        }
        persistData();
    }

    @Override
    public synchronized void persistData() {
        try {
            Files.createDirectories(Paths.get("data"));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFilePath))) {
                for (Account acc : accounts.values()) {
                    writer.write(acc.serialize());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to save account records: " + e.getMessage());
        }
    }

    @Override
    public synchronized void loadData() {
        File file = new File(dataFilePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length < 5) continue;
                String type = tokens[0];
                String accNum = tokens[1];
                String holder = tokens[2];
                double bal = Double.parseDouble(tokens[3]);

                if ("SAVINGS".equalsIgnoreCase(type)) {
                    double rate = Double.parseDouble(tokens[4]);
                    accounts.put(accNum, new SavingsAccount(accNum, holder, bal, rate));
                } else if ("CURRENT".equalsIgnoreCase(type)) {
                    double overdraft = Double.parseDouble(tokens[4]);
                    accounts.put(accNum, new CurrentAccount(accNum, holder, bal, overdraft));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Notice: Could not load initial state: " + e.getMessage());
        }
    }
}