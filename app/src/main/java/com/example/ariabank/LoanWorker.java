package com.example.ariabank;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ariabank.dataBase.AppDataBase;

public class LoanWorker extends Worker {
    private static final String TAG = "LoanWorker";
    private AppDataBase db;

    public LoanWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        db=AppDataBase.getInstance(context);

    }

    @NonNull
    @Override
    public Result doWork() {
        Data data=getInputData();
        int loan_id= data.getInt("loan_id",-1);
        int user_id=data.getInt("user_id",-1);
        Double monthly_payment= data.getDouble("monthly_payment",0.0);
        String name= data.getString("name");
        if (loan_id==-1 || user_id==-1||monthly_payment==0.0){
            return Result.failure();
        }




    }
}
