package com.example.freeman_option1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventoryItemAdapter(private val itemArrayList: ArrayList<Item>, private val context: Context) : RecyclerView.Adapter<InventoryItemAdapter.ViewHolder>() {

    var database: InventoryDatabase? = null
    var cache: InventoryCache<String, Item>? = null


    override fun onCreateViewHolder(parent: ViewGroup, ViewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inventory_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemArrayList[position]
        holder.itemNameTextView.text = item.name
        holder.itemCountTextView.text = item.count
    }

    override fun getItemCount(): Int {
        return itemArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.editName)
        val itemCountTextView: TextView = itemView.findViewById(R.id.itemCountText)
        private val itemRemoveButton: ImageButton = itemView.findViewById(R.id.deleteItemButton)
        private val countIncrementButton: ImageButton = itemView.findViewById(R.id.incrementButton)
        private val countDecrementButton: ImageButton = itemView.findViewById(R.id.decrementButton)


        init {
            database = InventoryDatabase(context)

            itemRemoveButton.setOnClickListener {
                database!!.deleteItem(itemArrayList[bindingAdapterPosition].name)
                cache!!.remove(itemArrayList[bindingAdapterPosition].name);
                itemArrayList.removeAt(bindingAdapterPosition)
                notifyItemRemoved(bindingAdapterPosition)
            }

            countIncrementButton.setOnClickListener {
                var currentCount = itemArrayList[bindingAdapterPosition].count.toInt()
                currentCount++
                val count = currentCount.toString()
                itemArrayList[bindingAdapterPosition].count = count
                database!!.updateItemCount(itemArrayList[bindingAdapterPosition].name, count)
                cache!!.put(itemArrayList[bindingAdapterPosition].name, itemArrayList[bindingAdapterPosition])
                notifyItemChanged(bindingAdapterPosition)
            }

            countDecrementButton.setOnClickListener {
                var currentCount = itemArrayList[bindingAdapterPosition].count.toInt()
                currentCount--
                val count = currentCount.toString()
                itemArrayList[bindingAdapterPosition].count = count
                database!!.updateItemCount(itemArrayList[bindingAdapterPosition].name, count)
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }
}
