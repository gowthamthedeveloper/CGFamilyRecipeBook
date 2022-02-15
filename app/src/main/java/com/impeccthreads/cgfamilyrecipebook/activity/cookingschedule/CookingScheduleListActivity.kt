package com.impeccthreads.cgfamilyrecipebook.activity.cookingschedule

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.adapters.CookingScheduleListAdapter
import com.impeccthreads.cgfamilyrecipebook.application.AppCacheManager
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.dto.CookingScheduleDetails
import com.impeccthreads.cgfamilyrecipebook.modelhandler.FirebaseDataHandler
import com.impeccthreads.cgfamilyrecipebook.modelhandler.RecipeDataHandler
import kotlinx.android.synthetic.main.activity_cooking_schedule_list.*

class CookingScheduleListActivity : BaseActivity(), CookingScheduleListAdapter.CookingScheduleListAdapterListener, FirebaseDataHandler.FirebaseDataHandlerListener { //RecipeDataHandler.RecipeDataHandlerListener

    companion object {
        val COOKING_SCHEDULE_DETAIL = "COOKING_SCHEDULE_DETAIL"
    }

//    var dataHandler = RecipeDataHandler(this, this)
    var firebaseDataHandler = FirebaseDataHandler( this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cooking_schedule_list)

        showProgressBar()
        firebaseDataHandler.getCookingScheduleDetailsList()
    }

    fun btnCookingScheduleListBack(view: View) {
        finish()
    }

    fun btnAddCookingScheduleOnClick(view: View) {
        val intent = Intent(this, ScheduleCookingActivity::class.java)
        startActivity(intent)
    }

    fun showProgressBar() {
        loadingpanelmask_cookingScheduleList.visibility = View.VISIBLE
        loadingPanel_cookingScheduleList.visibility = View.VISIBLE
    }

    override fun apiHitFailure() {
        hideProgressBar()
    }

    override fun cookingRecipeDetailsListReceived() {
    }

    override fun cookingScheduleListReceived() {
        hideProgressBar()


        cookingScheduleListRecyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        cookingScheduleListRecyclerView.adapter = CookingScheduleListAdapter(this, AppCacheManager.allCookingScheduleDetailsList, this)
    }

    fun hideProgressBar() {
        loadingpanelmask_cookingScheduleList.visibility = View.GONE
        loadingPanel_cookingScheduleList.visibility = View.GONE
    }

    //Recycler Listener
    override fun didSelectViewAtPosition(position: Int) {

        val intent = Intent(this, CookingScheduleDetailsActivity::class.java)
        intent.putExtra(COOKING_SCHEDULE_DETAIL, AppCacheManager.allCookingScheduleDetailsList[position])
        startActivity(intent)
    }
}
