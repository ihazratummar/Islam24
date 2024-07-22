package com.hazrat.islam24.core.presentation.home.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.islam24.R
import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import com.hazrat.islam24.util.DateUtil.formatLocalTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId


@Composable
fun DisplayCurrentPrayerName(
    data: List<PrayerTimeEntity>,
) {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val currentDate = LocalDate.now()
    var previousPrayerName by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        val job = coroutineScope.launch {
            while (true) {
                delay(1000) // Update every second
                currentTime = System.currentTimeMillis()
            }
        }

        onDispose {
            job.cancel()
        }
    }

    // Filter prayer times for the current day
    val currentDayPrayer = data.firstOrNull { it.day == currentDate.dayOfMonth }

    val currentPrayerName: String? = currentDayPrayer?.let {
        val isFajrTime = currentTime in (it.fajrTime + 1)..it.sunriseTime
        val isSunriseTime = currentTime in (it.sunriseTime - 300000)..(it.sunriseTime + 300000)
        val isDhuhrTime = currentTime in (it.sunriseTime + 1)..(it.asrTime)
        val isAsrTime = currentTime in (it.asrTime + 1)..(it.maghribTime)
        val isMaghribTime = currentTime in (it.maghribTime + 1)..(it.ishaTime)
        val isIshaTime = currentTime in (it.ishaTime + 1)..it.midnightTime

        when {
            isFajrTime -> stringResource(id = R.string.fajr)
            isSunriseTime -> stringResource(id = R.string.sunrise)
            isDhuhrTime -> stringResource(id = R.string.dhuhr)
            isAsrTime -> stringResource(id = R.string.asr)
            isMaghribTime -> stringResource(id = R.string.maghrib)
            isIshaTime -> stringResource(id = R.string.isha_a)
            else -> null
        }
    }

    if (currentPrayerName != previousPrayerName) {
        previousPrayerName = currentPrayerName
        Log.d("PrayerTime", "Current Prayer: $currentPrayerName")
    }

    currentPrayerName?.let { prayerName ->
        Text(
            text = prayerName,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style =  MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun DisplayCurrentPrayerTime(
    data: List<PrayerTimeEntity>,
    gregorianDay: Int,
    hijriDay: String
) {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    // Launch a coroutine to update the current time every second
    DisposableEffect(Unit) {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val job = coroutineScope.launch {
            while (true) {
                delay(1000)
                currentTime = LocalTime.now()
            }
        }

        onDispose {
            job.cancel()
        }
    }

    // Find the prayer times for the current day
    val currentDayPrayerTimes =
        data.find { it.gregorianDay == gregorianDay.toString() && it.hijriDay == hijriDay }

    val prayerTimes = mapOf(
        stringResource(id = R.string.fajr) to currentDayPrayerTimes?.fajrTime?.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
        stringResource(id = R.string.dhuhr) to currentDayPrayerTimes?.dhuhrTime?.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
        stringResource(id = R.string.asr) to currentDayPrayerTimes?.asrTime?.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
        stringResource(id = R.string.maghrib) to currentDayPrayerTimes?.maghribTime?.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
        stringResource(id = R.string.isha_a) to currentDayPrayerTimes?.ishaTime?.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
    )

    val nextPrayer = prayerTimes
        .filterValues { it != null }
        .filterValues { it!! > currentTime }
        .minByOrNull { it.value ?: LocalTime.MAX }



    nextPrayer?.let { (prayerName, time) ->

        Column {
            Text(
                text = stringResource(R.string.next_prayer, prayerName),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            formatLocalTime(time)?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}