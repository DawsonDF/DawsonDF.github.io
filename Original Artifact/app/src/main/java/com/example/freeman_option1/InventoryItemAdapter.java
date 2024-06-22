package com.example.freeman_option1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.ViewHolder> {

    private ArrayList<Item> itemArrayList;
    private Context context;

    private InventoryItemAdapter inventoryItemAdapter;

    InventoryDatabase database;

    public InventoryItemAdapter(ArrayList<Item> itemArrayList, Context context){
        this.itemArrayList = itemArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Item item = itemArrayList.get(position);
        holder.itemNameTextView.setText(item.getName());
        holder.itemCountTextView.setText(item.getCount());


    }

    @Override
    public int getItemCount(){
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView itemNameTextView;
        private final TextView itemCountTextView;
        private ImageButton itemRemoveButton;
        private ImageButton countIncrementButton;
        private ImageButton countDecrementButton;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTextView = itemView.findViewById(R.id.editName);
            itemCountTextView = itemView.findViewById(R.id.itemCountText);
            itemRemoveButton = itemView.findViewById(R.id.deleteItemButton);
            countIncrementButton = itemView.findViewById(R.id.incrementButton);
            countDecrementButton = itemView.findViewById(R.id.decrementButton);

            database = new InventoryDatabase(context);

            itemRemoveButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    database.deleteItem(itemArrayList.get(getBindingAdapterPosition()).getName());
                    itemArrayList.remove(getBindingAdapterPosition());
                    notifyItemRemoved(getBindingAdapterPosition());
                }
            });

            countIncrementButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int currentCount = Integer.parseInt(itemArrayList.get(getBindingAdapterPosition()).getCount());
                    currentCount++;
                    String count = Integer.toString(currentCount);
                    itemArrayList.get(getBindingAdapterPosition()).setCount(count);
                    database.updateItemCount(itemArrayList.get(getBindingAdapterPosition()).getName(), count);
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });

            countDecrementButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int currentCount = Integer.parseInt(itemArrayList.get(getBindingAdapterPosition()).getCount());
                    currentCount--;
                    String count = Integer.toString(currentCount);
                    itemArrayList.get(getBindingAdapterPosition()).setCount(count);
                    database.updateItemCount(itemArrayList.get(getBindingAdapterPosition()).getName(), count);
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });
        }
    }
}
