package com.itransition.protectoria.demo_linking_app.data.retrofit

import com.google.gson.annotations.SerializedName
import com.itransition.okay.sdkdemo.BuildConfig
import com.itransition.okay.sdkdemo.DemoApplication
import com.itransition.protectoria.demo_linking_app.utils.NetworkUtils
import com.protectoria.psa.PsaManager
import com.protectoria.psa.api.entities.SpaAuthorizationData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

const val ON: String = "on"

class TenantRepository {

    init {
        DemoApplication.appComponent.inject(this)
    }

    @Inject
    lateinit var  apiInterface: RequestsApi

//    @Inject
//    lateinit var  preferenceRepository = PreferenceRepository(context)

//    fun sendLinkingRequest(): Observable<Response<LinkingTenantResponse>> {
//        var request = ServerLinkUserRequest()
//        request.userExternalId = preferenceRepository.getUUID()
//        request.tenantId = BuildConfig.TENANT_ID_NUMBER
//        var signature =   HashingSignatureService(Sha256HashingFunction()).generate(request, BuildConfig.TENANT_SECRET)
//        return LinkingTenantRequest().run{
//            userID = preferenceRepository.getUUID()
//            apiInterface.linkTenant(NetworkUtils.createRequestBody(this))
//                .subscribeOn(Schedulers.io())
//                .toObservable()
//        }
//    }

    fun sendLinkingRequest(): Observable<Response<LinkingTenantResponse>> {
        return apiInterface.linkTenant(NetworkUtils.createRequestBody(LinkingTenantRequest()))
            .subscribeOn(Schedulers.io())
            .toObservable()
    }

    fun sendLoginRequest(login: String, password: String): Observable<Response<LoginTenantResponse>> {
        return apiInterface.tenantLogin(
            NetworkUtils.createRequestBodyXWWW(LoginTenantRequest(
                FormData(login, password, authOk = ON)
            ))
        ).subscribeOn(Schedulers.io())
            .toObservable()
    }

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
