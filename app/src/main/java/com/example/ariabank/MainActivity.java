package com.example.ariabank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ariabank.Adapters.TransactionAdapter;
import com.example.ariabank.Authentication.LoginActivity;
import com.example.ariabank.Dialogs.AddItemDialog;
import com.example.ariabank.Dialogs.AddTransactionDialog;
import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.Shopping_table;
import com.example.ariabank.dataBase.Transaction;
import com.example.ariabank.dataBase.Users;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAGHELLO";
    private Utils utils;

    private BottomNavigationView bottomNavigationView;
    private TextView txtAmount,txtWelcome;
    private RecyclerView transactions;
    private BarChart barchar;
    private LineChart linecharts;
    private FloatingActionButton fbAddTransaction;
    private Toolbar toolbar;
    private getAccountAmount getAccountAmount;
    private TransactionAdapter adapter;
    private GetTransactionslst getTransactionslst;
    private GetProfit getProfit;
    private TextView txtName;


    

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initBottomNavView();


        setSupportActionBar(toolbar);

        utils=new Utils(this);



        Users user=utils.isUserLoggedIn();
        if(null!= user){
            Toast.makeText(this, "User "+user.getEmail()+" logged in", Toast.LENGTH_SHORT).show();
            txtName.setText(user.getFirst_name());
        }else {
            Intent intent=new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        setUpAmount();
        
        setOnClickListeners();
         initTransactionRecView();
        initLineChart();
        initBarChart();



        
        



    }
    private GetSpengind getSpenging;

    private void initBarChart() {
        getSpenging=new GetSpengind();
        getSpenging.execute(utils.isUserLoggedIn().getId());




    }

    private void initLineChart() {
        getProfit=new GetProfit();
        Users user=utils.isUserLoggedIn();
        if (user!=null){
            getProfit.execute(user.getId());

        }

    }

    private void initTransactionRecView() {
        adapter=new TransactionAdapter();
        transactions.setAdapter(adapter);
        transactions.setLayoutManager(new LinearLayoutManager(this));
        getTransactions();


    }

    private void getTransactions() {
        getTransactionslst=new GetTransactionslst();
        Users user=utils.isUserLoggedIn();
        getTransactionslst.execute(user.getId());

    }

    private void setOnClickListeners() {

        txtWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Aria Bank")
                        .setMessage("Designed and developed by Aria Taghizade.")
                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Visit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(MainActivity.this,WebsiteActivity.class);
                                startActivity(intent);
                            }
                        });

                builder.show();

            }
        });

        fbAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddTransactionDialog addTransactionDialog=new AddTransactionDialog();
                addTransactionDialog.show(getSupportFragmentManager(),"Add transaction Dialog");

            }
        });

    }

    private void setUpAmount() {
        Users user= utils.isUserLoggedIn();

        if(user!=null){
            getAccountAmount=new getAccountAmount();
            getAccountAmount.execute(user);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTransactions();
        setUpAmount();
        initLineChart();
        initBarChart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTransactions();
        setUpAmount();
        initLineChart();
        initBarChart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(getAccountAmount!=null){
            if(!getAccountAmount.isCancelled()){
                getAccountAmount.cancel(true);
            }
        }

        if(getTransactionslst!=null){
            if(!getTransactionslst.isCancelled()){
                getTransactionslst.cancel(true);
            }
        }

        if(getProfit!=null){
            if(!getProfit.isCancelled()){
                getProfit.cancel(true);
            }
        }

        if(getSpenging!=null){
            if(!getSpenging.isCancelled()){
                getSpenging.cancel(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_aria_bank:
                AlertDialog.Builder builder=new AlertDialog.Builder(this)
                        .setTitle("Aria Bank")
                        .setMessage("Developed by Aria Taghizade")
                        .setNegativeButton("Visit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(MainActivity.this,WebsiteActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("Invite Friends", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String txt="Hi, How are you?\n Checkout this cool app. It was developed by Aria Taghizade.";
                                Intent intent=new Intent(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_TEXT,txt);
                                intent.setType("text/plain");
                                Intent chooserIntent= Intent.createChooser(intent,"Send Message Via: ");
                                startActivity(chooserIntent);
                            }
                        });

                builder.show();
                break;

            case R.id.menu_exite:
                utils.signOutUser();
                recreate();

            case R.id.AddItem:
                AddItemDialog diag=new AddItemDialog();
                diag.show(getSupportFragmentManager(),"Add Item Dialog");




        }
        return super.onOptionsItemSelected(item);
    }

    private void initBottomNavView() {
        bottomNavigationView.setSelectedItemId(R.id.menu_item_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_stats:
                        Intent intent7=new Intent(MainActivity.this,StatsActivity.class);
                        intent7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent7);

                        break;

                    case R.id.menu_item_investment:

                        Intent intent=new Intent(MainActivity.this,InvestmentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        break;

                    case R.id.menu_item_loan:

                        Intent intent1=new Intent(MainActivity.this,LoanActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);

                        break;

                    case R.id.menu_item_transaction:

                        Intent intent5=new Intent(MainActivity.this,TransactionActivity.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent5);

                        break;

                    case R.id.menu_item_home:

                        break;





                }
                return false;
            }
        });

    }

    public class GetProfit extends AsyncTask<Integer,Void,ArrayList<Transaction>>{


        @Override
        protected ArrayList<Transaction> doInBackground(Integer... integers) {
           AppDataBase db=AppDataBase.getInstance(MainActivity.this);
           List<Transaction> transList=db.transactiondao().getprofit(integers[0],"profit");
           if(transList.size()>0){

               return new ArrayList<>(transList);

           }else {
               return null;
           }

        }


        @Override
        protected void onPostExecute(ArrayList<Transaction> transactions) {
            super.onPostExecute(transactions);
            
            if(transactions!=null){
                ArrayList<Entry> entries=new ArrayList<>();

                for(Transaction t:transactions){

                    try {

                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(t.getDate());


                        Calendar calendar=Calendar.getInstance();

                        int year=calendar.get(Calendar.YEAR);


                        calendar.setTime(date);

                        int month=calendar.get(Calendar.MONTH)+1;
                        if(calendar.get(Calendar.YEAR) == year){

                            boolean doesmonthexist=false;

                            for (Entry e:entries){
                                if(e.getX()==month){
                                    doesmonthexist=true;
                                    break;
                                }else {
                                    doesmonthexist=false;
                                }



                            }

                            if (!doesmonthexist){
                                entries.add(new Entry(month,(float) t.getAmount()));

                            }else {
                                for (Entry en:entries){
                                    if(en.getX()==month){
                                        en.setY(en.getY()+(float)t.getAmount());
                                    }
                                }
                            }



                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }

                for (Entry e:entries){
                    Log.d(TAG, "onPostExecute: x:  "+e.getX()+" Y: "+e.getY() );
                }

                LineDataSet dataset=new LineDataSet(entries,"Profit chart");

                dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                dataset.setDrawFilled(true);
                dataset.setFillColor(Color.GREEN);
                LineData data=new LineData(dataset);
                XAxis xAxis= linecharts.getXAxis();
                xAxis.setSpaceMin(1);
                xAxis.setSpaceMax(1);
                xAxis.setAxisMaximum(12);
                xAxis.setEnabled(false);
                YAxis yAxis= linecharts.getAxisRight();
                yAxis.setEnabled(false);
                YAxis yAxis1= linecharts.getAxisLeft();
                yAxis1.setAxisMaximum(200);
                yAxis1.setAxisMinimum(10);
                yAxis1.setDrawGridLines(false);
                linecharts.setNoDataTextColor(Color.GREEN);

                linecharts.setDescription(null);
                linecharts.animateY(4000);
                linecharts.setData(data);
                linecharts.invalidate();

            }else {
                Log.d(TAG, "onPostExecute: transaction was null");

            }
        }
    }

    private class GetTransactionslst extends AsyncTask<Integer,Void, ArrayList<Transaction>>{

        @Override
        protected ArrayList<Transaction> doInBackground(Integer... integers) {

            List<Transaction> lst=AppDataBase.getInstance(MainActivity.this).transactiondao().getUserTransactions(integers[0]);
            if(lst.size()>0){
                return new ArrayList<>(lst);
            }else {
                return null;
            }







        }

        @Override
        protected void onPostExecute(ArrayList<Transaction> transactions) {
            super.onPostExecute(transactions);
            if(transactions!=null){
                adapter.setTransactions(transactions);
            }else {
                adapter.setTransactions(new ArrayList<Transaction>());
            }


        }
    }

    private class getAccountAmount extends AsyncTask<Users,Void,Double>{
        private Utils util=new Utils(MainActivity.this);

        @Override
        protected Double doInBackground(Users... users) {
               AppDataBase db=AppDataBase.getInstance(MainActivity.this);
            Users user= util.isUserLoggedIn();
            List<Users> lst=db.usersdao().getSpecificUser(user.getEmail());
            Users myUser= lst.get(0);
            return myUser.getRemained_amount();








        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);


                txtAmount.setText(String.valueOf(aDouble)+"$");

        }
    }

    private class GetSpengind extends AsyncTask<Integer,Void,ArrayList<Shopping_table>>{

        @Override
        protected ArrayList<Shopping_table> doInBackground(Integer... integers) {
            AppDataBase db=AppDataBase.getInstance(MainActivity.this);

            List<Shopping_table> lt=db.shoppingDAO().getSPENDING(integers[0]);




           if(lt!=null) {
               if (lt.size() > 0) {
                   return new ArrayList<>(lt);
               } else {
                   return null;
               }
           }
           return null;



        }


        @Override
        protected void onPostExecute(ArrayList<Shopping_table> list) {
            super.onPostExecute(list);

            if (list!=null) {


                if (list.size() > 0) {
                    Log.d(TAG, "onPostExecute: size: " + list.size());

                    ArrayList<BarEntry> entries = new ArrayList<>();
                    for (Shopping_table s : list) {


                        try {
                            Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(s.getDate());

                            Calendar calendar = Calendar.getInstance();
                            int month = calendar.get(Calendar.MONTH) + 1;
                            calendar.setTime(date);
                            int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;


                            if (calendar.get(Calendar.MONTH) + 1 == month) {
                                boolean doesdayexist = false;

                                for (BarEntry e : entries) {
                                    if (e.getX() == day) {
                                        doesdayexist = true;
                                    } else {
                                        doesdayexist = false;
                                    }


                                }
                                Log.d(TAG, "onPostExecute: in For loop: " + String.valueOf(doesdayexist));

                                if (!doesdayexist) {
                                    entries.add(new BarEntry(day, (float) s.getPrice()));
                                } else {
                                    for (BarEntry e : entries) {
                                        if (e.getX() == day) {
                                            e.setY(e.getY() + (float) s.getPrice());
                                        }
                                    }
                                }


                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                    for (Entry e : entries) {
                        Log.d(TAG, "onPostExecute:Shopping:  x: " + e.getX() + " Y: " + e.getY());
                    }


                    BarDataSet dataset = new BarDataSet(entries, "Shopping Chart");
                    dataset.setValueTextSize(10f);

                    dataset.setColor(Color.GREEN);

                    BarData data = new BarData(dataset);
                    data.setBarWidth(0.9f);

                    barchar.getAxisRight().setEnabled(false);
                    XAxis xAxis = barchar.getXAxis();

                    xAxis.setAxisMaximum(30);
                    xAxis.setAxisMinimum(1);


                    xAxis.setEnabled(false);
                    YAxis yAxis = barchar.getAxisLeft();
                    yAxis.setAxisMaximum(150);
                    yAxis.setAxisMinimum(10);
                    yAxis.setDrawGridLines(false);

                    barchar.setData(data);

                    barchar.setDescription(null);


                    barchar.invalidate();


                } else {
                    Log.d(TAG, "onPostExecute: Null");
                }
            }


        }
    }

    private void initViews() {
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNavView);
        txtAmount= (TextView) findViewById(R.id.txtAmount);
        txtWelcome= (TextView) findViewById(R.id.txtwelcome);
        transactions= (RecyclerView) findViewById(R.id.transactionRecView);
        barchar= (BarChart) findViewById(R.id.profitChart);
        barchar.setNoDataTextColor(getResources().getColor(R.color.American_Green));
        linecharts= (LineChart) findViewById(R.id.dailySpentChart);
        linecharts.setNoDataTextColor(getResources().getColor(R.color.American_Green));
        fbAddTransaction= (FloatingActionButton) findViewById(R.id.fbTransaction);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        txtName= (TextView) findViewById(R.id.Name);
    }
}