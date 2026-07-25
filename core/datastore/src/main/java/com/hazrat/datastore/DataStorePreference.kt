package com.hazrat.datastore

import android.content.Context
import androidx.core.content.edit
import com.hazrat.model.DateType
import com.hazrat.model.Languages
import com.hazrat.model.al_quran_model.RecentReadSurah
import com.hazrat.utils.Constants.KEY_SORT_BY
import org.json.JSONArray
import org.json.JSONObject

class DataStorePreference(
    private val context: Context
) {

    companion object {
        private const val PREF_NAME = "app_setting"

        private const val LAST_READ_SURAH = "quran_last_surah"
        private const val LAST_READ_AYAH = "quran_last_ayah"

        private const val DAILY_VERSE_DATE = "daily_verse_date"
        private const val DAILY_VERSE_SURAH = "daily_verse_surah"
        private const val DAILY_VERSE_AYAH = "daily_verse_ayah"

        private const val DAILY_DUA_DATE = "daily_dua_date"
        private const val DAILY_DUA_CAT = "daily_dua_cat"
        private const val DAILY_DUA_ID = "daily_dua_id"

        private const val RECENT_READS_KEY = "quran_recent_reads"
        private const val KEY_LANGUAGE = "language"
    }

    fun saveQuranLastRead(surahNumber: Int, ayahNumber: Int) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit {
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

    fun saveRecentSurahRead(surahNumber: Int, surahName: String, ayahNumber: Int, formattedDate: String) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val existingList = getRecentSurahReads().toMutableList()

        existingList.removeAll { it.surahNumber == surahNumber }
        existingList.add(0, RecentReadSurah(surahNumber, surahName, ayahNumber, formattedDate))

        val trimmed = existingList.take(10)
        val jsonArray = JSONArray()
        trimmed.forEach { item ->
            val obj = JSONObject().apply {
                put("surahNumber", item.surahNumber)
                put("surahName", item.surahName)
                put("ayahNumber", item.ayahNumber)
                put("formattedDate", item.formattedDate)
                put("timestamp", item.timestamp)
            }
            jsonArray.put(obj)
        }

        pref.edit {
            putString(RECENT_READS_KEY, jsonArray.toString())
        }
    }

    fun getRecentSurahReads(): List<RecentReadSurah> {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonStr = pref.getString(RECENT_READS_KEY, null)
        if (jsonStr.isNullOrEmpty()) {
            return emptyList()
        }
        return try {
            val array = JSONArray(jsonStr)
            val list = mutableListOf<RecentReadSurah>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    RecentReadSurah(
                        surahNumber = obj.getInt("surahNumber"),
                        surahName = obj.getString("surahName"),
                        ayahNumber = obj.getInt("ayahNumber"),
                        formattedDate = obj.optString("formattedDate", "Today"),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveDailyVerse(dateString: String, surahNumber: Int, ayahNumber: Int) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit {
            putString(DAILY_VERSE_DATE, dateString)
            putInt(DAILY_VERSE_SURAH, surahNumber)
            putInt(DAILY_VERSE_AYAH, ayahNumber)
        }
    }

    fun getDailyVerse(currentDateString: String): Pair<Int, Int>? {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedDate = pref.getString(DAILY_VERSE_DATE, null)
        if (savedDate == currentDateString && pref.contains(DAILY_VERSE_SURAH)) {
            val surah = pref.getInt(DAILY_VERSE_SURAH, 29)
            val ayah = pref.getInt(DAILY_VERSE_AYAH, 45)
            return Pair(surah, ayah)
        }
        return null
    }

    fun saveDailyDua(dateString: String, categoryId: Int, duaId: Int) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit {
            putString(DAILY_DUA_DATE, dateString)
            putInt(DAILY_DUA_CAT, categoryId)
            putInt(DAILY_DUA_ID, duaId)
        }
    }

    fun getDailyDua(currentDateString: String): Pair<Int, Int>? {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedDate = pref.getString(DAILY_DUA_DATE, null)
        if (savedDate == currentDateString && pref.contains(DAILY_DUA_CAT)) {
            val cat = pref.getInt(DAILY_DUA_CAT, 1)
            val id = pref.getInt(DAILY_DUA_ID, 1)
            return Pair(cat, id)
        }
        return null
    }

    fun setLanguage(language: Languages) {
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
}