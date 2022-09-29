package com.example.ariabank.Dialogs;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ariabank.Adapters.ItemsAdapter;
import com.example.ariabank.R;
import com.example.ariabank.dataBase.AppDataBase;
import com.example.ariabank.dataBase.item_table;

import java.util.ArrayList;
import java.util.List;

public class SelectItemDialog extends DialogFragment implements ItemsAdapter.GetItem {
    private EditText edttxtItemName;
    private RecyclerView itemRecView;
    private ItemsAdapter.GetItem getItem;

    private ItemsAdapter adapter;
    private GetAllItems getAllItems;
    private SearchForItems searchForItems;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_select_item,null);

        edttxtItemName= (EditText) view.findViewById(R.id.edttxtItemName);
        itemRecView=  view.findViewById(R.id.itemRecView);

        adapter=new ItemsAdapter(getActivity(),this);
        itemRecView.setAdapter(adapter);
        itemRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

        edttxtItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchForItems=new SearchForItems();
                searchForItems.execute(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        getAllItems=new GetAllItems();
        getAllItems.execute();



        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Select an Item");


        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null!=getAllItems){
            if(!getAllItems.isCancelled()){
                getAllItems.cancel(true);
            }
        }

        if(null!=searchForItems){
            if(!searchForItems.isCancelled()){
                searchForItems.cancel(true);
            }
        }
    }

    @Override
    public void onGettingItemResult(item_table item) {
        try {
            getItem=(ItemsAdapter.GetItem) getActivity();
            getItem.onGettingItemResult(item);
            dismiss();

        }catch (ClassCastException e){
            e.printStackTrace();
        }


    }

    private class GetAllItems extends AsyncTask<Void,Void, ArrayList<item_table>>{


        @Override
        protected ArrayList<item_table> doInBackground(Void... voids) {
            AppDataBase db=AppDataBase.getInstance(getActivity());
            List<item_table> lst=db.itemdao().getAllItems();
            if (lst.size()>0){
                Log.d(TAG, "doInBackground: GetAll Item NOT Empty");
                return new ArrayList<>(lst);
            }else {
                Log.d(TAG, "doInBackground: Get All Item Empty");
                return null;
            }


        }

        @Override
        protected void onPostExecute(ArrayList<item_table> item_tables) {
            super.onPostExecute(item_tables);
            if(item_tables!=null){
                adapter.setItems(item_tables);

            }else {
                adapter.setItems(new ArrayList<>());
            }
        }
    }

    private class SearchForItems extends AsyncTask<String,Void,ArrayList<item_table>>{


        @Override
        protected ArrayList<item_table> doInBackground(String... strings) {
            AppDataBase db=AppDataBase.getInstance(getActivity());
            List<item_table> lst=db.itemdao().getItemWithName(strings[0]);
            if (lst.size()>0){
                return new ArrayList<>(lst);
            }else {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<item_table> item_tables) {
            super.onPostExecute(item_tables);
            if(item_tables!=null){
                adapter.setItems(item_tables);

            }else {
                adapter.setItems(new ArrayList<>());
            }
        }
    }
}
