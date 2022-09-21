package com.example.ariabank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ariabank.R;
import com.example.ariabank.dataBase.item_table;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface GetItem{

        void onGettingItemResult(item_table item);
    }
    private GetItem getItem;

    private ArrayList<item_table> items=new ArrayList<>();

    private Context context;
    private DialogFragment dialogFragment;

    public ItemsAdapter() {
    }

    public void setItems(ArrayList<item_table> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public ItemsAdapter(Context context, DialogFragment dialogFragment) {
        this.context = context;
        this.dialogFragment = dialogFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemName.setText(items.get(position).getName());
        Glide.with(context).asBitmap().load(items.get(position).getImage_url()).into(holder.image);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getItem=(GetItem) dialogFragment;
                    getItem.onGettingItemResult(items.get(position));

                }catch (ClassCastException e){
                    e.printStackTrace();
                }




            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;
        private TextView itemName;
        private CardView parent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image= (ImageView) itemView.findViewById(R.id.itemimg);
            itemName=itemView.findViewById(R.id.itemName);
            parent=itemView.findViewById(R.id.parentItem);
        }
    }
}
