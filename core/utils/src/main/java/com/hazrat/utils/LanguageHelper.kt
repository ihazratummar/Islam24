package com.hazrat.utils

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/**
 * @author Hazrat Ummar Shaikh
 */

fun changeLanguage(context: Context, languageString: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageString))
}
