package com.okay.android.sdkdemo.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.okay.android.sdkdemo.*
import com.okay.android.sdkdemo.repository.PreferenceRepository
import com.okay.android.sdkdemo.ui.BaseTheme
import com.protectoria.psa.PsaManager
import com.protectoria.psa.api.PsaConstants
import com.protectoria.psa.api.PsaFields
import com.protectoria.psa.api.converters.PsaIntentUtils
import com.protectoria.psa.api.entities.PsaErrorData
import com.protectoria.psa.api.entities.SpaAuthorizationData
import com.protectoria.psa.api.entities.SpaEnrollData
import com.protectoria.psa.dex.common.data.enums.PsaType
import javax.inject.Inject

const val AUTH_DATA_SESSION_ID = "auth_data_session_id"

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DemoApplication.appComponent.inject(this)
        setContentView(R.layout.activity_single_container)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        // You should check if app has all permissions required by PsaManager
        checkPermissions()
        // We check if this activity started by MessagingService with data for Authorization. And start authorization flow
        intent?.getLongExtra(AUTH_DATA_SESSION_ID, 0)?.run {
            if (this > 0) startAuthorizationActivity(this)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //If user granted all required permissions - you can start Enroll
        if (!PsaManager.getInstance().isEnrolled) {
            startEnroll()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PsaConstants.ACTIVITY_REQUEST_CODE_PSA_ENROLL) {
            if (resultCode == RESULT_OK) {
                //We should save data from Enrollment result, fo future usage
                data?.run {
                    val resultData = PsaIntentUtils.enrollResultFromIntent(this)
                    resultData.let {
                        preferenceRepository.putExternalId(it.externalId)
                    }
                }
                Toast.makeText(this, getString(R.string.enroll_success), Toast.LENGTH_SHORT).show()
            } else {
                val errorData = data?.getParcelableExtra<PsaErrorData>(PsaFields.PSA_FAILED_DATA)
                Toast.makeText(this, getString(R.string.enroll_error) + errorData?.message, Toast.LENGTH_SHORT).show()
            }
        }
        // Here you can receive result of the authorization flow
        if (requestCode == PsaConstants.ACTIVITY_REQUEST_CODE_PSA_AUTHORIZATION) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, getString(R.string.auth_success), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.auth_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions() {
        val requiredPermissions = PsaManager.getRequiredPermissions()
        val permissionManager = PermissionManager(this)
        if (!permissionManager.hasPermissions(this, requiredPermissions)) {
            permissionManager.requestPermissions(requiredPermissions)
        }
    }

    private fun startEnroll() {
        val spaEnrollData = SpaEnrollData(
            preferenceRepository.appPNS,
            BuildConfig.PUB_PSS_B64,
            BuildConfig.INSTALLATION_ID,
            BaseTheme(this).DEFAULT_PAGE_THEME,
            PsaType.OKAY
        )
        PsaManager.startEnrollmentActivity(this@MainActivity, spaEnrollData)
    }

    private fun startAuthorizationActivity(sessionID: Long) {
        val authorizationData = SpaAuthorizationData(
            sessionID,
            PreferenceRepository(this).appPNS,
            BaseTheme(this).DEFAULT_PAGE_THEME,
            PsaType.OKAY
        )
        PsaManager.startAuthorizationActivity(this, authorizationData)
        //Waite the response in onActivityResult()
    }

}
