package com.hazrat.utils

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.hazrat.model.MinimalPrayerData

object HijriDateUtils {


    data class HijriDate(val year: Int, val month: Int, val day: Int) {
        fun toShortKey(): Int = year * 10000 + month * 100 + day
    }

    fun today(): HijriDate {
        val cal = UmmalquraCalendar()
        return HijriDate(
            year = cal.get(UmmalquraCalendar.YEAR),
            month = cal.get(UmmalquraCalendar.MONTH) + 1,
            day = cal.get(UmmalquraCalendar.DAY_OF_MONTH)
        )
    }

    fun offset(from: HijriDate, day: Int) : HijriDate {
        val cal = UmmalquraCalendar().apply {
            set(from.year, from.month - 1, from.day)
            add(UmmalquraCalendar.DAY_OF_MONTH, day)
        }
        return HijriDate(
            year = cal.get(UmmalquraCalendar.YEAR),
            month = cal.get(UmmalquraCalendar.MONTH) + 1,
            day = cal.get(UmmalquraCalendar.DAY_OF_MONTH)
        )
    }

    fun fromMinimalPrayerData(data: MinimalPrayerData)  = HijriDate(
        year = data.hijriYear,
        month = data.hijriMonthNumber,
        day = data.hijriDay
    )

}