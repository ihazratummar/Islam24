package com.hazrat.calendar

import android.graphics.drawable.GradientDrawable
import android.icu.util.IslamicCalendar
import android.text.style.ForegroundColorSpan
import com.github.eltohamy.materialhijricalendarview.CalendarDay
import com.github.eltohamy.materialhijricalendarview.DayViewDecorator
import com.github.eltohamy.materialhijricalendarview.DayViewFacade

/**
 * @author Hazrat Ummar Shaikh
 * Created on 15-06-2025
 */

class ColorDayDecorator(
    private val shouldColor: (CalendarDay) -> Boolean,
    private val color: Int
) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean = shouldColor(day)
    override fun decorate(view: DayViewFacade) {
        view.addSpan(object : ForegroundColorSpan(color) {})
    }
}

class TodayCircleDecorator(
    private val backgroundColor: Int
) : DayViewDecorator {

    private val todayHijri: Triple<Int, Int, Int> = IslamicCalendar().let {
        Triple(
            it.get(IslamicCalendar.YEAR),
            it.get(IslamicCalendar.MONTH),
            it.get(IslamicCalendar.DATE)
        )
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        if (day == null) return false
        // Extract Hijri equivalent of this day
        val cal = IslamicCalendar()
        cal.set(day.year, day.month, day.day - 1) // CalendarDay uses 1-based month, IslamicCalendar is 0-based
        val y = cal.get(IslamicCalendar.YEAR)
        val m = cal.get(IslamicCalendar.MONTH)
        val d = cal.get(IslamicCalendar.DATE)

        return todayHijri == Triple(y, m, d)
    }

    override fun decorate(view: DayViewFacade) {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(backgroundColor)
            setSize(48, 48)
        }
        view.setBackgroundDrawable(drawable)
    }
}

