package com.okay.android.sdkdemo.utils

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody

const val APPLICATION_TYPE_JSON = "application/json"

object NetworkUtils {
    fun createRequestBody(body: Any): RequestBody = RequestBody
        .create(MediaType.parse(APPLICATION_TYPE_JSON), Gson().toJson(body))

}