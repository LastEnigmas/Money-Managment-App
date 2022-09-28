package com.example.ariabank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.ariabank.Adapters.TransactionAdapter;
import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.Transaction;
import com.example.ariabank.dataBase.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {

    private RadioGroup rgType;
    private EditText edtxtMin;
    private Button btnSearch;
    private RecyclerView transactionsRecView;
    private TextView txtNoTransaction;
    private BottomNavigationView bottomNavigationView;
    private TransactionAdapter adapter;
    private GetTransaction getTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initviews();
        initBottomNavView();
        adapter=new TransactionAdapter();

        transactionsRecView.setAdapter(adapter);
        transactionsRecView.setLayoutManager(new LinearLayoutManager(this));
        initSearch();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSearch();
            }
        });
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                initSearch();
            }
        });
    }


    private void initSearch() {
        Utils utils=new Utils(TransactionActivity.this);
        Users user= utils.isUserLoggedIn();
        if (user!=null){
            getTransaction=new GetTransaction();
            getTransaction.execute(user.getId());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(getTransaction!=null){
            if (!getTransaction.isCancelled()){
                getTransaction.cancel(true);
            }
        }
    }
    private void initviews() {
        rgType= (RadioGroup) findViewById(R.id.rgType);
        edtxtMin= (EditText) findViewById(R.id.edttxtMin);
        btnSearch= (Button) findViewById(R.id.btnSearch);

        transactionsRecView= (RecyclerView) findViewById(R.id.transactionsrecview1);

        txtNoTransaction= (TextView) findViewById(R.id.txtNoTransaction);
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNavViewtransaction);
    }

    private class GetTransaction extends AsyncTask<Integer,Void, ArrayList<Transaction>>{
        private String type="all";
        private Double min=0.0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.min=Double.valueOf(edtxtMin.getText().toString());
            switch (rgType.getCheckedRadioButtonId()){
                case R.id.rbInvestment:
                    type="investment";
                    break;
                case R.id.rbLoan:
                    type="loan";
                    break;
                case R.id.rbLoanPayment:
                    type="loan_payment";
                    break;
                case R.id.rbshopping:
                    type="shopping";
                    break;
                case R.id.rbSend:
                    type="send";
                    break;
                case R.id.rbProfit:
                    type="profit";
                    break;
                case R.id.rbrecieve:
                    type="receive";
                    break;
                default:
                    type="all";
                    break;



            }
        }

        @Override
        protected ArrayList<Transaction> doInBackground(Integer... integers) {
            AppDataBase db=AppDataBase.getInstance(TransactionActivity.this);
            if(type.equals("all")){
                List<Transaction> lst=db.transactiondao().getAllWithLimit(integers[0],min);
                return new ArrayList<>(lst);

            }else {
                List<Transaction> list=db.transactiondao().getTransaction(min,integers[0],type);
                return new ArrayList<>(list);

            }

        }

        @Override
        protected void onPostExecute(ArrayList<Transaction> transactions) {
            super.onPostExecute(transactions);
            if(null!= transactions){
                txtNoTransaction.setVisibility(View.GONE);
                adapter.setTransactions(transactions);


            }else {
                txtNoTransaction.setVisibility(View.VISIBLE);
                adapter.setTransactions(new ArrayList<>());
            }
        }
    }


    private void initBottomNavView() {
        bottomNavigationView.setSelectedItemId(R.id.menu_item_transaction);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_stats:
                      Intent last=new Intent(TransactionActivity.this,StatsActivity.class);
                        last.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(last);

                        break;

                    case R.id.menu_item_investment:

                        Intent intent=new Intent(TransactionActivity.this,InvestmentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        break;

                    case R.id.menu_item_loan:

                        Intent intent1=new Intent(TransactionActivity.this,LoanActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);

                        break;

                    case R.id.menu_item_transaction:

                        //TODO: complete
                        break;

                    case R.id.menu_item_home:

                        Intent intent2=new Intent(TransactionActivity.this,MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);

                        break;





                }
                return false;
            }
        });

    }
}