package com.impeccthreads.cgfamilyrecipebook.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.activity.cookingschedule.CookingScheduleListActivity
import com.impeccthreads.cgfamilyrecipebook.activity.recipes.CategoryActivity
import com.impeccthreads.cgfamilyrecipebook.activity.useraccount.SettingsActivity
import com.impeccthreads.cgfamilyrecipebook.adapters.HomeAdapter
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.application.Constants
import com.impeccthreads.cgfamilyrecipebook.modelhandler.SheetDataHandler
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), HomeAdapter.HomeAdapterListener {

    var moduleArrayList: ArrayList<String> = arrayListOf(Constants.cookingSchedule, Constants.cookDeliciousRecipe)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_home)

        listViewTable.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        listViewTable.adapter = HomeAdapter(this, moduleArrayList, this)
    }

    fun btnSettingsOnClick(view: View) {
        startActivity(Intent(this, SettingsActivity::class.java))

//        showToast("Coming Soon..!")
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)

        builder.setTitle("")

        builder.setMessage("Are you sure do you want exit?")

        builder.setPositiveButton("YES"){dialog, which ->
            // Do something when user press the positive button
            finishAffinity()
        }

        builder.setNegativeButton("No"){dialog,which ->

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    //Recycler Listener
    override fun didSelectViewAtPosition(position: Int) {

        when(moduleArrayList[position]) {
            Constants.cookingSchedule -> {
                startActivity(Intent(this, CookingScheduleListActivity::class.java))
            }
            Constants.cookDeliciousRecipe -> {
                startActivity(Intent(this, CategoryActivity::class.java))
            }
        }
    }
}
