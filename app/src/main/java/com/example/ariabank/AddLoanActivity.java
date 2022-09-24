package com.example.ariabank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddLoanActivity extends AppCompatActivity {
    private EditText edttxtName,edttxtInitAmount,edttxtROI,edttxtinitDate,edttxtFinishDate,edttxtMonthlyPayment;
    private Button btnPickinitDate,btnPickFinishDate,btnAddLoan;
    private TextView txtWarning;
    private Calendar initcalendar=Calendar.getInstance();
    private Calendar finishCalender=Calendar.getInstance();


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
        setOnClickListeners();
    }

    private void setOnClickListeners() {

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