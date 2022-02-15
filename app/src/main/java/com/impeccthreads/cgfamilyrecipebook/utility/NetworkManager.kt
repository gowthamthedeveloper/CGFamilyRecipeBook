package com.impeccthreads.cgfamilyrecipebook.utility

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class NetworkManager {

    companion object {
        fun isNetWorkAvailableNow(context: Context): Boolean {
            var isNetworkAvailable = false

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            setIsWifiEnable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected)
            setIsMobileNetworkAvailable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected)

            if (isWifiEnable() || isMobileNetworkAvailable()) {
                /*Sometime wifi is connected but service provider never connected to internet
            so cross check one more time*/
//                if (isOnline())
                    isNetworkAvailable = true
            }

            return isNetworkAvailable
        }

        fun isWifiEnable(): Boolean {
            return isWifiEnable
        }

        fun setIsWifiEnable(isWifiEnable: Boolean) {
            this.isWifiEnable = isWifiEnable
        }

        fun isMobileNetworkAvailable(): Boolean {
            return isMobileNetworkAvailable
        }

        fun setIsMobileNetworkAvailable(isMobileNetworkAvailable: Boolean) {
            this.isMobileNetworkAvailable = isMobileNetworkAvailable
        }

        private var isWifiEnable = false
        private var isMobileNetworkAvailable = false



        fun isOnline(): Boolean {
            /*Just to check Time delay*/
            val t = Calendar.getInstance().getTimeInMillis()

            val runtime = Runtime.getRuntime()
            try {
                /*Pinging to Google server*/
                val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
                val exitValue = ipProcess.waitFor()
                return exitValue == 0
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                val t2 = Calendar.getInstance().getTimeInMillis()
                Log.i("NetWork check Time", (t2 - t).toString())
            }
            return false
        }
    }


}