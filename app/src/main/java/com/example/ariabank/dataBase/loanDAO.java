package com.example.ariabank.dataBase;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface loanDAO {

    @Insert(entity = LoanTable.class,onConflict = OnConflictStrategy.IGNORE)
    void insertLoan(LoanTable loan);

    @Query("SELECT * FROM loans_table WHERE user_id=:id")
    List<LoanTable> getLoans(int id);

    @Query("SELECT * FROM loans_table")
    List<LoanTable> getAll();

    @Query("SELECT remained_amount FROM loans_table WHERE  name=:name AND monthly_payment=:monthly AND user_id=:id")
    Double getLoanAmount(String name , Double monthly,int id);

    @Query("SELECT id FROM LOANS_TABLE WHERE init_date=:initDate AND finish_date=:finishdate AND name=:loan_name AND init_amount=:amount")
    int getLoanID(String initDate,String finishdate,String loan_name,Double amount);

    @Query("UPDATE loans_table SET remained_amount=:amount WHERE name=:name AND monthly_payment=:monthly AND user_id=:id ")
    void updateLoan(Double amount,String name,Double monthly,int id);
}
