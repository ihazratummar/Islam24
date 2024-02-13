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
import kotlin.math.abs

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

    // Define time ranges and their corresponding actions
    val timeRanges = listOf(
        "Fajr" to Pair(LocalTime.parse(data.imsakTime.split(" ")[0]), LocalTime.parse(data.sunriseTime.split(" ")[0])),
        "Dhuhr" to Pair(LocalTime.parse(data.sunriseTime.split(" ")[0]), LocalTime.parse(data.asrTime.split(" ")[0])),
        "Asr" to Pair(LocalTime.parse(data.asrTime.split(" ")[0]), LocalTime.parse(data.maghribTime.split(" ")[0])),
        "Maghrib" to Pair(LocalTime.parse(data.maghribTime.split(" ")[0]), LocalTime.parse(data.ishaTime.split(" ")[0])),
        "Isha's" to Pair(LocalTime.parse(data.ishaTime.split(" ")[0]), LocalTime.parse(data.firstThirdTime.split(" ")[0]))
    )

    var currentPrayerAction: String? = null

    // Find the action for the current time
    for ((action, timeRange) in timeRanges) {
        val (startTime, endTime) = timeRange
        Log.d("PrayerTime", "$action: Start Time: $startTime, End Time: $endTime")
        if (currentTime in startTime..endTime) {
            currentPrayerAction = action
            break
        }
    }

    currentPrayerAction?.let { action ->
        Text(
            text = action,
            color = Color.White,
            style = textStyle
        )
    }
}