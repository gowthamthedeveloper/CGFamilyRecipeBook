package com.impeccthreads.cgfamilyrecipebook.utility

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.impeccthreads.cgfamilyrecipebook.application.Constants
import com.impeccthreads.cgfamilyrecipebook.dto.User

object PreferenceHelper {

    private var defaultPrefs: SharedPreferences? = null

    private val appCustomPreferenceName = "CGFamilyRecipeBookPref"
    private var customPrefs: SharedPreferences? = null

    fun init(context: Context) {
        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun customInit(context: Context) {
        customPrefs = context.getSharedPreferences(appCustomPreferenceName, Context.MODE_PRIVATE)
    }

    private operator fun set(key: String, value: Any?) {
        when (value) {
            is String? -> defaultPrefs?.edit()?.putString(key, value)?.apply()
            is Int -> defaultPrefs?.edit()?.putInt(key, value)?.apply()
            is Boolean -> defaultPrefs?.edit()?.putBoolean(key, value)?.apply()
            is Float -> defaultPrefs?.edit()?.putFloat(key, value)?.apply()
            is Long -> defaultPrefs?.edit()?.putLong(key, value)?.apply()
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    private operator inline fun <reified T : Any> get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> defaultPrefs?.getString(key, defaultValue as? String) as T?
            Int::class -> defaultPrefs?.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> defaultPrefs?.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> defaultPrefs?.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> defaultPrefs?.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun isRecipeBookSynced(): Boolean? {
        return get(Constants.SharedPrefKeys.isRecipeBookSynced)
    }

    fun setRecipeBookSynced(isDocSynced: Boolean) {
        set(Constants.SharedPrefKeys.isRecipeBookSynced, isDocSynced)
    }

    fun isRecipeBookDownloaded(): Boolean {
        return get(Constants.SharedPrefKeys.isRecipeBookSynced) ?: false
    }

    fun setRecipeBookDownloaded(isDocDownloaded: Boolean) {
        set(Constants.SharedPrefKeys.isRecipeBookDownloaded, isDocDownloaded)
    }

    fun isUserRegistered(): Boolean? {
        return get(Constants.SharedPrefKeys.isUserRegistered)
    }

    fun setIsUserRegistered(isUserRegistered: Boolean) {
        set(Constants.SharedPrefKeys.isUserRegistered, isUserRegistered)
    }

    fun getCurrentUserDetails(): User {

        get(Constants.SharedPrefKeys.localUserDetails, "").let {
            Log.d("Get Json From Pref", it!!)

            if (it!!.isNotEmpty())
            {
                return Gson().fromJson(it!!, User::class.java)
            }
        }

        return User()
    }


    fun setCurrentUserDetails(user: User) {

        Log.d("Convert to Json", user.toString())

        set(Constants.SharedPrefKeys.localUserDetails, Gson().toJson(user))
    }

}