package com.example.freeman_option1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Add item
 *
 * @constructor Create empty Add item
 */
class AddItem : AppCompatActivity() {
    var inventoryDatabase: InventoryDatabase? = null
    var cache: InventoryCache<String, Item>? = null

    var addItem: Button? = null

    var editTextItemCount: EditText? = null
    var editTextItemName: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        addItem = findViewById<View>(R.id.addItemButton) as Button

        editTextItemCount = findViewById<View>(R.id.editCount) as EditText
        editTextItemName = findViewById<View>(R.id.editName) as EditText

        addItem!!.setOnClickListener(View.OnClickListener {
            val itemName = editTextItemName!!.text.toString()
            val itemCount = editTextItemCount!!.text.toString()

            inventoryDatabase = InventoryDatabase(baseContext)
            try {
                itemCount.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(baseContext, "Item Count must be an integer", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            val success = inventoryDatabase!!.insertItem(itemName, itemCount)
            val item = Item(itemName, itemCount);
            cache!!.put(itemName, item);

            if (success) {
                Toast.makeText(baseContext, "Item Inserted successfully", Toast.LENGTH_LONG).show()
                editTextItemName!!.setText("")
                editTextItemCount!!.setText("")
                val intent = Intent(baseContext, GridViewLayout::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(baseContext, "Item insertion failure", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Go to grid view layout
     *
     */
    fun goToGridViewLayout() {
        val intent = Intent(this, GridViewLayout::class.java)
        startActivity(intent)
    }
}