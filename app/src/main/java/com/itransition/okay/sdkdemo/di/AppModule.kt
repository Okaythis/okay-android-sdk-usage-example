package com.itransition.okay.sdkdemo.di

import android.content.Context
import com.itransition.okay.sdkdemo.DemoApplication
import com.itransition.protectoria.demo_linking_app.data.retrofit.TenantRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: DemoApplication) {

    @Provides
    @Singleton
    fun provideApplication(): DemoApplication = application

    @Provides
    @Singleton
    fun provideApplicationContext(app: DemoApplication): Context = app

    @Provides
    fun provideTenantRepository(): TenantRepository = TenantRepository()
}
