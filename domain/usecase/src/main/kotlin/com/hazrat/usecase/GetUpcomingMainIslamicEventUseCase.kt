package com.hazrat.usecase

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.hazrat.model.IslamicEventType
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


/**
 * @author hazratummar
 * Created on 16/05/26
 */


data class UpcomingIslamicEvent(
    val eventType: IslamicEventType,
    val daysRemaining: Long,
    val hijriMonth: String,
    val hijriYear : Int
)

data class EventCandidate(
    val type: IslamicEventType,
    val calendar: UmmalquraCalendar
)

class GetUpcomingMainIslamicEventUseCase {

    operator fun invoke() : UpcomingIslamicEvent {

        val current = UmmalquraCalendar()

        val currentHijriYear = current.get(UmmalquraCalendar.YEAR)

        val events = listOf(
            createEvent(
                type = IslamicEventType.RAMADAN,
                year = currentHijriYear,
                month = 8,
                day = 1
            ),
            createEvent(
                type = IslamicEventType.EID_UL_FITR,
                year = currentHijriYear,
                month = 9,
                day = 1,
            ),
            createEvent(
                type = IslamicEventType.EID_UL_ADHA,
                year = currentHijriYear,
                month = 11,
                day = 10
            )
        ).map {
            if (it.calendar.timeInMillis < System.currentTimeMillis()){
                createEvent(
                    type = it.type,
                    year = currentHijriYear + 1,
                    month = it.calendar.get(UmmalquraCalendar.MONTH),
                    day = it.calendar.get(UmmalquraCalendar.DAY_OF_MONTH)
                )
            }else{
                it
            }
        }

        val nearestEvent  = events.minBy {
            it.calendar.timeInMillis
        }

        val remainingDays = (nearestEvent.calendar.timeInMillis - System.currentTimeMillis()) / TimeUnit.DAYS.toMillis(1)

        return UpcomingIslamicEvent(
            eventType = nearestEvent.type,
            daysRemaining = remainingDays,
            hijriMonth = nearestEvent.calendar.getDisplayName(
                UmmalquraCalendar.MONTH,
                Calendar.LONG,
                Locale.ENGLISH
            ) !!,
            hijriYear = nearestEvent.calendar.get(UmmalquraCalendar.YEAR)
        )
    }

    private fun createEvent(
        type: IslamicEventType,
        year: Int,
        month: Int,
        day: Int
    ) :EventCandidate {
        val calendar  = UmmalquraCalendar().apply {
            clear()
            set(year, month, day)
        }
        return EventCandidate(
            type = type,
            calendar = calendar
        )
    }
}



