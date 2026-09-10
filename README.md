# 🏦 Core Banking & Transaction Engine (CLI)

> A robust, console-based banking application built in **Java** demonstrating **Object-Oriented Programming, multithreading, custom exception handling, and persistent file I/O**.

---


## ✨ Features

### 🔹 Object-Oriented Architecture
Implements all four core OOP principles:

- **Abstraction** through the `Account` base class.
- **Encapsulation** using private fields with controlled access methods.
- **Inheritance** via `SavingsAccount` and `CurrentAccount`.
- **Polymorphism** for specialized account behavior.

### 🔹 Multithreading & Concurrency

- Background daemon thread (`InterestAccrualTask`) automatically accrues savings account interest.
- Thread-safe money transfers using synchronized locks.
- Ordered resource locking prevents race conditions and deadlocks.

### 🔹 Custom Exception Handling

Implements domain-specific checked exceptions:

- `InsufficientFundsException`
- `AccountNotFoundException`

This ensures graceful error handling during banking operations.

### 🔹 Persistent File Storage

- Uses `BufferedReader` and `BufferedWriter`.
- Stores account information in `data/accounts.txt`.
- Automatically reloads account balances when the application starts.
- Saves all account data safely before exit.

### 🔹 Interactive Command-Line Interface

- Built entirely using `java.util.Scanner`.
- Lightweight CLI with no external libraries or frameworks.
- Suitable for automated command-line evaluation environments.

---

# 📁 Project Structure

```text
banking-transaction-engine/
├── src/
│   └── com/
│       └── bank/
│           ├── exceptions/
│           │   ├── AccountNotFoundException.java
│           │   └── InsufficientFundsException.java
│           ├── model/
│           │   ├── Account.java
│           │   ├── CurrentAccount.java
│           │   ├── SavingsAccount.java
│           │   └── Transaction.java
│           ├── service/
│           │   ├── BankService.java
│           │   ├── BankServiceImpl.java
│           │   └── InterestAccrualTask.java
│           └── Main.java
├── data/
│   └── accounts.txt
├── .gitignore
└── README.md
```

---

# ⚙️ Prerequisites

Before running the project, ensure you have:

- **Java Development Kit (JDK) 17 or above**
  - Tested on **JDK 25**
- A terminal environment:
  - Windows Command Prompt
  - PowerShell
  - Bash (Linux/macOS)

---

# 🚀 Compilation & Execution

## 1️⃣ Clone the Repository

```bash
git clone https://github.com/aanya25bai10190/banking-transaction-engine.git
cd banking-transaction-engine
```

## 2️⃣ Compile the Project

### Windows (Command Prompt / PowerShell)

```cmd
javac -d bin -sourcepath src src\com\bank\Main.java
```

### Linux / macOS

```bash
javac -d bin -sourcepath src src/com/bank/Main.java
```

## 3️⃣ Run the Application

```bash
java -cp bin com.bank.Main
```

---

# 🖥️ CLI Menu Options

| Option | Description |
|--------|-------------|
| **1** | Create a new Savings or Current account. |
| **2** | Deposit funds into an existing account. |
| **3** | Withdraw funds with balance and overdraft validation. |
| **4** | Transfer money securely between accounts using synchronized locking. |
| **5** | Display account balance and account type. |
| **6** | View complete transaction history for an account. |
| **7** | Save all account data and exit safely. |

---

# 💾 Persistent Storage

Account information is stored locally in:

```text
data/accounts.txt
```

On application exit:

- Current balances are written to the file.
- Existing account records are updated.
- The interest daemon thread shuts down safely before termination.

---

# 🧵 Concurrency Implementation

The project includes a background daemon thread:

```java
InterestAccrualTask
```

Responsibilities:

- Periodically calculates interest for savings accounts.
- Updates balances automatically.
- Runs concurrently without interrupting user operations.

Safe transfers use synchronized locking to ensure data consistency during simultaneous transactions.

---

# ⚠️ Exception Handling

Custom checked exceptions improve reliability:

| Exception | Purpose |
|-----------|---------|
| `AccountNotFoundException` | Thrown when an account number does not exist. |
| `InsufficientFundsException` | Thrown when withdrawal or transfer exceeds available balance or overdraft limit. |

---

# 🧩 Core Java Concepts Demonstrated

- Object-Oriented Programming
- Abstract Classes
- Inheritance & Polymorphism
- Interfaces
- Collections (`ArrayList`, `HashMap`)
- File Handling (`BufferedReader`, `BufferedWriter`)
- Exception Handling
- Multithreading
- Synchronization & Deadlock Prevention
- Command-Line User Interaction (`Scanner`)

---

# 🎯 Learning Outcomes

This project demonstrates the implementation of a simplified banking system while applying important Java programming concepts including:

- Modular software design.
- Thread-safe transaction processing.
- Persistent data management.
- Exception-driven error handling.
- Clean command-line application architecture.

---

## 👩‍💻 Author

**Aanya Yadav**

*Course Project — CSE2006: Programming in Java*
