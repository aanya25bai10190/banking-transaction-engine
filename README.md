# Core Banking & Transaction Engine (CLI)

A robust, console-based banking application built in Java demonstrating core object-oriented design, multithreaded operations, custom exception handling, and persistent file I/O.

## Course Information
* **Course Code:** CSE2006 - Programming in Java
* **Project Type:** Evaluated Course Project

## Features
* **OOP Architecture:** Employs abstraction, encapsulation, inheritance, and polymorphism through the base `Account` class and its specialized `SavingsAccount` and `CurrentAccount` subclasses.
* **Multithreading & Concurrency:** Incorporates a background daemon thread (`InterestAccrualTask`) for automated interest processing and synchronized locks on bidirectional fund transfers to avoid race conditions and deadlocks.
* **Custom Exception Handling:** Employs domain-specific checked exceptions (`InsufficientFundsException`, `AccountNotFoundException`) for robust error management.
* **Persistent Storage:** Uses Java File I/O character streams (`BufferedReader`/`BufferedWriter`) to persist and reload account balances across application lifecycles in `data/accounts.txt`.

## Prerequisites
* Java Development Kit (JDK) 17 or higher
* Standard Command Line / Terminal environment

## Compilation and Execution (Command Line)

1. **Clone the repository:**
   ```bash
   git clone https://github.com/aanya25bai10190/banking-transaction-engine.git
   cd banking-transaction-engine
