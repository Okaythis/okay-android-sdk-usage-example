package com.itransition.okay.sdkdemo.ui.main

import androidx.lifecycle.MutableLiveData
import com.itransition.okay.sdkdemo.repository.BankTransactionType
import com.itransition.okay.sdkdemo.repository.TenantRepository
import com.itransition.okay.sdkdemo.ui.base.BaseViewModel
import com.protectoria.psa.PsaManager
import io.reactivex.android.schedulers.AndroidSchedulers

class MainViewModel(private val tenantRepository: TenantRepository) : BaseViewModel() {

    val isEnrolled = MutableLiveData<Boolean>().apply {
        value = PsaManager.getInstance().isEnrolled
    }

    val startEnroll: MutableLiveData<Unit> = MutableLiveData()

    // This is only for demo the user flow. In yor application - it's yor responsibility how to perform transactions
    fun startPaymentCardTransaction() {
        tenantRepository.sendBankTransactionRequest(128, "12345678", BankTransactionType.PAYMENT_CARD, "AlphaOmega")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = it.body()?.status?.message
            }, {
                message.value = it.message.toString()
            }).let { disposables.add(it) }
    }

    // This is only for demo the user flow. In yor application - it's yor responsibility how to perform transactions
    fun startRemittanceTransaction() {
        tenantRepository.sendBankTransactionRequest(200, "12345678", BankTransactionType.REMITTANCE, "AlphaOmega")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = it.body()?.status?.message
            }, {
                message.value = it.message.toString()
            }).let { disposables.add(it) }
    }

    // This is only for demo the user flow. In yor application - it's yor responsibility how to perform transactions
    fun startECommerceTransaction() {
        tenantRepository.sendBankTransactionRequest(500, "12345678", BankTransactionType.E_COMMERCE, "AlphaOmega")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = it.body()?.status?.message
            }, {
                message.value = it.message.toString()
            }).let { disposables.add(it) }
    }
}
