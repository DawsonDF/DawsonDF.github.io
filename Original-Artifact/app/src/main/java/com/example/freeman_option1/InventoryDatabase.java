package com.example.freeman_option1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InventoryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int VERSION = 2;



    public InventoryDatabase(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }


    // Create table to store user credentials
    private static final class LoginTable{
        private static final String TABLE = "login";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
    }

    // Create table to store item information
    private static final class ItemTable{
        private static final String TABLE = "items";
        private static final String COL_ITEMNAME = "itemName";
        private static final String COL_ITEMCOUNT = "itemCount";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute query to create login table
        db.execSQL("create table " + LoginTable.TABLE + " (" +
                LoginTable.COL_USERNAME + " text PRIMARY KEY, " +
                LoginTable.COL_PASSWORD + " text) ");

        // Execute query to create item table
        db.execSQL("create table " + ItemTable.TABLE + " (" +
                ItemTable.COL_ITEMNAME + " text PRIMARY KEY, " +
                ItemTable.COL_ITEMCOUNT + " text) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + LoginTable.TABLE);
        db.execSQL("drop table if exists " + ItemTable.TABLE);
        onCreate(db);
    }

    // Insert user into database (used for registration)
    public boolean insertUser(String username, String password){
        SQLiteDatabase inventoryDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = inventoryDatabase.insert(LoginTable.TABLE, null, contentValues);

        if (result == -1) return false;
        else return true;
    }

    // Check if username already exists in database
    public boolean checkUsername(String username){
        SQLiteDatabase inventoryDatabase = this.getWritableDatabase();
        Cursor cursor = inventoryDatabase.rawQuery("SELECT * FROM login WHERE username = ?", new String[]{username});

        if (cursor.getCount() > 0){
            return true;
        } else{
            return false;
        }
    }

    // Query username and password for login verification
    public boolean checkUsernameAndPassword(String username, String password){
        SQLiteDatabase inventoryDatabase = this.getWritableDatabase();
        Cursor cursor = inventoryDatabase.rawQuery("SELECT * FROM login WHERE username = ? and password = ?", new String[]{username, password});

        if (cursor.getCount() > 0){
            return true;
        } else{
            return false;
        }
    }

    public boolean insertItem(String itemName, String itemCount){
        SQLiteDatabase inventoryDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("itemName", itemName);
        contentValues.put("itemCount", itemCount);
        long result = inventoryDatabase.insert(ItemTable.TABLE, null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    public ArrayList<Item> listAllItems(){
        SQLiteDatabase inventoryDatabase = this.getReadableDatabase();

        Cursor items = inventoryDatabase.rawQuery("SELECT * FROM " + ItemTable.TABLE, null);

        ArrayList<Item> itemArrayList = new ArrayList<>();

        if (items.moveToFirst()){
            do{
                itemArrayList.add(new Item(
                        items.getString(0),
                        items.getString(1)));

            }while(items.moveToNext());
        }
        return itemArrayList;
    }

    public boolean deleteItem(String name){
        SQLiteDatabase inventoryDatabase = this.getWritableDatabase();
        return inventoryDatabase.delete(ItemTable.TABLE, "itemName=?", new String[]{name}) > 0;
    }

    public void updateItemCount(String itemName, String itemCount){
        SQLiteDatabase inventoryDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("itemCount",itemCount);
        inventoryDatabase.update(ItemTable.TABLE, cv, "itemName = ?", new String[]{itemName});
    }

}
