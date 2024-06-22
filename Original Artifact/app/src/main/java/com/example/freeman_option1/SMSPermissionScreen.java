package com.example.freeman_option1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SMSPermissionScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smspermission_screen);

    }

    public void onClickSmsPermission(View v)
    {
        //Switches to the grid view screen once clicked, which prompts user for SMS permission
        startActivity(new Intent(SMSPermissionScreen.this, GridViewLayout.class));
    }
}