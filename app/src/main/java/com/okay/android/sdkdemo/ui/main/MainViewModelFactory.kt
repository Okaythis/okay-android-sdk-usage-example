package com.okay.android.sdkdemo.ui.main

import com.okay.android.sdkdemo.repository.TenantRepository
import com.okay.android.sdkdemo.ui.base.BaseViewModelFactory

class MainViewModelFactory(val tenantRepository: TenantRepository) :
    BaseViewModelFactory<MainViewModel>(MainViewModel::class.java) {

    override fun createViewModel() = MainViewModel(tenantRepository)
}