package com.okay.android.sdkdemo.repository

import android.content.Context
import android.content.SharedPreferences
import com.itransition.protectoria.psa_multitenant.data.SpaStorage
import javax.inject.Inject

class PreferenceRepository @Inject constructor(context: Context) : SpaStorage {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE)

    override fun putAppPNS(appPns: String) {
        sharedPreferences.edit().putString(APP_PNS, appPns).apply()
    }

    override fun getAppPNS(): String? = sharedPreferences.getString(APP_PNS, null)

    fun saveUUID(uuid: String) {
        sharedPreferences.edit().putString(UUID, uuid).apply()
    }

    fun getUUID(): String? = sharedPreferences.getString(UUID, null)

    override fun putExternalId(externalID: String) {
        sharedPreferences.edit().putString(EXTERNAL_ID, externalID).apply()
    }

    override fun getExternalId() : String? {
        return sharedPreferences.getString(EXTERNAL_ID, null)
    }

    override fun getPubPssBase64(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putPubPssBase64(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getEnrollmentId(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putEnrollmentId(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun putInstallationId(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun getInstallationId(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        private const val APP_PNS = "app_pns"
        private const val UUID = "uuid"
        private const val EXTERNAL_ID = "external_id"
        private const val USER_PREFERENCE = "user_preference"
    }
}