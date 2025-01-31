package com.example.dime.Classes;

public class Transaction {
    private String category;
    private String Name;
    private String amount;
    private String date;
    private String userID;

    private String expenseID;

    // Default constructor required for Firebase
    public Transaction() {}

    public Transaction(String category, String Name, String amount, String date, String userID) {
        this.category = category;
        this.Name = Name;
        this.amount = amount;
        this.date = date;
        this.userID = userID;
    }
    public String getExpenseID() {
        return expenseID;
    }
    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
