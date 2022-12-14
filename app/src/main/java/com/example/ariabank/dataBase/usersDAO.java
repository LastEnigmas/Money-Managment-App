package com.example.ariabank.dataBase;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface usersDAO {

    @Query("SELECT * FROM users WHERE email=:email")
    List<Users> getSpecificUser(String email);


    @Query("UPDATE users SET remained_amount=:amount WHERE email=:email")
    Void UpdateWithEmail(String email,Double amount);

    @Insert(entity = Users.class,onConflict = OnConflictStrategy.ABORT)
    void addUser(Users user);

    @Query("SELECT * FROM users WHERE email=:email AND password=:password")
    List<Users> login(String email,String password);

    @Query("SELECT * FROM users WHERE id=:id ")
    List<Users> getUserAmount(int id);

    @Query("SELECT remained_amount From users WHERE id=:id")
    Double getUserRemainder(int id);

    @Query("UPDATE users SET remained_amount= :amount WHERE id= :id")
    void UpdateAmount(Double amount,int id);






}
