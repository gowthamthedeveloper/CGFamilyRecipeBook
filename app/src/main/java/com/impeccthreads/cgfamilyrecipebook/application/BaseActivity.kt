package com.impeccthreads.cgfamilyrecipebook.application

import android.Manifest
import android.app.AlertDialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

open class BaseActivity: AppCompatActivity() {

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                235)
    }

    fun showDialog() {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        builder.setTitle("App background color")

        // Display a message on alert dialog
        builder.setMessage("Are you want to set the app background color to RED?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES"){dialog, which ->
            // Do something when user press the positive button
            showToast("Ok, we change the app background.")

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No"){dialog,which ->
            showToast("You are not agree.")
        }


        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->
            showToast("You cancelled the dialog.")
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

}