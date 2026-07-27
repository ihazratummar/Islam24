package com.hazrat.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hazrat.ui.R
import com.hazrat.ui.common.BasicTopBar
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onBackClick: () -> Unit = {}
) {
    val initialPage = 1000
    val pagerState = rememberPagerState(initialPage = initialPage, pageCount = { 2000 })
    val coroutineScope = rememberCoroutineScope()

    val todayCal = remember { com.github.msarhan.ummalqura.calendar.UmmalquraCalendar() }
    val todayDay = remember { todayCal.get(com.github.msarhan.ummalqura.calendar.UmmalquraCalendar.DAY_OF_MONTH) }
    var selectedDayNumber by remember { mutableStateOf<Int?>(todayDay) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val currentOffset = pagerState.currentPage - initialPage
    val activeMonthData = remember(pagerState.currentPage, selectedDayNumber) {
        HijriCalendarHelper.getMonthDataForOffset(currentOffset, selectedDayNumber)
    }

    val isCurrentMonth = (pagerState.currentPage == initialPage)
    val selectedDayObj = activeMonthData.days.find { it.hijriDay == selectedDayNumber }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            BasicTopBar(
                topBarTitle = "Islamic Calendar",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimens.space20)
        ) {
            // 1. Hero Card with Smooth Crossfade Animation
            AnimatedContent(
                targetState = activeMonthData,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
                label = "HeroCardAnimation"
            ) { targetMonthData ->
                CalendarHeroCard(
                    monthData = targetMonthData,
                    selectedDay = selectedDayObj
                )
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            // 2. Month Navigation Bar with "Today" button placed cleanly between Prev & Next
            MonthNavigationBar(
                isCurrentMonth = isCurrentMonth,
                onPreviousMonth = {
                    selectedDayNumber = null
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage - 1,
                            animationSpec = tween(350, easing = FastOutSlowInEasing)
                        )
                    }
                },
                onNextMonth = {
                    selectedDayNumber = null
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1,
                            animationSpec = tween(350, easing = FastOutSlowInEasing)
                        )
                    }
                },
                onTodayClick = {
                    selectedDayNumber = todayDay
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            initialPage,
                            animationSpec = tween(350, easing = FastOutSlowInEasing)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(dimens.space12))

            // 3. Weekday Header (Sun .. Sat)
            DaysOfWeekHeader()

            Spacer(modifier = Modifier.height(dimens.space8))

            // 4. Smooth Pager for Calendar Grid with Pre-warmed Viewports
            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 1,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val pageOffset = page - initialPage
                val monthData = remember(page, selectedDayNumber) {
                    HijriCalendarHelper.getMonthDataForOffset(
                        pageOffset,
                        if (page == pagerState.currentPage) selectedDayNumber else null
                    )
                }

                CalendarMonthGrid(
                    monthData = monthData,
                    onDayClick = { day ->
                        selectedDayNumber = day.hijriDay
                        showBottomSheet = true
                    }
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ) {
                DateDetailContent(
                    day = selectedDayObj,
                    monthData = activeMonthData
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CalendarHeroCard(
    monthData: HijriMonthData,
    selectedDay: HijriCalendarDay?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimens.cornerXl))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(dimens.space20)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "${monthData.gregorianMonthName} ${monthData.gregorianYear}",
                        style = MaterialTheme.typography.labelMedium,
                        color = customColors.secondaryText
                    )
                    Spacer(modifier = Modifier.height(dimens.space4))
                    Text(
                        text = monthData.hijriMonthName,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(dimens.space2))
                    Text(
                        text = "${monthData.hijriYear} AH",
                        style = MaterialTheme.typography.bodyMedium,
                        color = customColors.secondaryText
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimens.space16))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(dimens.space8),
                verticalArrangement = Arrangement.spacedBy(dimens.space8)
            ) {
                if (selectedDay != null) {
                    EventChip(text = "${selectedDay.hijriDay} ${monthData.hijriMonthName}")
                    if (selectedDay.eventTitle != null) {
                        EventChip(text = selectedDay.eventTitle)
                    } else if (selectedDay.isFriday) {
                        EventChip(text = "Jummah Prayer")
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthNavigationBar(
    isCurrentMonth: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onTodayClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.space4),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prev Button
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(dimens.cornerMd))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onPreviousMonth() }
                .padding(horizontal = dimens.space12, vertical = dimens.space8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.backicon),
                contentDescription = "Previous Month",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(dimens.iconXs)
            )
            Spacer(modifier = Modifier.width(dimens.space4))
            Text(
                text = "Prev",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Center Button: Today or Current Month Indicator
        if (!isCurrentMonth) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { onTodayClick() }
                    .padding(horizontal = dimens.space16, vertical = dimens.space8),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.space8)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                )
                Spacer(modifier = Modifier.width(dimens.space8))
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(horizontal = dimens.space16, vertical = dimens.space8),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Current Month",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = customColors.secondaryText
                )
            }
        }

        // Next Button
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(dimens.cornerMd))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onNextMonth() }
                .padding(horizontal = dimens.space12, vertical = dimens.space8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Next",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(dimens.space4))
            Icon(
                painter = painterResource(id = R.drawable.backicon),
                contentDescription = "Next Month",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(dimens.iconXs)
                    .graphicsLayer(rotationZ = 180f)
            )
        }
    }
}

