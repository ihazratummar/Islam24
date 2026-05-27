package com.hazrat.prayer.ui.prayertime


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.permission.PermissionRationaleDialog
import com.hazrat.permission.PermissionTypes
import com.hazrat.permission.isPermissionGranted
import com.hazrat.permission.rememberPermissionRequester
import com.hazrat.prayer.ui.component.NotificationSettingCard
import com.hazrat.prayer.ui.component.PrayerProgressCard
import com.hazrat.prayer.ui.component.PrayerTimeCard
import com.hazrat.prayer.ui.component.PrayerTimeData
import com.hazrat.prayer.ui.notification.NotificationState
import com.hazrat.ui.R
import com.hazrat.ui.common.IconWithBackground
import com.hazrat.ui.common.PrayerType
import com.hazrat.ui.common.PulsingLiveDot
import com.hazrat.ui.common.TopAppBarTitle
import com.hazrat.ui.common.rememberPrayerState
import com.hazrat.ui.theme.MutedTextColor
import com.hazrat.ui.theme.PrayerLocationColor
import com.hazrat.ui.theme.PrayerScreenGradient
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil.dateLongToString

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PrayerTimeScreen(
    event: (PrayerEvent) -> Unit,
    prayerTimeUiState: PrayerTimeUiState,
    onPrayerSettingClick: () -> Unit,
    dailyPrayerStatus: DailyPrayerStatus?,
    notificationState: NotificationState
) {
    // ── Guard: wait until pages are loaded ──────────────────────────────────
    val pages = prayerTimeUiState.pages
    val initialIndex = prayerTimeUiState.selectedIndex.coerceAtLeast(0)
    if (pages.isEmpty() || initialIndex == -1) return

    val density = LocalDensity.current
    val context = LocalContext.current

    // ── Pager setup ─────────────────────────────────────────────────────────
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = {pages.size}
    )

    // Notify ViewModel when page settles (not on every drag frame)
    LaunchedEffect(pagerState.settledPage) {
        event(PrayerEvent.OnPageChanged(pagerState.settledPage))
    }


    // ── Permission state (unchanged from your code) ──────────────────────────

    var showLocationRationale by remember { mutableStateOf(false) }
    var showNotificationRationale by remember { mutableStateOf(false) }
    var pendingNotificationPrayer by remember { mutableStateOf<PrayerType?>(null) }

    val requestLocationPermission = rememberPermissionRequester(
        permission = PermissionTypes.LOCATION,
        onGranted = { event(PrayerEvent.RefreshPrayer) },
        onDenied = { Toast.makeText(context, "Location permission is needed for prayer times", Toast.LENGTH_SHORT).show() }
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
        onDenied = { Toast.makeText(context, "Notification permission is needed for Prayer Notification.", Toast.LENGTH_SHORT).show() }
    )

    if (showLocationRationale) {
        PermissionRationaleDialog(
            title = "Location Access Required",
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
            title = "Notification Access",
            message = "Enable notifications to receive timely Azan alerts for each prayer.",
            onConfirm = {
                showNotificationRationale = false
                requestNotificationPermission()
            },
            onDismiss = { showNotificationRationale = false }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    TopAppBarTitle(R.string.prayer_times)
                },
                actions = {
                    IconWithBackground(
                        icon = R.drawable.share,
                        onClick = { event(PrayerEvent.SharePrayer) }
                    )
                    IconWithBackground(
                        icon = R.drawable.settings,
                        onClick = onPrayerSettingClick
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
            // ── Fetching-next-year progress bar ──────────────────────
            AnimatedVisibility(visible = prayerTimeUiState.isFetchingNextYear) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color    = MaterialTheme.colorScheme.primary
                )
            }
        },
        contentWindowInsets = WindowInsets()
    ) { paddingValue ->

        val pullToRefreshState = rememberPullToRefreshState()
        
        PullToRefreshBox(
            state = pullToRefreshState,
            onRefresh = {
                if (isPermissionGranted(context, PermissionTypes.LOCATION)) {
                    event(PrayerEvent.RefreshPrayer)
                }else{
                    showLocationRationale = true
                }
            },
            isRefreshing = prayerTimeUiState.isRefreshing
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1,
                key = {pages[it].timeStamp}
            ) {pageIndex ->
                val prayerTime = pages[pageIndex]
                val prayerState = rememberPrayerState(prayerTimes = prayerTime)
                val isNow = prayerState.isNow
                // ── Per-page cardBottom — no cross-page interference ────────────
                var rawCardBottom by remember { mutableStateOf(0.dp) }

                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(rawCardBottom)
                            .background(
                                brush = Brush.linearGradient(
                                    PrayerScreenGradient
                                )
                            )
                    )
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValue)
                            .padding(horizontal = dimens.space20),
                        verticalArrangement = Arrangement.spacedBy(dimens.space16)
                    ) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(dimens.space4),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                prayerTimeUiState.locationNane?.let { name ->
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = PrayerLocationColor
                                        )
                                    )
                                }
                                Text(
                                    text = dateLongToString(
                                        prayerTime.timeStamp.times(1000),
                                        format = "EEEE, dd MM yyyy"
                                    ) + " • ${prayerTime.hijriDay} ${prayerTime.hijriMonthEn} ${prayerTime.hijriYear} AH",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        MutedTextColor
                                    )
                                )
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(0.1f)),
                                shape = RoundedCornerShape(dimens.cornerLg)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = dimens.space16)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {

                                    if (isNow) {
                                        PulsingLiveDot()
                                    } else {
                                        Text(
                                            text = "NEXT PRAYER",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = PrayerLocationColor
                                            )
                                        )
                                    }

                                    Column(
                                        modifier = Modifier
                                            .padding(vertical = dimens.space8)
                                            .fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(dimens.space4),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        val prayerName =
                                            if (isNow) prayerState.currentPrayer?.name.toString()
                                            else prayerState.nextPrayer?.name.toString()

                                        Text(
                                            text = prayerName,
                                            style = MaterialTheme.typography.displaySmall.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        val prayerTime =
                                            if (isNow) "Started At ${dateLongToString(prayerState.currentPrayerTime)}"
                                            else "at ${dateLongToString(prayerState.nextPrayerTimeMillis)}"
                                        Text(
                                            text = prayerTime,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MutedTextColor
                                            )
                                        )
                                    }
                                    val text = if (isNow) "NOW" else prayerState.countdownText
                                    Text(
                                        text =text,
                                        style = MaterialTheme.typography.displaySmall.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                        item {
                            PrayerProgressCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        val bounds = coordinates.boundsInWindow()
                                        val centerYPx = bounds.top + (bounds.height / 3f)
                                        rawCardBottom = with(density) {
                                            centerYPx.toDp()
                                        }
                                    },
                                todayTimeStamp = prayerTime.timeStamp.times(1000),
                                prayerCompletePercent = dailyPrayerStatus?.completionPercentage ?: 0,
                                completionRatio = dailyPrayerStatus?.completionRatio ?: 0f,
                                completePrayerCount = dailyPrayerStatus?.loggedPrayers?.size ?: 0
                            )
                        }
                        item {
                            Spacer(Modifier.height(dimens.space2))
                        }
                        item {
                            // [PRAYERTIME][TODO][HIGH] Add Notification settings screen
                            NotificationSettingCard(
                                totalNotificationOn = notificationState.notificationCount()
                            )
                        }

                        val listOfPrayerTIme = listOf(
                            PrayerTimeData(
                                prayerType = PrayerType.FAJR,
                                prayerTime = prayerTime.fajrTime
                            ),
                            PrayerTimeData(
                                prayerType = PrayerType.SUNRISE,
                                prayerTime = prayerTime.sunriseTime
                            ),
                            PrayerTimeData(
                                prayerType = PrayerType.DHUHR,
                                prayerTime = prayerTime.dhuhrTime
                            ),
                            PrayerTimeData(
                                prayerType = PrayerType.ASR,
                                prayerTime = prayerTime.asrTime
                            ),
                            PrayerTimeData(
                                prayerType = PrayerType.MAGHRIB,
                                prayerTime = prayerTime.maghribTime
                            ),
                            PrayerTimeData(
                                prayerType = PrayerType.ISHA,
                                prayerTime = prayerTime.ishaTime
                            ),
                        )

                        itemsIndexed(listOfPrayerTIme) { index, data ->
                            PrayerTimeCard(
                                prayerType = data.prayerType,
                                prayerTime = data.prayerTime,
                                onLogPrayerClick = { prayerType ->
                                    event(
                                        PrayerEvent.LogPrayer(
                                            date = data.prayerTime,
                                            prayer = prayerType.prayer
                                        )
                                    )
                                },
                                isLogged = dailyPrayerStatus?.isLogged(prayer = data.prayerType.prayer)
                                    ?: false,
                                onNotificationClick = {
                                    if (isPermissionGranted(context = context, permission = PermissionTypes.NOTIFICATION )){
                                        if (notificationState.isEnable(prayer = data.prayerType)) {
                                            event(
                                                PrayerEvent.PrayerNotificationToggle(
                                                    prayer = data.prayerType.prayer,
                                                    enabled = false
                                                )
                                            )
                                        } else {
                                            event(
                                                PrayerEvent.PrayerNotificationToggle(
                                                    prayer = data.prayerType.prayer,
                                                    enabled = true,
                                                    prayerTIme = data.prayerTime
                                                )
                                            )
                                        }
                                    }else{
                                        pendingNotificationPrayer = data.prayerType
                                        showNotificationRationale = true
                                    }
                                },
                                isNotificationEnabled = notificationState.isEnable(prayer = data.prayerType)
                            )
                        }
                    }
                }

            }

        }
    }
}