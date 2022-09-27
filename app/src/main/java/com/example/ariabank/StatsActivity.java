package com.example.ariabank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.Transaction;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    private BarChart barChart;
    private PieChart pieChart;
    private BottomNavigationView bottomNavigationView;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        initViews();
        initBottomNavView();
        utils=new Utils(this);

    }

    private class GetTransactions extends AsyncTask<Integer,Void, ArrayList<Transaction>>{

        @Override
        protected ArrayList<Transaction> doInBackground(Integer... integers) {
            AppDataBase db=AppDataBase.getInstance(StatsActivity.this);
            return null;
        }
    }

    private void initViews() {
        barChart= (BarChart) findViewById(R.id.barChartActivities);
        pieChart= (PieChart) findViewById(R.id.pieChartLoans);
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNavViewStats);
    }


    private void initBottomNavView() {
        bottomNavigationView.setSelectedItemId(R.id.menu_item_stats);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_stats:
                        //TODO: complete
                        break;

                    case R.id.menu_item_investment:

                        Intent intent=new Intent(StatsActivity.this,InvestmentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        break;

                    case R.id.menu_item_loan:

                        Intent intent1=new Intent(StatsActivity.this,LoanActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);

                        break;

                    case R.id.menu_item_transaction:

                        Intent intent5=new Intent(StatsActivity.this,TransactionActivity.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent5);

                        break;

                    case R.id.menu_item_home:

                        Intent intent2=new Intent(StatsActivity.this,MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                        break;





                }
                return false;
            }
        });

    }
}