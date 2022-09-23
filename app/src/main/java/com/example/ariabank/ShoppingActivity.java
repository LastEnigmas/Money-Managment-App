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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ariabank.Adapters.ItemsAdapter;
import com.example.ariabank.Dialogs.SelectItemDialog;
import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.Shopping_table;
import com.example.ariabank.dataBase.Transaction;
import com.example.ariabank.dataBase.Users;
import com.example.ariabank.dataBase.item_table;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity implements ItemsAdapter.GetItem {
    private static final String TAG = "ShoppingActivity";
    private Button btnPickDate,btnPickItem,btnAdd;
    private EditText edttxtDate,edttxtDesc,edttxtItemPrice,edttxtStore;
    private TextView txtWarning,txtItemName;
    private RelativeLayout itemRelLayout;
    private ImageView itemimg;
    private Calendar calendar=Calendar.getInstance();
    private item_table Selecteditem;

    private DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
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
        setContentView(R.layout.activity_shopping);
        initViews();
        btnPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ShoppingActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnPickItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectItemDialog selectItemDialog=new SelectItemDialog();
                selectItemDialog.show(getSupportFragmentManager(),"Select an Item");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAdd();
            }
        });


    }

    private AddShopping addShopping;

    private void initAdd() {
        if(null!= Selecteditem ){
            if(!edttxtItemPrice.getText().toString().equals("")){
                if (!edttxtDate.getText().toString().equals("")){
                    addShopping=new AddShopping();
                    addShopping.execute();
                }else {
                    txtWarning.setVisibility(View.VISIBLE);
                    txtWarning.setText("Please select a date");

                }

            }else {
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("Please add a price");

            }


        }else {
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Please select an item");
        }
    }

    private class AddShopping extends AsyncTask<Void,Void,Void>{

        private Users loggesInUser;
        private String date;
        private Double price;
        private String store;
        private String description;
        private AppDataBase db;
        private Utils util=new Utils(ShoppingActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Utils utils=new Utils(ShoppingActivity.this);
            loggesInUser= utils.isUserLoggedIn();
            this.date=edttxtDate.getText().toString();
            this.store=edttxtStore.getText().toString();
            this.price=-Double.parseDouble(edttxtItemPrice.getText().toString());
            this.description=edttxtDesc.getText().toString();

            db=AppDataBase.getInstance(ShoppingActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Transaction t1=new Transaction(price,date,"shopping",loggesInUser.getId(),store,description);
            db.transactiondao().insertTransaction(t1);

            int transaction_id=db.transactiondao().getBackTransactionID(date,"shopping",loggesInUser.getId(),description,price);



            Shopping_table shop1=new Shopping_table(Selecteditem.getId(),loggesInUser.getId(),transaction_id,-price,date,description);
            db.shoppingDAO().insertAnewPurchase(shop1);

            Users user= util.isUserLoggedIn();
            List<Users> lst=db.usersdao().getSpecificUser(user.getEmail());
            Users myUser= lst.get(0);
            db.usersdao().UpdateWithEmail(myUser.getEmail(), myUser.getRemained_amount()+price);




            return null;


        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Toast.makeText(ShoppingActivity.this, Selecteditem.getName()+" added successfully.", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(ShoppingActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void initViews() {
        btnPickDate= (Button) findViewById(R.id.btnSelectDate);
        btnAdd= (Button) findViewById(R.id.btnAdd);
        btnPickItem= (Button) findViewById(R.id.btnPick);
        edttxtDate= (EditText) findViewById(R.id.edttxtDate);
        edttxtDesc= (EditText) findViewById(R.id.edttxtDescription);
        edttxtItemPrice= (EditText) findViewById(R.id.edttxtPrice);
        edttxtStore= (EditText) findViewById(R.id.edttxtStore);

        txtWarning= (TextView) findViewById(R.id.txtWarning);
        txtItemName= (TextView) findViewById(R.id.txtItemName);
        itemRelLayout= (RelativeLayout) findViewById(R.id.invisiblerellayout);
        itemimg= (ImageView) findViewById(R.id.itemimg);

    }

    @Override
    public void onGettingItemResult(item_table item) {
        Selecteditem=item;
        itemRelLayout.setVisibility(View.VISIBLE);
        Glide.with(this).asBitmap().load(item.getImage_url()).into(itemimg);
        txtItemName.setText(item.getName());
        edttxtDesc.setText(item.getDescription());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(addShopping!=null){
            if(!addShopping.isCancelled()){
                addShopping.cancel(true);
            }
        }
    }
}