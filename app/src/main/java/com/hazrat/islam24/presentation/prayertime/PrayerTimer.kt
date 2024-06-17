package com.hazrat.islam24.presentation.prayertime


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.data.entity.PrayerTimeEntity
import com.hazrat.islam24.presentation.mainActivity.MainViewModel
import com.hazrat.islam24.presentation.nvgraph.Route
import com.hazrat.islam24.presentation.prayertime.component.PrayerDateCard
import com.hazrat.islam24.presentation.prayertime.component.PrayerTimeCard
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.DateUtil

@Composable
fun PrayerTimer(viewModel: MainViewModel = hiltViewModel(), navController: NavController) {
    ShowData(viewModel, navController)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowData(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {
    val prayerTimes by viewModel.prayerTimes.collectAsState()

    Scaffold(
        modifier = Modifier,
        topBar = {

            TopAppBar(
                title = {
                    Text(text = "Prayer Times", color = colorResource(id = R.color.text))
                },
                actions = {
                    Icon(imageVector = Icons.Default.Settings,
                        contentDescription = "Setting Icon",
                        modifier = Modifier
                            .clickable {
                                navController.navigate(Route.UserSettings.route)
                            }
                            .padding(end = MaterialTheme.dimens.size20),
                        tint = colorResource(id = R.color.text)

                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Transparent)

            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            ViewPager(viewModel, prayerTimes = prayerTimes, navController)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPager(
    viewModel: MainViewModel,
    prayerTimes: List<PrayerTimeEntity>,
    navController: NavController
) {

    val todayDay = DateUtil.getCurrentDay()
    val todayIndex = prayerTimes.indexOfFirst { data ->
        val day = data.day
        todayDay == day
    }
    val initialPage =
        if (todayIndex != -1) todayIndex else todayDay - 1 // Use 0 as the default page if today's date is not found

    val pagerState = rememberPagerState(
        pageCount = { prayerTimes.size },
        initialPage = initialPage
    )

    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            // Do something with each page change, for example:
            // viewModel.sendPageSelectedEvent(page)
            Log.d("Page change", "Page changed to $page")
        }
    }

    HorizontalPager(
        state = pagerState,
    ) { page ->
        PrayerTimesDay(prayerTimes[page], navController)
    }
}


@Composable
fun PrayerTimesDay(data: PrayerTimeEntity, navController: NavController) {

    Column(
        modifier = Modifier.padding(top = MaterialTheme.dimens.size10)
    ) {
        PrayerDateCard(
            modifier = Modifier.clickable {
                navController.navigate(route = Route.CalendarScreen.route)
            },
            enDate = "${data.gregorianWeekday},${data.gregorianDay} ${data.gregorianMonthName} ",
            hrDate = "${data.hijriDay} ${data.hijriMonthEn} ${data.hijriYear} ${data.hijriab}"

        )
        PrayerTimeCard(
            icon = R.drawable.fajr,
            text = "Fajr",
            time = getTime(data.fajrTime),
            onClick = {}
        )
        PrayerTimeCard(
            icon = R.drawable.sunrise,
            text = "Sunrise",
            time = getTime(data.sunriseTime),
            onClick = {}
        )
        PrayerTimeCard(
            icon = R.drawable.dhuhr,
            text = "Dhuhr",
            time = getTime(data.dhuhrTime),
            onClick = {}
        )
        PrayerTimeCard(
            icon = R.drawable.asr,
            text = "Asr",
            time = getTime(data.asrTime),
            onClick = {}
        )
        PrayerTimeCard(
            icon = R.drawable.maghrib,
            text = "Maghrib",
            time = getTime(data.maghribTime),
            onClick = {}
        )
        PrayerTimeCard(
            icon = R.drawable.isha,
            text = "Isha'a",
            time = getTime(data.ishaTime),
            onClick = {}
        )
    }
}

fun getTime(prayerTime: String): String {
    return prayerTime.substring(0, 5) // Extract HH:mm part
}




