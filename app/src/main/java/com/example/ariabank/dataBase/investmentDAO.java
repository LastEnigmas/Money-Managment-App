package com.example.ariabank.dataBase;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface investmentDAO {

    @Insert(entity = InvestmentTable.class,onConflict = OnConflictStrategy.ABORT)
    void InsertInvestment(InvestmentTable invest);

    @Query("SELECT * FROM investment_table WHERE user_id=:id ORDER BY init_date DESC")
    List<InvestmentTable> getInvestments(int id);


}
