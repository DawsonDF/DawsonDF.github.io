package com.example.freeman_option1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItem extends AppCompatActivity {

    InventoryDatabase inventoryDatabase;

    Button addItem;

    EditText editTextItemCount, editTextItemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addItem = (Button) findViewById(R.id.addItemButton);

        editTextItemCount = (EditText) findViewById(R.id.editCount);
        editTextItemName = (EditText) findViewById(R.id.editName);

        addItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                String itemName = editTextItemName.getText().toString();
                String itemCount = editTextItemCount.getText().toString();

                inventoryDatabase = new InventoryDatabase(getBaseContext());
                try{
                    int x = Integer.parseInt(itemCount);
                }catch(NumberFormatException e){
                    Toast.makeText(getBaseContext(), "Item Count must be an integer", Toast.LENGTH_LONG).show();
                    return;
                }
                boolean success = inventoryDatabase.insertItem(itemName, itemCount);
                if (success){
                    Toast.makeText(getBaseContext(), "Item Inserted successfully", Toast.LENGTH_LONG).show();
                    editTextItemName.setText("");
                    editTextItemCount.setText("");
                    Intent intent = new Intent (getBaseContext(), GridViewLayout.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getBaseContext(), "Item insertion failure", Toast.LENGTH_LONG).show();
                };
            }
        });


    }

    public void goToGridViewLayout (View view){
        Intent intent = new Intent (this, GridViewLayout.class);
        startActivity(intent);
    }
}