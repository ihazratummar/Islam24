package com.hazrat.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * @author hazratummar
 * Created on 15/05/26
 */

class Converter {


    @TypeConverter
    fun fromHolidayList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toHolidayList(value: String) : List<String> {
        val type = object  : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type)
    }
}