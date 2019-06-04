package com.itransition.okay.sdkdemo.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.itransition.okay.sdkdemo.*
import com.itransition.okay.sdkdemo.repository.PreferenceRepository
import com.itransition.okay.sdkdemo.repository.BankTransactionType
import com.itransition.okay.sdkdemo.repository.TenantRepository
import com.protectoria.psa.PsaManager
import com.protectoria.psa.api.PsaConstants
import com.protectoria.psa.api.converters.PsaIntentUtils
import com.protectoria.psa.api.entities.SpaAuthorizationData
import com.protectoria.psa.api.entities.SpaEnrollData
import com.protectoria.psa.dex.common.data.enums.PsaType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

const val AUTH_DATA_SESSION_ID = "auth_data_session_id"


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var tenantRepository: TenantRepository
    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DemoApplication.appComponent.inject(this)
        setContentView(R.layout.activity_main)
        checkPermissions()

        btnStartEnroll.setOnClickListener {
            if (!PsaManager.getInstance().isEnrolled) {
                startEnroll()
            } else {
                Toast.makeText(this, getString(R.string.already_enrolled), LENGTH_SHORT).show()
            }
        }
        btnECommerceTransaction.setOnClickListener {
            if (PsaManager.getInstance().isEnrolled) {
                startBankTransaction()
            } else {
                Toast.makeText(this, getString(R.string.not_enrolled), LENGTH_SHORT).show()
            }
        }
        // We check if this activity started by MessagingService with data for Authorization
        intent?.getLongExtra(AUTH_DATA_SESSION_ID, 0)?.run {
            if (this > 0) startAuthorizationActivity(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
                        preferenceRepository.saveExternalID(it.externalId)
                    }
                }
                Toast.makeText(this, getString(R.string.enroll_success), LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.enroll_error), LENGTH_SHORT).show()
            }
        }
        if (requestCode == PsaConstants.ACTIVITY_REQUEST_CODE_PSA_AUTHORIZATION) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, getString(R.string.auth_success), LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.auth_error), LENGTH_SHORT).show()
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
            preferenceRepository.getAppPns(),
            BuildConfig.PUB_PSS_B64,
            BuildConfig.INSTALLATION_ID,
            null,
            PsaType.OKAY
        )
        PsaManager.startEnrollmentActivity(this@MainActivity, spaEnrollData)
    }

    // This is only for demo the user flow. In yor application - it's yor responsibility how to perform transactions
    private fun startBankTransaction() {
        tenantRepository.sendBankTransactionRequest(128, "12345678", BankTransactionType.PAYMENT_CARD, "AlphaOmega")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(this, it.body().toString(), Toast.LENGTH_LONG).show()
            }, {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }).let { disposables.add(it) }
    }

    private fun startAuthorizationActivity(sessionID: Long) {

        val authorizationData = SpaAuthorizationData(
            sessionID,
            PreferenceRepository(this).getAppPns(),
            BaseTheme(this).DEFAULT_PAGE_THEME,
            PsaType.OKAY
        )
        PsaManager.startAuthorizationActivity(this, authorizationData)
        //Waiting the response on onActivityResult()
    }

}
