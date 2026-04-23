package com.hazrat.datastore

import android.content.Context
import android.media.MediaFormat.KEY_LANGUAGE
import androidx.core.content.edit
import com.hazrat.model.DateType
import com.hazrat.model.Languages
import com.hazrat.utils.Constants.KEY_SORT_BY

class DataStorePreference (
    private val context: Context
) {

    companion object{
        private const val PREF_NAME = "app_setting"

        const val KEY_FAJR_NOTIFICATION = "fajr_notification"
        const val KEY_DHUHR_NOTIFICATION = "dhuhr_notification"
        const val KEY_ASR_NOTIFICATION = "asr_notification"
        const val KEY_MAGHRIB_NOTIFICATION = "maghrib_notification"
        const val KEY_ISHA_NOTIFICATION = "isha_notification"

        private const val LAST_READ_SURAH = "quran_last_surah"
        private const val LAST_READ_AYAH = "quran_last_ayah"
    }


    fun saveQuranLastRead(surahNumber: Int, ayahNumber: Int){
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit{
            putInt(LAST_READ_SURAH, surahNumber)
            putInt(LAST_READ_AYAH, ayahNumber)

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
        pref.edit { putString(KEY_LANGUAGE, language.name) }
    }

    fun getLanguage(): Languages {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val language =
            pref.getString(KEY_LANGUAGE, Languages.ENGLISH.name) ?: Languages.ENGLISH.name
        return Languages.valueOf(language)
    }


    fun setSortType(sortType: DateType) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit { putString(KEY_SORT_BY, sortType.name) }
    }

    fun getSortType(): DateType {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val sortType = pref.getString(KEY_SORT_BY, DateType.DATE_DESC.name)
        return DateType.valueOf(sortType!!)

    }


    fun setPrayerNotification(isNotification: Boolean, prayerKey: String){
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit {putBoolean(prayerKey, isNotification) }
    }

    fun getPrayerNotification(prayerKey: String): Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(prayerKey, false)
    }
}