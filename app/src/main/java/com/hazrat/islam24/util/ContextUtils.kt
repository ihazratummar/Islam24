package com.hazrat.islam24.util

/**
 * @author Hazrat Ummar Shaikh
 */


import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import java.util.Locale
import javax.inject.Inject

class ContextUtils @Inject constructor(base: Context) : ContextWrapper(base) {

    companion object {
        fun wrap(context: Context, languageCode: Languages): ContextWrapper {
            val languageString = when (languageCode) {
                Languages.ENGLISH -> "en"
                Languages.BENGALI -> "bn"
            }
            var newContext = context
            val locale = Locale(languageString)
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            newContext = newContext.createConfigurationContext(config)
            return ContextUtils(newContext)
        }
    }
}
