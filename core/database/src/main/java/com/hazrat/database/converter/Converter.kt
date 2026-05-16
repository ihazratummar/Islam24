package com.hazrat.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * @author hazratummar
 * Created on 15/05/26
 */

class Converter {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromHolidayList(value: List<String>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toHolidayList(value: String): List<String> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}