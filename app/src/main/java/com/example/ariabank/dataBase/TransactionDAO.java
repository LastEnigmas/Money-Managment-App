package com.example.ariabank.dataBase;


import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDAO {
    @Query("SELECT * FROM transactions WHERE user_id=:id ORDER BY date DESC ")
    List<Transaction> getUserTransactions(int id);

    @Query("SELECT * FROM transactions WHERE user_id=:id AND type=:type")
    List<Transaction> getprofit(int id,String type);

    @Query("SELECT * FROM transactions WHERE user_id=:id")
    List<Transaction> getAll(int id);

    @Query("SELECT * FROM transactions WHERE :amount < ABS(amount) AND user_id=:id AND type=:type  ORDER BY date DESC")
    List<Transaction> getTransaction(Double amount,int id,String type);


    @Query("SELECT * FROM transactions WHERE user_id=:id AND :amount < ABS(amount) ")
    List<Transaction> getAllWithLimit(int id,Double amount);


    @Query("SELECT * FROM transactions")
    List<Transaction> getEverything();





    @Insert(entity=Transaction.class,onConflict = OnConflictStrategy.ABORT)
    void insertTransaction(Transaction transaction);

    @Query("SELECT id FROM transactions WHERE date=:date AND type=:type AND user_id=:id AND description=:desc AND amount=:price")
    int getBackTransactionID(String date,String type,int id,String desc,Double price);
}
