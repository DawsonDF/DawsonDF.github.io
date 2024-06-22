package com.example.freeman_option1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Main activity
 *
 * @constructor Create empty Main activity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHandler = InventoryDatabase(this)

        // Edit text for username and password
        val editTextUsername: EditText = findViewById(R.id.textUsername)
        val editTextPassword: EditText = findViewById(R.id.textPassword)

        // Buttons for register and login
        val buttonRegister: Button = findViewById(R.id.buttonRegister)
        val buttonLogin: Button = findViewById(R.id.buttonLogin)

        buttonRegister.setOnClickListener(View.OnClickListener {
            val username = editTextUsername.getText().toString()
            val password = editTextPassword.getText().toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter both username and password",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Check if the user already exists
                if (dbHandler.checkUsername(username)) {
                    Toast.makeText(this@MainActivity, "User already exists", Toast.LENGTH_LONG)
                        .show()
                    return@OnClickListener
                }

                val success = dbHandler.insertUser(username, password)
                if (success) {
                    Toast.makeText(
                        this@MainActivity,
                        "User registered successfully",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this@MainActivity, "User registration failed", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

        buttonLogin.setOnClickListener {
            val username = editTextUsername.getText().toString()
            val password = editTextPassword.getText().toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter both username and password",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val validateUser = dbHandler.checkUsernameAndPassword(username, password)
                if (validateUser) {
                    Toast.makeText(this@MainActivity, "Logged in successfully", Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent(this@MainActivity, SMSPermissionScreen::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Username or Password is invalid",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    /**
     * Go to sms
     *
     * @param view
     */
    fun goToSms(view: View?) {
        val intent = Intent(this, SMSPermissionScreen::class.java)
        startActivity(intent)
    }
}