package com.example.ariabank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ariabank.Adapters.InvestmentAdapter;
import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.InvestmentTable;
import com.example.ariabank.dataBase.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class InvestmentActivity extends AppCompatActivity {

    private RecyclerView InvestsRecView;
    private BottomNavigationView bottomnav;
    private InvestmentAdapter adapter;
    private GetInvestment getInvestment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);
        initView();
        initBottomNavView();

        adapter=new InvestmentAdapter(this);

        InvestsRecView.setAdapter(adapter);
        InvestsRecView.setLayoutManager(new LinearLayoutManager(this));
        getInvestment=new GetInvestment();
        Utils utils=new Utils(this);
        Users user= utils.isUserLoggedIn();
        if (user != null) {
            getInvestment.execute(user.getId());

        }

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(InvestmentActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(getInvestment!=null){
            if(!getInvestment.isCancelled()){
                getInvestment.cancel(true);
            }
        }
    }

    private class GetInvestment extends AsyncTask<Integer,Void, ArrayList<InvestmentTable>>{

        private AppDataBase db;



        @Override
        protected ArrayList<InvestmentTable> doInBackground(Integer... integers) {
            db=AppDataBase.getInstance(InvestmentActivity.this);
            List<InvestmentTable> lst=db.investmentdao().getInvestments(integers[0]);
            ArrayList<InvestmentTable> list=new ArrayList<>(lst);
            if(list.size()>0){
                return list;
            }else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<InvestmentTable> investments) {
            super.onPostExecute(investments);

            if(null!=investments){
                adapter.setInvestments(investments);
            }else{
                adapter.setInvestments(new ArrayList<InvestmentTable>());
            }

        }
    }

    private void initView() {
        InvestsRecView= (RecyclerView) findViewById(R.id.recviewinvestment);
        bottomnav= (BottomNavigationView) findViewById(R.id.bottomNavViewInvest);
    }

    private void initBottomNavView() {
        bottomnav.setSelectedItemId(R.id.menu_item_investment);
        bottomnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_stats:
                        //TODO: complete
                        break;

                    case R.id.menu_item_investment:

                        //TODO: complete
                        break;

                    case R.id.menu_item_loan:

                        //TODO: complete
                        break;

                    case R.id.menu_item_transaction:

                        //TODO: complete
                        break;

                    case R.id.menu_item_home:

                        Intent intent=new Intent(InvestmentActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;





                }
                return false;
            }
        });

    }

}