package com.cs_499.app_inventory_management

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

/**
 * Grid view layout
 *
 * @constructor Create empty Grid view layout
 */
class GridViewLayout : AppCompatActivity() {
    var inventoryDatabase: InventoryDatabase? = null
    var itemArrayList: ArrayList<Item>? = null
    var inventoryItemAdapter: InventoryItemAdapter? = null
    val mainActivity = MainActivity()

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_view_layout)
        val signOutButton = findViewById<ImageButton>(R.id.signoutButton)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val searchText = findViewById<EditText>(R.id.searchText)

        signOutButton.setOnClickListener {
            mainActivity.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val customCache = InventoryCache<String?, String?>(10000)


        //If permission is not already set on device, prompt user for permission to send sms
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED -> {
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) -> {
            }
            else -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), PERMISSIONSMS)
            }
        }

        itemArrayList = ArrayList()
        inventoryDatabase = InventoryDatabase(this@GridViewLayout)
        val database = Firebase.database
        val databaseRef = database.getReference("items")
        lifecycleScope.launch {
            inventoryDatabase!!.getTop10Items(databaseRef).collect { topItems ->
                // Process the list of top 10 items
                for (item in topItems) {
                    itemArrayList!!.add(item)
                    customCache.put(item.name, item.count)
                }

                // Now that itemArrayList is populated, create the adapter and set it
                inventoryItemAdapter = InventoryItemAdapter(itemArrayList!!, this@GridViewLayout)
                val recyclerView = findViewById<RecyclerView>(R.id.gridViewInventory)
                val linearLayoutManager = LinearLayoutManager(this@GridViewLayout, RecyclerView.VERTICAL, false)
                recyclerView.setLayoutManager(linearLayoutManager)
                recyclerView.setAdapter(inventoryItemAdapter)
            }
        }
        searchButton.setOnClickListener {
            val itemNameToSearch = searchText.text.toString()
            if (itemNameToSearch.isNotEmpty()) {
                lifecycleScope.launch {
                    var found = false;
                    inventoryDatabase!!.searchItem(databaseRef, itemNameToSearch).collect { itemToSearch ->
                        if (itemToSearch != null) {
                            // Item found, use the item object
                            println("Item found: ${itemToSearch.name}, count: ${itemToSearch.count}")
                            for (item in itemArrayList!!) {
                                if (item.name == itemNameToSearch) {
                                    found = true
                                }
                            }
                            if (!found){
                                itemArrayList!!.add(Item(itemNameToSearch, itemToSearch.count))
                            }
                        } else {
                            // Item not found
                            println("Item not found")
                        }
                    }
                }
            }
        }


        //itemArrayList = inventoryDatabase!!.listAllItems()


        /*inventoryItemAdapter = InventoryItemAdapter(itemArrayList!!, this@GridViewLayout)
        val recyclerView = findViewById<RecyclerView>(R.id.gridViewInventory)

        val linearLayoutManager = LinearLayoutManager(this@GridViewLayout, RecyclerView.VERTICAL, false)
        recyclerView.setLayoutManager(linearLayoutManager)

        recyclerView.setAdapter(inventoryItemAdapter)*/

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, permissionResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, permissionResults)
        when (requestCode) {
            PERMISSIONSMS -> {
                if (permissionResults.size > 0 && permissionResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Displays a toast that notifies the user that permission is granted
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show()
                } else {
                    //Displays a toast that notifies the user that permission is denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Go to add item
     *
     */
    fun goToAddItem(view: View) {
        val intent = Intent(this, AddItem::class.java)
        startActivity(intent)
    }

    /**
     * Go to login screen
     *
     */
    fun goToLoginScreen(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    /**
     * Go to app info
     *
     */// Navigates to app information screen
    fun goToAppInfo(view: View) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }

    companion object {
        private const val PERMISSIONSMS = 0
    }
}
