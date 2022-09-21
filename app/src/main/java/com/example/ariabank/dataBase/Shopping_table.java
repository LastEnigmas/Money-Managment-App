package com.example.ariabank.dataBase;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "shopping_table")
public class Shopping_table {


    @PrimaryKey(autoGenerate = true)
    private int id;


    private int item_id;
    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    private int tranaction_id;
    private double price;

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private String date;
    private String description;

    public Shopping_table(int item_id, int user_id, int tranaction_id, double price, String date, String description) {
        this.item_id = item_id;
        this.user_id = user_id;
        this.tranaction_id = tranaction_id;
        this.price = price;
        this.date = date;
        this.description = description;
    }

    public Shopping_table() {
    }

    public int getId() {
        return id;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getTranaction_id() {
        return tranaction_id;
    }

    public void setTranaction_id(int tranaction_id) {
        this.tranaction_id = tranaction_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
