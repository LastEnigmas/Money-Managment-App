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
import com.example.ariabank.dataBase.LoanTable;
import com.example.ariabank.dataBase.Transaction;
import com.example.ariabank.dataBase.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AddLoanActivity extends AppCompatActivity {
    private EditText edttxtName,edttxtInitAmount,edttxtROI,edttxtinitDate,edttxtFinishDate,edttxtMonthlyPayment;
    private Button btnPickinitDate,btnPickFinishDate,btnAddLoan;
    private TextView txtWarning;
    private Calendar initcalendar=Calendar.getInstance();
    private Calendar finishCalender=Calendar.getInstance();
    private Utils utils;
    private AddTransaction addTransaction;
    private AddLoan addLoan;



    private DatePickerDialog.OnDateSetListener initDateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            initcalendar.set(Calendar.YEAR,i);
            initcalendar.set(Calendar.MONTH,i1);
            initcalendar.set(Calendar.DAY_OF_MONTH,i2);

            edttxtinitDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(initcalendar.getTime()));

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
        setContentView(R.layout.activity_add_loan);
        initView();
        utils=new Utils(this);
        setOnClickListeners();
    }

    private void setOnClickListeners() {

        btnPickinitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddLoanActivity.this,initDateSetListener,initcalendar.get(Calendar.YEAR),initcalendar.get(Calendar.MONTH),initcalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        btnPickFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddLoanActivity.this,FinishDateListener,finishCalender.get(Calendar.YEAR),finishCalender.get(Calendar.MONTH),finishCalender.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        btnAddLoan.setOnClickListener(new View.OnClickListener() {
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

    private boolean validateData() {
        if (edttxtName.getText().toString().equals("")){
            return false;
        }
        if (edttxtFinishDate.getText().toString().equals("")){
            return false;
        }
        if (edttxtinitDate.getText().toString().equals("")){
            return false;
        }

        if (edttxtROI.getText().toString().equals("")){
            return false;
        }
        if (edttxtInitAmount.getText().toString().equals("")){
            return false;
        }
        if (edttxtMonthlyPayment.getText().toString().equals("")){
            return false;
        }
        return true;


    }

    private void initAdding(){

        Users user= utils.isUserLoggedIn();
        if(null!=user){
            addTransaction=new AddTransaction();
            addTransaction.execute(user.getId());

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
        if(addLoan!=null){
            if(!addLoan.isCancelled()){
                addLoan.cancel(true);
            }
        }
    }

    private class AddTransaction extends AsyncTask<Integer,Void,Integer>{
        private String date,name;
        private Double amount;
        private AppDataBase db=AppDataBase.getInstance(AddLoanActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.amount=Double.valueOf(edttxtInitAmount.getText().toString());
            this.name=edttxtName.getText().toString();
            this.date=edttxtinitDate.getText().toString();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            Transaction t1=new Transaction(amount,date,"loan",integers[0],name,"Received amount for "+name+" Loan");
            db.transactiondao().insertTransaction(t1);

            int trans_id=db.transactiondao().getBackTransactionID(date,"loan",integers[0],"Received amount for "+name+" Loan",amount);
            return trans_id;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(integer!=null){
                addLoan=new AddLoan();
                addLoan.execute(integer);
            }
        }
    }

    private class AddLoan extends AsyncTask<Integer,Void,Integer>{

        private int userID;
        private String name,initDate,FinishDate;
        private Double initAmount,monthlyROI,monthlyPayment;
        private AppDataBase db=AppDataBase.getInstance(AddLoanActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.name=edttxtName.getText().toString();
            this.initDate=edttxtinitDate.getText().toString();
            this.FinishDate=edttxtFinishDate.getText().toString();
            this.initAmount=Double.valueOf(edttxtInitAmount.getText().toString());
            this.monthlyROI=Double.valueOf(edttxtROI.getText().toString());
            this.monthlyPayment=Double.valueOf(edttxtMonthlyPayment.getText().toString());
            Users user= utils.isUserLoggedIn();;
            if (user!=null){
                this.userID=user.getId();
            }else {
                this.userID=-1;
            }
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            if(userID!=-1){
                LoanTable loan=new LoanTable(integers[0],initDate,FinishDate,initAmount,initAmount,monthlyPayment,name,userID);
                db.loandao().insertLoan(loan);

                int loan_id=db.loandao().getLoanID(initDate,FinishDate,name,initAmount);

                List<Users> user=db.usersdao().getSpecificUser(utils.isUserLoggedIn().getEmail());
                Double remainder=user.get(0).getRemained_amount();
                db.usersdao().UpdateAmount(remainder+initAmount,userID);
                return loan_id;


            }
            return null;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer != null) {
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date initDate=sdf.parse(this.initDate);
                    calendar.setTime(initDate);
                    int initMonths=calendar.get(Calendar.YEAR)*12 +calendar.get(Calendar.MONTH);

                    Date finishDate=sdf.parse(this.FinishDate);
                    calendar.setTime(finishDate);
                    int finishMonths=calendar.get(Calendar.YEAR)*12 +calendar.get(Calendar.MONTH);

                    int Months=finishMonths-initMonths;
                    int days=0;

                    for (int i=0;i<Months;i++){
                        days+=30;

                        Data data=new Data.Builder()
                                .putInt("loan_id",integer)
                                .putInt("user_id",userID)
                                .putDouble("monthly_payment",monthlyPayment)
                                .putString("name",name)
                                .build();

                        Constraints constraints=new Constraints.Builder()
                                .setRequiresBatteryNotLow(true)
                                .build();

                        OneTimeWorkRequest request=new OneTimeWorkRequest.Builder(LoanWorker.class)
                                .setInputData(data)
                                .setConstraints(constraints)
                                .setInitialDelay(days, TimeUnit.DAYS)
                                .addTag("loan_payment")
                                .build();

                        WorkManager.getInstance(AddLoanActivity.this).enqueue(request);



                    }



                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Intent intent=new Intent(AddLoanActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        }
    }

    private void initView() {
        edttxtName= (EditText) findViewById(R.id.edttxtLoanName);
        edttxtInitAmount= (EditText) findViewById(R.id.edttxtLoanInitialAmount);
        edttxtROI= (EditText) findViewById(R.id.edttxtLoanMonthlyRoi);
        edttxtinitDate= (EditText) findViewById(R.id.edttxtloanInitialDate);
        edttxtFinishDate= (EditText) findViewById(R.id.edttxtloanFinishDate);
        edttxtMonthlyPayment= (EditText) findViewById(R.id.edttxtLoanMonthlyPay);

        btnPickinitDate= (Button) findViewById(R.id.btnInitialloanDate);
        btnPickFinishDate= (Button) findViewById(R.id.btnloanFinishDate);
        btnAddLoan= (Button) findViewById(R.id.btnAddLoan);

        txtWarning= (TextView) findViewById(R.id.txtAddLoanWarning);
    }
}