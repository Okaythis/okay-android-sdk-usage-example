package com.okay.android.sdkdemo

import android.app.Application
import com.okay.android.sdkdemo.network.RetrofitWrapper
import com.okay.android.sdkdemo.di.ApplicationComponent
import com.okay.android.sdkdemo.di.AppModule
import com.okay.android.sdkdemo.di.DaggerApplicationComponent
import com.okay.android.sdkdemo.repository.PreferenceRepository
import com.okay.android.sdkdemo.utils.SystemUtils
import com.okay.android.sdkdemo.retrofit.RequestsApi
import com.protectoria.psa.PsaManager
import dagger.android.DaggerApplication_MembersInjector
import dagger.android.support.DaggerApplication

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
        psaManager.setPssAddress(BuildConfig.SERVER_URL)
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