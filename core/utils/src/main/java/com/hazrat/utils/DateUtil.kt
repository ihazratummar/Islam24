package com.hazrat.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Object containing utility methods for date manipulation.
 */
object DateUtil {


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
     * Retrieves the current date in the "yyyy-MM-dd" format for sortable database storage.
     *
     * @return The current date as a string.
     */
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return dateFormat.format(Date())
    }

    /**
     * Converts a "dd-MM-yyyy" date string to "yyyy-MM-dd".
     */
    fun convertToDbFormat(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }

    /**
     * Checks if the provided date string represents today's date.
     *
     * @param dateString The date string to be checked (in "yyyy-MM-dd" format).
     * @return true if the provided date string represents today's date, false otherwise.
     */
    fun isToday(dateString: String): Boolean {
        return dateString == getCurrentDate()
    }


    fun timeStringToLong(dateString: String, format: String = "dd-MM-yyyy HH:mm"): Long{
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        val date = dateFormat.parse(dateString) ?: return 0L
        return date.time
    }

    fun getDateFromLong(dateLong: Long, format: String = "dd/MM/yyyy"): String {
        val date = Date(dateLong)
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun dateLongToString(dateLong: Long, format: String = "hh:mm a"): String {
        val formatter =SimpleDateFormat(format, Locale.ENGLISH)
        val date = Date(dateLong)
        return formatter.format(date)
    }

    /**
     *  Create Readable date for Time millis
     */

    fun Long.toReadableDate() : String {
        val zoneId = ZoneId.systemDefault()
        val targetDate = Instant
            .ofEpochMilli(this)
            .atZone(zoneId)
            .toLocalDate()

        val today = LocalDate.now(zoneId)

        return when {
            targetDate.isEqual(today) ->{
                "Today"
            }
            targetDate.isEqual(today.minusDays(1)) -> {
                "Yesterday"
            }
            targetDate.isEqual(today.plusDays(1)) -> {
                "Tomorrow"
            }
            targetDate.year == today.year -> {
                targetDate.format(DateTimeFormatter.ofPattern("d MMM"))
            }

            else -> {
                targetDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
            }
        }
    }

    fun toLocalDate(
        timeMillis: Long,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): LocalDate {
        return Instant
            .ofEpochMilli(timeMillis)
            .atZone(zoneId)
            .toLocalDate()
    }

}