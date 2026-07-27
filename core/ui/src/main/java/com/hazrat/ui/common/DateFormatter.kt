package com.hazrat.ui.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Human-friendly relative date formatter for Islam24.
 *
 * Rules:
 * - Current day: "Today"
 * - Previous day: "Yesterday"
 * - Same year: "Friday 15th Jul"
 * - Different year: "15 Jul 2025"
 *
 * @author hazratummar
 */
object DateFormatter {

    private fun getOrdinalSuffix(day: Int): String {
        if (day in 11..13) return "th"
        return when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    fun formatHumanReadableDate(dateString: String): String {
        if (dateString.equals("Today", ignoreCase = true)) return "Today"
        if (dateString.equals("Yesterday", ignoreCase = true)) return "Yesterday"

        val parsedDate: LocalDate = try {
            if (dateString.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
            } else if (dateString.matches(Regex("\\d{1,2} [A-Za-z]{3} \\d{4}"))) {
                LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))
            } else {
                return dateString
            }
        } catch (e: Exception) {
            return dateString
        }

        val today = LocalDate.now()

        return when {
            parsedDate.isEqual(today) -> "Today"
            parsedDate.isEqual(today.minusDays(1)) -> "Yesterday"
            parsedDate.year == today.year -> {
                val dayOfWeek = parsedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                val dayOfMonth = parsedDate.dayOfMonth
                val suffix = getOrdinalSuffix(dayOfMonth)
                val month = parsedDate.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                "$dayOfWeek $dayOfMonth$suffix $month"
            }
            else -> {
                val dayOfMonth = parsedDate.dayOfMonth
                val month = parsedDate.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                val year = parsedDate.year
                "$dayOfMonth $month $year"
            }
        }
    }
}
