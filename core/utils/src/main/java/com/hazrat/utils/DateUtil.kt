package com.hazrat.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Object containing utility methods for date manipulation using modern java.time APIs.
 */
object DateUtil {

    /**
     * Retrieves the current month.
     *
     * @return The current month as an integer (1-12).
     */
    fun getCurrentMonth(): Int {
        return LocalDate.now().monthValue
    }

    /**
     * Retrieves the current date in the "yyyy-MM-dd" format for sortable database storage.
     *
     * @return The current date as a string.
     */
    fun getCurrentDate(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH))
    }

    /**
     * Retrieves tomorrow's date in the "yyyy-MM-dd" format.
     */
    fun getTomorrowDate(): String {
        return LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH))
    }

    /**
     * Converts a "dd-MM-yyyy" date string to "yyyy-MM-dd".
     */
    fun convertToDbFormat(dateString: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH)
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
            val date = LocalDate.parse(dateString, inputFormatter)
            date.format(outputFormatter)
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

    fun timeStringToLong(dateString: String, format: String = "dd-MM-yyyy HH:mm"): Long {
        if (dateString.isBlank()) return 0L
        val cleanedDate = dateString.replace(Regex("\\s*\\(.*?\\)"), "")
            .replace(Regex("\\s*\\+.*"), "")
            .trim()
        return try {
            val sdf = java.text.SimpleDateFormat(format, Locale.ENGLISH)
            val parsed = sdf.parse(cleanedDate)
            parsed?.time ?: 0L
        } catch (e: Exception) {
            try {
                val sdf2 = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
                sdf2.parse(cleanedDate)?.time ?: 0L
            } catch (e2: Exception) {
                0L
            }
        }
    }

    fun getDateFromLong(dateLong: Long, format: String = "dd/MM/yyyy"): String {
        if (dateLong <= 0L) return ""
        val formatter = DateTimeFormatter.ofPattern(format, Locale.getDefault())
        val localDate = Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.format(formatter)
    }

    fun dateLongToString(dateLong: Long, format: String = "hh:mm a", locale: Locale = Locale.getDefault()): String {
        if (dateLong <= 0L) return ""
        return try {
            val formatter = DateTimeFormatter.ofPattern(format, locale)
            val localDateTime = Instant.ofEpochMilli(dateLong).atZone(ZoneId.systemDefault()).toLocalDateTime()
            localDateTime.format(formatter)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     *  Create Readable date for Time millis
     */
    fun Long.toReadableDate(): String {
        val zoneId = ZoneId.systemDefault()
        val targetDate = Instant
            .ofEpochMilli(this)
            .atZone(zoneId)
            .toLocalDate()

        val today = LocalDate.now(zoneId)

        return when {
            targetDate.isEqual(today) -> {
                "Today"
            }
            targetDate.isEqual(today.minusDays(1)) -> {
                "Yesterday"
            }
            targetDate.isEqual(today.plusDays(1)) -> {
                "Tomorrow"
            }
            targetDate.year == today.year -> {
                targetDate.format(DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH))
            }
            else -> {
                targetDate.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH))
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


    fun kotlinx.datetime.Instant.toReadableLocale() : String {
        val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())

        val formatter = DateTimeFormatter.ofPattern(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        )

        return formatter.format(localDateTime.toJavaLocalDateTime())
    }

}