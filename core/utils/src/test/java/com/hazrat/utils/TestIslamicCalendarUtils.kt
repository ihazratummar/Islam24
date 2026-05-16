package com.hazrat.utils

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import org.junit.Test


/**
 * @author hazratummar
 * Created on 16/05/26
 */

class TestIslamicCalendarUtils {


    @Test
    fun getNextRamadan() {

        val nextRamadan = IslamicCalendarUtils.getNextRamadan()
        println("This is my log ${nextRamadan.get(UmmalquraCalendar.YEAR)}")
        assert(nextRamadan.get(UmmalquraCalendar.YEAR) == 1448)
    }

}