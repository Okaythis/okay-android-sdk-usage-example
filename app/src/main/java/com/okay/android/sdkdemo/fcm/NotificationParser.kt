package com.okay.android.sdkdemo.fcm

import com.google.gson.Gson
import com.okay.android.sdkdemo.BuildConfig.DEBUG
import com.protectoria.pss.dto.notification.WakeUpNotification

interface NotificationParser<N> {
    fun extractSpsNotification(notification: RemoteNotification): N?
}

abstract class BaseNotificationParser<N>(private val gson: Gson) :
    NotificationParser<N> {

    override fun extractSpsNotification(notification: RemoteNotification): N? {
        return try {
            notification.type
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


class WakeUpNotificationParser(gson: Gson) : BaseNotificationParser<WakeUpNotification>(gson) {

    override fun getNotificationType(): Class<WakeUpNotification> {
        return WakeUpNotification::class.java
    }
}

class TransactionResultNotificationParser(gson: Gson) : BaseNotificationParser<SpsResponseStatus>(gson) {
    override fun getNotificationType(): Class<SpsResponseStatus> {
        return SpsResponseStatus::class.java
    }
}

class GenericNotificationParser(gson: Gson) : BaseNotificationParser<GenericResponse>(gson) {
    override fun getNotificationType(): Class<GenericResponse> {
        return GenericResponse::class.java
    }
}

data class SpsResponseStatus(var message: String, var code: SpsResponseStatusCode)

data class GenericResponse(var code: String, var message: String)