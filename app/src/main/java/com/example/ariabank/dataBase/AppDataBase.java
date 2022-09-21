package com.example.ariabank.dataBase;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {InvestmentTable.class,item_table.class,LoanTable.class,Shopping_table.class, Transaction.class,Users.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract investmentDAO investmentdao();
    public abstract itemDAO itemdao();
    public abstract loanDAO loandao();
    public abstract shoppingDAO shoppingDAO();
    public abstract TransactionDAO transactiondao();
    public abstract usersDAO usersdao();

    private static AppDataBase instance;

    public static synchronized AppDataBase getInstance(Context context){
        if(instance==null){
            instance=Room.databaseBuilder(context,AppDataBase.class,"AppDataBase")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(initialCallback)
                    .build();

        }
        return instance;
    }

    private static RoomDatabase.Callback initialCallback=new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new init_asyn(instance).execute();
        }
    };

    private static class init_asyn extends AsyncTask<Void,Void,Void>{
        private itemDAO itemdao;
        private TransactionDAO transactionDAO;
        private shoppingDAO shoppingDAO;

        public init_asyn(AppDataBase db) {
            this.itemdao = db.itemdao();
            this.transactionDAO=db.transactiondao();
            this.shoppingDAO=db.shoppingDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            item_table it1=new item_table("Bike","https://hips.hearstapps.com/hmg-prod/images/marin-hawkhill-3-slider-1555704455.jpg",
                    "Professional MARIN Mountain Bike ");

            itemdao.insertItem(it1);


            Transaction trans1=new Transaction(55.0, "2022-08-10", "profit",0,"TD Bank","Monthly ROI");

            transactionDAO.insertTransaction(trans1);

            Transaction trans2=new Transaction(30.0, "2022-08-26", "profit",0,"Simple Invest","Monthly ROI");


            Transaction trans3=new Transaction(110.0, "2022-07-11", "profit",0,"TD Bank","Monthly ROI");

            transactionDAO.insertTransaction(trans3);
            transactionDAO.insertTransaction(trans2);


            Shopping_table b1=new Shopping_table(1,0,1,25.0,"2022-09-01","boughtnumber1");
            Shopping_table b2=new Shopping_table(2,0,2,20.0,"2022-09-01","boughtnumber1");
            Shopping_table b3=new Shopping_table(3,0,3,35.0,"2022-09-02","boughtnumber1");

            shoppingDAO.insertAnewPurchase(b1);
            shoppingDAO.insertAnewPurchase(b2);
            shoppingDAO.insertAnewPurchase(b3);






            return null;
        }
    }





}
