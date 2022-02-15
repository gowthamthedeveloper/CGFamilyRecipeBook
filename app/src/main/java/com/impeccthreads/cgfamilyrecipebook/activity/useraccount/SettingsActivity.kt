package com.impeccthreads.cgfamilyrecipebook.activity.useraccount

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.BuildConfig
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.adapters.HomeAdapter
import com.impeccthreads.cgfamilyrecipebook.application.AppCacheManager
import com.impeccthreads.cgfamilyrecipebook.utility.PreferenceHelper
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), HomeAdapter.HomeAdapterListener {

    var settingsArrayList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.title = "Settings"

//        settingsArrayList.add("Mobile: ${PreferenceHelper.getUserMobileNo()}")

        settingsArrayList.add("Name: ${AppCacheManager.currentUser.username}")
        settingsArrayList.add("Mobile: ${AppCacheManager.currentUser.mobileno}")
        settingsArrayList.add("Family: ${AppCacheManager.currentUser.family}")
        settingsArrayList.add("Version: ${BuildConfig.VERSION_NAME}")

        listViewSettings.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        listViewSettings.adapter = HomeAdapter(this, settingsArrayList, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    //Recycler Listener
    override fun didSelectViewAtPosition(position: Int) {

    }
}

