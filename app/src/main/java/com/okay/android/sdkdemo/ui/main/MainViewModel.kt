package com.okay.android.sdkdemo.ui.main

import androidx.lifecycle.MutableLiveData
import com.okay.android.sdkdemo.repository.BankTransactionType
import com.okay.android.sdkdemo.repository.TenantRepository
import com.okay.android.sdkdemo.ui.base.BaseViewModel
import com.protectoria.psa.PsaManager
import io.reactivex.android.schedulers.AndroidSchedulers

class MainViewModel(private val tenantRepository: TenantRepository) : BaseViewModel() {

    val isEnrolled = MutableLiveData<Boolean>().apply {
        value = PsaManager.getInstance().isEnrolled
    }

    val startEnroll: MutableLiveData<Unit> = MutableLiveData()

    val startLinkTenant: MutableLiveData<String> = MutableLiveData()

    private val resetEnroll: MutableLiveData<Unit> = MutableLiveData()

    private var linkingCode: String = ""

    fun loadStates() {
        isEnrolled.postValue(PsaManager.getInstance().isEnrolled)
    }

    // This is only to demonstrate possible user scenario. In yor application - it's yor responsibility how to perform transactions
    fun startPaymentCardTransaction() {
        tenantRepository.sendBankTransactionRequest(
            128,
            null,
            BankTransactionType.PAYMENT_CARD,
            "AlphaOmega"
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = it.body()?.status?.message
            }, {
                message.value = it.message.toString()
            }).let { disposables.add(it) }
    }

    // This is only to demonstrate possible user scenario. In yor application - it's yor responsibility how to perform transactions
    fun startRemittanceTransaction() {
        tenantRepository.sendBankTransactionRequest(
            200,
            "12345678",
            BankTransactionType.REMITTANCE,
            "AlphaOmega"
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = it.body()?.status?.message
            }, {
                message.value = it.message.toString()
            }).let { disposables.add(it) }
    }

    // This is only to demonstrate possible user scenario. In yor application - it's yor responsibility how to perform transactions
    fun startECommerceTransaction() {
        tenantRepository.sendBankTransactionRequest(
            499,
            "12345678",
            BankTransactionType.E_COMMERCE,
            "AlphaECommerce"
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                message.value = it.body()?.status?.message
            }, {
                message.value = it.message.toString()
            }).let { disposables.add(it) }
    }

    fun getResetEnroll() = resetEnroll

    fun startEnroll() {
        startEnroll.value = Unit
    }

    fun resetEnroll() {
        resetEnroll.value = Unit
        loadStates()
    }

    fun setLinkingCode(code: String) {
        linkingCode = code
    }

    fun linkTenant() {
        startLinkTenant.value = linkingCode
    }
}
