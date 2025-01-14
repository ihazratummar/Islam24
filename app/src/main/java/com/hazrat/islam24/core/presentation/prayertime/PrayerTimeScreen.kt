package com.hazrat.islam24.core.presentation.prayertime


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.core.presentation.prayertime.component.PrayerDateCard
import com.hazrat.islam24.core.presentation.prayertime.component.PrayerTimeCard
import com.hazrat.islam24.core.presentation.prayertime.component.PrayerTimeScreenAnimation
import com.hazrat.islam24.main.navigation.MainRoute
import com.hazrat.islam24.main.navigation.nvgraph.AsrSetting
import com.hazrat.islam24.main.navigation.nvgraph.DhuhrSetting
import com.hazrat.islam24.main.navigation.nvgraph.FajrSetting
import com.hazrat.islam24.main.navigation.nvgraph.IshaSetting
import com.hazrat.islam24.main.navigation.nvgraph.MaghribSetting
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.datastore.DataStorePreference
import com.hazrat.islam24.util.DateUtil.dateLongToString
import com.hazrat.islam24.util.DateUtil.getCountdownText
import com.hazrat.islam24.util.DateUtil.getCurrentDate
import com.hazrat.islam24.util.DateUtil.getCurrentDay
import com.hazrat.islam24.util.datastore.DataStore
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PrayerTimeScreen(
    navController: NavController,
    event: (PrayerEvent) -> Unit,
    prayerTimes: List<PrayerTimeEntity>
) {
    val methods = prayerTimes.firstOrNull()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val today = getCurrentDate()
        Log.d("Today", today)
        val todayPrayerIndex = prayerTimes.indexOfFirst { it.gregorianDate == today }
        val initialPage = if (todayPrayerIndex != -1) {
            todayPrayerIndex
        } else {
            0
        }
        if (prayerTimes.any { it.gregorianDate == today }) {
            PrayerTimeScreenAnimation(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimens.size250),
                prayerTimeEntity = prayerTimes[initialPage]

            )
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.prayer_times),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${methods?.methodName ?: ""} - ${methods?.methodFajrParam}°/${methods?.methodIshaParam}°",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { event(PrayerEvent.SharePrayer) },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground

                        )
                    }
                    IconButton(
                        onClick = { navController.navigate(MainRoute.PrayerSetting) },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.settings),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground

                        )
                    }
                },
                windowInsets = WindowInsets(top = dimens.size40),
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { paddingValue ->
        ShowData(
            modifier = Modifier.padding(paddingValue),
            navController = navController,
            prayerTimes = prayerTimes
        )
    }

}

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowData(
    modifier: Modifier = Modifier,
    navController: NavController,
    prayerTimes: List<PrayerTimeEntity>,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ViewPager(
            prayerTimes = prayerTimes,
            navController = navController,
        )
    }

}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ViewPager(
    prayerTimes: List<PrayerTimeEntity>,
    navController: NavController,
) {

    val currentGregorianDay = getCurrentDate()
    val prayerTimeIndex = prayerTimes.indexOfFirst {
        it.gregorianDate == currentGregorianDay
    }

    val pagerState = rememberPagerState(
        pageCount = { prayerTimes.size },
        initialPage = prayerTimeIndex  // Default to the first page or modify as needed
    )


    HorizontalPager(
        state = pagerState,
    ) { page ->
        PrayerTimesDay(
            prayerTimes[page], navController
        )
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun PrayerTimesDay(
    data: PrayerTimeEntity,
    navController: NavController
) {
    val gregorianDay = data.gregorianDay.toInt()

    val prayerDay = gregorianDay == getCurrentDay()

    // Define state for countdown times
    var fajrCountDown by remember { mutableStateOf(getCountdownText(data.fajrTime)) }
    var sunriseCountDown by remember { mutableStateOf(getCountdownText(data.sunriseTime)) }
    var dhuhrCountDown by remember { mutableStateOf(getCountdownText(data.dhuhrTime)) }
    var asrCountDown by remember { mutableStateOf(getCountdownText(data.asrTime)) }
    var maghribCountDown by remember { mutableStateOf(getCountdownText(data.maghribTime)) }
    var ishaCountDown by remember { mutableStateOf(getCountdownText(data.ishaTime)) }

    // Update countdown times periodically
    LaunchedEffect(Unit) {
        while (true) {
            fajrCountDown = getCountdownText(data.fajrTime)
            sunriseCountDown = getCountdownText(data.sunriseTime)
            dhuhrCountDown = getCountdownText(data.dhuhrTime)
            asrCountDown = getCountdownText(data.asrTime)
            maghribCountDown = getCountdownText(data.maghribTime)
            ishaCountDown = getCountdownText(data.ishaTime)
            delay(1000) // Update every second
        }
    }

    LazyColumn(
        modifier = Modifier.padding(top = dimens.size10)
    ) {
        item {
            PrayerDateCard(
                modifier = Modifier.clickable {
                    navController.navigate(MainRoute.CalendarScreen)
                },
                enDate = "${data.gregorianWeekday},${data.gregorianDay} ${data.gregorianMonthName} ",
                hrDate = "${data.hijriDay} ${data.hijriMonthEn} ${data.hijriYear} ${data.hijriab}"

            )
        }
        item {
            PrayerTime(
                data,
                prayerDay,
                fajrCountDown,
                dhuhrCountDown,
                asrCountDown,
                maghribCountDown,
                ishaCountDown,
                navController = navController
            )
        }
    }
}

@Composable
private fun PrayerTime(
    data: PrayerTimeEntity,
    prayerDay: Boolean,
    fajrCountDown: String,
    dhuhrCountDown: String,
    asrCountDown: String,
    maghribCountDown: String,
    ishaCountDown: String,
    navController: NavController
) {

    val currentTime = System.currentTimeMillis()
    val context = LocalContext.current
    val dataStorePreference = DataStorePreference(context = context)

    val isFajrTime = currentTime in (data.fajrTime + 1)..(data.sunriseTime - 300000)
    val isSunriseTime = currentTime in (data.sunriseTime + 1)..(data.sunriseTime)
    val isDhuhrTime = currentTime in (data.dhuhrTime + 1)..(data.dhuhrTime + 3600000)
    val isAsrTime = currentTime in (data.asrTime + 1)..(data.maghribTime - 600000)
    val isMaghribTime = currentTime in (data.maghribTime + 1)..(data.ishaTime - 600000)
    val isIshaTime = currentTime in (data.ishaTime + 1)..(data.firstThirdTime)

    val isNextFajrTime = currentTime in (data.lastThirdTime + 1)..(data.fajrTime)
    val isNextDhurTime = currentTime in (data.sunriseTime + 1)..(data.dhuhrTime)
    val isNextAsrTime = currentTime in (data.dhuhrTime + 1)..(data.asrTime)
    val isNextMaghribTime = currentTime in (data.asrTime + 1)..(data.maghribTime)
    val isNextIshaTime = currentTime in (data.maghribTime + 1)..(data.ishaTime)
    PrayerTimeCard(
        icon = R.drawable.fajr,
        text = stringResource(R.string.fajr),
        time = dateLongToString(data.fajrTime),
        countDownText = if (prayerDay && isNextFajrTime) fajrCountDown else "",
        isPrayerTime = isFajrTime,
        onClick = {
            navController.navigate(FajrSetting)
        },
        isNotification = dataStorePreference.getFajrNotification()

    )
    PrayerTimeCard(
        icon = R.drawable.sunrise,
        text = stringResource(R.string.sunrise),
        time = dateLongToString(data.sunriseTime),
        countDownText = "",
        isPrayerTime = isSunriseTime,
    )
    PrayerTimeCard(
        icon = R.drawable.dhuhr,
        text = stringResource(R.string.dhuhr),
        time = dateLongToString(data.dhuhrTime),
        countDownText = if (prayerDay && isNextDhurTime) dhuhrCountDown else "",
        isPrayerTime = isDhuhrTime,
        onClick = {
            navController.navigate(DhuhrSetting)
        },
        isNotification = dataStorePreference.getDhuhrNotification()
    )
    PrayerTimeCard(
        icon = R.drawable.asr,
        text = stringResource(R.string.asr),
        time = dateLongToString(data.asrTime),
        countDownText = if (prayerDay && isNextAsrTime) asrCountDown else "",
        isPrayerTime = isAsrTime,
        onClick = {
            navController.navigate(AsrSetting)
        },
        isNotification = dataStorePreference.getAsrNotification()
    )
    PrayerTimeCard(
        icon = R.drawable.maghrib,
        text = stringResource(R.string.maghrib),
        time = dateLongToString(data.maghribTime),
        countDownText = if (prayerDay && isNextMaghribTime) maghribCountDown else "",
        isPrayerTime = isMaghribTime,
        onClick = {
            navController.navigate(MaghribSetting)
        },
        isNotification = dataStorePreference.getMaghribNotification()
    )
    PrayerTimeCard(
        icon = R.drawable.isha,
        text = stringResource(R.string.isha_a),
        time = dateLongToString(data.ishaTime),
        countDownText = if (prayerDay && isNextIshaTime) ishaCountDown else "",
        isPrayerTime = isIshaTime,
        onClick = {
            navController.navigate(IshaSetting)
        },
        isNotification = dataStorePreference.getIshaNotification()
    )
}




