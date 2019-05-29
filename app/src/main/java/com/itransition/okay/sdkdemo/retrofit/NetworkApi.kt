package com.itransition.protectoria.demo_linking_app.data.retrofit

import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

const val FB_TENANT = "/tenant-fb"
const val API = "api"
const val LOGIN = "login"
const val LINKING = "link-via-app"

interface RequestsApi {

    @POST("$FB_TENANT/$API/$LINKING")
    fun linkTenant(@Body body: RequestBody): Single<Response<LinkingTenantResponse>>

    @POST("$FB_TENANT/$LOGIN")
    fun tenantLogin(@Body body: RequestBody): Single<Response<LoginTenantResponse>>
}