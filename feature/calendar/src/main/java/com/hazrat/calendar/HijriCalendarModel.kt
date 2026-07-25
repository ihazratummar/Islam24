package com.hazrat.calendar

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.util.Calendar
import java.util.concurrent.ConcurrentHashMap

data class HijriCalendarDay(
    val hijriDay: Int,
    val gregorianDay: Int,
    val dayOfWeek: Int, // 1 = Sun, 2 = Mon, ..., 6 = Fri, 7 = Sat
    val isFriday: Boolean = dayOfWeek == Calendar.FRIDAY,
    val isToday: Boolean = false,
    val isSelected: Boolean = false,
    val hasEvent: Boolean = false,
    val eventTitle: String? = null
)

data class HijriMonthData(
    val hijriYear: Int,
    val hijriMonth: Int, // 1..12
    val hijriMonthName: String,
    val gregorianMonthName: String,
    val gregorianYear: Int,
    val startPaddingCount: Int, // Empty slots before day 1 (0..6)
    val days: List<HijriCalendarDay>,
    val events: List<HijriEvent>
)

data class HijriEvent(
    val hijriDay: Int,
    val title: String
)

object HijriCalendarHelper {

    private val monthCache = ConcurrentHashMap<String, HijriMonthData>()

    fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "Muharram"
            2 -> "Safar"
            3 -> "Rabi' al-Awwal"
            4 -> "Rabi' al-Thani"
            5 -> "Jumada al-Ula"
            6 -> "Jumada al-Akhirah"
            7 -> "Rajab"
            8 -> "Sha'ban"
            9 -> "Ramadan"
            10 -> "Shawwal"
            11 -> "Dhu al-Qi'dah"
            12 -> "Dhu al-Hijjah"
            else -> ""
        }
    }

    private fun getGregorianMonthName(month: Int): String {
        return when (month) {
            0 -> "January"
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
            else -> ""
        }
    }

    fun getEventsForMonth(hijriMonth: Int): List<HijriEvent> {
        val events = mutableListOf<HijriEvent>()

        // Sunnah Fasting on 14, 15, 16 for every Hijri month
        events.add(HijriEvent(14, "Sunnah Fasting"))
        events.add(HijriEvent(15, "Sunnah Fasting"))
        events.add(HijriEvent(16, "Sunnah Fasting"))

        when (hijriMonth) {
            1 -> {
                events.add(HijriEvent(1, "Islamic New Year"))
                events.add(HijriEvent(10, "Day of Ashura"))
            }
            3 -> {
                events.add(HijriEvent(12, "12 Rabi' al-Awwal"))
                events.add(HijriEvent(27, "Isra and Mi'raj"))
            }
            7 -> {
                events.add(HijriEvent(27, "Isra and Mi'raj"))
            }
            8 -> {
                events.add(HijriEvent(15, "Shab-e-Barat"))
            }
            9 -> {
                events.add(HijriEvent(1, "First Day of Ramadan"))
                events.add(HijriEvent(27, "Laylat al-Qadr"))
            }
            10 -> {
                events.add(HijriEvent(1, "Eid al-Fitr"))
            }
            12 -> {
                events.add(HijriEvent(9, "Day of Arafah"))
                events.add(HijriEvent(10, "Eid al-Adha"))
            }
        }
        return events.sortedBy { it.hijriDay }
    }

    private fun generateMonthData(year: Int, month: Int): HijriMonthData {
        val cal = UmmalquraCalendar()
        cal.set(UmmalquraCalendar.YEAR, year)
        cal.set(UmmalquraCalendar.MONTH, month - 1)
        cal.set(UmmalquraCalendar.DAY_OF_MONTH, 1)

        val todayCal = UmmalquraCalendar()
        val todayYear = todayCal.get(UmmalquraCalendar.YEAR)
        val todayMonth = todayCal.get(UmmalquraCalendar.MONTH) + 1
        val todayDay = todayCal.get(UmmalquraCalendar.DAY_OF_MONTH)

        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val startPaddingCount = (firstDayOfWeek - 1) % 7
        val lengthOfMonth = cal.lengthOfMonth()

        val events = getEventsForMonth(month)
        val eventDayMap = events.associateBy { it.hijriDay }
        val days = mutableListOf<HijriCalendarDay>()

        var middleGregorianMonth = ""
        var middleGregorianYear = 0
        val gregCal = java.util.GregorianCalendar()

        for (d in 1..lengthOfMonth) {
            cal.set(UmmalquraCalendar.DAY_OF_MONTH, d)
            gregCal.time = cal.time

            val gregDay = gregCal.get(Calendar.DAY_OF_MONTH)
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            val isToday = (year == todayYear && month == todayMonth && d == todayDay)
            val event = eventDayMap[d]

            if (d == 15) {
                middleGregorianMonth = getGregorianMonthName(gregCal.get(Calendar.MONTH))
                middleGregorianYear = gregCal.get(Calendar.YEAR)
            }

            days.add(
                HijriCalendarDay(
                    hijriDay = d,
                    gregorianDay = gregDay,
                    dayOfWeek = dayOfWeek,
                    isFriday = (dayOfWeek == Calendar.FRIDAY),
                    isToday = isToday,
                    isSelected = false,
                    hasEvent = (event != null),
                    eventTitle = event?.title
                )
            )
        }

        if (middleGregorianMonth.isEmpty()) {
            middleGregorianMonth = getGregorianMonthName(gregCal.get(Calendar.MONTH))
            middleGregorianYear = gregCal.get(Calendar.YEAR)
        }

        return HijriMonthData(
            hijriYear = year,
            hijriMonth = month,
            hijriMonthName = getMonthName(month),
            gregorianMonthName = middleGregorianMonth,
            gregorianYear = middleGregorianYear,
            startPaddingCount = startPaddingCount,
            days = days,
            events = events
        )
    }

    fun loadMonthData(
        year: Int,
        month: Int,
        selectedDayNumber: Int? = null
    ): HijriMonthData {
        val cacheKey = "$year-$month"
        val baseData = monthCache.getOrPut(cacheKey) {
            generateMonthData(year, month)
        }

        if (selectedDayNumber == null) return baseData

        return baseData.copy(
            days = baseData.days.map { day ->
                day.copy(isSelected = (day.hijriDay == selectedDayNumber))
            }
        )
    }

    fun getMonthDataForOffset(
        offset: Int,
        selectedDayNumber: Int? = null
    ): HijriMonthData {
        val cal = UmmalquraCalendar()
        val initialYear = cal.get(UmmalquraCalendar.YEAR)
        val initialMonth = cal.get(UmmalquraCalendar.MONTH) + 1

        val totalMonths = (initialYear * 12) + (initialMonth - 1) + offset
        var targetYear = totalMonths / 12
        var targetMonth = (totalMonths % 12) + 1

        if (targetMonth <= 0) {
            targetMonth += 12
            targetYear -= 1
        }

        return loadMonthData(targetYear, targetMonth, selectedDayNumber)
    }
}
