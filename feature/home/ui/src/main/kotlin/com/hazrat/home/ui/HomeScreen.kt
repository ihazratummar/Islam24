package com.hazrat.home.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.home.ui.component.DashboardTile
import com.hazrat.home.ui.component.HomePageNavIcons
import com.hazrat.home.ui.component.HomeScreenEventCard
import com.hazrat.home.ui.component.HomeScreenStreakCard
import com.hazrat.home.ui.component.HomeTopCard
import com.hazrat.home.ui.component.QuickAccessMenu
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.EventType
import com.hazrat.model.PrayerStreakInfo
import com.hazrat.model.locationmodel.LocationName
import com.hazrat.permission.PermissionRationaleDialog
import com.hazrat.permission.PermissionTypes
import com.hazrat.permission.isPermissionGranted
import com.hazrat.permission.rememberPermissionRequester
import com.hazrat.ui.R
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil
import com.hazrat.utils.IslamicCalendarUtils

@SuppressLint("RememberReturnType")
@Composable
fun HomeScreen(
    navigateToPrayerTime: () -> Unit,
    locationName: LocationName,
    onWidgetClick: (HomePageNavIcons) -> Unit,
    homeState: HomeState,
    refreshLocation: () -> Unit,
    dailyPrayerStatus: DailyPrayerStatus?
) {
    val context = LocalContext.current
    var showLocationRationale by remember { mutableStateOf(false) }

    val requestLocationPermission = rememberPermissionRequester(
        permission = PermissionTypes.LOCATION,
        onGranted = { refreshLocation() }
    )

    if (showLocationRationale) {
        PermissionRationaleDialog(
            title = "Location Required",
            message = "Allow Islam24 to access your location to show accurate prayer times and your current city.",
            onConfirm = {
                showLocationRationale = false
                requestLocationPermission()
            },
            onDismiss = { showLocationRationale = false }
        )
    }

    // Automatically check on first composition
    remember {
        if (!isPermissionGranted(context, PermissionTypes.LOCATION)) {
            showLocationRationale = true
        }
        Unit
    }

    Scaffold(
        contentWindowInsets = WindowInsets(top = dimens.space20)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimens.space20),
            verticalArrangement = Arrangement.spacedBy(dimens.space16)
        ) {
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
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.W700
                            )
                        )
                        Text(
                            text = stringResource(R.string.your_daily_companion),
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
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(dimens.cornerMd)
                            )
                    ) {
                        if (homeState.isLocationLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(dimens.iconSm))
                        } else {
                            Text(
                                text = locationName.address ?: "Location",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.surfaceTint
                                ),
                                modifier = Modifier.padding(
                                    horizontal = dimens.space8,
                                    vertical = dimens.space4
                                )
                            )
                        }
                    }
                }
            }

            item {
                HomeTopCard(
                    prayerData = homeState.prayerData,
                    onLogPrayerClick = navigateToPrayerTime
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimens.space12)
                ) {
                    val hijriDate = IslamicCalendarUtils.getCurrentHijriDateInfo()
                    DashboardTile(
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.hijri_date_label),
                        mainText = "${hijriDate.day} ${hijriDate.monthName}",
                        bottomLabel = "${hijriDate.year} AH"
                    )
                    homeState.upcomingIslamicEvent?.let { event ->
                        DashboardTile(
                            modifier = Modifier.weight(1f),
                            label = event.eventType.toString(),
                            mainText = "${event.daysRemaining} Days",
                            bottomLabel = stringResource(
                                R.string.until_event,
                                event.hijriMonth,
                                event.hijriYear
                            )
                        )
                    }
                }
            }
            item {
                HomeScreenStreakCard(
                    dailyPrayerStatus = dailyPrayerStatus
                )
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(dimens.space8)
                ) {
                    Text(
                        text = stringResource(R.string.quick_access),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    QuickAccessMenu(
                        onClick = { onWidgetClick(it) }
                    )
                }
            }

            if (homeState.islamicEventsInfoModel.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.upcoming_events),
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
                            eventType = EventType.JUMMA
                        )
                    }
                }
                items(homeState.islamicEventsInfoModel.take(2)) {model ->
                    model?.let {
                        HomeScreenEventCard(
                            eventName = it.holidays,
                            eventDate = "${it.hijriDate} AH • ${
                                DateUtil.dateLongToString(
                                    dateLong = (it.timestamp?.times(1000)) ?: 0L,
                                    format = "EEEE, dd MMMM yyyy"
                                )
                            }",
                            eventType = it.type,
                        )
                    }
                }
            }
        }
    }
}
