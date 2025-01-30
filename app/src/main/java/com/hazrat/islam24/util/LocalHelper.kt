package com.hazrat.islam24.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.Locale

object LocaleHelper {

    @SuppressLint("ObsoleteSdkInt")
    fun wrap(context: Context, locale: Locale): ContextWrapper {
        var updatedContext = context
        val config = context.resources.configuration

        // For Android N (Nougat) and later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
        } else {
            // For devices below Android N
            config.setLocale(locale)
        }

        // Set layout direction for RTL (Right-to-Left) languages like Arabic
        config.setLayoutDirection(locale)

        // Create a new context with the updated configuration
        updatedContext = context.createConfigurationContext(config)
        return ContextWrapper(updatedContext)
    }
}
