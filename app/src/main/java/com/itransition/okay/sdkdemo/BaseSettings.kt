package com.itransition.okay.sdkdemo

import android.graphics.Color
import com.protectoria.psa.dex.common.ui.PageTheme

/**
 * @author i.statkevich on 8/8/17.
 */

object BaseSettings {
    val PSA_VERSION = 1
    val APP_TYPE = 101

    val DEFAULT_PAGE_THEME: PageTheme

    init {
        DEFAULT_PAGE_THEME = PageTheme()
        DEFAULT_PAGE_THEME.actionBarBackgroundColor = Color.parseColor("#6DAF30")
        DEFAULT_PAGE_THEME.actionBarTextColor = Color.parseColor("#E7FCE7")
        DEFAULT_PAGE_THEME.buttonTextColor = Color.parseColor("#E7FCE7")
        DEFAULT_PAGE_THEME.buttonBackgroundColor = Color.parseColor("#6DAF30")
        DEFAULT_PAGE_THEME.screenBackgroundColor = Color.parseColor("#FFFFFF")
        DEFAULT_PAGE_THEME.nameTextColor = Color.parseColor("#960F0F")
        DEFAULT_PAGE_THEME.titleTextColor = Color.parseColor("#2C0B73")
        DEFAULT_PAGE_THEME.messageTextColor = Color.parseColor("#154D05")
    }
}
