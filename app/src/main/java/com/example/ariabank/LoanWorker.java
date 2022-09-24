package com.example.ariabank;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.Transaction;
import com.example.ariabank.dataBase.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class LoanWorker extends Worker {
    private static final String TAG = "LoanWorker";
    private AppDataBase db;
    private Utils utils;

    public LoanWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        db=AppDataBase.getInstance(context);
        utils=new Utils(context);

    }

    @NonNull
    @Override
    public Result doWork() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(calendar.getTime());
        Data data=getInputData();
        int loan_id= data.getInt("loan_id",-1);
        int user_id=data.getInt("user_id",-1);
        Double monthly_payment= data.getDouble("monthly_payment",0.0);
        String name= data.getString("name");
        if (loan_id==-1 || user_id==-1||monthly_payment==0.0){
            return Result.failure();
        }


        Transaction t1=new Transaction(-monthly_payment,date,"loan_payment",user_id,name,"Monthly payment for "+name+" loan");
        db.transactiondao().insertTransaction(t1);

        List<Users> user=db.usersdao().getSpecificUser(utils.isUserLoggedIn().getEmail());
        Double remainder=user.get(0).getRemained_amount();

        db.usersdao().UpdateAmount(remainder-monthly_payment,user_id);

        Double LoanInitial=db.loandao().getLoanAmount(name,monthly_payment,user_id);

        db.loandao().updateLoan(LoanInitial-monthly_payment,name,monthly_payment,user_id);
        return Result.success();










    }
}
