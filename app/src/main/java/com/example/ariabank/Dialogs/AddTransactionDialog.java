package com.example.ariabank.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.ariabank.AddInvestment;
import com.example.ariabank.R;
import com.example.ariabank.ShoppingActivity;
import com.example.ariabank.TransferActivity;

public class AddTransactionDialog extends DialogFragment {

    private RelativeLayout shopping,investment,loan,transaction;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_add_transaction,null);

        shopping=view.findViewById(R.id.shoppingrellayout);
        investment=view.findViewById(R.id.investmentrellayout);
        loan=view.findViewById(R.id.loanrellayout);
        transaction=view.findViewById(R.id.senlayout);

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ShoppingActivity.class);
                startActivity(intent);
            }
        });

        investment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddInvestment.class);
                startActivity(intent);

            }
        });

        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Navigate the user.

            }
        });

        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), TransferActivity.class);
                startActivity(intent);

            }
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity())
                .setTitle("Add Transaction")
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setView(view);


        return builder.create();
    }
}
