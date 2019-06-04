package com.itransition.okay.sdkdemo

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


const val MY_PERMISSIONS_REQUEST_FOR_PSA = 1
class PermissionManager(private val activity: Activity) {

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, MY_PERMISSIONS_REQUEST_FOR_PSA);    }
}