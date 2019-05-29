package com.itransition.okay.sdkdemo.notifications

enum class NotificationType(code: Int) {
    WAKE_UP(10),
    AUTH_RESULT(20),
    UNDEFINED(0);

    private val code: Int = 0

    fun getCode(): Int {
        return this.code
    }

    companion object {
        fun creator(code: Int): NotificationType {
            for (type in NotificationType.values()) {
                if (type.getCode() == code) {
                    return type
                }
            }
            return UNDEFINED
        }
    }

}