package com.hazrat.prayer.ui.prayertime

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.permission.PermissionRationaleDialog
import com.hazrat.permission.PermissionTypes
import com.hazrat.permission.isPermissionGranted
import com.hazrat.permission.rememberPermissionRequester
import com.hazrat.prayer.ui.component.LocationDisplayCard
import com.hazrat.prayer.ui.component.NextPrayerHeroCard
import com.hazrat.prayer.ui.component.NotificationSettingCard
import com.hazrat.prayer.ui.component.PrayerDatePaginationHeader
import com.hazrat.prayer.ui.component.PrayerProgressCard
import com.hazrat.prayer.ui.component.PrayerTimeCard
import com.hazrat.prayer.ui.notification.NotificationState
import com.hazrat.ui.R
import com.hazrat.ui.common.IconWithBackground
import com.hazrat.ui.common.IslamicLoadingBar
import com.hazrat.ui.common.IslamicPullToRefresh
import com.hazrat.ui.common.PrayerType
import com.hazrat.ui.common.rememberPrayerState
import com.hazrat.ui.theme.customColors
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PrayerTimeScreen(
    event: (PrayerEvent) -> Unit,
    prayerTimeUiState: PrayerTimeUiState,
    onPrayerSettingClick: () -> Unit,
    onManageNotificationsClick: () -> Unit = {},
    dailyPrayerStatus: DailyPrayerStatus?,
    notificationState: NotificationState
) {
    val pages = prayerTimeUiState.pages
    val initialIndex = prayerTimeUiState.selectedIndex.coerceAtLeast(0)
    if (pages.isEmpty() || initialIndex == -1) return

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { pages.size }
    )

    LaunchedEffect(pagerState.settledPage) {
        event(PrayerEvent.OnPageChanged(pagerState.settledPage))
    }

    var showLocationRationale by remember { mutableStateOf(false) }
    var showNotificationRationale by remember { mutableStateOf(false) }
    var pendingNotificationPrayer by remember { mutableStateOf<PrayerType?>(null) }

    val locationPermissionErrorText = stringResource(R.string.error_location_permission)
    val notificationPermissionErrorText = stringResource(R.string.error_notification_permission)

    val requestLocationPermission = rememberPermissionRequester(
        permission = PermissionTypes.LOCATION,
        onGranted = { event(PrayerEvent.RefreshPrayer) },
        onDenied = { Toast.makeText(context, locationPermissionErrorText, Toast.LENGTH_SHORT).show() }
    )

    val requestNotificationPermission = rememberPermissionRequester(
        permission = PermissionTypes.NOTIFICATION,
        onGranted = {
            pendingNotificationPrayer?.let { data ->
                if (notificationState.isEnable(prayer = data)) {
                    event(PrayerEvent.PrayerNotificationToggle(prayer = data.prayer, enabled = false))
                } else {
                    event(PrayerEvent.PrayerNotificationToggle(prayer = data.prayer, enabled = true))
                }
            }
        },
        onDenied = { Toast.makeText(context, notificationPermissionErrorText, Toast.LENGTH_SHORT).show() }
    )

    if (showLocationRationale) {
        PermissionRationaleDialog(
            title = stringResource(R.string.error_location_access_required_title),
            message = "Islam24 needs your location to calculate accurate prayer times for your city.",
            onConfirm = {
                showLocationRationale = false
                requestLocationPermission()
            },
            onDismiss = { showLocationRationale = false }
        )
    }

    if (showNotificationRationale) {
        PermissionRationaleDialog(
            title = stringResource(R.string.error_notification_access_title),
            message = "Enable notifications to receive timely Azan alerts for each prayer.",
            onConfirm = {
                showNotificationRationale = false
                requestNotificationPermission()
            },
            onDismiss = { showNotificationRationale = false }
        )
    }

    com.hazrat.ui.common.IslamicGridBackground {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.prayer_times),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        prayerTimeUiState.locationNane?.let { location ->
                            Text(
                                text = location,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = customColors.secondaryText
                                )
                            )
                        }
                    }
                },
                actions = {
                    IconWithBackground(
                        icon = R.drawable.settings,
                        onClick = onPrayerSettingClick
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            AnimatedVisibility(visible = prayerTimeUiState.isFetchingNextYear) {
                IslamicLoadingBar(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        contentWindowInsets = WindowInsets()
    ) { paddingValues ->
            IslamicPullToRefresh(
            onRefresh = {
                if (isPermissionGranted(context, PermissionTypes.LOCATION)) {
                    event(PrayerEvent.RefreshPrayer)
                } else {
                    showLocationRationale = true
                }
            },
            isRefreshing = prayerTimeUiState.isRefreshing,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1,
                key = { pages[it].timeStamp }
            ) { pageIndex ->
                val prayerTimeData = pages[pageIndex]
                val prayerState = rememberPrayerState(prayerTimes = prayerTimeData)
                val isNow = prayerState.isNow

                val activePrayer = if (isNow) prayerState.currentPrayer else prayerState.nextPrayer
                val prayerName = activePrayer?.name?.lowercase()
                    ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: "Dhuhr"

                val scheduledTimeStr = if (isNow) {
                    "Started at " + DateUtil.dateLongToString(prayerState.currentPrayerTime, "hh:mm a")
                } else {
                    "at " + DateUtil.dateLongToString(prayerState.nextPrayerTimeMillis, "hh:mm a")
                }

                val nextPrayerName = prayerState.nextPrayer?.name?.lowercase()
                    ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

                val countdownText = prayerState.countdownText

                // Calculate Page Date Info
                val pageDate = Instant.ofEpochSecond(prayerTimeData.timeStamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                val todayDate = LocalDate.now()

                val dateTitleStr = when (pageDate) {
                    todayDate -> "Today"
                    todayDate.minusDays(1) -> "Yesterday"
                    todayDate.plusDays(1) -> "Tomorrow"
                    else -> DateUtil.dateLongToString(prayerTimeData.timeStamp * 1000, "EEEE, MMM dd")
                }

                val hijriSubtitleStr = "${prayerTimeData.hijriDay} ${prayerTimeData.hijriMonthEn} ${prayerTimeData.hijriYear} AH"

                val selectedChipIndex = when (pageDate) {
                    todayDate.minusDays(1) -> 0
                    todayDate -> 1
                    todayDate.plusDays(1) -> 2
                    else -> -1
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimens.space16),
                    verticalArrangement = Arrangement.spacedBy(dimens.space16)
                ) {
                    // Top Next Prayer Hero Countdown Card (ONLY shown for TODAY!)
                    if (pageDate == todayDate) {
                        item {
                            NextPrayerHeroCard(
                                prayerName = prayerName,
                                scheduledTimeStr = scheduledTimeStr,
                                countdownText = countdownText,
                                isNow = isNow,
                                nextPrayerName = nextPrayerName
                            )
                        }
                    }

                    // Date Pagination Header & Quick Filter Chips
                    item {
                        PrayerDatePaginationHeader(
                            dateTitleStr = dateTitleStr,
                            hijriSubtitleStr = hijriSubtitleStr,
                            onPrevClick = {
                                if (pagerState.currentPage > 0) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                }
                            },
                            onNextClick = {
                                if (pagerState.currentPage < pages.size - 1) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            },
                            selectedChipIndex = selectedChipIndex,
                            onChipSelect = { chipIdx ->
                                val targetDate = when (chipIdx) {
                                    0 -> todayDate.minusDays(1)
                                    1 -> todayDate
                                    else -> todayDate.plusDays(1)
                                }
                                val targetPageIdx = pages.indexOfFirst {
                                    Instant.ofEpochSecond(it.timeStamp)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate() == targetDate
                                }
                                if (targetPageIdx >= 0) {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(targetPageIdx)
                                    }
                                }
                            }
                        )
                    }

                    // Prayer Progress Card
                    item {
                        PrayerProgressCard(
                            todayTimeStamp = prayerTimeData.timeStamp * 1000,
                            prayerCompletePercent = dailyPrayerStatus?.completionPercentage ?: 0,
                            completionRatio = dailyPrayerStatus?.completionRatio ?: 0f,
                            completePrayerCount = dailyPrayerStatus?.loggedPrayers?.size ?: 0
                        )
                    }

                    // 6 Daily Prayer Cards (Fajr, Sunrise, Dhuhr, Asr, Maghrib, Isha)
                    val dailyPrayers = listOf(
                        PrayerType.FAJR to prayerTimeData.fajrTime,
                        PrayerType.SUNRISE to prayerTimeData.sunriseTime,
                        PrayerType.DHUHR to prayerTimeData.dhuhrTime,
                        PrayerType.ASR to prayerTimeData.asrTime,
                        PrayerType.MAGHRIB to prayerTimeData.maghribTime,
                        PrayerType.ISHA to prayerTimeData.ishaTime
                    )

                    itemsIndexed(dailyPrayers) { _, (type, time) ->
                        val isLogged = dailyPrayerStatus?.isLogged(prayer = type.prayer) ?: false
                        val isNext = (activePrayer == type)

                        PrayerTimeCard(
                            prayerType = type,
                            prayerTime = time,
                            isLogged = isLogged,
                            isNextPrayer = isNext,
                            isNotificationEnabled = notificationState.isEnable(prayer = type),
                            onNotificationClick = { pType ->
                                if (isPermissionGranted(context, PermissionTypes.NOTIFICATION)) {
                                    val enabled = !notificationState.isEnable(prayer = pType)
                                    event(
                                        PrayerEvent.PrayerNotificationToggle(
                                            prayer = pType.prayer,
                                            enabled = enabled,
                                            prayerTIme = time
                                        )
                                    )
                                } else {
                                    pendingNotificationPrayer = pType
                                    showNotificationRationale = true
                                }
                            },
                            onLogPrayerClick = { pType ->
                                event(
                                    PrayerEvent.LogPrayer(
                                        date = time,
                                        prayer = pType.prayer
                                    )
                                )
                            }
                        )
                    }

                    // Notifications Settings Card
                    item {
                        NotificationSettingCard(
                            totalNotificationOn = notificationState.notificationCount,
                            onClick = onManageNotificationsClick
                        )
                    }

                    // Automatic Location Card
                    item {
                        LocationDisplayCard(
                            locationName = prayerTimeUiState.locationNane ?: "Location"
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(dimens.space32))
                    }
                }
            }
        }
    }
}
}