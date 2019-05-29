package com.itransition.okay.sdkdemo.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.iid.FirebaseInstanceId
import com.itransition.okay.sdkdemo.*
import com.itransition.okay.sdkdemo.repository.PreferenceRepository
import com.itransition.protectoria.demo_linking_app.data.retrofit.TenantRepository
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

const val START_AUTH_ACTIVITY = "start_auth_activity"
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
        if (!PsaManager.getInstance().isEnrolled) {
            startEnroll()
        }
        btnStartEnroll.setOnClickListener {
            if (!PsaManager.getInstance().isEnrolled) {
                startEnroll()
            } else {
                Toast.makeText(this, getString(R.string.already_enrolled), LENGTH_SHORT).show()
            }
        }
        btnLinkTenant.setOnClickListener {
            if (PsaManager.getInstance().isEnrolled) {
                linkTenant()
            } else {
                Toast.makeText(this, getString(R.string.not_enrolled), LENGTH_SHORT).show()
            }
        }
        btnLoginTenant.setOnClickListener {
            if (PsaManager.getInstance().isEnrolled) {
                loginTenant()
            } else {
                Toast.makeText(this, getString(R.string.not_enrolled), LENGTH_SHORT).show()
            }
        }
        intent?.extras?.getLong(AUTH_DATA_SESSION_ID)?.run {
            startAuthorizationActivity(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PsaConstants.ACTIVITY_REQUEST_CODE_PSA_ENROLL
            && resultCode == RESULT_OK
        ) {
            data?.run {
               var resultData = PsaIntentUtils.enrollResultFromIntent(this)
                resultData.let {
                    preferenceRepository.saveExternalID(it.externalId)
                }
            }
            Toast.makeText(this, getString(R.string.enroll_success), LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.enroll_error), LENGTH_SHORT).show()
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
        var spaEnrollData: SpaEnrollData = SpaEnrollData(
            FirebaseInstanceId.getInstance().id,
            BuildConfig.PUB_PSS_B64,
            BuildConfig.INSTALLATION_ID,
            null,
            PsaType.OKAY
        )
        PsaManager.startEnrollmentActivity(this@MainActivity, spaEnrollData)
    }

    private fun startAuthorizationActivity(sessionID: Long) {

        val authorizationData = SpaAuthorizationData(
            sessionID,
            PreferenceRepository(this).getAppPns(),
            BaseSettings.DEFAULT_PAGE_THEME,
            PsaType.BASE
        )
        PsaManager.startAuthorizationActivity(this, authorizationData)
        //Waiting the response on onActivityResult()
    }

    private fun linkTenant() {
        tenantRepository.sendLinkingRequest()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                if (it.isSuccessful) {
                    Toast.makeText(this, it.body().toString(), Toast.LENGTH_LONG).show()
                }
            }, {Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()})
            .let { disposables.add(it) }
    }

    private fun loginTenant() {
        tenantRepository.sendLoginRequest(BuildConfig.TENANT_ID, BuildConfig.TENANT_PASSWORD)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(this, it.body().toString(), Toast.LENGTH_LONG).show()
            }, {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }).let { disposables.add(it) }
    }

}
