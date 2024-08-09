package com.hazrat.islam24.util

import android.content.Context
import android.media.MediaFormat.KEY_LANGUAGE
import androidx.appcompat.app.AppCompatDelegate

object DataStorePreference {


    private const val PREF_NAME = "app_setting"
    private const val KEY_THEME_MODE = "theme_mode"



    fun setLanguage(context: Context, language: String) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getLanguage(context: Context): String{
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun setThemeMode(context: Context, themeMode: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(KEY_THEME_MODE, themeMode).apply()
    }

    fun getThemeMode(context: Context): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_THEME_MODE, false)
    }

}