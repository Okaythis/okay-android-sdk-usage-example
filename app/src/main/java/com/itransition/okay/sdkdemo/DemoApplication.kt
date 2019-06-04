package com.itransition.okay.sdkdemo

import android.app.Application
import com.itransition.okay.sdkdemo.di.ApplicationComponent
import com.itransition.okay.sdkdemo.di.AppModule
import com.itransition.okay.sdkdemo.di.DaggerApplicationComponent
import com.itransition.okay.sdkdemo.repository.PreferenceRepository
import com.itransition.okay.sdkdemo.utils.SystemUtils
import com.itransition.okay.sdkdemo.retrofit.RequestsApi
import com.itransition.protectoria.demo_linking_app.network.RetrofitWrapper
import com.protectoria.psa.PsaManager

class DemoApplication : Application() {

    lateinit var requestsApi: RequestsApi

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        initUUID()
        createRetrofit()
        initPsa()
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
        val psaManager = PsaManager.init(this, MyExceptionLogger())
        psaManager.setPssAddress("http://protdemo.demohoster.com")
    }

    private fun initUUID() {
        PreferenceRepository(this).apply {
            if (getUUID() == null) {
                saveUUID(SystemUtils.generateUUID())
            }
        }
    }
    companion object {
        lateinit var appComponent: ApplicationComponent
    }



}