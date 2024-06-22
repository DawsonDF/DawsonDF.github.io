package com.example.freeman_option1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Inventory database
 *
 * @constructor
 *
 * @param context
 */
class InventoryDatabase(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
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
        db.execSQL(
            "create table " + LoginTable.TABLE + " (" +
                    LoginTable.COL_USERNAME + " text PRIMARY KEY, " +
                    LoginTable.COL_PASSWORD + " text) "
        )

        // Execute query to create item table
        db.execSQL(
            "create table " + ItemTable.TABLE + " (" +
                    ItemTable.COL_ITEMNAME + " text PRIMARY KEY, " +
                    ItemTable.COL_ITEMCOUNT + " text) "
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists " + LoginTable.TABLE)
        db.execSQL("drop table if exists " + ItemTable.TABLE)
        onCreate(db)
    }

    /**
     * Insert user
     *
     * @param username
     * @param password
     * @return
     */// Insert user into database (used for registration)
    fun insertUser(username: String?, password: String?): Boolean {
        val inventoryDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        val result = inventoryDatabase.insert(LoginTable.TABLE, null, contentValues)

        return if (result == -1L) false
        else true
    }

    /**
     * Check username
     *
     * @param username
     * @return
     */// Check if username already exists in database
    @SuppressLint("Recycle")
    fun checkUsername(username: String): Boolean {
        val inventoryDatabase = this.writableDatabase
        val cursor =
            inventoryDatabase.rawQuery("SELECT * FROM login WHERE username = ?", arrayOf(username))

        return if (cursor.count > 0) {
            true
        } else {
            false
        }
    }

    /**
     * Check username and password
     *
     * @param username
     * @param password
     * @return
     */// Query username and password for login verification
    fun checkUsernameAndPassword(username: String, password: String): Boolean {
        val inventoryDatabase = this.writableDatabase
        val cursor = inventoryDatabase.rawQuery(
            "SELECT * FROM login WHERE username = ? and password = ?",
            arrayOf(username, password)
        )

        return if (cursor.count > 0) {
            true
        } else {
            false
        }
    }

    /**
     * Insert item
     *
     * @param itemName
     * @param itemCount
     * @return
     */
    fun insertItem(itemName: String?, itemCount: String?): Boolean {
        val inventoryDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("itemName", itemName)
        contentValues.put("itemCount", itemCount)
        val result = inventoryDatabase.insert(ItemTable.TABLE, null, contentValues)
        return if (result == -1L) false
        else true
    }

    /**
     * List all items
     *
     * @return
     */
    fun listAllItems(): ArrayList<Item> {
        val inventoryDatabase = this.readableDatabase

        val items = inventoryDatabase.rawQuery("SELECT * FROM " + ItemTable.TABLE, null)

        val itemArrayList = ArrayList<Item>()

        if (items.moveToFirst()) {
            do {
                itemArrayList.add(
                    Item(
                        items.getString(0),
                        items.getString(1)
                    )
                )
            } while (items.moveToNext())
        }
        return itemArrayList
    }

    /**
     * Delete item
     *
     * @param name
     * @return
     */
    fun deleteItem(name: String): Boolean {
        val inventoryDatabase = this.writableDatabase
        return inventoryDatabase.delete(ItemTable.TABLE, "itemName=?", arrayOf(name)) > 0
    }

    /**
     * Update item count
     *
     * @param itemName
     * @param itemCount
     */
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
