package com.hazrat.islam24.util

import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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


    fun timeStringToLong(dateString: String, format: String = "dd-MM-yyyy HH:mm"): Long{
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        val date = dateFormat.parse(dateString) ?: return 0L
        return date.time
    }


    fun dateLongToString(dateLong: Long, format: String = "hh:mm a"): String {
        val formatter =SimpleDateFormat(format, Locale.getDefault())
        val date = Date(dateLong)
        return formatter.format(date)
    }


    fun getCountdownText(targetTimeMillis: Long): String {
        val currentTimeMillis = System.currentTimeMillis()

        // Calculate the difference in milliseconds
        val diffMillis = targetTimeMillis - currentTimeMillis

        if (diffMillis <= 0) {
            return ""
        }

        // Convert milliseconds to hours, minutes, and seconds
        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        // Format the remaining time into a countdown text
        val hoursLeft = hours % 24
        val minutesLeft = minutes % 60
        val secondsLeft = seconds % 60

        return String.format("%02d:%02d:%02d", hoursLeft, minutesLeft, secondsLeft)
    }


    fun formatLocalTime(time: LocalTime?): String? {
        return time?.let {
            val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
            it.format(formatter)
        }
    }
}