package com.example.freeman_option1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class GridViewLayout extends AppCompatActivity {

    private static final int permissionSms = 0;
    InventoryDatabase inventoryDatabase;
    ArrayList<Item> itemArrayList;
    RecyclerView recyclerView;
    InventoryItemAdapter inventoryItemAdapter;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_layout);


        //If permission is not already set on device, prompt user for permission to send sms
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
            }
            //Request permission for SEND_SMS
            else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, permissionSms);
            }
        }

        itemArrayList = new ArrayList<>();
        inventoryDatabase = new InventoryDatabase(GridViewLayout.this);

        itemArrayList = inventoryDatabase.listAllItems();

        inventoryItemAdapter = new InventoryItemAdapter(itemArrayList, GridViewLayout.this);
        recyclerView = findViewById(R.id.gridViewInventory);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GridViewLayout.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(inventoryItemAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] permissionResults) {
        super.onRequestPermissionsResult(requestCode, permissions, permissionResults);
        switch (requestCode) {
            case permissionSms: {
                if (permissionResults.length > 0 && permissionResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Displays a toast that notifies the user that permission is granted
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show();
                } else {
                    //Displays a toast that notifies the user that permission is denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void goToAddItem (View view){
        Intent intent = new Intent (this, AddItem.class);
        startActivity(intent);
    }

    public void goToLoginScreen(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Navigates to app information screen
    public void goToAppInfo(View view){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

}
