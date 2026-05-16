package com.hazrat.model.al_quran_model.local_quran_ar

import android.content.Context
import kotlinx.serialization.json.Json

/**
 * @author Hazrat Ummar Shaikh
 * Created on 17-12-2024
 */

class LocalQuranModelAr {
    private val json = Json { ignoreUnknownKeys = true }

    fun getQuranAr(context: Context, fileName: String): List<LocalQuranModelArItem> {
        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            json.decodeFromString<List<LocalQuranModelArItem>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
