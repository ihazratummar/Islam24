package com.hazrat.islam24.core.data.entity

import androidx.room.TypeConverter

class HolidayListConverter {

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}