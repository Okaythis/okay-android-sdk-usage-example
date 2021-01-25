package com.okay.android.sdkdemo.utils

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

const val APPLICATION_TYPE_JSON = "application/json"

object NetworkUtils {
    fun createRequestBody(body: Any): RequestBody = RequestBody
        .create(APPLICATION_TYPE_JSON.toMediaTypeOrNull(), Gson().toJson(body))

}