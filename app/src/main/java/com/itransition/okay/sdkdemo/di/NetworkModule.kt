package com.itransition.okay.sdkdemo.di

import com.itransition.protectoria.demo_linking_app.data.retrofit.RequestsApi
import com.itransition.protectoria.demo_linking_app.network.RetrofitWrapper
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideNetworkApi() : RequestsApi {
        return RetrofitWrapper.createRetrofit().create<RequestsApi>(RequestsApi::class.java)
    }
}