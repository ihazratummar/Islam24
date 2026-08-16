package com.hazrat.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.home.ui.component.CleanQuickAccessGrid
import com.hazrat.home.ui.component.DailyDuaCard
import com.hazrat.home.ui.component.DailyVerseCard
import com.hazrat.home.ui.component.HomePageNavIcons
import com.hazrat.home.ui.component.HomeRecentsSection
import com.hazrat.home.ui.component.HomeScreenEventCard
import com.hazrat.home.ui.component.NextPrayerHeroCard
import com.hazrat.home.ui.component.PrayerTimelineCard
import com.hazrat.home.ui.component.StreakAndRamadanRow
import com.hazrat.home.ui.component.WeeklyPrayerConsistencyCard
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.permission.PermissionRationaleDialog
import com.hazrat.permission.PermissionTypes
import com.hazrat.permission.isPermissionGranted
import com.hazrat.permission.rememberPermissionRequester
import androidx.compose.foundation.lazy.items
import com.hazrat.ui.R
import com.hazrat.ui.common.IslamicGridBackground
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil
import com.hazrat.utils.IslamicCalendarUtils

@SuppressLint("RememberReturnType")
@Composable
fun HomeScreen(
    navigateToPrayerTime: () -> Unit,
    onWidgetClick: (HomePageNavIcons) -> Unit,
    onRecentReadClick: (com.hazrat.model.quran.RecentReadSurah) -> Unit,
    homeState: HomeState,
    refreshLocation: () -> Unit,
    dailyPrayerStatus: DailyPrayerStatus?,
    onSupportClick: (() -> Unit)? = null,
    onDailyVerseClick: (DailyVerseData) -> Unit = {}
) {
    val context = LocalContext.current
    var showLocationRationale by remember { mutableStateOf(false) }

    val requestLocationPermission = rememberPermissionRequester(
        permission = PermissionTypes.LOCATION,
        onGranted = { refreshLocation() }
    )

    if (showLocationRationale) {
        PermissionRationaleDialog(
            title = stringResource(R.string.error_location_required_title),
            message = "Allow Islam24 to access your location to show accurate prayer times and your current city.",
            onConfirm = {
                showLocationRationale = false
                requestLocationPermission()
            },
            onDismiss = { showLocationRationale = false }
        )
    }

    remember {
        if (!isPermissionGranted(context, PermissionTypes.LOCATION)) {
            showLocationRationale = true
        }
        Unit
    }

    val hijriDate = IslamicCalendarUtils.getCurrentHijriDateInfo()
    val hijriPillStr = "${hijriDate.day} ${hijriDate.monthName}"

    Scaffold(
        contentWindowInsets = WindowInsets(top = dimens.space20),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        IslamicGridBackground(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = dimens.space20),
                verticalArrangement = Arrangement.spacedBy(dimens.space20)
            ) {
                // 1. Top Header Row: Logo (Color.Unspecified), Title, Subtitle, Hijri Date Badge
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = dimens.space16),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.space8)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.splash_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(dimens.iconXl)
                                .background(
                                    color = com.hazrat.ui.theme.customColors.logoBackground,
                                    shape = RoundedCornerShape(dimens.cornerLg)
                                ),
                            tint = Color.Unspecified
                        )

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.home_app_name),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.W700
                                )
                            )
                            Text(
                                text = stringResource(R.string.home_app_tagline),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(dimens.cornerMd)
                                )
                                .padding(
                                    horizontal = dimens.space12,
                                    vertical = dimens.space4
                                )
                        ) {
                            if (homeState.isLocationLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(dimens.iconSm))
                            } else {
                                Text(
                                    text = hijriPillStr,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }

                // 2. Next Prayer Hero Card (Screenshot 1 Top Red Box)
                item {
                    NextPrayerHeroCard(
                        prayerData = homeState.prayerData,
                        onViewScheduleClick = navigateToPrayerTime
                    )
                }

                // 3. 5-Prayer Timeline Card (Screenshot 1 Top Red Box)
                item {
                    PrayerTimelineCard(
                        prayerData = homeState.prayerData,
                        onFullViewClick = navigateToPrayerTime
                    )
                }

                // 4. Streak & Ramadan Side-by-Side Card Row (Screenshot 1)
                item {
                    StreakAndRamadanRow(
                        dailyPrayerStatus = dailyPrayerStatus,
                        upcomingEvent = homeState.upcomingIslamicEvent
                    )
                }

                // 5. Clean Quick Access 2x3 Grid (Screenshot 2 Red Box)
                item {
                    CleanQuickAccessGrid(
                        onClick = { onWidgetClick(it) }
                    )
                }

                // 5.5. Real-Time Recents Horizontal Slider (if recent reads exist)
                if (homeState.recentReads.isNotEmpty()) {
                    item {
                        HomeRecentsSection(
                            recentReads = homeState.recentReads,
                            onRecentReadClick = onRecentReadClick
                        )
                    }
                }

                // 6. Weekly Prayer Consistency Card (Screenshot 2 Red Box)
                item {
                    WeeklyPrayerConsistencyCard(
                        weeklyStats = homeState.weeklyPrayerStats,
                        onDetailsClick = navigateToPrayerTime
                    )
                }

                // 7. Daily Verse Card (Screenshot 1 Bottom Red Box)
                item {
                    DailyVerseCard(
                        dailyVerse = homeState.dailyVerse,
                        onVerseClick = { onDailyVerseClick(homeState.dailyVerse) }
                    )
                }

                // 8. Daily Dua Card
                item {
                    DailyDuaCard(
                        dailyDua = homeState.dailyDua,
                        onAllDuasClick = { onWidgetClick(HomePageNavIcons.Dua) }
                    )
                }

                // 8.5. Support Islam 24 Voluntary Banner Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(dimens.cornerLg))
                            .border(
                                width = dimens.divider,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(dimens.cornerLg)
                            )
                            .clickable { onSupportClick?.invoke() },
                        shape = RoundedCornerShape(dimens.cornerLg),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimens.space16),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(dimens.iconXl)
                                    .clip(RoundedCornerShape(dimens.cornerMd))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.heart),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(dimens.iconMd)
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(dimens.space2)
                            ) {
                                Text(
                                    text = "Islam 24 is free & ad-free forever",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "Support our mission if you find it valuable",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = com.hazrat.ui.theme.customColors.secondaryText
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(dimens.iconLg)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.chevron_right),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(dimens.iconSm)
                                )
                            }
                        }
                    }
                }

                // 9. Upcoming Islamic Events
                if (homeState.islamicEventsInfoModel.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.home_upcoming_event_label),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(bottom = dimens.space8)
                        )
                        homeState.fridayTime?.let { time ->
                            HomeScreenEventCard(
                                eventName = stringResource(R.string.jummah_prayer),
                                eventDate = DateUtil.dateLongToString(
                                    dateLong = time,
                                    format = "EEEE, dd MMMM yyyy • hh:mm a"
                                ),
                                eventType = com.hazrat.model.EventType.JUMMA
                            )
                        }
                    }
                    items(homeState.islamicEventsInfoModel.take(2)) { model ->
                        model?.let {
                            HomeScreenEventCard(
                                eventName = it.holidays,
                                eventDate = "${it.hijriDate} AH • ${
                                    DateUtil.dateLongToString(
                                        dateLong = (it.timestamp?.times(1000)) ?: 0L,
                                        format = "EEEE, dd MMMM yyyy"
                                    )
                                }",
                                eventType = it.type
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(dimens.space24))
                }
            }
        }
    }
}
