package com.impeccthreads.cgfamilyrecipebook.activity.cookingschedule

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.adapters.CookingScheduleDetailAdaptor
import com.impeccthreads.cgfamilyrecipebook.application.AppCacheManager
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.dto.CookingScheduleDetails
import com.impeccthreads.cgfamilyrecipebook.dto.FieldValueListDto
import com.impeccthreads.cgfamilyrecipebook.modelhandler.FirebaseDataHandler
import kotlinx.android.synthetic.main.activity_cooking_schedule_details.*

class CookingScheduleDetailsActivity : BaseActivity(), CookingScheduleDetailAdaptor.CookingScheduleDetailAdaptorListener {

    companion object {
        const val PICK_EDITED_SLOT_REQUEST_CODE = 10
        const val ACTION_EDIT_COOKING_SCHEDULE = 3

        val EDIT_COOKING_SCHEDULE_DETAIL = "EDIT_COOKING_SCHEDULE_DETAIL"
    }

    var cookingScheduleDetails: CookingScheduleDetails? = null
    var fieldValueList: FieldValueListDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cooking_schedule_details)

        cookingScheduleDetails = intent.getParcelableExtra(CookingScheduleListActivity.COOKING_SCHEDULE_DETAIL)

        cookingScheduleDetails.let {

            textViewCookingScheduleDetailTitle.setText(it!!.scheduledate)

            fieldValueList = it!!.convertToFieldValueListDto(false)

            fieldValueList.let {

                if (it != null)
                {
                    cookingScheduleDetailsRecyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
                    cookingScheduleDetailsRecyclerView.adapter = CookingScheduleDetailAdaptor(this, it!!, this)
                }
            }
        }


    }

    fun btnCookingScheduleDetailBack(view: View) {
        finish()
    }

    fun showProgressBar() {
        loadingpanelmask_cookingScheduleDetails.visibility = View.VISIBLE
        loadingPanel_cookingScheduleDetails.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        loadingpanelmask_cookingScheduleDetails.visibility = View.GONE
        loadingPanel_cookingScheduleDetails.visibility = View.GONE
    }

    fun btnEditCookingScheduleOnClick(view: View) {

        val popupMenu: PopupMenu = PopupMenu(this,view)

        popupMenu.menu.add(Menu.NONE, ACTION_EDIT_COOKING_SCHEDULE, 4,"Edit Schedule")

        popupMenu.setOnMenuItemClickListener({ item ->
            when(item.itemId) {
                ACTION_EDIT_COOKING_SCHEDULE -> {
                    val intent = Intent(this, ScheduleCookingActivity::class.java)
                    intent.putExtra(EDIT_COOKING_SCHEDULE_DETAIL, cookingScheduleDetails!!)
                    startActivityForResult(intent, PICK_EDITED_SLOT_REQUEST_CODE)
                }
            }
            true
        })

        popupMenu.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_EDITED_SLOT_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                data.let {

                    if (it != null) {

                        if (it!!.hasExtra(ScheduleCookingActivity.EDITED_CAFE_SLOT_DETAILS))
                        {
                            cookingScheduleDetails = it!!.getParcelableExtra(ScheduleCookingActivity.EDITED_CAFE_SLOT_DETAILS)

                            FirebaseDataHandler.updateCookingScheduleInList(cookingScheduleDetails!!, false)

                            reloadRecyclerView()
                        }
                    }
                }

            }
        }
    }

    fun reloadRecyclerView() {
        fieldValueList = cookingScheduleDetails!!.convertToFieldValueListDto(false)

        fieldValueList.let {

            if (it != null)
            {
                (cookingScheduleDetailsRecyclerView.adapter as CookingScheduleDetailAdaptor).setItemList(it!!)
            }
        }
    }

    //LISTENER METHODS
    override fun didSelectViewAtPosition(position: Int) {


    }

}
