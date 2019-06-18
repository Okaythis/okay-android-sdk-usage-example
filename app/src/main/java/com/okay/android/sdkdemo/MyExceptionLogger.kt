package com.okay.android.sdkdemo

import android.util.Log
import com.protectoria.psa.dex.common.utils.logger.ExceptionLogger
import java.lang.Exception

const val TAG = "OKAY_SDK"
class MyExceptionLogger : ExceptionLogger {

    override fun exception(p0: String?, p1: Exception?) {
        Log.e(TAG, p0 + " " + p1?.message)
    }

    override fun setUserIdentificator(p0: String?) {
    }
}