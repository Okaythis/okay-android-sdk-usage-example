package com.itransition.okay.sdkdemo.di

import com.itransition.okay.sdkdemo.DemoApplication
import com.itransition.okay.sdkdemo.ui.main.MainActivity
import com.itransition.okay.sdkdemo.repository.TenantRepository
import com.itransition.okay.sdkdemo.ui.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(app: DemoApplication)

    fun inject(mainActivity: MainActivity)

    fun inject(mainFragment: MainFragment)

    fun inject(tenantRepository: TenantRepository)

}