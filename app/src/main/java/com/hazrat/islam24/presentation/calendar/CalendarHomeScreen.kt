
package com.hazrat.islam24.presentation.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hazrat.islam24.R
import com.hazrat.islam24.data.entity.HijriCalendarEntity
import com.hazrat.islam24.presentation.mainActivity.MainViewModel
import com.hazrat.islam24.ui.theme.dimens
import java.time.LocalDate

@Composable
fun CalendarHomeScreen(viewModel: MainViewModel = hiltViewModel()) {
    val hijriCalendar by viewModel.hijriCalendar.collectAsState()
    val calendar = hijriCalendar.firstOrNull()
    val startingDay = hijriCalendar.firstOrNull()?.gregorianWeekDayEn
    val weekNames = startingDay?.let { getWeekNames(it) } ?: listOf()
    // Calculate the index of the first day of the month in the week
    val firstDayOfMonthIndex = weekNames.indexOf(startingDay)
    val daysWithOffset = mutableListOf<HijriCalendarEntity?>()
    // Add empty days before the first day of the month
    repeat(firstDayOfMonthIndex) {
        daysWithOffset.add(null)
    }
    // Add the actual days of the month
    daysWithOffset.addAll(hijriCalendar)

    val selectedDays = remember { mutableStateOf<List<HijriCalendarEntity>>(emptyList()) }

    // Calculate the padding for the last row
    val lastWeek = daysWithOffset.takeLast(7).filterNotNull()
    val remainingDays = 7 - lastWeek.size

    // Add padding days at the end
    repeat(remainingDays) {
        daysWithOffset.add(null)
    }

    val paddedDays = daysWithOffset.chunked(7)

    val hijriDay by viewModel.hijriDate.collectAsState()
    val hijriDayNew = hijriDay.firstOrNull()
    if (hijriDayNew != null) {
        Log.d("HomeScreen", "${hijriDayNew.day}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimens.size10),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (calendar != null) {
            if (hijriDayNew != null) {
                Text(text = "${hijriDayNew.day} ${hijriDayNew.monthEn} ${hijriDayNew.year}${calendar.hijriAbbreviated}",
                    color = colorResource(id = R.color.text))
            }
        }
        WeekNamesRow(weekNames)
        // Display the calendar grid
        paddedDays.forEachIndexed { index, week ->
            WeekItem(
                week = week.filterNotNull(),
                selectedDays = selectedDays,
                onDaySelected = { day ->
                    selectedDays.value = listOf(day)
                },
                isLastRow = index == paddedDays.lastIndex // Determine if it's the last row
            )
        }
    }
}


fun getWeekNames(startingDay: String): List<String> {
    val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    val startIndex = days.indexOf(startingDay)
    val adjustedWeekNames = mutableListOf<String>()
    if (startIndex != -1) {
        for (i in 0 until 7) {
            adjustedWeekNames.add(days[(startIndex + i) % 7].take(3))
        }
    }
    return adjustedWeekNames
}

@Composable
fun WeekNamesRow(weekNames: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.size8),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        weekNames.forEach { weekName ->
            Text(
                text = weekName,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = colorResource(id = R.color.text)
            )
        }
    }
}

@Composable
fun WeekItem(
    week: List<HijriCalendarEntity>,
    selectedDays: MutableState<List<HijriCalendarEntity>>,
    onDaySelected: (HijriCalendarEntity) -> Unit,
    isLastRow: Boolean // New parameter to indicate if this is the last row
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isLastRow) Arrangement.Start else Arrangement.SpaceBetween // Conditional arrangement
    ) {
        week.forEach { day ->
            DayItem(
                day = day,
                isSelected = selectedDays.value.contains(day),
                onDaySelected = {
                    val updatedSelectedDays = if (selectedDays.value.contains(it)) {
                        selectedDays.value.filterNot { selectedDay -> selectedDay == it }
                    } else {
                        selectedDays.value + it
                    }
                    selectedDays.value = updatedSelectedDays
                    onDaySelected(it)
                }
            )
        }
    }
}

@Composable
fun DayItem(
    day: HijriCalendarEntity,
    isSelected: Boolean,
    onDaySelected: (HijriCalendarEntity) -> Unit
) {
    val currentDay = LocalDate.now().dayOfMonth
    val backgroundColor = if (isSelected && day.gregorianDay != currentDay) {
        Color.Cyan.copy(0.4f)
    } else {
        if (day.gregorianDay == currentDay) {
            Color.Cyan.copy(0.2f)
        } else {
            Color.Transparent
        }
    }
    Box(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.dimens.size10, vertical = MaterialTheme.dimens.size6)
            .width(MaterialTheme.dimens.size40)
            .clickable {
                onDaySelected(day)
            }
            .background(backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.hijriDay.toString(),
                color = colorResource(id = R.color.text),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = day.gregorianDay.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.primary)
            )
            if (currentDay == day.gregorianDay) {
                Box(
                    modifier = Modifier
                        .size(MaterialTheme.dimens.size6)
                        .background(Color.Cyan, shape = CircleShape)
                        .padding(bottom = MaterialTheme.dimens.size4)
                ) {

                }
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size5))
            }
        }
    }
}
