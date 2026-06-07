package com.hazrat.calendar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.eltohamy.materialhijricalendarview.CalendarDay
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.hazrat.ui.R

@Composable
fun HijriCalendarHomeScreen(
    onDateSelected: (CalendarDay) -> Unit = {}
) {
    val textColor = MaterialTheme.colorScheme.onBackground.toArgb()
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainer.toArgb()
    val onDateSelectedRef = rememberUpdatedState(onDateSelected)

    // Track selected date in state
    var selectedDay by remember { mutableStateOf<CalendarDay?>(null) }

    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            MaterialHijriCalendarView(context).apply {
                val hijriToday = UmmalquraCalendar()
                val day = CalendarDay.from(
                    hijriToday.get(UmmalquraCalendar.YEAR),
                    hijriToday.get(UmmalquraCalendar.MONTH) + 1,
                    hijriToday.get(UmmalquraCalendar.DAY_OF_MONTH),
                )
                setSelectedDate(day)
                selectedDay = day

                setWeekDayTextAppearance(R.style.CalendarWeekDayText)
                setHeaderTextAppearance(R.style.CalendarHeaderText)


                val navColor = ContextCompat.getColor(context, R.color.calendar_day_text_light)
                leftArrowMask.setColorFilter(navColor, android.graphics.PorterDuff.Mode.SRC_ATOP)
                rightArrowMask.setColorFilter(navColor, android.graphics.PorterDuff.Mode.SRC_ATOP)
            }
        },
        update = { view ->
            view.removeDecorators()
            view.addDecorator(
                ColorDayDecorator(
                    shouldColor = { true },
                    color = textColor
                )
            )
            view.addDecorator(
                TodayCircleDecorator(backgroundColor = backgroundColor)
            )

            view.setOnDateChangedListener { _, date, selected ->
                if (selected) {
                    selectedDay = date
                    onDateSelectedRef.value(date)
                }
            }

            view.invalidateDecorators()
        }
    )
}