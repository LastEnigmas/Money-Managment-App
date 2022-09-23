package com.example.ariabank.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ariabank.R;

import com.example.ariabank.dataBase.Transaction;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.Viewholder>{
    private ArrayList<Transaction> transactions=new ArrayList<>();

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_transaction,parent,false);
        Viewholder holder=new Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.txtDate.setText(String.valueOf(transactions.get(position).getDate()));
        holder.txtDesc.setText(transactions.get(position).getDescription());
        holder.txtTransactionID.setText("Transaction Id: "+String.valueOf(transactions.get(position).getId()));
        holder.txtSender.setText(transactions.get(position).getRecipient());
        double amount=transactions.get(position).getAmount();
        if(amount>0){
            holder.txtAmount.setText("+ "+amount);
            holder.txtAmount.setTextColor(Color.rgb(52,179,52));
        }else {
            holder.txtAmount.setText(String.valueOf(amount));
            holder.txtAmount.setTextColor(Color.RED);

        }

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView txtAmount,txtSender,txtDesc,txtDate,txtTransactionID;
        private CardView parent;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            txtAmount=itemView.findViewById(R.id.txtAmount);
            txtSender=itemView.findViewById(R.id.txtSender);
            txtDesc=itemView.findViewById(R.id.txtDescription);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtTransactionID=itemView.findViewById(R.id.txtTransactionID);
            parent=itemView.findViewById(R.id.parent);
        }
    }
}