@Composable
private fun EventChip(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(dimens.cornerFull))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = dimens.space12, vertical = dimens.space8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dimens.space8)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary)
        )
        Spacer(modifier = Modifier.width(dimens.space8))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DaysOfWeekHeader() {
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        daysOfWeek.forEach { dayName ->
            Text(
                text = dayName,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = customColors.secondaryText
            )
        }
    }
}

@Composable
private fun CalendarMonthGrid(
    monthData: HijriMonthData,
    onDayClick: (HijriCalendarDay) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(dimens.space8),
        verticalArrangement = Arrangement.spacedBy(dimens.space8),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Start Padding Empty Slots
        items(monthData.startPaddingCount) {
            Spacer(modifier = Modifier.aspectRatio(0.85f))
        }

        // Days
        items(monthData.days) { day ->
            CalendarDayCard(
                day = day,
                onClick = { onDayClick(day) }
            )
        }
    }
}

@Composable
private fun CalendarDayCard(
    day: HijriCalendarDay,
    onClick: () -> Unit
) {
    val cardBg = when {
        day.isSelected || day.isToday -> MaterialTheme.colorScheme.primary
        day.isFriday -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val cardBorder = when {
        day.isFriday && !day.isSelected && !day.isToday -> Modifier.border(
            dimens.divider,
            MaterialTheme.colorScheme.secondary,
            RoundedCornerShape(dimens.cornerLg)
        )
        else -> Modifier
    }

    val mainTextColor = when {
        day.isSelected || day.isToday -> MaterialTheme.colorScheme.onPrimary
        day.isFriday -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val subTextColor = when {
        day.isSelected || day.isToday -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        day.isFriday -> MaterialTheme.colorScheme.secondary
        else -> customColors.secondaryText
    }

    Box(
        modifier = Modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(dimens.cornerLg))
            .background(cardBg)
            .then(cardBorder)
            .clickable { onClick() }
            .padding(vertical = dimens.space4, horizontal = dimens.space4),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main Hijri Day Number
            Text(
                text = "${day.hijriDay}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = mainTextColor
            )

            // Sub Gregorian Day Number
            Text(
                text = "${day.gregorianDay}",
                style = MaterialTheme.typography.bodySmall,
                color = subTextColor
            )

            // Bottom Indicator (J for Friday, Dot for Selected/Event)
            Box(
                modifier = Modifier
                    .height(dimens.space12)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (day.isSelected || day.isToday) {
                    Box(
                        modifier = Modifier
                            .size(dimens.space4)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimary)
                    )
                } else if (day.isFriday) {
                    Text(
                        text = "J",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else if (day.hasEvent) {
                    Box(
                        modifier = Modifier
                            .size(dimens.space4)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                }
            }
        }
    }
}

@Composable
private fun DateDetailContent(
    day: HijriCalendarDay?,
    monthData: HijriMonthData
) {
    if (day == null) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimens.space24),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.calendar_selected_date),
            style = MaterialTheme.typography.titleMedium,
            color = customColors.secondaryText
        )

        Spacer(modifier = Modifier.height(dimens.space12))

        Text(
            text = "${day.hijriDay} ${monthData.hijriMonthName} ${monthData.hijriYear} AH",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(dimens.space8))

        Text(
            text = "Gregorian Day: ${day.gregorianDay} ${monthData.gregorianMonthName} ${monthData.gregorianYear}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(dimens.space24))

        if (day.eventTitle != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(dimens.cornerMd))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = dimens.space16, vertical = dimens.space12)
            ) {
                Box(
                    modifier = Modifier
                        .size(dimens.space8)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                )
                Spacer(modifier = Modifier.width(dimens.space12))
                Text(
                    text = day.eventTitle,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Text(
                text = stringResource(R.string.calendar_upcoming_events_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = customColors.secondaryText,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(dimens.space32))
    }
}
