package com.example.ariabank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.LoanTable;
import com.example.ariabank.dataBase.Transaction;
import com.example.ariabank.dataBase.Users;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatsActivity extends AppCompatActivity {
    private BarChart barChart;
    private PieChart pieChart;
    private BottomNavigationView bottomNavigationView;
    private Utils utils;
    private GetLoans getLoans;
    private GetTransactions getTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        initViews();
        initBottomNavView();

        utils=new Utils(this);
        Users user= utils.isUserLoggedIn();
        if (null!=user){
            getTransactions=new GetTransactions();
            getTransactions.execute(user.getId());

            getLoans=new GetLoans();
            getLoans.execute(user.getId());

        }

    }

    private class GetTransactions extends AsyncTask<Integer,Void, ArrayList<Transaction>>{

        @Override
        protected ArrayList<Transaction> doInBackground(Integer... integers) {
            AppDataBase db=AppDataBase.getInstance(StatsActivity.this);
            List<Transaction> lst=db.transactiondao().getEverything();
            if(lst!=null){
                return new ArrayList<>(lst);
            }else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Transaction> transactions) {
            super.onPostExecute(transactions);
            if (null!=transactions){

                Calendar calendar=Calendar.getInstance();
                int currentmonth=calendar.get(Calendar.MONTH);
                int currentyear=calendar.get(Calendar.YEAR);
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

                ArrayList<BarEntry> entries=new ArrayList<>();
                for (Transaction t:transactions){
                    try {
                        Date date=sdf.parse(t.getDate());

                        calendar.setTime(date);
                        int month= calendar.get(Calendar.MONTH);
                        int year= calendar.get(Calendar.YEAR);
                        int day= calendar.get(Calendar.DAY_OF_MONTH)+1;
                        if(month==currentmonth && year==currentyear){
                            boolean doesDayExist=false;

                            for (BarEntry b:entries){
                                if (b.getX()==day){
                                    doesDayExist=true;
                                    break;

                                }else {
                                    doesDayExist=false;
                                }
                            }

                            if (doesDayExist){
                                for (BarEntry e:entries){
                                    if (e.getX()==day){
                                        e.setY(e.getY()+(float) Math.abs(t.getAmount()));
                                    }
                                }
                            }else {
                                entries.add(new BarEntry(day,(float) Math.abs(t.getAmount())));
                            }
                        }



                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                BarDataSet dataset = new BarDataSet(entries, "Account Activity");
                dataset.setValueTextSize(10f);


                dataset.setColor(Color.GREEN);

                BarData data = new BarData(dataset);
                data.setBarWidth(0.8f);
                data.setValueTextSize(8f);


                barChart.getAxisRight().setEnabled(false);
                XAxis xAxis = barChart.getXAxis();

                xAxis.setAxisMaximum(30);
                xAxis.setAxisMinimum(1);



                xAxis.setEnabled(false);
                YAxis yAxis = barChart.getAxisLeft();

                yAxis.setAxisMinimum(10);
                yAxis.setDrawGridLines(false);

                barChart.setData(data);

                Description description=new Description();
                description.setText("All of the accounts transaction.");
                description.setTextSize(12f);
                barChart.setDescription(description);

                barChart.setDescription(null);


                barChart.invalidate();


            }
        }
    }

    private class GetLoans extends AsyncTask<Integer,Void,ArrayList<LoanTable>>{

        @Override
        protected ArrayList<LoanTable> doInBackground(Integer... integers) {
            AppDataBase db=AppDataBase.getInstance(StatsActivity.this);
            List<LoanTable> lst=db.loandao().getAll();
            if(lst!=null){
                return new ArrayList<>(lst);
            }else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<LoanTable> loanTables) {
            super.onPostExecute(loanTables);
            if (loanTables!=null){
                ArrayList<PieEntry> entries=new ArrayList<>();
                double totalLoansAmount=0.0;
                double totalRemainedAmount=0.0;
                for (LoanTable l:loanTables){
                    totalLoansAmount+= l.getInit_amount();
                    totalRemainedAmount+=l.getRemained_amount();
                }
                entries.add(new PieEntry((float) totalLoansAmount,"Total"));

                entries.add(new PieEntry((float) totalRemainedAmount,"Remained"));
                PieDataSet dataset=new PieDataSet(entries,"");
                dataset.setColors(ColorTemplate.JOYFUL_COLORS);
                dataset.setSliceSpace(5f);

                PieData data=new PieData(dataset);
                data.setValueTextSize(15f);


                pieChart.setDrawHoleEnabled(false);
                pieChart.setDescription(null);
                pieChart.animateY(2000, Easing.EaseOutBack);
                pieChart.setData(data);
                pieChart.invalidate();
            }
        }
    }

    private void initViews() {
        barChart= (BarChart) findViewById(R.id.barChartActivities);
        barChart.setNoDataTextColor(getResources().getColor(R.color.American_Green));
        pieChart= (PieChart) findViewById(R.id.pieChartLoans);
        pieChart.setNoDataTextColor(getResources().getColor(R.color.American_Green));
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNavViewStats);
    }


    private void initBottomNavView() {
        bottomNavigationView.setSelectedItemId(R.id.menu_item_stats);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_stats:

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