package com.impeccthreads.cgfamilyrecipebook.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.activity.recipes.CategoryActivity
import com.impeccthreads.cgfamilyrecipebook.activity.useraccount.LoginActivity
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.utility.NetworkManager
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        configureView()
    }

    fun configureView() {
        if (NetworkManager.isNetWorkAvailableNow(this))
        {
            no_network_view.visibility = View.GONE

            Handler().postDelayed({
                /* Create an Intent that will start the Menu-Activity. */
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }, 500)
        }
        else
        {
            no_network_view.visibility = View.VISIBLE

        }
    }

    fun retryOnClickSplash(view: View) {

        if (NetworkManager.isNetWorkAvailableNow(this))
        {
            no_network_view.visibility = View.GONE

            val mainIntent = Intent(this, LoginActivity::class.java)
            startActivity(mainIntent)
        }
    }
}
