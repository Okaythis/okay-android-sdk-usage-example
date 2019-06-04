package com.itransition.okay.sdkdemo.retrofit

import com.itransition.okay.sdkdemo.repository.BankTransactionResponse
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

const val BANK_TRANSACTION = "/bank/transaction"

interface RequestsApi {

    @POST(BANK_TRANSACTION)
    fun startBankTransaction(@Body body: RequestBody): Single<Response<BankTransactionResponse>>

}