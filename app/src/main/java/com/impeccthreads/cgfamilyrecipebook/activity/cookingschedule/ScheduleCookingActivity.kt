package com.impeccthreads.cgfamilyrecipebook.activity.cookingschedule

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dosavillage.cxtdebug.adapter.AddTableItemAdapter
import com.google.firebase.database.FirebaseDatabase
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.application.Constants
import com.impeccthreads.cgfamilyrecipebook.application.CookingScheduleDetailsTable
import com.impeccthreads.cgfamilyrecipebook.application.DatabaseTable
import com.impeccthreads.cgfamilyrecipebook.dto.CookingScheduleDetails
import com.impeccthreads.cgfamilyrecipebook.dto.FieldValueDto
import kotlinx.android.synthetic.main.activity_schedule_cooking.*

class ScheduleCookingActivity : BaseActivity(), AddTableItemAdapter.AddTableItemAdapterListener {

    companion object {
        val EDITED_CAFE_SLOT_DETAILS = "EDITED_CAFE_SLOT_DETAILS"
    }

    var isEditing: Boolean = false
    var cookingScheduleDetailsDetail : CookingScheduleDetails? = null
    var cookingScheduleFieldValues: ArrayList<FieldValueDto> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_cooking)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.title = Constants.addCookingSchedule

        cookingScheduleFieldValues = Constants.getAddCookingScheduleFieldValueList()

        if (intent.hasExtra(CookingScheduleDetailsActivity.EDIT_COOKING_SCHEDULE_DETAIL))
        {
            isEditing = true
            this.title = Constants.editCookingScheduleActivity

            cookingScheduleDetailsDetail = intent.getParcelableExtra(CookingScheduleDetailsActivity.EDIT_COOKING_SCHEDULE_DETAIL)
            updateEditCookingScheduleFieldValues()
        }


        configureRecyclerView()
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

    fun updateEditCookingScheduleFieldValues() {

       for (i in cookingScheduleFieldValues.indices) {

           if (isEditing)
           {
               if (cookingScheduleFieldValues[i].name == CookingScheduleDetailsTable.family.toString() ||
                   cookingScheduleFieldValues[i].name == CookingScheduleDetailsTable.key.toString() ||
                   cookingScheduleFieldValues[i].name == CookingScheduleDetailsTable.scheduledate.toString())
               {
                   cookingScheduleFieldValues[i].isEditable = false
               }
           }


           when (cookingScheduleFieldValues[i].name)
           {
               CookingScheduleDetailsTable.key.toString() ->
                   cookingScheduleFieldValues[i].value = cookingScheduleDetailsDetail!!.key.toString()
               CookingScheduleDetailsTable.scheduledate.toString() ->
                   cookingScheduleFieldValues[i].value = cookingScheduleDetailsDetail!!.scheduledate
               CookingScheduleDetailsTable.family.toString() ->
                   cookingScheduleFieldValues[i].value = cookingScheduleDetailsDetail!!.family
               CookingScheduleDetailsTable.breakfast.toString() ->
                   cookingScheduleFieldValues[i].value = cookingScheduleDetailsDetail!!.breakfast
               CookingScheduleDetailsTable.lunch.toString() ->
                    cookingScheduleFieldValues[i].value = cookingScheduleDetailsDetail!!.lunch
               CookingScheduleDetailsTable.evening.toString() ->
                   cookingScheduleFieldValues[i].value = cookingScheduleDetailsDetail!!.evening
               CookingScheduleDetailsTable.dinner.toString() ->
                   cookingScheduleFieldValues[i].value = cookingScheduleDetailsDetail!!.dinner
               CookingScheduleDetailsTable.others.toString() ->
                   cookingScheduleFieldValues[i].value = cookingScheduleDetailsDetail!!.others
           }
       }
    }

    fun configureRecyclerView() {

        listViewAddCookingScheduleItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        listViewAddCookingScheduleItem.adapter = AddTableItemAdapter(this, cookingScheduleFieldValues, this)
    }

    fun showProgressBar() {
        loadingpanelmask_addCookingSchedulereport.visibility = View.VISIBLE
        loadingPanel_addCookingSchedulereport.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        loadingpanelmask_addCookingSchedulereport.visibility = View.GONE
        loadingPanel_addCookingSchedulereport.visibility = View.GONE
    }

    fun generateCookingScheduleRequest() : CookingScheduleDetails? {

        (listViewAddCookingScheduleItem.adapter as AddTableItemAdapter).getEnteredAttributes().let {

            if (it != null)
            {
                val request = CookingScheduleDetails()

                for (fieldValue in it!!)
                {
                    when (fieldValue.name)
                    {
                        CookingScheduleDetailsTable.family.toString() ->
                            request.family = fieldValue.value
                        CookingScheduleDetailsTable.scheduledate.toString() ->
                            request.scheduledate = fieldValue.value
                        CookingScheduleDetailsTable.breakfast.toString() ->
                            request.breakfast = fieldValue.value
                        CookingScheduleDetailsTable.lunch.toString() ->
                            request.lunch = fieldValue.value
                        CookingScheduleDetailsTable.evening.toString() ->
                            request.evening = fieldValue.value
                        CookingScheduleDetailsTable.dinner.toString() ->
                            request.dinner = fieldValue.value
                        CookingScheduleDetailsTable.others.toString() ->
                            request.others = fieldValue.value
                    }
                }


                return request
            }
        }

        return null
    }


    fun btnSaveCookingScheduleDetailsOnClick(view: View) {

        generateCookingScheduleRequest().let {

            if (it != null) {
                Log.d("Cafe Slot Request", it!!.toString())

                val builder = AlertDialog.Builder(this)

                builder.setTitle("Confirmation")

                builder.setMessage("Are you sure do you want to save slot?")

                builder.setPositiveButton("YES") {dialog, which ->
                    // Do something when user press the positive button
                    if (!isEditing)
                    {
                        //Add Cooking Schedule
                        FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingscheduledetails.toString()).push().setValue(it!!.toHashMap())
                        finish()
                    }
                    else
                    {
                        if (cookingScheduleDetailsDetail!!.key != null)
                        {
                            if (cookingScheduleDetailsDetail!!.key!!.isNotEmpty())
                            {
                                //Update Cooking Schedule
                                val updateCafeSlotRef = FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingscheduledetails.toString() + "/" + cookingScheduleDetailsDetail!!.key!!)
                                updateCafeSlotRef.updateChildren(it!!.toHashMap())

                                val result = Intent()
                                result.putExtra(EDITED_CAFE_SLOT_DETAILS, it!!)
                                setResult(Activity.RESULT_OK, result)
                                finish()
                            }
                        }
                    }
                }


                builder.setNegativeButton("No"){dialog,which ->

                }

                val dialog: AlertDialog = builder.create()

                dialog.show()

            }
        }

    }

    //LISTENER METHODS
    override fun didSelectViewAtPosition(position: Int) {

    }
}
