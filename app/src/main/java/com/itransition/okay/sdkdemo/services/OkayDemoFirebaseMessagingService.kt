package com.itransition.okay.sdkdemo.services

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.itransition.okay.sdkdemo.BuildConfig
import com.itransition.okay.sdkdemo.fcm.*
import com.itransition.okay.sdkdemo.repository.PreferenceRepository
import com.itransition.okay.sdkdemo.ui.main.AUTH_DATA_SESSION_ID
import com.itransition.okay.sdkdemo.ui.main.MainActivity

const val TAG = "Firebase"

class OkayDemoFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "From: " + remoteMessage!!.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            parseNotification(remoteMessage)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }

    }

    override fun onNewToken(token: String?) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Refreshed token: $token")
        token?.run {
            PreferenceRepository(this@OkayDemoFirebaseMessagingService).saveAppPNS(token)
        }
    }

    private fun startMainActivityForAuth(bundle: Bundle) {
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtras(bundle)
        })
    }

    private fun parseNotification(remoteMessage: RemoteMessage) {
        val remoteNotification = RemoteNotification(
            NotificationType.creator(remoteMessage.data["type"]!!.toInt()),
            remoteMessage.data["data"]!!
        )
        when (remoteNotification.type) {
            NotificationType.WAKE_UP -> {
                val wakeUpNotification = WakeUpNotificationParser(Gson()).extractSpsNotification(remoteNotification)
                wakeUpNotification?.let {
                    startMainActivityForAuth(Bundle().apply {
                        putLong(AUTH_DATA_SESSION_ID, it.sessionId)
                    })
                }
            }
            NotificationType.AUTH_RESULT -> {
                val authResult = TransactionResultNotificationParser(Gson()).extractSpsNotification(remoteNotification)
                //TODO: show result to user
            }
            NotificationType.UNDEFINED -> {
                val notification = GenericNotificationParser(Gson()).extractSpsNotification(remoteNotification)
            }
        }

    }

}
