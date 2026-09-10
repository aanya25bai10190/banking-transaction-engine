package com.bank;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.CurrentAccount;
import com.bank.model.SavingsAccount;
import com.bank.model.Transaction;
import com.bank.service.BankService;
import com.bank.service.BankServiceImpl;
import com.bank.service.InterestAccrualTask;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankService bankService = new BankServiceImpl();

        InterestAccrualTask accrualTask = new InterestAccrualTask(bankService, 60000);
        Thread daemonThread = new Thread(accrualTask);
        daemonThread.setDaemon(true);
        daemonThread.setName("InterestEngine");
        daemonThread.start();

        Scanner scanner = new Scanner(System.in);
        boolean active = true;

        System.out.println("=================================================");
        System.out.println("   WELCOME TO CORE BANKING & TRANSACTION ENGINE  ");
        System.out.println("=================================================");

        while (active) {
            printMenu();
            System.out.print("Select an option [1-7]: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> handleCreateAccount(bankService, scanner);
                case "2" -> handleDeposit(bankService, scanner);
                case "3" -> handleWithdraw(bankService, scanner);
                case "4" -> handleTransfer(bankService, scanner);
                case "5" -> handleCheckBalance(bankService, scanner);
                case "6" -> handlePrintHistory(bankService, scanner);
                case "7" -> {
                    System.out.println("Saving session state and shutting down. Goodbye!");
                    bankService.persistData();
                    accrualTask.stop();
                    active = false;
                }
                default -> System.out.println("[Error] Invalid selection. Please enter a number between 1 and 7.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("-------------------------------------------------");
        System.out.println("1. Create New Account (Savings / Current)");
        System.out.println("2. Deposit Funds");
        System.out.println("3. Withdraw Funds");
        System.out.println("4. Transfer Funds (Thread-Safe)");
        System.out.println("5. Check Account Balance & Type");
        System.out.println("6. View Transaction Statement");
        System.out.println("7. Exit & Save");
        System.out.println("-------------------------------------------------");
    }

    private static void handleCreateAccount(BankService service, Scanner scanner) {
        try {
            System.out.print("Enter Account Number: ");
            String accNum = scanner.nextLine().trim();
            System.out.print("Enter Account Holder Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Account Type (1 for Savings, 2 for Current): ");
            String type = scanner.nextLine().trim();
            System.out.print("Enter Opening Balance: ");
            double balance = Double.parseDouble(scanner.nextLine().trim());

            if (type.equals("1")) {
                System.out.print("Enter Annual Interest Rate (e.g., 0.05 for 5%): ");
                double rate = Double.parseDouble(scanner.nextLine().trim());
                service.createAccount(new SavingsAccount(accNum, name, balance, rate));
                System.out.println("[Success] Savings Account created successfully!");
            } else if (type.equals("2")) {
                System.out.print("Enter Overdraft Limit: ");
                double overdraft = Double.parseDouble(scanner.nextLine().trim());
                service.createAccount(new CurrentAccount(accNum, name, balance, overdraft));
                System.out.println("[Success] Current Account created successfully!");
            } else {
                System.out.println("[Error] Invalid type choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[Error] Invalid numeric input provided.");
        }
    }

    private static void handleDeposit(BankService service, Scanner scanner) {
        try {
            System.out.print("Enter Account Number: ");
            String accNum = scanner.nextLine().trim();
            System.out.print("Enter Amount to Deposit: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            service.deposit(accNum, amount);
            System.out.println("[Success] Deposit completed.");
        } catch (AccountNotFoundException | IllegalArgumentException e) {
            System.out.println("[Failure] " + e.getMessage());
        }
    }

    private static void handleWithdraw(BankService service, Scanner scanner) {
        try {
            System.out.print("Enter Account Number: ");
            String accNum = scanner.nextLine().trim();
            System.out.print("Enter Amount to Withdraw: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            service.withdraw(accNum, amount);
            System.out.println("[Success] Withdrawal completed.");
        } catch (AccountNotFoundException | InsufficientFundsException | IllegalArgumentException e) {
            System.out.println("[Failure] " + e.getMessage());
        }
    }

    private static void handleTransfer(BankService service, Scanner scanner) {
        try {
            System.out.print("Enter Source Account Number: ");
            String from = scanner.nextLine().trim();
            System.out.print("Enter Destination Account Number: ");
            String to = scanner.nextLine().trim();
            System.out.print("Enter Transfer Amount: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            service.transfer(from, to, amount);
            System.out.println("[Success] Transfer completed successfully.");
        } catch (AccountNotFoundException | InsufficientFundsException | IllegalArgumentException e) {
            System.out.println("[Failure] " + e.getMessage());
        }
    }

    private static void handleCheckBalance(BankService service, Scanner scanner) {
        try {
            System.out.print("Enter Account Number: ");
            String accNum = scanner.nextLine().trim();
            Account acc = service.getAccount(accNum);
            System.out.println("Holder: " + acc.getAccountHolderName());
            System.out.println("Type: " + acc.getAccountType());
            System.out.printf("Current Balance: Rs %.2f%n", acc.getBalance());
        } catch (AccountNotFoundException e) {
            System.out.println("[Failure] " + e.getMessage());
        }
    }

    private static void handlePrintHistory(BankService service, Scanner scanner) {
        try {
            System.out.print("Enter Account Number: ");
            String accNum = scanner.nextLine().trim();
            Account acc = service.getAccount(accNum);
            System.out.println("\n--- TRANSACTION HISTORY FOR " + accNum + " ---");
            for (Transaction t : acc.getTransactionHistory()) {
                System.out.println(t);
            }
        } catch (AccountNotFoundException e) {
            System.out.println("[Failure] " + e.getMessage());
        }
    }
}