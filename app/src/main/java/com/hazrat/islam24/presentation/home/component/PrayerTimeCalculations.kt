package com.hazrat.islam24.presentation.home.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.islam24.R
import com.hazrat.islam24.data.entity.PrayerTimeEntity
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
    textStyle: TextStyle = TextStyle()
) {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    val currentDate = LocalDate.now()
    var previousPrayerName by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        val job = coroutineScope.launch {
            while (true) {
                delay(1000) // Update every second
                currentTime = LocalTime.now()
            }
        }

        onDispose {
            job.cancel()
        }
    }

    // Convert the long times to LocalTime
    fun Long.toLocalTime(): LocalTime =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalTime()

    // Filter prayer times for the current day
    val currentDayPrayer = data.firstOrNull { it.day == currentDate.dayOfMonth }

    // Get prayer times for the current day, converting from Long to LocalTime
    val fajrTime = currentDayPrayer?.fajrTime?.toLocalTime()
    val sunriseTime = currentDayPrayer?.sunriseTime?.toLocalTime()
    val zuhrTime = currentDayPrayer?.dhuhrTime?.toLocalTime()
    val asrTime = currentDayPrayer?.asrTime?.toLocalTime()
    val sunsetTime = currentDayPrayer?.sunsetTime?.toLocalTime()
    val maghribTime = currentDayPrayer?.maghribTime?.toLocalTime()
    val ishaTime = currentDayPrayer?.ishaTime?.toLocalTime()
    val midnightTime = currentDayPrayer?.midnightTime?.toLocalTime()

    val timeRanges = mapOf(
        stringResource(id = R.string.fajr) to Pair(fajrTime, sunriseTime),
        stringResource(id = R.string.dhuhr) to Pair(sunriseTime, asrTime),
        stringResource(id = R.string.asr) to Pair(asrTime, maghribTime),
        stringResource(id = R.string.maghrib) to Pair(maghribTime, ishaTime),
        stringResource(id = R.string.isha_a) to Pair(ishaTime, midnightTime)
    )

    var currentPrayerName: String? = null

    for ((prayerName, timeRange) in timeRanges) {
        val (startTime, endTime) = timeRange
        if (startTime != null && endTime != null) {
            if (currentTime in startTime..endTime) {
                currentPrayerName = prayerName
                break
            }
        } else {
            // Handle the case where startTime or endTime is null
        }
    }

    if (currentPrayerName != previousPrayerName) {
        previousPrayerName = currentPrayerName
        Log.d("PrayerTime", "Current Prayer: $currentPrayerName")
    }

    currentPrayerName?.let { prayerName ->
        Text(
            text = prayerName,
            color = Color.White,
            style = textStyle
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
        stringResource(id = R.string.sunrise) to currentDayPrayerTimes?.sunriseTime?.let {
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
                color = Color.White
            )
            formatLocalTime(time)?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}