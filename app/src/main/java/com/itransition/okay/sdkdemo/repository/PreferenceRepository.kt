package com.itransition.okay.sdkdemo.repository

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE)

    fun saveAppPNS(appPns: String) {
        sharedPreferences.edit().putString(APP_PNS, appPns).apply()
    }

    fun getAppPns(): String? = sharedPreferences.getString(APP_PNS, null)

    fun saveUUID(uuid: String) {
        sharedPreferences.edit().putString(UUID, uuid).apply()
    }

    fun getUUID(): String? = sharedPreferences.getString(UUID, null)

    fun saveExternalID(externalID: String) {
        sharedPreferences.edit().putString(EXTERNAL_ID, externalID).apply()
    }

    fun getExternalID() {
        sharedPreferences.getString(EXTERNAL_ID, null)
    }
    companion object {
        private const val APP_PNS = "app_pns"
        private const val UUID = "uuid"
        private const val EXTERNAL_ID = "external_id"
        private const val USER_PREFERENCE = "user_preference"
    }
}