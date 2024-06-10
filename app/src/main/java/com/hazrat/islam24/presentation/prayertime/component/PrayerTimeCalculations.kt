package com.hazrat.islam24.presentation.prayertime.component

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.hazrat.islam24.data.entity.PrayerTimeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime


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

//@Composable
//fun DisplayCurrentPrayerTime(
//    data: PrayerTimeEntity,
//    textStyle: TextStyle = TextStyle()
//) {
//
//    var currentTime by remember { mutableStateOf(LocalTime.now()) }
//    var currentPrayerIndex by remember { mutableStateOf(-1) }
//
//    DisposableEffect(Unit) {
//        val coroutineScope = CoroutineScope(Dispatchers.Default)
//
//        val job = coroutineScope.launch {
//            while (true) {
//                delay(1000) // Update every second
//                currentTime = LocalTime.now()
//            }
//        }
//
//        onDispose {
//            job.cancel()
//        }
//    }
//
//    val timeRanges = listOf(
//        "Fajr" to LocalTime.parse(data.fajrTime.split(" ")[0]),
//        "Dhuhr" to LocalTime.parse(data.dhuhrTime.split(" ")[0]),
//        "Asr" to LocalTime.parse(data.asrTime.split(" ")[0]),
//        "Maghrib" to LocalTime.parse(data.maghribTime.split(" ")[0]),
//        "Isha" to LocalTime.parse(data.ishaTime.split(" ")[0])
//    )
//
//    // Determine the current prayer index based on the current time
//    for (i in timeRanges.indices) {
//        val prayerTimeRange = timeRanges[i].second
//        if (currentTime in prayerTimeRange) {
//            currentPrayerIndex = i
//            break
//        }
//    }
//
//    if (currentPrayerIndex != -1) {
//        val currentPrayer = timeRanges[currentPrayerIndex].first
//        val currentPrayerTime = timeRanges[currentPrayerIndex].second
//        val nextPrayerIndex = (currentPrayerIndex + 1) % timeRanges.size
//        val nextPrayerTime = timeRanges[nextPrayerIndex].second
//
//        val timeUntilNextPrayer = calculateTimeDifference(currentTime, nextPrayerTime)
//
//        if (currentTime in currentPrayerTime..nextPrayerTime) {
//            // Show "Now" for one hour
//            val remainingTime = calculateTimeDifference(currentTime, currentPrayerTime.plusHours(1))
//            Text(text = "Now for 1 hour", style = textStyle)
//        } else {
//            // Show countdown to the next prayer
//            val formattedTimeUntilNextPrayer = formatTimeDifference(timeUntilNextPrayer)
//            Text(text = formattedTimeUntilNextPrayer, style = textStyle)
//        }
//    }
//}
//
//
//@Composable
//fun calculateTimeDifference(currentTime: LocalTime, nextPrayerTime: LocalTime): Long {
//    val currentSeconds = currentTime.toSecondOfDay()
//    val nextSeconds = nextPrayerTime.toSecondOfDay()
//    val difference = nextSeconds - currentSeconds
//
//    return if (difference < 0) {
//        // If difference is negative, it means the next prayer time has passed, so calculate the time until the next occurrence of the next prayer
//        difference.toLong() + (24 * 3600) // Add a day in seconds to get the next occurrence
//    } else {
//        // If difference is positive, it means the current prayer is ongoing, so return the remaining time until the end of the current prayer
//        difference.toLong()
//    }
//}
//
//@Composable
//fun formatTimeDifference(timeDifference: Long): String {
//    val absoluteTimeDifference = abs(timeDifference)
//    val hours = absoluteTimeDifference / 3600
//    val minutes = (absoluteTimeDifference % 3600) / 60
//    val seconds = absoluteTimeDifference % 60
//    val sign = if (timeDifference < 0) "-" else ""  // Determine the sign of the time difference
//    return String.format("%s%02d:%02d:%02d", sign, hours, minutes, seconds)
//}