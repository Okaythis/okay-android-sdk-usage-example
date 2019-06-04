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
    val responseStatusCode: Int
)

enum class BankTransactionType constructor(val code: Int) {
    E_COMMERCE(10),
    REMITTANCE(20),
    PAYMENT_CARD(30)
}

data class LinkingTenantRequest(
    @SerializedName("username")
    val tenantID: String = BuildConfig.TENANT_ID,
    val password: String = BuildConfig.TENANT_PASSWORD,
    @SerializedName("userExternalId")
    var userID: String? = "",
    @SerializedName("signature")
    var signature: String = ""
)

data class LinkingTenantResponse(val linkingCode: String)

data class LoginTenantRequest(
    val formData: FormData
)

data class FormData(
    @SerializedName("username")
    val tenantID: String = BuildConfig.TENANT_ID,
    val password: String = BuildConfig.TENANT_PASSWORD,
    @SerializedName("auth-ok")
    val authOk: String? = null,
    @SerializedName("auth-pin")
    val authPin: String? = null
)


data class LoginTenantResponse(val linkingCode: String)
