package com.example.freeman_option1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.freeman_option1.GridViewLayout

/**
 * SMS permission screen
 *
 * @constructor Create empty SMS permission screen
 */
class SMSPermissionScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smspermission_screen)
    }

    /**
     * On click sms permission
     *
     * @param v
     */
    fun onClickSmsPermission(v: View?) {
        //Switches to the grid view screen once clicked, which prompts user for SMS permission
        startActivity(Intent(this@SMSPermissionScreen, GridViewLayout::class.java))
    }
}