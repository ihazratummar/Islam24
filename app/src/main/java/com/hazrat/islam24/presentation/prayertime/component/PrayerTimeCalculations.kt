package com.hazrat.islam24.presentation.prayertime.component

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.hazrat.islam24.data.prayertime.PrayerTimeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter


private fun parsePrayerTime(prayerTime: String): LocalTime? {
    return try {
        LocalTime.parse(prayerTime.substring(0, 5), DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        null
    }
}

private fun calculateTimeDifference(currentTime: LocalTime, nextPrayerTime: LocalTime): LocalTime {
    return nextPrayerTime
        .minusHours(currentTime.hour.toLong())
        .minusMinutes(currentTime.minute.toLong())
        .minusSeconds(currentTime.second.toLong())
}



private fun calculateTimeDifferences(currentTime: LocalTime, nextPrayerTime: LocalTime): Long {
    val currentSeconds = currentTime.toSecondOfDay()
    val nextSeconds = nextPrayerTime.toSecondOfDay()
    return nextSeconds - currentSeconds.toLong()
}
@Composable
fun DisplayCurrentPrayerTime(
    data: PrayerTimeEntity,
    textStyle: TextStyle = TextStyle()
) {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

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

    val prayerTimes = listOf(
        "Fajr" to data.fajrTime,
        "Dhuhr" to data.dhuhrTime,
        "Asr" to data.asrTime,
        "Maghrib" to data.maghribTime,
        "Isha" to data.ishaTime,
    )

    // Find the index of the current prayer time
    val currentIndex = prayerTimes.indexOfFirst { (_, time) ->
        val parsedTime = parsePrayerTime(time)
        parsedTime != null && currentTime.isAfter(parsedTime.minusHours(1)) && currentTime.isBefore(parsedTime.plusHours(1))
    }

    if (currentIndex != -1) {
        val (_, currentPrayerTime) = prayerTimes[currentIndex]
        val parsedCurrentPrayerTime = parsePrayerTime(currentPrayerTime)!!
        val prayerEndTime = parsedCurrentPrayerTime.plusHours(1) // End time for the current prayer

        if (currentTime.isBefore(prayerEndTime)) {
            // Within one hour after the end of the prayer time
            Text(text = "Now", style = textStyle)
        } else {
            // After the current prayer time and more than one hour after its end
            val nextPrayerIndex = (currentIndex + 1).coerceIn(0, prayerTimes.size - 1)
            val (_, nextPrayerTime) = prayerTimes[nextPrayerIndex]
            val parsedNextPrayerTime = parsePrayerTime(nextPrayerTime)!!
            val timeUntilNextPrayer = calculateTimeDifferences(currentTime, parsedNextPrayerTime)
            val hours = timeUntilNextPrayer / 3600
            val minutes = (timeUntilNextPrayer % 3600) / 60
            val seconds = timeUntilNextPrayer % 60
            val formattedTimeUntilNextPrayer = String.format("-%02d:%02d:%02d", hours, minutes, seconds)
            Text(text = formattedTimeUntilNextPrayer, style = textStyle)
        }
    }
}



@Composable
fun DisplayCurrentPrayerName(
    data: PrayerTimeEntity,
    textStyle: TextStyle = TextStyle()
) {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

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

    val prayerTimes = listOf(
        "Fajr" to data.fajrTime,
        "Sunrise" to data.sunriseTime,
        "Dhuhr" to data.dhuhrTime,
        "Asr" to data.asrTime,
        "Maghrib" to data.maghribTime,
        "Isha" to data.ishaTime
    )

    var currentPrayerName: String? = null

    // Iterate through the list of prayer times
    for ((prayerName, prayerTime) in prayerTimes) {
        val parsedPrayerTime = parsePrayerTime(prayerTime)

        // Check if the current time is between the current and next prayer times
        if (parsedPrayerTime != null && currentTime.isAfter(parsedPrayerTime)) {
            currentPrayerName = prayerName
        } else {
            // If the current time is before the current prayer time, break the loop
            break
        }
    }

    currentPrayerName?.let { prayerName ->
        Text(
            text = prayerName,
            color = Color.White,
            style = textStyle
        )
    }
}