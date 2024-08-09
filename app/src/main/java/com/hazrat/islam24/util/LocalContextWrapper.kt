package com.hazrat.islam24.util

import android.app.LocaleManager
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

fun changeLanguage(language: Languages, context: Context) {
    val languageString = when (language) {
        Languages.ENGLISH -> "en"
        Languages.BENGALI -> "bn"
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Use LocaleManager for Android 12 and later
        context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(languageString)
    } else {
        // For earlier versions, update the app's configuration manually
        val locale = Locale(languageString)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}

enum class Languages {
    ENGLISH,
    BENGALI
}