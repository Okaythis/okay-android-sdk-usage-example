package com.okay.android.sdkdemo.di

import com.okay.android.sdkdemo.retrofit.RequestsApi
import com.okay.android.sdkdemo.network.RetrofitWrapper
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideNetworkApi() : RequestsApi {
        return RetrofitWrapper.createRetrofit().create<RequestsApi>(RequestsApi::class.java)
    }
}