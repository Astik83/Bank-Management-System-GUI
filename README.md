# Bank-Management-System-GUI


# Bank Management System GUI

This is a simple Java-based GUI application for managing bank accounts using `JDBC` to connect with a MySQL database. The system allows users to:

- Create new bank accounts
- Deposit money into accounts
- Withdraw money from accounts
- Check account balance

## Features

- **Account Management**: Create new accounts by providing an account number and account holder name.
- **Deposits**: Add funds to an existing account.
- **Withdrawals**: Withdraw funds, with validation for sufficient balance.
- **Balance Check**: Check the current balance of a specific account.
- **JDBC Integration**: Uses JDBC to connect and interact with a MySQL database for persistent data storage.

## Technologies Used

- Java
- Swing (for GUI)
- MySQL
- JDBC (Java Database Connectivity)

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/Bank-Management-System-GUI.git
   ```

2. Set up your MySQL database:
   - Create a database named `jdbc_db`.
   - Create a table `bank_accounts` with columns for `account_number`, `account_holder_name`, and `balance`.

   Example SQL:
   ```sql
   CREATE TABLE bank_accounts (
       account_number VARCHAR(50) PRIMARY KEY,
       account_holder_name VARCHAR(100),
       balance DOUBLE DEFAULT 0
   );
   ```

3. Modify the database connection settings in the `BankManagementSystemGUI.java` file:
   ```java
   conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_db", "root", "your_password");
   ```

4. Compile and run the project in your IDE or using the terminal.

---

 

