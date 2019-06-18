package com.okay.android.sdkdemo.fcm

enum class SpsResponseStatusCode(val code: Int) {
    SUCCESS(0),
    INCOMPLETE(-1),
    ERROR_UNKNOWN(101),
    ERROR_TIMEOUT(102),
    ERROR_CRYPTO(103),
    ERROR_CLIENT(104),
    ERROR_CODE_BLOCK(105),
    ERROR_INSTALLATION(106),
    ERROR_SESSION(107),
    ERROR_TTS(108),
    ERROR_WATERMARK(109),
    ERROR_PNS(110),
    ERROR_VOIP(111),
    ERROR_CALL_NO_ANSWER(112);

    fun fromCode(code: Int): SpsResponseStatusCode? {
        val var1 = values()
        val var2 = var1.size

        for (var3 in 0 until var2) {
            val status = var1[var3]
            if (status.code == code) {
                return status
            }
        }
        return null
    }
}