package com.cs_499.app_inventory_management

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventoryItemAdapter(private val itemArrayList: ArrayList<Item>, private val context: Context) : RecyclerView.Adapter<InventoryItemAdapter.ViewHolder>() {

    lateinit var database: InventoryDatabase
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
                database.deleteItem(itemArrayList[bindingAdapterPosition].name)
                itemArrayList.removeAt(bindingAdapterPosition)
                notifyItemRemoved(bindingAdapterPosition)
            }

            countIncrementButton.setOnClickListener {
                var currentCount = itemArrayList[bindingAdapterPosition].count?.toInt()
                currentCount = currentCount!! + 1
                val count = currentCount.toString()
                itemArrayList[bindingAdapterPosition].count = count
                itemArrayList[bindingAdapterPosition].name?.let { it1 ->
                    database!!.updateItemCount(
                        it1, count)
                }
                notifyItemChanged(bindingAdapterPosition)
            }

            countDecrementButton.setOnClickListener {
                var currentCount = itemArrayList[bindingAdapterPosition].count?.toInt()
                currentCount = currentCount!! - 1
                val count = currentCount.toString()
                itemArrayList[bindingAdapterPosition].count = count
                itemArrayList[bindingAdapterPosition].name?.let { it1 ->
                    database!!.updateItemCount(
                        it1, count)
                }
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }
}
