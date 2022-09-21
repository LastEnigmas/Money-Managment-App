package com.example.ariabank.dataBase;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_table")
public class item_table {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String image_url;
    private String description;

    public void setId(int id) {
        this.id = id;
    }

    public item_table(String name, String image_url, String description) {
        this.name = name;
        this.image_url = image_url;
        this.description = description;
    }

    public item_table() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
