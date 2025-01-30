package com.hazrat.islam24.util

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import com.hazrat.islam24.R
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
        val localeManager = context.getSystemService(LocaleManager::class.java)
        localeManager?.applicationLocales = LocaleList.forLanguageTags(languageString)
    } else {
        val locale = Locale(languageString)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        // Use LocaleList for Android 7.0 and later
        config.setLocales(LocaleList(locale))

        // Apply the configuration change to the resources
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        val wrappedContext = LocaleHelper.wrap(context, Locale(languageString))
        // Ensure activity is recreated if needed
        if (wrappedContext is Activity) {
            wrappedContext.recreate()
        } else {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }

        // Set application-wide locale using AppCompatDelegate
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageString))
    }
}

enum class Languages {
    ENGLISH {
        override fun getString() = R.string.english
    },
    BENGALI {
        override fun getString() = R.string.bengali
    };

    @StringRes
    abstract fun getString(): Int
}