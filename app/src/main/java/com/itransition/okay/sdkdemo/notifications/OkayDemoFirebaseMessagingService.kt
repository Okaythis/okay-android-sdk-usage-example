package com.itransition.okay.sdkdemo.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.itransition.okay.sdkdemo.repository.PreferenceRepository
import com.itransition.okay.sdkdemo.ui.AUTH_DATA_SESSION_ID
import com.itransition.okay.sdkdemo.ui.MainActivity


class OkayDemoFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "Firebase"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage!!.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)


            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                // scheduleJob()
            } else {
                // Handle message within 10 seconds
                // handleNow()
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }

    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token");
        token?.run {
            PreferenceRepository(this@OkayDemoFirebaseMessagingService).saveAppPNS(token)
        }
    }

    private fun startMainActivityForAuth(bundle: Bundle) {
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun parseNotification(remoteMessage: RemoteMessage) {
        var remoteNotification = RemoteNotification(
            NotificationType.creator(remoteMessage.data["type"]!!.toInt()),
            remoteMessage.data["data"]!!
        )
        var wakeUpNotification = WakeUpNotificationParser(Gson()).extractSpsNotification(remoteNotification)
        wakeUpNotification?.let {
            startMainActivityForAuth(Bundle().apply {
                putLong(AUTH_DATA_SESSION_ID, it.sessionId)
            })
        }
    }


}
