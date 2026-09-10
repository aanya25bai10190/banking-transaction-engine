package com.bank.service;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.model.Account;
import java.util.Collection;

public interface BankService {
    void createAccount(Account account);
    Account getAccount(String accountNumber) throws AccountNotFoundException;
    Collection<Account> getAllAccounts();
    void deposit(String accountNumber, double amount) throws AccountNotFoundException;
    void withdraw(String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException;
    void transfer(String fromAcc, String toAcc, double amount) throws AccountNotFoundException, InsufficientFundsException;
    void applyInterestToSavings();
    void persistData();
    void loadData();
}