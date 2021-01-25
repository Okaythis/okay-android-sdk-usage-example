package com.okay.android.sdkdemo

import android.app.Application
import com.itransition.protectoria.psa_multitenant.restapi.GatewayRestServer
import com.okay.android.sdkdemo.di.AppModule
import com.okay.android.sdkdemo.di.ApplicationComponent
import com.okay.android.sdkdemo.di.DaggerApplicationComponent
import com.okay.android.sdkdemo.network.RetrofitWrapper
import com.okay.android.sdkdemo.repository.PreferenceRepository
import com.okay.android.sdkdemo.retrofit.RequestsApi
import com.okay.android.sdkdemo.utils.SystemUtils
import com.protectoria.psa.PsaManager
import com.protectoria.psa.dex.common.data.json.PsaGsonFactory
import com.protectoria.psa.dex.ui.texts.TransactionResourceProvider

class DemoApplication : Application() {

    lateinit var requestsApi: RequestsApi

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        initUUID()
        createRetrofit()
        initPsa()
        initGatewayServer()
    }

    private fun initAppComponent() {
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    private fun createRetrofit() {
        requestsApi = RetrofitWrapper.createRetrofit().create<RequestsApi>(RequestsApi::class.java)
    }

    private fun initPsa() {
        val psaManager = PsaManager.init(this, MyExceptionLogger(), TransactionResourceProvider(this))
        psaManager.setPssAddress(BuildConfig.SERVER_URL)
        psaManager.setScreenshotEnabled(true)
    }

    private fun initUUID() {
        PreferenceRepository(this).apply {
            if (getUUID() == null) {
                saveUUID(SystemUtils.generateUUID())
            }
        }
    }

    private fun initGatewayServer() {
        GatewayRestServer.init(PsaGsonFactory().create(), BuildConfig.SERVER_URL + "/gateway/")
    }

    companion object {
        lateinit var appComponent: ApplicationComponent
    }


}