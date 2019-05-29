package com.itransition.okay.sdkdemo

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat


const val MY_PERMISSIONS_REQUEST_FOR_PSA = 1
class PermissionManager(private val activity: Activity) {

//    fun checkPermission(manifestPermissionID: String) {
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(
//                context,
//                manifestPermissionID)
//            != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted we can request the permission.
//                ActivityCompat.requestPermissions(
//                    context,
//                    arrayOf(manifestPermissionID),
//                    MY_PERMISSIONS_REQUEST_FOR_PSA)
//        } else {
//            // Permission has already been granted
//        }
//    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(activity, permissions, MY_PERMISSIONS_REQUEST_FOR_PSA);    }
}