package com.hazrat.islam24.core.presentation.calendar

import android.icu.util.IslamicCalendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import com.hazrat.islam24.core.presentation.calendar.component.HijriMonth
import com.hazrat.islam24.core.presentation.calendar.component.WeekDay
import com.hazrat.islam24.ui.theme.dimens


@Composable
fun HijriCalendarHomeScreen(
    gregorianToHijriEntity: List<GregorianToHijriEntity>
) {

    val hijriDayNew = gregorianToHijriEntity.firstOrNull()
    val todayHijriDate = hijriDayNew?.let {
        Triple(it.year.toInt(), it.monthNumber, it.day)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens.size10),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hijriDayNew != null) {
            Text(
                text = "${hijriDayNew.day} ${hijriDayNew.monthEn} ${hijriDayNew.year}",
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            Text(text = "Loading...")
        }

        todayHijriDate?.let {
            HijriCalendar(todayHijriDate = it)
        } ?: Text("Loading Date...")
    }
}

@Composable
fun HijriCalendar(
    todayHijriDate: Triple<Int, Int, Int>
) {
    val (todayHijriYear, todayHijriMonth, todayHijriDay) = todayHijriDate
    val calendar = remember { IslamicCalendar() }
    var currentMonthNumber by remember { mutableStateOf(todayHijriMonth -1) } //Months are 0-indexed
    var year by remember { mutableStateOf(todayHijriYear) }
    var lastDayOfMonth by remember { mutableStateOf(0) }
    var firstDayOfWeek by remember { mutableStateOf(0) }
    var totalDays by remember { mutableStateOf(0) }


    fun updateCalendar() {
        calendar.set(IslamicCalendar.YEAR, year)
        calendar.set(IslamicCalendar.MONTH, currentMonthNumber)
        lastDayOfMonth = calendar.getActualMaximum(IslamicCalendar.DAY_OF_MONTH)
        firstDayOfWeek = getFirstDayOfWeek(calendar) //Your existing function
        totalDays = lastDayOfMonth
    }

    LaunchedEffect(todayHijriDate) {
        updateCalendar()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HijriMonthNavigation(
            currentMonth = currentMonthNumber,
            year = year,
            onPrevMonth = {
                currentMonthNumber = (currentMonthNumber - 1 + 12) % 12 //12 months
                if (currentMonthNumber == 11) year--
                updateCalendar()
            },
            onNextMonth = {
                currentMonthNumber = (currentMonthNumber + 1) % 12
                if (currentMonthNumber == 0) year++
                updateCalendar()
            }
        )
        HijriWeekDaysHeader()
        Spacer(modifier = Modifier.height(dimens.size4))
        DaysGrid(
            firstDayOfWeek = firstDayOfWeek,
            totalDays = totalDays,
            todayHijriDate = todayHijriDate,
            currentMonth = currentMonthNumber,
            year = year
        )
    }
}
fun getFirstDayOfWeek(calendar: IslamicCalendar): Int {

    val clonedCalendar = calendar.clone() as IslamicCalendar
    clonedCalendar.set(IslamicCalendar.DAY_OF_MONTH, 0)
    return clonedCalendar.get(IslamicCalendar.DAY_OF_WEEK) % 7
}


@Composable
fun HijriMonthNavigation(
    currentMonth: Int,
    year: Int,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimens.size10),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(id = R.drawable.previous),
            contentDescription = "Previous Month",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(dimens.size35)
                .clip(CircleShape)
                .padding(dimens.size4)
                .clickable {
                    onPrevMonth()
                }
        )
        Text(
            text = "${HijriMonth.entries[currentMonth % HijriMonth.entries.size]} $year",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Icon(
            painter = painterResource(id = R.drawable.next),
            contentDescription = "Previous Month",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(dimens.size35)
                .clip(CircleShape)
                .padding(dimens.size4)
                .clickable {
                    onNextMonth()
                }
        )
    }
}


@Composable
fun HijriWeekDaysHeader() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        WeekDay.entries.forEach { day ->
            Text(
                text = day.name,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
fun DaysGrid(
    firstDayOfWeek: Int,
    totalDays: Int,
    todayHijriDate: Triple<Int, Int, Int>,
    currentMonth: Int,
    year: Int
) {
    val (todayHijriYear, todayHijriMonth, todayHijriDay) = todayHijriDate
    val calendar = remember { IslamicCalendar() }
    calendar.set(IslamicCalendar.YEAR, year)
    calendar.set(IslamicCalendar.MONTH, currentMonth)


    Column(modifier = Modifier) {
        var day = 1
        for (week in 0..5) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (dayOfWeek in 0..6) {
                    if (week == 0 && dayOfWeek < firstDayOfWeek || day > totalDays) {
                        Box(modifier = Modifier.weight(1f))
                    } else {
                        val isToday = day == todayHijriDay && currentMonth == todayHijriMonth - 1 && year == todayHijriYear

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(dimens.size4)
                                .then(
                                    if (isToday) Modifier.background(
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    ) else Modifier
                                )
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(dimens.size10)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                style = MaterialTheme.typography.titleSmall,
                                color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                            )
                        }
                        day++
                    }
                }
            }
        }
    }
}
