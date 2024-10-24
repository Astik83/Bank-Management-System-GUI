package com.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BankManagementSystemGUI extends JFrame {
    private JTextField accountNumberField;
    private JTextField accountHolderNameField;
    private JTextField amountField;
    private JTextArea outputArea;

    private Connection conn;

    public BankManagementSystemGUI() {
        setTitle("Bank Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Input panel with grid layout
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Account number label and text field
        inputPanel.add(new JLabel("Account Number:"));
        accountNumberField = new JTextField();
        inputPanel.add(accountNumberField);

        // Account holder name label and text field
        inputPanel.add(new JLabel("Account Holder Name:"));
        accountHolderNameField = new JTextField();
        inputPanel.add(accountHolderNameField);

        // Amount label and text field
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        // Button panel with flow layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Buttons
        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount();
            }
        });
        buttonPanel.add(createAccountButton);

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deposit();
            }
        });
        buttonPanel.add(depositButton);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                withdraw();
            }
        });
        buttonPanel.add(withdrawButton);

        JButton checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBalance();
            }
        });
        buttonPanel.add(checkBalanceButton);

        // Output area
        outputArea = new JTextArea();
        mainPanel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);

        // Connect to the database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_db", "root", "your password");
        } catch (Exception ex) {
            handleException("Failed to connect to the database.", ex);
        }
    }

    private void createAccount() {
        String accountNumber = accountNumberField.getText();
        String accountHolderName = accountHolderNameField.getText();
        if (accountNumber.isEmpty() || accountHolderName.isEmpty()) {
            showError("Please enter account number and account holder name.");
            return;
        }
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO bank_accounts (account_number, account_holder_name, balance) VALUES (?, ?, 0)");
            stmt.setString(1, accountNumber);
            stmt.setString(2, accountHolderName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                outputArea.append("Account created successfully.\n");
            } else {
                outputArea.append("Failed to create account.\n");
            }
        } catch (SQLException ex) {
            handleException("Failed to create account.", ex);
        }
    }

    private void deposit() {
        String accountNumber = accountNumberField.getText();
        String amountText = amountField.getText();
        if (accountNumber.isEmpty() || amountText.isEmpty()) {
            showError("Please enter account number and amount.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Please enter a positive amount.");
                return;
            }
            PreparedStatement stmt = conn.prepareStatement("UPDATE bank_accounts SET balance = balance + ? WHERE account_number = ?");
            stmt.setDouble(1, amount);
            stmt.setString(2, accountNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                outputArea.append("Deposit successful.\n");
            } else {
                outputArea.append("Failed to deposit.\n");
            }
        } catch (NumberFormatException | SQLException ex) {
            handleException("Invalid amount or account number.", ex);
        }
    }

    private void withdraw() {
        String accountNumber = accountNumberField.getText();
        String amountText = amountField.getText();
        if (accountNumber.isEmpty() || amountText.isEmpty()) {
            showError("Please enter account number and amount.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Please enter a positive amount.");
                return;
            }
            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM bank_accounts WHERE account_number = ?");
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    stmt = conn.prepareStatement("UPDATE bank_accounts SET balance = balance - ? WHERE account_number = ?");
                    stmt.setDouble(1, amount);
                    stmt.setString(2, accountNumber);
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        outputArea.append("Withdrawal successful.\n");
                    } else {
                        outputArea.append("Failed to withdraw.\n");
                    }
                } else {
                    outputArea.append("Insufficient funds.\n");
                }
            } else {
                outputArea.append("Account not found.\n");
            }
        } catch (NumberFormatException | SQLException ex) {
            handleException("Invalid amount or account number.", ex);
        }
    }

    private void checkBalance() {
        String accountNumber = accountNumberField.getText();
        if (accountNumber.isEmpty()) {
            showError("Please enter account number.");
            return;
        }
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM bank_accounts WHERE account_number = ?");
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                outputArea.append("Balance: $" + balance + "\n");
            } else {
                outputArea.append("Account not found.\n");
            }
        } catch (SQLException ex) {
            handleException("Failed to check balance.", ex);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void handleException(String message, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        new BankManagementSystemGUI();
    }
}
