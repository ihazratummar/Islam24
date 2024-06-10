package com.hazrat.islam24.presentation.prayertime.component

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.islam24.data.entity.PrayerTimeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import kotlin.math.abs


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

    // Filter prayer times for the current day
    val currentDayPrayer = data.firstOrNull { it.day == currentDate.dayOfMonth }

    val fajrTime = currentDayPrayer?.fajrTime?.let {
        if (it.isNotBlank()) LocalTime.parse(it.split(" ")[0]) else null
    }
    val sunriseTime = currentDayPrayer?.sunriseTime?.let {
        if (it.isNotBlank()) LocalTime.parse(it.split(" ")[0]) else null
    }
    val dhuhrTime = currentDayPrayer?.dhuhrTime?.let {
        if (it.isNotBlank()) LocalTime.parse(it.split(" ")[0]) else null
    }
    val asrTime = currentDayPrayer?.asrTime?.let {
        if (it.isNotBlank()) LocalTime.parse(it.split(" ")[0]) else null
    }
    val maghribTime = currentDayPrayer?.maghribTime?.let {
        if (it.isNotBlank()) LocalTime.parse(it.split(" ")[0]) else null
    }
    val ishaTime = currentDayPrayer?.ishaTime?.let {
        if (it.isNotBlank()) LocalTime.parse(it.split(" ")[0]) else null
    }
    val midnightTime = currentDayPrayer?.midnightTime?.let {
        if (it.isNotBlank()) LocalTime.parse(it.split(" ")[0]) else null
    }

    val timeRanges = mapOf(
        "Fajr" to Pair(fajrTime, sunriseTime),
        "Dhuhr" to Pair(sunriseTime, asrTime),
        "Asr" to Pair(asrTime, maghribTime),
        "Maghrib" to Pair(maghribTime, ishaTime),
        "Isha'a" to Pair(ishaTime, midnightTime)
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
    data: List<PrayerTimeEntity>
) {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    var currentPrayerIndex by remember { mutableIntStateOf(-1) }

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

    // Define the prayer times
    val timeRanges = listOf(
        "Fajr" to LocalTime.parse(data.firstOrNull()?.fajrTime?.split(" ")?.get(0) ?: ""),
        "Dhuhr" to LocalTime.parse(data.firstOrNull()?.dhuhrTime?.split(" ")?.get(0) ?: ""),
        "Asr" to LocalTime.parse(data.firstOrNull()?.asrTime?.split(" ")?.get(0) ?: ""),
        "Maghrib" to LocalTime.parse(data.firstOrNull()?.maghribTime?.split(" ")?.get(0) ?: ""),
        "Isha" to LocalTime.parse(data.firstOrNull()?.ishaTime?.split(" ")?.get(0) ?: "")
    )

    // Find the current prayer index
    for ((index, time) in timeRanges.withIndex()) {
        if (currentTime.isBefore(time.second)) {
            currentPrayerIndex = index
            break
        }
    }

    // Show the current prayer time and time until the next prayer
    if (currentPrayerIndex != -1) {
        val currentPrayer = timeRanges[currentPrayerIndex].first
        val currentPrayerTime = timeRanges[currentPrayerIndex].second
        val nextPrayerIndex = (currentPrayerIndex + 1) % timeRanges.size
        val nextPrayerTime = timeRanges[nextPrayerIndex].second

        val timeUntilNextPrayer = calculateTimeDifference(currentTime, nextPrayerTime)

        if (currentTime.isBefore(currentPrayerTime.plusHours(1))) {
            Text(text = "Next $currentPrayerTime",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold)
        } else {
            val formattedTimeUntilNextPrayer = formatTimeDifference(timeUntilNextPrayer)
            Text(text = formattedTimeUntilNextPrayer,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun calculateTimeDifference(currentTime: LocalTime, nextPrayerTime: LocalTime): Long {
    val currentSeconds = currentTime.toSecondOfDay()
    val nextSeconds = nextPrayerTime.toSecondOfDay()
    val difference = nextSeconds - currentSeconds

    return if (difference < 0) {
        difference.toLong() + (24 * 3600)
    } else {
        difference.toLong()
    }
}

@Composable
fun formatTimeDifference(timeDifference: Long): String {
    val absoluteTimeDifference = abs(timeDifference)
    val hours = absoluteTimeDifference / 3600
    val minutes = (absoluteTimeDifference % 3600) / 60
    val seconds = absoluteTimeDifference % 60
    val sign = if (timeDifference < 0) "-" else ""
    return String.format(Locale.getDefault(), "%s%02d:%02d:%02d", sign, hours, minutes, seconds)
}

