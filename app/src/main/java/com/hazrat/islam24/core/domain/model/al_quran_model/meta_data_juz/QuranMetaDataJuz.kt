package com.hazrat.islam24.core.domain.model.al_quran_model.meta_data_juz

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

data class QuranMetaDataJuz(
    val juzData: List<X1>
)

data class X1(
    val start: Int,
    val end: Int
)

fun parseJuzJson(context: Context): QuranMetaDataJuz? {
    return try {
        // Open the "juz.json" file from the assets folder
        val inputStream = context.assets.open("quran_data/juz.json")
        val reader = InputStreamReader(inputStream)

        // Use Gson to parse the JSON into a Map where key is the Juz number (String), and value is the X1 object
        val juzMap: Map<String, X1> = Gson().fromJson(reader, object : TypeToken<Map<String, X1>>() {}.type)

        // Convert the Map values (X1) into a List
        QuranMetaDataJuz(juzData = juzMap.values.toList())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
