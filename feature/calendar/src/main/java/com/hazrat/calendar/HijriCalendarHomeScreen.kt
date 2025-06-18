package com.hazrat.calendar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.eltohamy.materialhijricalendarview.CalendarDay
import com.github.eltohamy.materialhijricalendarview.MaterialHijriCalendarView

@Composable
fun HijriCalendarHomeScreen(
) {

    val textColorRes = when {
        else -> MaterialTheme.colorScheme.onBackground.toArgb()
    }
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainer.toArgb()
    val sundayColor = MaterialTheme.colorScheme.error.toArgb()
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            MaterialHijriCalendarView(context).apply {
                setSelectedDate(CalendarDay.today())
                addDecorator(
                    ColorDayDecorator(
                        shouldColor = { false },
                        color = textColorRes
                    )
                )
                setOnDateChangedListener { widget, date, selected ->
                    if (selected) {

                    }
                }
            }
        },
        update = { view ->
            view.invalidateDecorators()
            view.removeDecorators() // remove previously added
            view.addDecorator(
                ColorDayDecorator(
                    shouldColor = { true },
                    color = textColorRes
                )
            )
            view.addDecorator(
                TodayCircleDecorator(
                    backgroundColor = backgroundColor
                )
            )
        }
    )
}

