package com.hazrat.islam24.core.presentation.calendar

/**
 * @author Hazrat Ummar Shaikh
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.hazrat.islam24.R
import com.hazrat.islam24.core.presentation.calendar.component.WeekDay
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.DateUtil.getCurrentDateWithMonthName
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
) {
    val date = getCurrentDateWithMonthName()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date,
            color = MaterialTheme.colorScheme.onBackground
        )
        var currentMonth by remember { mutableStateOf(YearMonth.now()) }
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }

        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                MonthNavigation(
                    currentMonth = currentMonth,
                    onPrevMonth = {
                        currentMonth = currentMonth.minusMonths(1)
                        selectedDate =
                            if (currentMonth == YearMonth.now()) LocalDate.now() else currentMonth.atDay(
                                1
                            )
                    },
                    onNextMonth = {
                        currentMonth = currentMonth.plusMonths(1)
                        selectedDate =
                            if (currentMonth == YearMonth.now()) LocalDate.now() else currentMonth.atDay(
                                1
                            )
                    }
                )
                CalendarViewWithWeeksAndDays(
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                    },
                    onSwipeLeft = {
                        currentMonth = currentMonth.minusMonths(1)
                        selectedDate =
                            if (currentMonth == YearMonth.now()) LocalDate.now() else currentMonth.atDay(
                                1
                            )
                    },
                    onSwipeRight = {
                        currentMonth = currentMonth.plusMonths(1)
                        selectedDate =
                            if (currentMonth == YearMonth.now()) LocalDate.now() else currentMonth.atDay(
                                1
                            )
                    }
                )
            }
        }
    }


}

@Composable
fun CalendarViewWithWeeksAndDays(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    Column(
        modifier = Modifier
    ) {
        WeekDaysHeader()
        Spacer(modifier = Modifier.height(dimens.size4))
        DaysGrid(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected,
            onSwipeRight = {
                onSwipeRight()
            },
            onSwipeLeft = {
                onSwipeLeft()
            }
        )
    }
}

@Composable
fun DaysGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val totalDays = lastDayOfMonth.dayOfMonth

    Column(
        modifier = Modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { _, dragAmount ->
                when {
                    dragAmount > 0 -> onSwipeRight()
                    dragAmount < 0 -> onSwipeLeft()
                }
            }
        }
    ) {
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
                        val date = currentMonth.atDay(day)
                        val isSelected = date == selectedDate
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(dimens.size4)
                                .background(
                                    color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
                                    shape = RoundedCornerShape(dimens.size10)
                                )
                                .clickable { onDateSelected(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Text(
                                    text = day.toString(),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        day++
                    }
                }
            }
        }
    }
}

@Composable
fun WeekDaysHeader() {
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
fun MonthNavigation(
    currentMonth: YearMonth,
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
            text = currentMonth.month.getDisplayName(
                TextStyle.FULL,
                Locale.getDefault()
            ) + " " + currentMonth.year,
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