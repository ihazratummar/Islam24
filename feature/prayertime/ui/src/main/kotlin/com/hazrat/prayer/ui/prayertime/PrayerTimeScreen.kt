package com.hazrat.prayer.ui.prayertime


import android.os.Build
import android.widget.Space
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hazrat.model.MinimalPrayerData
import com.hazrat.permission.PermissionTypes
import com.hazrat.permission.isPermissionGranted
import com.hazrat.permission.rememberPermissionRequester
import com.hazrat.prayer.ui.component.NotificationSettingCard
import com.hazrat.prayer.ui.component.PrayerProgressCard
import com.hazrat.ui.R
import com.hazrat.ui.common.IconWithBackground
import com.hazrat.ui.common.PulsingLiveDot
import com.hazrat.ui.common.rememberPrayerState
import com.hazrat.ui.theme.MutedTextColor
import com.hazrat.ui.theme.PrayerLocationColor
import com.hazrat.ui.theme.PrayerScreenGradient
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.DateUtil.dateLongToString
import com.hazrat.utils.openAppSettings

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PrayerTimeScreen(
    event: (PrayerEvent) -> Unit,
    prayerTimeUiState: PrayerTimeUiState,
    onPrayerSettingClick: () -> Unit
) {

    val prayerTime = prayerTimeUiState.prayerTimes
    val prayerState = rememberPrayerState(prayerTimes = prayerTime ?: MinimalPrayerData())
    val isNow = prayerState.isNow

    var cardBottom by remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.prayer_times),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
        }
    ) { paddingValue ->

        val pullToRefreshState = rememberPullToRefreshState()
        val context = LocalContext.current
        val requestLocationPermission =
            rememberPermissionRequester(
                permission = PermissionTypes.LOCATION,
                onGranted = {
                    event(PrayerEvent.RefreshPrayer)
                },
                onDenied = {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                },
                onPermissionDenied = {
                    openAppSettings(context = context)
                }
            )
        PullToRefreshBox(
            state = pullToRefreshState,
            onRefresh = {
                if (isPermissionGranted(context, PermissionTypes.LOCATION)) {
                    event(PrayerEvent.RefreshPrayer)
                } else {
                    requestLocationPermission()
                }
            },
            isRefreshing = prayerTimeUiState.isRefreshing
        ) {

            if (prayerTimeUiState.prayerTimes != null) {
                CircularProgressIndicator()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardBottom)
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
                                prayerTime?.timeStamp?.times(1000) ?: 0L,
                                format = "EEEE, dd MM yyyy"
                            ) + " • ${prayerTime?.hijriDay} ${prayerTime?.hijriMonthEn} ${prayerTime?.hijriYear} AH",
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

                            if (isNow){
                                PulsingLiveDot()
                            }else{
                                Text(
                                    text = "NEXT PRAYER",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = PrayerLocationColor
                                    )
                                )
                            }

                            Column(
                                modifier = Modifier.padding(vertical = dimens.space8).fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(dimens.space4),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val prayerName = if (isNow) prayerState.currentPrayer?.name.toString()
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
                            Text(
                                text = prayerState.countdownText,
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
                            .onGloballyPositioned {coordinates ->
                            val bounds = coordinates.boundsInWindow()
                            val centerYPx = bounds.top + (bounds.height / 3f)
                            cardBottom = with(density){
                                centerYPx.toDp()
                            }
                        },
                        todayTimeStamp = prayerTime?.timeStamp?.times(1000)?:0L
                    )
                }
                item {
                    Spacer(Modifier.height(dimens.space2))
                }
                item {
                    NotificationSettingCard()
                }
            }
        }
    }
}