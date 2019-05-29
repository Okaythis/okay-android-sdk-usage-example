package com.itransition.protectoria.demo_linking_app.utils

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody

const val APPLICATION_TYPE_JSON = "application/json"
const val APPLICATION_TYPE_X_WWW = "application/x-www-form-urlencoded"

object NetworkUtils {
    fun createRequestBody(body: Any): RequestBody = RequestBody
            .create(MediaType.parse(APPLICATION_TYPE_JSON), Gson().toJson(body))

    fun createRequestBodyXWWW(body: Any): RequestBody = RequestBody
            .create(MediaType.parse(APPLICATION_TYPE_X_WWW), Gson().toJson(body))

}