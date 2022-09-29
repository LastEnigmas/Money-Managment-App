package com.example.ariabank.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.ariabank.R;
import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.item_table;

import org.w3c.dom.Text;

public class AddItemDialog extends DialogFragment {
    private Button btnAdd;
    private EditText name,url,description;
    private TextView warning;
    private AddItem addItem;

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (addItem!=null){
            if(!addItem.isCancelled()){
                addItem.cancel(true);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_add_item,null);
        btnAdd= (Button) view.findViewById(R.id.btnAddItem);
        name= (EditText) view.findViewById(R.id.edttxtItemsName);
        url= (EditText) view.findViewById(R.id.edttxtimgurl);
        description= (EditText) view.findViewById(R.id.edttxtItemDesc);
        warning= (TextView) view.findViewById(R.id.txtWarningItem);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(ValidateData()){
                   addItem=new AddItem();
                   addItem.execute();

               }else {
                   warning.setText("Please fill all the blanks");
               }

            }
        });

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity())
                .setTitle("Add Item")
                .setView(view);





        return builder.create();

    }

    private boolean ValidateData() {
        if (name.getText().toString().equals("")){

            return false;
        }
        if (url.getText().toString().equals("")){

            return false;
        }
        if (description.getText().toString().equals("")){

            return false;
        }
        return true;
    }


    private class AddItem extends AsyncTask<Void,Void,Void>{
        private String name,url,desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.name=AddItemDialog.this.name.getText().toString();
            this.url=AddItemDialog.this.url.getText().toString();
            this.desc=AddItemDialog.this.description.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            item_table item= new item_table(name,url,desc);
            AppDataBase db=AppDataBase.getInstance(getContext());
            db.itemdao().insertItem(item);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            dismiss();
        }
    }
}
