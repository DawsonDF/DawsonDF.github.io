package com.cs_499.app_inventory_management

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InventoryDatabase(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {


    fun addItemToFirebase(itemName: String, itemCount: String) {
        val database = Firebase.database
        val databaseReference = database.getReference("items")

        val itemData = mapOf(
            "name" to itemName,
            "count" to itemCount
        )

        databaseReference.child(itemName).setValue(itemData)
        .addOnSuccessListener {
            Log.d("InventoryDatabase", "Item added to Firebase");
        }.addOnFailureListener {
                Log.d("InventoryDatabase", "Failed to add item to Firebase");
        }
    }
    fun deleteItem(itemName: String?) {
        val database = Firebase.database
        val databaseReference = database.getReference("items")
        if (itemName != null) {
            databaseReference.child(itemName).removeValue()
                .addOnSuccessListener {
                    println("Item $itemName deleted from Firebase Realtime Database")
                    // You can add UI updates or other actions here
                }
                .addOnFailureListener {
                    println("Error deleting item $itemName: ${it.message}")
                    // Handle deletion errors here
                }
        }
    }
    fun searchItem(reference: DatabaseReference, itemName: String): Flow<Item?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemSnapshot = snapshot.child(itemName)
                if (itemSnapshot.exists()) {
                    val item = itemSnapshot.getValue(Item::class.java)
                    trySend(item) // Item found, send the Item object
                } else {
                    trySend(null) // Item not found, send null
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        reference.addListenerForSingleValueEvent(listener)

        awaitClose { reference.removeEventListener(listener) }
    }
    fun getAllItems(dataSnapshot: DataSnapshot): ArrayList<Item> {
        var item: Item
        val items = ArrayList<Item>()

        for(itemSnapshot in dataSnapshot.children){
            val itemName = itemSnapshot.child("itemName").getValue(String::class.java)
            val itemCount = itemSnapshot.child("itemCount").getValue(String::class.java)
            item = Item(itemName!!, itemCount!!)
            items.add(item)
        }
        return items
    }






    // Create table to store user credentials
    private object LoginTable {
        const val TABLE: String = "login"
        const val COL_USERNAME: String = "username"
        const val COL_PASSWORD: String = "password"
    }

    // Create table to store item information
    private object ItemTable {
        const val TABLE: String = "items"
        const val COL_ITEMNAME: String = "itemName"
        const val COL_ITEMCOUNT: String = "itemCount"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Execute query to create login table
        db.execSQL("create table " + LoginTable.TABLE + " (" +
                LoginTable.COL_USERNAME + " text PRIMARY KEY, " +
                LoginTable.COL_PASSWORD + " text) ")

        // Execute query to create item table
        db.execSQL("create table " + ItemTable.TABLE + " (" +
                ItemTable.COL_ITEMNAME + " text PRIMARY KEY, " +
                ItemTable.COL_ITEMCOUNT + " text) ")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists " + LoginTable.TABLE)
        db.execSQL("drop table if exists " + ItemTable.TABLE)
        onCreate(db)
    }

    // Insert user into database (used for registration)
    fun insertUser(username: String?, password: String?): Boolean {
        val inventoryDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        val result = inventoryDatabase.insert(LoginTable.TABLE, null, contentValues)

        return if (result == -1L) false
        else true
    }

    // Check if username already exists in database
    fun checkUsername(username: String): Boolean {
        val inventoryDatabase = this.writableDatabase
        val cursor = inventoryDatabase.rawQuery("SELECT * FROM login WHERE username = ?", arrayOf(username))

        return if (cursor.count > 0) {
            true
        } else {
            false
        }
    }

    // Query username and password for login verification
    fun checkUsernameAndPassword(username: String, password: String): Boolean {
        val inventoryDatabase = this.writableDatabase
        val cursor = inventoryDatabase.rawQuery("SELECT * FROM login WHERE username = ? and password = ?", arrayOf(username, password))

        return if (cursor.count > 0) {
            true
        } else {
            false
        }
    }

    fun insertItem(itemName: String?, itemCount: String?): Boolean {
        val inventoryDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("itemName", itemName)
        contentValues.put("itemCount", itemCount)
        val result = inventoryDatabase.insert(ItemTable.TABLE, null, contentValues)
        return if (result == -1L) false
        else true
    }

    fun listAllItems(): ArrayList<Item> {
        val inventoryDatabase = this.readableDatabase

        val items = inventoryDatabase.rawQuery("SELECT * FROM " + ItemTable.TABLE, null)

        val itemArrayList = ArrayList<Item>()

        if (items.moveToFirst()) {
            do {
                itemArrayList.add(Item(
                        items.getString(0),
                        items.getString(1)))
            } while (items.moveToNext())
        }
        return itemArrayList
    }

    /*fun deleteItem(name: String): Boolean {
        val inventoryDatabase = this.writableDatabase
        return inventoryDatabase.delete(ItemTable.TABLE, "itemName=?", arrayOf(name)) > 0
    }*/

    fun updateItemCount(itemName: String, itemCount: String?) {
        val inventoryDatabase = this.writableDatabase
        val cv = ContentValues()
        cv.put("itemCount", itemCount)
        inventoryDatabase.update(ItemTable.TABLE, cv, "itemName = ?", arrayOf(itemName))
    }

    companion object {
        private const val DATABASE_NAME = "inventory.db"
        private const val VERSION = 2
    }
}
