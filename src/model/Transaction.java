package model;

import java.util.Date;

public class Transaction {
    private int id;
    private double amount;
    private int categoryId;
    private Date transactionDate;

    // Constructors
    public Transaction(double amount, int categoryId) {
        this.amount = amount;
        this.categoryId = categoryId;
    }

    public Transaction(int id, double amount, int categoryId, Date transactionDate) {
        this.id = id;
        this.amount = amount;
        this.categoryId = categoryId;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }
}
