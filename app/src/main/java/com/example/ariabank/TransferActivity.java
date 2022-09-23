package com.example.ariabank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ariabank.dataBase.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TransferActivity extends AppCompatActivity {
    private static final String TAG = "TransferActivity";

    private EditText edttxtAmount,edttxtDescription,edttxtRecipient,edttxtDate;
    private TextView txtWarning;
    private Button btnAddTransfer,btnPickDate;
    private RadioGroup rgtype;
    private Calendar calendar=Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener DateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR,i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH,i2);

            edttxtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        initviews();
        setOnListeners();
    }

    private void setOnListeners() {
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TransferActivity.this,DateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        btnAddTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateData()){
                    txtWarning.setVisibility(View.GONE);
                    initAdding();

                }else {
                    txtWarning.setVisibility(View.VISIBLE);
                    txtWarning.setText("Please fill all the blanks.");

                }
            }
        });
    }

    private void initAdding() {
        Utils utils=new Utils(this);
        Users user= utils.isUserLoggedIn();
        if(user!=null){
            //TODO:EXECUTE.
        }
    }

    private class AddTransaction extends AsyncTask<Integer,Void,Void>{
        private Double amount;
        private String recipient,date,description,type;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.amount=Double.valueOf(edttxtAmount.getText().toString());
            this.date=edttxtDate.getText().toString();
            this.description=edttxtDescription.getText().toString();
            this.recipient=edttxtRecipient.getText().toString();
            rgtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i){
                        case R.id.btnrecieve:
                            type="receive";
                            break;
                        case R.id.btnSend:
                            type="send";

                            amount=-amount;
                            break;
                    }

                }
            });
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            return null;
        }
    }

    private boolean ValidateData() {
        if(edttxtAmount.getText().toString().equals("")){
            return false;
        }
        if(edttxtRecipient.getText().toString().equals("")){
            return false;
        }
        if(edttxtDate.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void initviews() {
        edttxtAmount= (EditText) findViewById(R.id.edttxtAmountTransfer);
        edttxtDescription= (EditText) findViewById(R.id.edttxtDescription);
        edttxtRecipient= (EditText) findViewById(R.id.edttxtRecipient);
        edttxtDate= (EditText) findViewById(R.id.edttxtDate);

        txtWarning= (TextView) findViewById(R.id.transferWarning);

        btnAddTransfer= (Button) findViewById(R.id.btnAddTransfer);
        btnPickDate= (Button) findViewById(R.id.btnPickDate);

        rgtype= (RadioGroup) findViewById(R.id.rgtype);
    }
}