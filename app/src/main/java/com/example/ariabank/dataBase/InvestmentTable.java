package com.example.ariabank.dataBase;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "investment_table")
public class InvestmentTable {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private double amount;

    public void setId(int id) {
        this.id = id;
    }

    private double monthly_roi;
    private String name;


    private String init_date;


    private String finish_date;

    private int user_id;
    private int transaction;

    public InvestmentTable() {
    }

    public InvestmentTable(double amount, double monthly_roi, String name, String init_date, String finish_date, int user_id, int transaction) {
        this.amount = amount;
        this.monthly_roi = monthly_roi;
        this.name = name;
        this.init_date = init_date;
        this.finish_date = finish_date;
        this.user_id = user_id;
        this.transaction = transaction;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getMonthly_roi() {
        return monthly_roi;
    }

    public void setMonthly_roi(double monthly_roi) {
        this.monthly_roi = monthly_roi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInit_date() {
        return init_date;
    }

    public void setInit_date(String init_date) {
        this.init_date = init_date;
    }

    public String getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(String finish_date) {
        this.finish_date = finish_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTransaction() {
        return transaction;
    }

    public void setTransaction(int transaction) {
        this.transaction = transaction;
    }
}
