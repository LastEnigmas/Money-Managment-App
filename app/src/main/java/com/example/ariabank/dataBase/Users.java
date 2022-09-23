package com.example.ariabank.dataBase;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class Users {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    public void setId(int id) {

        this.id = id;
    }

    @NonNull
    private String email;
    @NonNull
    private String password;
    private String first_name;
    private String last_name;
    private String Address;
    private String image_url;
    @NonNull
    private Double remained_amount;

    public int getId() {
        return id;
    }

    public Users(@NonNull String email, @NonNull String password, String first_name, String last_name, String address, String image_url, @NonNull Double remained_amount) {
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        Address = address;
        this.image_url = image_url;
        this.remained_amount = remained_amount;
    }

    @Ignore
    public Users(@NonNull String email, @NonNull String password, String first_name, String last_name, String address, String image_url) {
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        Address = address;
        this.image_url = image_url;
        this.remained_amount = 0.0;
    }

    public Users() {
    }



    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Double getRemained_amount() {
        return remained_amount;
    }

    public void setRemained_amount(Double remained_amount) {
        this.remained_amount = remained_amount;
    }









}
