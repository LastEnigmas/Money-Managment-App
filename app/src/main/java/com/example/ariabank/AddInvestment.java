package com.example.ariabank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.InvestmentTable;
import com.example.ariabank.dataBase.Transaction;
import com.example.ariabank.dataBase.Users;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AddInvestment extends AppCompatActivity {
    private EditText edttxtname,edttxtInitAmount,edttxtROI,edttxtInitDate,edttxtFinishDate;
    private Button btnPickInitDate,btnPickFinishDate,btnAddINvestment;
    private TextView txtWarning;
    private Calendar initcalendar=Calendar.getInstance();
    private Calendar finishCalender=Calendar.getInstance();
    private Utils utils;


    private DatePickerDialog.OnDateSetListener initDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            initcalendar.set(Calendar.YEAR,i);
            initcalendar.set(Calendar.MONTH,i1);
            initcalendar.set(Calendar.DAY_OF_MONTH,i2);

            edttxtInitDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(initcalendar.getTime()));

        }
    };

    private DatePickerDialog.OnDateSetListener FinishDateListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            finishCalender.set(Calendar.YEAR,i);
            finishCalender.set(Calendar.MONTH,i1);
            finishCalender.set(Calendar.DAY_OF_MONTH,i2);

            edttxtFinishDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(finishCalender.getTime()));

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_investment);
        initViews();
        utils=new Utils(AddInvestment.this);
        setOnClickListeners();

    }

    private void setOnClickListeners() {
        btnPickInitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddInvestment.this,initDateSetListener,initcalendar.get(Calendar.YEAR),initcalendar.get(Calendar.MONTH),initcalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        btnPickFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddInvestment.this,FinishDateListener,finishCalender.get(Calendar.YEAR),finishCalender.get(Calendar.MONTH),finishCalender.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        btnAddINvestment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()){
                    txtWarning.setVisibility(View.GONE);
                    initAdding();

                }else {
                    txtWarning.setVisibility(View.VISIBLE);
                    txtWarning.setText("Please fill all the blanks");
                }
            }
        });
    }
    private AddTransaction addTransaction;
    private AddInvest addInvest;

    private void initAdding() {
        addTransaction=new AddTransaction();
        Users user= utils.isUserLoggedIn();
        if(user!=null){
            addTransaction.execute(user.getId());

        }

    }



    private class AddTransaction extends AsyncTask<Integer,Void,Integer>{
        private String date,name;
        private Double amount;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.date=edttxtInitDate.getText().toString();
            this.name=edttxtname.getText().toString();
            this.amount=Double.valueOf(edttxtInitDate.getText().toString());
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            AppDataBase db=AppDataBase.getInstance(AddInvestment.this);
            Transaction t1=new Transaction(amount,date,"investment",integers[0],name,"Initial amount for "+name+" investment");
            db.transactiondao().insertTransaction(t1);
            int id=db.transactiondao().getBackTransactionID(date,"investment",integers[0],"Initial amount for "+name+" investment",amount);
            return id;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer!= null){
                addInvest=new AddInvest();
                addInvest.execute(integer);
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(addTransaction!=null){
            if(!addTransaction.isCancelled()){
                addTransaction.cancel(true);
            }
        }

        if(addInvest!=null){
            if(!addInvest.isCancelled()){
                addInvest.cancel(true);
            }
        }
    }

    private class AddInvest extends AsyncTask<Integer,Void,Void>{
        private int user_id;
        private String initDate,FinishDate,name;
        private Double monthlyROI,amount;
        private AppDataBase db=AppDataBase.getInstance(AddInvestment.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.amount=Double.valueOf(edttxtInitAmount.getText().toString());
            this.monthlyROI=Double.valueOf(edttxtROI.getText().toString());
            this.name=edttxtname.getText().toString();
            this.initDate=edttxtInitAmount.getText().toString();
            this.FinishDate=edttxtFinishDate.getText().toString();
            Users user= utils.isUserLoggedIn();
            if(null!=user){
                this.user_id=user.getId();
            }else {
                this.user_id=-1;
            }

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            if(user_id!=-1){

                InvestmentTable invest1=new InvestmentTable(amount,monthlyROI,name,initDate,FinishDate,user_id,integers[0]);
                db.investmentdao().InsertInvestment(invest1);

                Double remainder=db.usersdao().getUserRemainder(utils.isUserLoggedIn().getId());

                db.usersdao().UpdateAmount(remainder-amount,user_id);



            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            Calendar calendar=Calendar.getInstance();

            SimpleDateFormat sdf=new SimpleDateFormat("yyy-MM-dd");

            try {
                Date initDate=sdf.parse(edttxtInitDate.getText().toString());
                calendar.setTime(initDate);
                int initmonths=calendar.get(Calendar.YEAR)*12 +calendar.get(Calendar.MONTH);
                Date finishDate =sdf.parse(edttxtFinishDate.getText().toString());
                calendar.setTime(finishDate);
                int finishmonths=calendar.get(Calendar.YEAR)*12 +calendar.get(Calendar.MONTH);

                int difference=finishmonths-initmonths;

                int days=0;


                for(int i=0;i<difference;i++){
                    days+=30;
                    Data data=new Data.Builder()
                            .putDouble("amount",amount* monthlyROI/100)
                            .putString("description","Profit for "+name)
                            .putInt("user_id",user_id)
                            .putString("recipient",name).build();

                    Constraints constraints=new Constraints.Builder()
                            .setRequiresBatteryNotLow(true).build();

                    OneTimeWorkRequest request=new OneTimeWorkRequest.Builder(InvestmentWorker.class)
                            .setInputData(data)
                            .setInitialDelay(days, TimeUnit.DAYS)
                            .addTag("Profit")
                            .setConstraints(constraints)
                            .build();

                    WorkManager.getInstance(AddInvestment.this).enqueue(request);

                    Intent intent=new Intent(AddInvestment.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);



                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean validateData() {
        if (edttxtname.getText().toString().equals("")){
            return false;
        }
        if (edttxtFinishDate.getText().toString().equals("")){
            return false;
        }
        if (edttxtInitDate.getText().toString().equals("")){
            return false;
        }

        if (edttxtROI.getText().toString().equals("")){
            return false;
        }
        if (edttxtInitAmount.getText().toString().equals("")){
            return false;
        }
        return true;


    }

    private void initViews() {
        edttxtname=  findViewById(R.id.edttxtInvestName);
        edttxtInitAmount=  findViewById(R.id.edttxtInitialAmount);
        edttxtROI=  findViewById(R.id.edttxtMonthlyRoi);
        edttxtInitDate=  findViewById(R.id.edttxtInitialDate);
        edttxtFinishDate=  findViewById(R.id.edttxtFinishDate);

        btnPickInitDate= (Button) findViewById(R.id.btnInitialDate);
        btnPickFinishDate= (Button) findViewById(R.id.btnFinishDate);
        btnAddINvestment= (Button) findViewById(R.id.btnAddInvestment);

        txtWarning= (TextView) findViewById(R.id.txtAddInvestmentWarning);
    }
}