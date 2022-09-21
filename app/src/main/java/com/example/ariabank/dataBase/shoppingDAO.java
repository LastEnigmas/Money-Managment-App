package com.example.ariabank.dataBase;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface shoppingDAO {

    @Query("SELECT * FROM shopping_table WHERE user_id=:id ")
    List<Shopping_table> getSPENDING(int id);

    @Insert(entity = Shopping_table.class,onConflict = OnConflictStrategy.IGNORE)
    void insertAnewPurchase(Shopping_table shop);
}
