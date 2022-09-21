package com.example.ariabank.dataBase;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "transactions")
public class Transaction {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private double amount;


    private String date;

    private String type;

    private int user_id;

    private String recipient;

    public void setId(int id) {
        this.id = id;
    }

    private String description;

    public Transaction(double amount, String date, String type, int user_id, String recipient, String description) {
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.user_id = user_id;
        this.recipient = recipient;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Transaction() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
