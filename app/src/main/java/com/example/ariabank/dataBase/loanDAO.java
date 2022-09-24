package com.example.ariabank.dataBase;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface loanDAO {

    @Insert(entity = LoanTable.class,onConflict = OnConflictStrategy.IGNORE)
    void insertLoan(LoanTable loan);

    @Query("SELECT id FROM LOANS_TABLE WHERE init_date=:initDate AND finish_date=:finishdate AND name=:loan_name AND init_amount=:amount")
    int getLoanID(String initDate,String finishdate,String loan_name,Double amount);
}
