package com.hazrat.islam24.util

import android.content.Context
import android.media.MediaFormat.KEY_LANGUAGE
import com.hazrat.islam24.auth.presentation.appSetting.Themes
import com.hazrat.islam24.util.Constants.KEY_SORT_BY
import com.hazrat.islam24.core.presentation.zakat.DateType
import javax.inject.Inject

class DataStorePreference @Inject constructor(
    private val context: Context
) {

    companion object{
        private const val PREF_NAME = "app_setting"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_THEME_NAME = "theme_name"
        private const val KEY_FAJR_NOTIFICATION = "fajr_notification"
        private const val KEY_DHUHR_NOTIFICATION = "dhuhr_notification"
        private const val KEY_ASR_NOTIFICATION = "asr_notification"
        private const val KEY_MAGHRIB_NOTIFICATION = "maghrib_notification"
        private const val KEY_ISHA_NOTIFICATION = "isha_notification"

        private const val LAST_READ_SURAH = "quran_last_surah"
        private const val LAST_READ_AYAH = "quran_last_ayah"
    }


    fun saveQuranLastRead(surahNumber: Int, ayahNumber: Int){
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(pref.edit()){
            putInt(LAST_READ_SURAH, surahNumber)
            putInt(LAST_READ_AYAH, ayahNumber)
            apply()

        }
    }

    fun getQuranLastRead(): Pair<Int?, Int?> {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val surahNumber = if (pref.contains(LAST_READ_SURAH)) pref.getInt(LAST_READ_SURAH, 0) else null
        val ayahNumber = if (pref.contains(LAST_READ_AYAH)) pref.getInt(LAST_READ_AYAH, 0) else null
        return Pair(surahNumber, ayahNumber)
    }


    fun setLanguage( language: Languages) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putString(KEY_LANGUAGE, language.name).apply()
    }

    fun getLanguage(): Languages {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val language =
            pref.getString(KEY_LANGUAGE, Languages.ENGLISH.name) ?: Languages.ENGLISH.name
        return Languages.valueOf(language)
    }

    fun setThemeMode(themeMode: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(KEY_THEME_MODE, themeMode).apply()
    }

    fun getThemeMode(): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_THEME_MODE, true)
    }

    fun setThemeName(themes: Themes) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putString(KEY_THEME_NAME, themes.name).apply()
    }

    fun getThemeName(): Themes {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val themeName = pref.getString(KEY_THEME_NAME, Themes.DARK.name) ?: Themes.DARK.name
        return Themes.valueOf(themeName)
    }


    fun setSortType(sortType: DateType) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putString(KEY_SORT_BY, sortType.name).apply()
    }

    fun getSortType(): DateType {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val sortType = pref.getString(KEY_SORT_BY, DateType.DATE_DESC.name)
        return DateType.valueOf(sortType!!)

    }


    fun setFajrNotification(isNotification: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(KEY_FAJR_NOTIFICATION, isNotification).apply()
    }

    fun getFajrNotification(): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_FAJR_NOTIFICATION, false)
    }

    fun setDhuhrNotification(isNotification: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(KEY_DHUHR_NOTIFICATION, isNotification).apply()
    }

    fun getDhuhrNotification(): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_DHUHR_NOTIFICATION, false)
    }

    fun setAsrNotification(isNotification: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(KEY_ASR_NOTIFICATION, isNotification).apply()
    }

    fun getAsrNotification(): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_ASR_NOTIFICATION, false)
    }

    fun setMaghribNotification(isNotification: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(KEY_MAGHRIB_NOTIFICATION, isNotification).apply()
    }

    fun getMaghribNotification(): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_MAGHRIB_NOTIFICATION, false)
    }

    fun setIshaNotification(isNotification: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(KEY_ISHA_NOTIFICATION, isNotification).apply()
    }

    fun getIshaNotification(): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_ISHA_NOTIFICATION, false)
    }


}