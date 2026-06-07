package com.hazrat.utils

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.util.Calendar
import java.util.Locale


data class HijriDateInfo(
    val day: Int,
    val monthName: String,
    val year: Int
)

object IslamicCalendarUtils  {

    fun getNextRamadan() : UmmalquraCalendar {
        val current = UmmalquraCalendar()

        val currentHijriYear = current.get(UmmalquraCalendar.YEAR)

        val currentHijriMonth = current.get(UmmalquraCalendar.MONTH) + 1

        val ramadanYear = if (currentHijriMonth >= 9){
            currentHijriYear + 1
        }else {
            currentHijriYear
        }
        return UmmalquraCalendar().apply {
            set(ramadanYear, 8, 1)
        }
    }

    fun getCurrentHijriDateInfo(): HijriDateInfo {

        val calendar = UmmalquraCalendar()

        return HijriDateInfo(

            day = calendar.get(
                UmmalquraCalendar.DAY_OF_MONTH
            ),

            monthName = calendar.getDisplayName(
                UmmalquraCalendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH
            ) ?: "",

            year = calendar.get(
                UmmalquraCalendar.YEAR
            )
        )
    }

}