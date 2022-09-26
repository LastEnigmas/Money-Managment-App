package com.example.ariabank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ariabank.R;
import com.example.ariabank.dataBase.LoanTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.Viewholder> {
    private Context context;
    private ArrayList<LoanTable> loans=new ArrayList<>();
    private int number=-1;

    public LoanAdapter(Context context) {
        this.context = context;
    }

    public LoanAdapter() {
    }

    public void setLoans(ArrayList<LoanTable> loans) {
        this.loans = loans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loans,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.name.setText(loans.get(position).getName());
        holder.initDate.setText(loans.get(position).getInit_date());
        holder.finishDate.setText(loans.get(position).getFinish_date());
        holder.amount.setText(String.valueOf(loans.get(position).getInit_amount()));
        holder.roi.setText(String.valueOf(loans.get(position).getMonthly_roi()));
        holder.remained_amount.setText(String.valueOf(loans.get(position).getRemained_amount()));
        holder.loss.setText(String.valueOf(getTotalLoss(loans.get(position))));
        holder.monthly_payment.setText(String.valueOf(loans.get(position).getMonthly_payment()));
        if (number== -1){
            holder.parent.setCardBackgroundColor(context.getResources().getColor(R.color.light_blue));
            number=1;
        }else {
            holder.parent.setCardBackgroundColor(context.getResources().getColor(R.color.light_green));
            number=-1;

        }


    }

    private double getTotalLoss(LoanTable loan) {
        double loss=0.0;

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");



        try {

            calendar.setTime(sdf.parse(loan.getInit_date()));

            int initmonths=calendar.get(Calendar.YEAR)*12+ calendar.get(Calendar.MONTH);
            calendar.setTime(sdf.parse(loan.getFinish_date()));

            int finishmonths= calendar.get(Calendar.YEAR)*12+ calendar.get(Calendar.MONTH);

            int difference=finishmonths-initmonths;

            for (int i=0;i<difference;i++){
                loss+=loan.getInit_amount()*loan.getMonthly_roi()/100;

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return loss;





    }

    @Override
    public int getItemCount() {
        return loans.size();
    }

    protected class Viewholder extends RecyclerView.ViewHolder{

        private TextView name,initDate,finishDate,roi,loss,amount,remained_amount,monthly_payment;
        private CardView parent;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=  itemView.findViewById(R.id.loanname);
            initDate=itemView.findViewById(R.id.initdateloan);
            finishDate=itemView.findViewById(R.id.finishdateloan);
            roi=itemView.findViewById(R.id.monthlyROIloan);
            loss=itemView.findViewById(R.id.lossAmountLoan);
            amount=itemView.findViewById(R.id.amountLoan);
            remained_amount=itemView.findViewById(R.id.remainedAmountLoan);
            monthly_payment= (TextView) itemView.findViewById(R.id.txtmonthlypaymentloan);

            parent= (CardView) itemView.findViewById(R.id.loanParent);
        }
    }
}
