package com.example.freeman_option1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity

class SMSPermissionScreen : AppCompatActivity() {

    @VisibleForTesting
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smspermission_screen)
    }

    fun onClickSmsPermission(v: View?) {
        //Switches to the grid view screen once clicked, which prompts user for SMS permission
        startActivity(Intent(this@SMSPermissionScreen, GridViewLayout::class.java))
    }
}