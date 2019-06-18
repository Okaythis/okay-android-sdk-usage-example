package com.okay.android.sdkdemo.di

import android.content.Context
import com.okay.android.sdkdemo.DemoApplication
import com.okay.android.sdkdemo.repository.TenantRepository
import com.okay.android.sdkdemo.ui.main.MainViewModelFactory
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

    @Provides
    fun provideMainViewModelFactory(repository: TenantRepository) = MainViewModelFactory(repository)

}
