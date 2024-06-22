package com.cs_499.app_inventory_management

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHandler = InventoryDatabase(this)

        // Edit text for username and password
        val editTextUsername = findViewById<EditText>(R.id.textUsername)
        val editTextPassword = findViewById<EditText>(R.id.textPassword)

        // Buttons for register and login
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)

        buttonRegister.setOnClickListener(View.OnClickListener {
            val username = editTextUsername.getText().toString()
            val password = editTextPassword.getText().toString()
            createAccount(username, password)
            /*if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@MainActivity, "Please enter both username and password", Toast.LENGTH_LONG).show()
            } else {
                // Check if the user already exists
                if (dbHandler.checkUsername(username)) {
                    Toast.makeText(this@MainActivity, "User already exists", Toast.LENGTH_LONG).show()
                    return@OnClickListener
                }

                val success = dbHandler.insertUser(username, password)
                if (success) {
                    Toast.makeText(this@MainActivity, "User registered successfully", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "User registration failed", Toast.LENGTH_LONG).show()
                }
            }*/
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
                loginUser(username, password)
            }
        }
    }
    fun createAccount(email: String, password: String) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error creating account", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun loginUser(email: String, password: String) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        val intent = Intent(this@MainActivity, GridViewLayout::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this@MainActivity, SMSPermissionScreen::class.java)
                        startActivity(intent)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun signOut() {
        auth = Firebase.auth
        auth.signOut()
    }

    /*fun goToSms() {
        val intent = Intent(this, SMSPermissionScreen::class.java)
        startActivity(intent)
    }*/

    fun goToSms(view: View) {
        val intent = Intent(this, SMSPermissionScreen::class.java)
        startActivity(intent)
    }
}