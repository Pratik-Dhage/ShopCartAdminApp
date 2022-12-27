package com.example.shopcartadminapp.helping_classes

import android.os.Build

object Constants {

    const val PREFS_APP_LANG = "app_language"

    var DEVICE_TOKEN = ""
    val DEVICE_MODEL = Build.MODEL
    const val DEVICE_TYPE = "Android"
    const val DEVICE_NAME = "ANDR"
    val OS_VERSION = Build.VERSION.RELEASE
    //const val APP_VERSION = BuildConfig.VERSION_NAME
}