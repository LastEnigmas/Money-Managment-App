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
import android.widget.TextView;

import com.example.ariabank.Adapters.LoanAdapter;
import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.LoanTable;
import com.example.ariabank.dataBase.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LoanActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private LoanAdapter adapter;
    private GetLoans getLoans;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        initViews();
        initBottomNavView();
        adapter=new LoanAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Utils util=new Utils(this);
        Users user= util.isUserLoggedIn();
        if (user!=null){
            getLoans=new GetLoans();
            getLoans.execute(user.getId());
        }

    }
    private class GetLoans extends AsyncTask<Integer,Void, ArrayList<LoanTable>>{


        @Override
        protected ArrayList<LoanTable> doInBackground(Integer... integers) {
            AppDataBase db=AppDataBase.getInstance(LoanActivity.this);
            List<LoanTable> lst=db.loandao().getLoans(integers[0]);
            if (lst!=null ){
                return new ArrayList<>(lst);
            }else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<LoanTable> loanTables) {
            super.onPostExecute(loanTables);
            if (loanTables!=null){
                if (loanTables.size()==0){
                    txt.setVisibility(View.VISIBLE);

                }
                adapter.setLoans(loanTables);



            }else {
                adapter.setLoans(new ArrayList<>());
                txt.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initViews() {
        recyclerView= (RecyclerView) findViewById(R.id.loansrecview);
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNavView);
        txt= (TextView) findViewById(R.id.noLOan);
    }

    private void initBottomNavView() {
        bottomNavigationView.setSelectedItemId(R.id.menu_item_loan);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_stats:
                        Intent last=new Intent(LoanActivity.this,StatsActivity.class);
                        last.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(last);
                        break;

                    case R.id.menu_item_investment:

                        Intent investmentintent=new Intent(LoanActivity.this,InvestmentActivity.class);
                        investmentintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(investmentintent);

                        break;

                    case R.id.menu_item_loan:

                        break;

                    case R.id.menu_item_transaction:

                        Intent intent4=new Intent(LoanActivity.this,TransactionActivity.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent4);

                        break;


                    case R.id.menu_item_home:

                        Intent intent=new Intent(LoanActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        break;





                }
                return false;
            }
        });

    }


}