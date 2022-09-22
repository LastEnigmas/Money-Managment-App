package com.example.ariabank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ariabank.R;
import com.example.ariabank.dataBase.InvestmentTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class InvestmentAdapter extends RecyclerView.Adapter<InvestmentAdapter.ViewHolder> {
    private ArrayList<InvestmentTable> investments=new ArrayList<>();

    private int number=-1;
    private Context context;

    public InvestmentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_investment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(investments.get(position).getName());
        holder.initDate.setText(investments.get(position).getInit_date());
        holder.finishDate.setText(investments.get(position).getFinish_date());
        holder.amount.setText(String.valueOf(investments.get(position).getAmount())+"$");
        holder.roi.setText(String.valueOf(investments.get(position).getMonthly_roi())+"%");
        holder.profit.setText(String.valueOf(getTotalProfit(investments.get(position)))+"$");
        if (number== -1){
            holder.parent.setCardBackgroundColor(context.getResources().getColor(R.color.light_blue));
            number=1;
        }else {
            holder.parent.setCardBackgroundColor(context.getResources().getColor(R.color.light_green));
            number=-1;

        }

    }

    private double getTotalProfit(InvestmentTable investment) {


        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        double profit=0.0;
        try {
            calendar.setTime(sdf.parse(investment.getInit_date()));

            int initmonths=calendar.get(Calendar.YEAR)*12+ calendar.get(Calendar.MONTH);

            calendar.setTime(sdf.parse(investment.getFinish_date()));

            int finishmonths= calendar.get(Calendar.YEAR)*12+ calendar.get(Calendar.MONTH);

            int difference=finishmonths-initmonths;

            for (int i=0;i<difference;i++){
                profit+= investment.getAmount()* investment.getMonthly_roi()/100;

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return profit;


    }

    public void setInvestments(ArrayList<InvestmentTable> investments) {
        this.investments = investments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return investments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView parent;
        private TextView name,initDate,finishDate,roi,profit,amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent= (CardView) itemView.findViewById(R.id.investmentParent);

            name= (TextView) itemView.findViewById(R.id.investmentname);
            initDate= (TextView) itemView.findViewById(R.id.initdate);
            finishDate= (TextView) itemView.findViewById(R.id.finishdate);
            roi= (TextView) itemView.findViewById(R.id.monthlyROI);
            profit= (TextView) itemView.findViewById(R.id.profitamount);
            amount= (TextView) itemView.findViewById(R.id.amount);
        }
    }
}
