package com.itransition.okay.sdkdemo.notifications

import com.google.gson.Gson
import com.itransition.okay.sdkdemo.BuildConfig.DEBUG

abstract class BaseNotificationParser<N>(private val gson: Gson) :
    NotificationParser<N> {

    override fun extractSpsNotification(notification: RemoteNotification): N? {
        return try {
            gson.fromJson(notification.data, getNotificationType())
        } catch (e: Exception) {
            if (DEBUG) {
                e.printStackTrace()
            }
            null
        }
    }

    protected abstract fun getNotificationType(): Class<N>
}

interface NotificationParser<N> {
    fun extractSpsNotification(notification: RemoteNotification): N?
}