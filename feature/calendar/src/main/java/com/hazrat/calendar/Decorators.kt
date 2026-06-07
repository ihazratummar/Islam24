package com.hazrat.calendar

import android.graphics.drawable.GradientDrawable
import android.icu.util.IslamicCalendar
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.github.eltohamy.materialhijricalendarview.CalendarDay
import com.github.eltohamy.materialhijricalendarview.DayViewDecorator
import com.github.eltohamy.materialhijricalendarview.DayViewFacade
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar

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

    private val today: CalendarDay = run {
        val hijri = UmmalquraCalendar()
        CalendarDay.from(
            hijri.get(UmmalquraCalendar.YEAR),
            hijri.get(UmmalquraCalendar.MONTH), // no +1
            hijri.get(UmmalquraCalendar.DAY_OF_MONTH)
        )
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == today
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
