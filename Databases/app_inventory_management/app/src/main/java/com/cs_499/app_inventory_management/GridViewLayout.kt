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
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.snapshots
import com.google.firebase.ktx.Firebase

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

        signOutButton.setOnClickListener {
            mainActivity.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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

        itemArrayList = inventoryDatabase!!.listAllItems()

        inventoryItemAdapter = InventoryItemAdapter(itemArrayList!!, this@GridViewLayout)
        val recyclerView = findViewById<RecyclerView>(R.id.gridViewInventory)

        val linearLayoutManager = LinearLayoutManager(this@GridViewLayout, RecyclerView.VERTICAL, false)
        recyclerView.setLayoutManager(linearLayoutManager)

        recyclerView.setAdapter(inventoryItemAdapter)

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
