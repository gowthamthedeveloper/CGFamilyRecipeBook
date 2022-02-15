package com.impeccthreads.cgfamilyrecipebook.application

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.location.LocationManager
import android.net.ConnectivityManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.WindowManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.impeccthreads.cgfamilyrecipebook.utility.PreferenceHelper

class CGFamilyRecipeBookApp: MultiDexApplication() {

    private var sharedPreferences: SharedPreferences? = null

    val connectivityManager: ConnectivityManager
        get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val locationManager: LocationManager
        get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val telephonyManager: TelephonyManager
        get() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    val deviceId: String
        @SuppressLint("MissingPermission")
        get() {
            val myAndroidDeviceId = this.telephonyManager.deviceId
            return myAndroidDeviceId
                    ?: Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        }

    val windowManager: WindowManager
        get() = windowManager

    private val editor: SharedPreferences.Editor
        get() = sharedPreferences!!.edit()

    override fun onCreate() {
        super.onCreate()
        application = this
        PreferenceHelper.init(applicationContext)

        //        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    /**
     * Checking the internet connectivity
     *
     * @return true if the connection is available otherwise false
     */
    fun hasNetworkConnection(): Boolean {

        val cm = connectivityManager
        var valid = false

        val wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting) {
            valid = true
        }

        val mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting) {
            valid = true
        }

        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            valid = true
        }

        return valid
    }

    fun setSharedIntData(key: String, value: Int) {
        val editor = editor
        editor.putInt(key, value)
        editor.commit()
    }

    fun getSharedIntData(key: String): Int {
        return sharedPreferences!!.getInt(key, -1)
    }

    fun setSharedStringData(key: String, value: String?) {
        val editor = editor
        if (value != null) {
            editor.putString(key, value)
        } else {
            editor.remove(key)
        }
        editor.commit()
    }

    fun getSharedFloatData(key: String): Float {
        return sharedPreferences!!.getFloat(key, -1f)
    }

    fun setSharedFloatData(key: String, value: Float) {
        val editor = editor
        editor.putFloat(key, value)
        editor.commit()
    }

    fun getSharedStringData(key: String): String? {
        return sharedPreferences!!.getString(key, null)
    }

    fun getSharedLongData(key: String): Long {
        return sharedPreferences!!.getLong(key, -1)
    }

    fun setSharedLongData(key: String, value: Long) {
        val editor = editor
        editor.putLong(key, value)
        editor.commit()
    }


    fun setSharedBooleanData(key: String, value: Boolean) {
        val editor = editor
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getSharedBooleanData(key: String): Boolean {
        return sharedPreferences!!.getBoolean(key, false)
    }

    fun clearData(key: String) {
        editor.remove(key).commit()
    }

    fun clearAllData() {
        editor.clear().commit()
    }

    companion object {
        lateinit var application: CGFamilyRecipeBookApp
            private set
    }
}