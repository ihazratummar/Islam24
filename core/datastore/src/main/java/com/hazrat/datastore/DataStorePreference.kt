package com.hazrat.datastore

import android.content.Context
import androidx.core.content.edit
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

        private const val DAILY_ATHKAR_DATE = "daily_athkar_date"
        private const val DAILY_ATHKAR_COUNTS = "daily_athkar_counts"
    }

    fun saveQuranLastRead(surahNumber: Int, ayahNumber: Int) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit {
            putInt(LAST_READ_SURAH, surahNumber)
            putInt(LAST_READ_AYAH, ayahNumber)
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



    fun saveDailyAthkarCounts(todayDateString: String, counts: Map<Int, Int>) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonObj = JSONObject()
        counts.forEach { (id, count) ->
            jsonObj.put(id.toString(), count)
        }
        pref.edit {
            putString(DAILY_ATHKAR_DATE, todayDateString)
            putString(DAILY_ATHKAR_COUNTS, jsonObj.toString())
        }
    }

    fun getDailyAthkarCounts(todayDateString: String): Map<Int, Int> {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedDate = pref.getString(DAILY_ATHKAR_DATE, null)
        if (savedDate != todayDateString) {
            return emptyMap()
        }
        val jsonStr = pref.getString(DAILY_ATHKAR_COUNTS, null) ?: return emptyMap()
        return try {
            val jsonObj = JSONObject(jsonStr)
            val map = mutableMapOf<Int, Int>()
            val keys = jsonObj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                map[key.toInt()] = jsonObj.getInt(key)
            }
            map
        } catch (e: Exception) {
            emptyMap()
        }
    }
}