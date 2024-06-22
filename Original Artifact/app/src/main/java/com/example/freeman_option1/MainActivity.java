package com.example.freeman_option1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    InventoryDatabase database;

    Button buttonRegister, buttonLogin;
    EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InventoryDatabase dbHandler = new InventoryDatabase(this);

        // Edit text for username and password
        editTextUsername = findViewById(R.id.textUsername);
        editTextPassword = findViewById(R.id.textPassword);

        // Buttons for register and login
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String username, password;

                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();


                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_LONG).show();
                }
                else{
                    // Check if the user already exists
                    if(dbHandler.checkUsername(username)){
                        Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_LONG).show();
                        return;
                    }

                    boolean success = dbHandler.insertUser(username, password);
                    if (success){
                        Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                    }else{
                       Toast.makeText(MainActivity.this, "User registration failed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String username, password;

                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();


                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_LONG).show();
                }
                else{
                    boolean validateUser = dbHandler.checkUsernameAndPassword(username, password);
                    if (validateUser){
                        Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent (MainActivity.this, SMSPermissionScreen.class );
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, "Username or Password is invalid", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public void goToSms (View view){
        Intent intent = new Intent (this, SMSPermissionScreen.class);
        startActivity(intent);
    }



    /*public void register(View view){

        EditText textUsername = (EditText) findViewById(R.id.textUsername);
        EditText textPassword = (EditText) findViewById(R.id.textPassword);

        String username = textUsername.getText().toString();
        String password = textPassword.getText().toString();

        if (username.isEmpty()){
            Toast.makeText(this, "Username cannot be null", Toast.LENGTH_LONG).show();
        }

        if (password.isEmpty()){
            Toast.makeText(this, "Password cannot be null", Toast.LENGTH_LONG).show();
        }

        if(username.length() >= 1 && password.length() >= 1){
            if (!database.checkUsername(username)){
                database.insertUser(username, password);
            }else{
                Toast.makeText(this, "User already exists!", Toast.LENGTH_LONG).show();
            }
        }
    }*/
}