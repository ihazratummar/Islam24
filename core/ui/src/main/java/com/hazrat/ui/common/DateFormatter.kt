package com.hazrat.ui.common

import com.hazrat.utils.toLocalizedDigits
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Human-friendly relative date formatter for Islam24.
 *
 * Rules:
 * - Current day: "আজ" (bn) / "Today" (en)
 * - Previous day: "গতকাল" (bn) / "Yesterday" (en)
 * - Same year: "শুক্রবার, ১৫ জুলাই" (bn) / "Friday 15th Jul" (en)
 * - Different year: "১৫ জুলাই ২০২৫" (bn) / "15 Jul 2025" (en)
 *
 * @author hazratummar
 */
object DateFormatter {

    private val bengaliMonths = mapOf(
        1 to "জানুয়ারি", 2 to "ফেব্রুয়ারি", 3 to "মার্চ", 4 to "এপ্রিল",
        5 to "মে", 6 to "জুন", 7 to "জুলাই", 8 to "আগস্ট",
        9 to "সেপ্টেম্বর", 10 to "অক্টোবর", 11 to "নভেম্বর", 12 to "ডিসেম্বর"
    )

    private val bengaliDaysOfWeek = mapOf(
        java.time.DayOfWeek.MONDAY to "সোমবার",
        java.time.DayOfWeek.TUESDAY to "মঙ্গলবার",
        java.time.DayOfWeek.WEDNESDAY to "বুধবার",
        java.time.DayOfWeek.THURSDAY to "বৃহস্পতিবার",
        java.time.DayOfWeek.FRIDAY to "শুক্রবার",
        java.time.DayOfWeek.SATURDAY to "শনিবার",
        java.time.DayOfWeek.SUNDAY to "রবিবার"
    )

    private fun getOrdinalSuffix(day: Int): String {
        if (day in 11..13) return "th"
        return when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    fun formatHumanReadableDate(dateString: String, locale: Locale = Locale.getDefault()): String {
        val isBengali = locale.language.equals("bn", ignoreCase = true)

        if (dateString.equals("Today", ignoreCase = true) || dateString.equals("আজ", ignoreCase = true)) {
            return if (isBengali) "আজ" else "Today"
        }
        if (dateString.equals("Yesterday", ignoreCase = true) || dateString.equals("গতকাল", ignoreCase = true)) {
            return if (isBengali) "গতকাল" else "Yesterday"
        }

        val parsedDate: LocalDate = try {
            if (dateString.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
            } else if (dateString.matches(Regex("\\d{1,2} [A-Za-z]{3} \\d{4}"))) {
                LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH))
            } else {
                return if (isBengali) dateString.toLocalizedDigits(locale) else dateString
            }
        } catch (e: Exception) {
            return if (isBengali) dateString.toLocalizedDigits(locale) else dateString
        }

        val today = LocalDate.now()

        return if (isBengali) {
            when {
                parsedDate.isEqual(today) -> "আজ"
                parsedDate.isEqual(today.minusDays(1)) -> "গতকাল"
                parsedDate.year == today.year -> {
                    val dayOfWeek = bengaliDaysOfWeek[parsedDate.dayOfWeek] ?: parsedDate.dayOfWeek.name
                    val dayOfMonth = parsedDate.dayOfMonth.toLocalizedDigits(locale)
                    val month = bengaliMonths[parsedDate.monthValue] ?: parsedDate.month.name
                    "$dayOfWeek, $dayOfMonth $month"
                }
                else -> {
                    val dayOfMonth = parsedDate.dayOfMonth.toLocalizedDigits(locale)
                    val month = bengaliMonths[parsedDate.monthValue] ?: parsedDate.month.name
                    val year = parsedDate.year.toLocalizedDigits(locale)
                    "$dayOfMonth $month $year"
                }
            }
        } else {
            when {
                parsedDate.isEqual(today) -> "Today"
                parsedDate.isEqual(today.minusDays(1)) -> "Yesterday"
                parsedDate.year == today.year -> {
                    val dayOfWeek = parsedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                    val dayOfMonth = parsedDate.dayOfMonth
                    val suffix = getOrdinalSuffix(dayOfMonth)
                    val month = parsedDate.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    "$dayOfWeek ${dayOfMonth}$suffix $month"
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
}
