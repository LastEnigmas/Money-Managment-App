package com.example.ariabank.dataBase;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao()
public interface itemDAO {

    @Insert(onConflict = OnConflictStrategy.ABORT,entity = item_table.class)
    void insertItem(item_table item);

    @Query("SELECT * FROM item_table")
    List<item_table> getAllItems();

    @Query("SELECT * FROM item_table WHERE name LIKE :name")
    List<item_table> getItemWithName(String name);
}
