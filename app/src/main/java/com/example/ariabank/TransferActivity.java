package com.example.ariabank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.Transaction;
import com.example.ariabank.dataBase.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TransferActivity extends AppCompatActivity {
    private static final String TAG = "TransferActivity";

    private EditText edttxtAmount, edttxtDescription, edttxtRecipient, edttxtDate;
    private TextView txtWarning;
    private Button btnAddTransfer, btnPickDate;
    private RadioGroup rgtype;
    private Calendar calendar = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener DateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);

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
                new DatePickerDialog(TransferActivity.this, DateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        btnAddTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidateData()) {
                    txtWarning.setVisibility(View.GONE);
                    initAdding();

                } else {
                    txtWarning.setVisibility(View.VISIBLE);
                    txtWarning.setText("Please fill all the blanks.");

                }
            }
        });
    }

    private AddTransaction addTransaction;

    private void initAdding() {
        Utils utils = new Utils(this);
        Users user = utils.isUserLoggedIn();
        if (user != null) {
            Log.d(TAG, "initAdding: user_id= "+user.getId());
            Log.d(TAG, "initAdding: Remained Amount= "+user.getRemained_amount());
            addTransaction = new AddTransaction();
            addTransaction.execute(user.getId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addTransaction != null) {
            if (addTransaction.isCancelled()) {
                addTransaction.cancel(true);
            }
        }
    }

    private boolean ValidateData() {
        if (edttxtAmount.getText().toString().equals("")) {
            return false;
        }
        if (edttxtRecipient.getText().toString().equals("")) {
            return false;
        }
        if (edttxtDate.getText().toString().equals("")) {
            return false;
        }
        return true;
    }

    private void initviews() {
        edttxtAmount = (EditText) findViewById(R.id.edttxtAmountTransfer);
        edttxtDescription = (EditText) findViewById(R.id.edttxtDescription);
        edttxtRecipient = (EditText) findViewById(R.id.edttxtRecipient);
        edttxtDate = (EditText) findViewById(R.id.edttxtDate);

        txtWarning = (TextView) findViewById(R.id.transferWarning);

        btnAddTransfer = (Button) findViewById(R.id.btnAddTransfer);
        btnPickDate = (Button) findViewById(R.id.btnPickDate);

        rgtype = (RadioGroup) findViewById(R.id.rgtype);
    }

    private class AddTransaction extends AsyncTask<Integer, Void, Void> {
        private Double amount;
        private String recipient, date, description, type;
        private AppDataBase db;
        private Utils util=new Utils(TransferActivity.this);

        @Override
        protected void onPreExecute() {
            db = AppDataBase.getInstance(TransferActivity.this);
            super.onPreExecute();
            this.amount = Double.valueOf(edttxtAmount.getText().toString());
            this.date = edttxtDate.getText().toString();
            this.description = edttxtDescription.getText().toString();
            this.recipient = edttxtRecipient.getText().toString();


            int rgId=rgtype.getCheckedRadioButtonId();
            switch (rgId) {
                case R.id.btnrecieve:
                    type = "receive";
                    break;
                case R.id.btnSend:
                    type = "send";

                    amount = -amount;
                    break;
            }

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            Transaction t1 = new Transaction(amount, date, type, integers[0], recipient, description);
            db.transactiondao().insertTransaction(t1);

            int id = db.transactiondao().getBackTransactionID(date, type, integers[0], description, amount);
            Users user= util.isUserLoggedIn();
            List<Users> lst=db.usersdao().getSpecificUser(user.getEmail());
            Users myUser= lst.get(0);
            Log.d(TAG, "doInBackground: AMount in new way: "+amount+myUser.getRemained_amount());
            db.usersdao().UpdateWithEmail(myUser.getEmail(), amount+myUser.getRemained_amount());

            return null;

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Intent intent = new Intent(TransferActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}


