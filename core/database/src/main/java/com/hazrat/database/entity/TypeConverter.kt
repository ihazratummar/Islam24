package com.hazrat.database.entity

import androidx.room.TypeConverter

class HolidayListConverter {

    /**
     * Converts a comma-separated string to a list of strings.
     *
     * @param value The comma-separated string to be converted.
     * @return A list of strings or null if the input string is null.
     */
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")
    }

    /**
     * Converts a list of strings to a comma-separated string.
     *
     * @param list The list of strings to be converted.
     * @return A comma-separated string or null if the input list is null.
     */
    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}