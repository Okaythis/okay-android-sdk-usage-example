package com.itransition.okay.sdkdemo.repository

import com.google.gson.annotations.SerializedName
import com.itransition.okay.sdkdemo.BuildConfig
import com.itransition.okay.sdkdemo.DemoApplication
import com.itransition.okay.sdkdemo.retrofit.RequestsApi
import com.itransition.okay.sdkdemo.utils.NetworkUtils
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class TenantRepository {

    init {
        DemoApplication.appComponent.inject(this)
    }

    @Inject
    lateinit var apiInterface: RequestsApi

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    /**
     * @param msisdn telephone number for TAN authorization
     * @param type E_COMMERCE = 10, REMITTANCE = 20, PAYMENT_CARD = 30
     */
    fun sendBankTransactionRequest(
        amount: Int, msisdn: String, type: BankTransactionType, recipient: String?
    ): Observable<Response<BankTransactionResponse>> {
        return apiInterface.startBankTransaction(
            NetworkUtils.createRequestBody(
                BankTransactionRequest(
                    amount,
                    preferenceRepository.getExternalID()!!,
                    preferenceRepository.getAppPns()!!,
                    msisdn,
                    type,
                    recipient
                )
            )
        ).subscribeOn(Schedulers.io()).toObservable()
    }
}

/**
 * @param msisdn telephone number for TAN authorization
 * @param type E_COMMERCE = 10, REMITTANCE = 20, PAYMENT_CARD = 30
 */
data class BankTransactionRequest(
    var amount: Int,
    val externalId: String,
    var appPNS: String,
    var msisdn: String,
    var type: BankTransactionType = BankTransactionType.E_COMMERCE,
    var recipient: String?
)

data class BankTransactionResponse(
    val status: ResponseStatus
)

data class ResponseStatus(
    val message: String,
    @SerializedName("code")
    val responseStatusCode: String
)

enum class BankTransactionType constructor(val code: Int) {
    E_COMMERCE(10),
    REMITTANCE(20),
    PAYMENT_CARD(30)
}
