package com.hazrat.islam24.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import java.util.*

object DateUtil {
    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1 // Adding 1 because Calendar.MONTH starts from 0
    }

    fun getCurrentDay(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun isToday(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        return dateString == currentDate
    }
}
