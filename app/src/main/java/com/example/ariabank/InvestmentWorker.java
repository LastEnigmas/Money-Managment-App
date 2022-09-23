package com.example.ariabank;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InvestmentWorker extends Worker {
    private static final String TAG = "InvestmentWorker";
    private AppDataBase db;

    public InvestmentWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        db=AppDataBase.getInstance(context);

    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: called");

        Data data=getInputData();
        double amount= data.getDouble("amount",0);
        String recipient=data.getString("recipient");
        String Description=data.getString("description");
        int user_id= data.getInt("user_id",-1);
        String type= "profit";
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(calendar.getTime());

        Transaction t1=new Transaction(amount,date,type,user_id,recipient,Description);
        db.transactiondao().insertTransaction(t1);

        Double current_amount=db.usersdao().getUserRemainder(user_id);
        db.usersdao().UpdateAmount(current_amount-amount,user_id);





        return Result.success();
    }
}
