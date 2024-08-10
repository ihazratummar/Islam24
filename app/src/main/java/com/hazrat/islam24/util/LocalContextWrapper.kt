package com.hazrat.islam24.util

import android.app.Activity
import android.app.Application
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.hazrat.islam24.main.mainActivity.MainActivity
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 */

fun changeLanguage(context: Context, language: Languages) {
    val languageString = when (language) {
        Languages.ENGLISH -> "en"
        Languages.BENGALI -> "bn"
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Use LocaleManager for Android 13 and later
        val localManager = context.getSystemService(LocaleManager::class.java)
        localManager?.applicationLocales = LocaleList.forLanguageTags(languageString)
    } else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageString))
    }
}

enum class Languages {
    ENGLISH,
    BENGALI
}

//fun updateLocale(context: Context, language: Languages): Context {
//    val languageString = when (language) {
//        Languages.ENGLISH -> "en"
//        Languages.BENGALI -> "bn"
//    }
//    val locale = Locale(languageString)
//    Locale.setDefault(locale)
//
//    val config = context.resources.configuration
//    config.setLocale(locale)
//    config.setLayoutDirection(locale)
//
//    return context.createConfigurationContext(config)
//}
//
//enum class Languages {
//    ENGLISH,
//    BENGALI
//}
