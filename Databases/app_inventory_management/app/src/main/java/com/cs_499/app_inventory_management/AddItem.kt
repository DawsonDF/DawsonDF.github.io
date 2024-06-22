package com.cs_499.app_inventory_management

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

/**
 * Add item
 *
 * @constructor Create empty Add item
 */
class AddItem : AppCompatActivity() {
    var inventoryDatabase: InventoryDatabase? = null
    var cache: InventoryCache<String, Item>? = null

    var addItem: Button? = null
    var searchItem: Button? = null

    var editTextItemCount: EditText? = null
    var editTextItemName: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        addItem = findViewById<View>(R.id.addItemButton) as Button
        searchItem = findViewById<View>(R.id.searchItemButton) as Button

        editTextItemCount = findViewById<View>(R.id.editCount) as EditText
        editTextItemName = findViewById<View>(R.id.editName) as EditText

        addItem!!.setOnClickListener(View.OnClickListener {
            val itemName = editTextItemName!!.text.toString()
            val itemCount = editTextItemCount!!.text.toString()

            inventoryDatabase = InventoryDatabase(baseContext)
            try {
                itemCount.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(baseContext, "Item Count must be an integer", Toast.LENGTH_LONG)
                    .show()
                return@OnClickListener
            }

            //val success = inventoryDatabase!!.insertItem(itemName, itemCount)
            try {
                inventoryDatabase!!.addItemToFirebase(itemName, itemCount)
                inventoryDatabase!!.insertItem(itemName, itemCount)
                Toast.makeText(baseContext, "Item Inserted successfully", Toast.LENGTH_LONG).show()
                editTextItemName!!.setText("")
                editTextItemCount!!.setText("")
                val intent = Intent(baseContext, GridViewLayout::class.java)
                startActivity(intent)

            } catch (e: Exception) {
                Toast.makeText(baseContext, "Item insertion failure", Toast.LENGTH_LONG).show()
            }
        })
        searchItem!!.setOnClickListener(View.OnClickListener {
            val database = Firebase.database
            val databaseReference = database.getReference("items")
            val itemName = editTextItemName!!.text.toString()
            inventoryDatabase = InventoryDatabase(baseContext)
            lifecycleScope.launch {
                inventoryDatabase!!.searchItem(databaseReference, itemName).collect { item ->
                    if (item != null) {
                        println("Item: ${item.name}" )
                        // Item found, use the item object
                        println("Item found: ${item.name}, count: ${item.count}")
                        inventoryDatabase!!.insertItem(item.name, item.count)
                        val intent = Intent(baseContext, GridViewLayout::class.java)
                        startActivity(intent)
                    } else {
                        // Item not found
                        println("Item not found")
                        val intent = Intent(baseContext, GridViewLayout::class.java)
                        startActivity(intent)
                    }
                }
            }
        })

        /*if (success) {
                Toast.makeText(baseContext, "Item Inserted successfully", Toast.LENGTH_LONG).show()
                editTextItemName!!.setText("")
                editTextItemCount!!.setText("")
                val intent = Intent(baseContext, GridViewLayout::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(baseContext, "Item insertion failure", Toast.LENGTH_LONG).show()
            }
        })*/

        /**
         * Go to grid view layout
         *
         */
        fun goToGridViewLayout(view: View) {
            val intent = Intent(this, GridViewLayout::class.java)
            startActivity(intent)
        }
    }
}