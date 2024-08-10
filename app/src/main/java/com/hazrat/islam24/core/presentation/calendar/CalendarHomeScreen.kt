package com.hazrat.islam24.core.presentation.calendar

import android.icu.util.IslamicCalendar
import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.calendar.component.HijriMonth
import com.hazrat.islam24.core.presentation.calendar.component.WeekDay
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens

@Composable
fun CalendarHomeScreen(viewModel: MainViewModel = hiltViewModel()) {
    val hijriCalendar by viewModel.hijriCalendar.collectAsState()
    val calendar = hijriCalendar.firstOrNull()

    val hijriDay by viewModel.hijriDate.collectAsState()
    val hijriDayNew = hijriDay.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens.size10),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (calendar != null) {
            if (hijriDayNew != null) {
                Text(
                    text = "${hijriDayNew.day} ${hijriDayNew.monthEn} ${hijriDayNew.year}${calendar.hijriAbbreviated}",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }else{
            Text(text = "Loading")
        }
        HijriCalendar()
    }
}

@Composable
fun HijriCalendar() {
    val calendar by remember { mutableStateOf(IslamicCalendar()) }

    val month by remember { mutableIntStateOf(calendar.get(IslamicCalendar.MONTH )) }

    var currentMonthNumber by remember { mutableIntStateOf(calendar.get(IslamicCalendar.MONTH )) }
    var year by remember { mutableIntStateOf(calendar.get(IslamicCalendar.YEAR)) }
    var lastDayOfMonth by remember { mutableIntStateOf(calendar.getActualMaximum(IslamicCalendar.DAY_OF_MONTH )) }
    var firstDayOfWeek by remember { mutableIntStateOf(getFirstDayOfWeek(calendar)) }
    var totalDays by remember { mutableIntStateOf(lastDayOfMonth) }


    fun updateCalendar() {
        calendar.set(IslamicCalendar.YEAR, year)
        calendar.set(IslamicCalendar.MONTH, currentMonthNumber)
        lastDayOfMonth = calendar.getActualMaximum(IslamicCalendar.DAY_OF_MONTH)
        firstDayOfWeek = getFirstDayOfWeek(calendar)
        totalDays = lastDayOfMonth
    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        HijriMonthNavigation(
            currentMonth = currentMonthNumber,
            year = year,
            onPrevMonth = {
                currentMonthNumber = (currentMonthNumber - 1 + HijriMonth.entries.size) % HijriMonth.entries.size
                if (currentMonthNumber == HijriMonth.entries.size - 1) {
                    year--
                }
                updateCalendar()
            },
            onNextMonth = {
                currentMonthNumber = (currentMonthNumber + 1) % HijriMonth.entries.size
                if (currentMonthNumber == 0) {
                    year++
                }
                updateCalendar()
            }
        )
        HijriWeekDaysHeader()
        Spacer(modifier = Modifier.height(dimens.size4))
        DaysGrid(
            firstDayOfWeek = firstDayOfWeek,
            totalDays = totalDays
        )
    }


}

fun getFirstDayOfWeek(calendar: IslamicCalendar): Int {
    // Set the calendar to the first day of the current month
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
    totalDays: Int
) {
    Column(
        modifier = Modifier
    ) {
        val calendar by remember { mutableStateOf(IslamicCalendar()) }
        var day = 1
        for (week in 0..5) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                for (dayOfWeek in 0..6) {
                    if (week == 0 && dayOfWeek < firstDayOfWeek || day > totalDays) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(dimens.size4)
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(dimens.size10)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        day++
                    }
                }
            }
        }
    }
}

