package com.hazrat.islam24.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 * Created on 18-12-2024
 */

class LocaleContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, languageCode: String): ContextWrapper {
            var newContext = context
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            newContext = newContext.createConfigurationContext(config)
            return LocaleContextWrapper(newContext)
        }
    }
}