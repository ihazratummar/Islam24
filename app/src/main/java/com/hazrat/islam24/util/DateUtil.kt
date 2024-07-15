package com.hazrat.islam24.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import java.util.*

/**
 * Object containing utility methods for date manipulation.
 */
object DateUtil {

    /**
     * Retrieves the current year.
     *
     * @return The current year as an integer.
     */
    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    /**
     * Retrieves the current month.
     *
     * @return The current month as an integer (1-12).
     */
    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1 // Adding 1 because Calendar.MONTH starts from 0
    }

    /**
     * Retrieves the current day of the month.
     *
     * @return The current day of the month as an integer.
     */
    fun getCurrentDay(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * Retrieves the current date in the "dd-MM-yyyy" format.
     *
     * @return The current date as a string.
     */
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return dateFormat.format(Date())
    }

    /**
     * Checks if the provided date string represents today's date.
     *
     * @param dateString The date string to be checked (in "dd-MM-yyyy" format).
     * @return true if the provided date string represents today's date, false otherwise.
     */
    fun isToday(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val currentDate = dateFormat.format(Date())
        return dateString == currentDate
    }
}