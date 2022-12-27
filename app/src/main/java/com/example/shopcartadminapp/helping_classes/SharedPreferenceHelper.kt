package com.example.shopcartadminapp.helping_classes

import android.content.Context
import android.content.SharedPreferences

// for saving Login Credentials purpose
object SharedPreferenceHelper {

    private const val SHARED_PREFS_NAME = "ShopCartApp"
    private var sharedPreferences: SharedPreferences? = null

    fun writeString(context: Context, key: String, value: String): Boolean {
        if (context == null) {
            return false }
            getSharedPreferencesEditor(context)?.putString(key, value)?.apply()
            return true
    }

    fun getString(context: Context, key: String, defaultValue: String): String? {
        return if (context == null) {
            defaultValue
        } else getSharedPreferences(context)?.getString(key, defaultValue)
    }

    private fun getSharedPreferencesEditor(context: Context): SharedPreferences.Editor? {

        return if (context == null) {
            null
        } else getSharedPreferences(context)?.edit()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences? {

        if (context == null) {
            return null
        }
        if (sharedPreferences == null) {

            run { sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE) }

        }
        return sharedPreferences
    }
}