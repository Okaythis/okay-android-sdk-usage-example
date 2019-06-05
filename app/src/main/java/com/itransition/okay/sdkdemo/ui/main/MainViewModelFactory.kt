package com.itransition.okay.sdkdemo.ui.main

import com.itransition.okay.sdkdemo.repository.TenantRepository
import com.itransition.okay.sdkdemo.ui.base.BaseViewModelFactory

class MainViewModelFactory(val tenantRepository: TenantRepository) :
    BaseViewModelFactory<MainViewModel>(MainViewModel::class.java) {

    override fun createViewModel() = MainViewModel(tenantRepository)
}