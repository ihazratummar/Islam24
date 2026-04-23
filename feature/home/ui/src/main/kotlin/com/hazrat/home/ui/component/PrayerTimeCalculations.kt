package com.hazrat.home.ui.component


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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.model.PrayerTimeModel
import com.hazrat.ui.R
import com.hazrat.utils.DateUtil.formatLocalTime
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
    data: PrayerTimeModel,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
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
    val currentDayPrayer = data

    val currentPrayerName: String? = currentDayPrayer.let {
        val isFajrTime = currentTime in (it.fajrTime + 1)..it.sunriseTime
        val isSunriseTime = currentTime in (it.sunriseTime - 300000)..(it.sunriseTime + 300000)
        val isDhuhrTime = currentTime in (it.sunriseTime + 1)..(it.asrTime)
        val isAsrTime = currentTime in (it.asrTime + 1)..(it.maghribTime)
        val isMaghribTime = currentTime in (it.maghribTime + 1)..(it.ishaTime)
        val isIshaTime = currentTime in (it.ishaTime + 1)..it.firstThirdTime

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
            color = textColor,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun DisplayCurrentPrayerTime(
    currentDayPrayer: PrayerTimeModel,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Launch a coroutine to update the current time every second
    DisposableEffect(Unit) {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        val job = coroutineScope.launch {
            while (true) {
                delay(1000)
                currentTime = System.currentTimeMillis()
            }
        }

        onDispose {
            job.cancel()
        }
    }

    Log.d("PrayerTimeInCalculation", "Current Prayer Day: $currentDayPrayer")

    val currentPrayerName: String? = currentDayPrayer.let { prayerTimes ->
        when (currentTime) {
            in (prayerTimes.fajrTime + 1)..prayerTimes.sunriseTime - 300000 -> stringResource(id = R.string.now)
            in (prayerTimes.dhuhrTime + 1)..prayerTimes.dhuhrTime + 3600000 -> stringResource(id = R.string.now)
            in (prayerTimes.asrTime + 1)..prayerTimes.maghribTime - 600000 -> stringResource(id = R.string.now)
            in (prayerTimes.maghribTime + 1)..prayerTimes.ishaTime - 600000 -> stringResource(id = R.string.now)
            in (prayerTimes.ishaTime + 1)..prayerTimes.firstThirdTime -> stringResource(id = R.string.now)
            else -> null
        }
    }

    val prayerTimes = mapOf(
        stringResource(id = R.string.fajr) to currentDayPrayer.fajrTime.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
        stringResource(id = R.string.dhuhr) to currentDayPrayer.dhuhrTime.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
        stringResource(id = R.string.asr) to currentDayPrayer.asrTime.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
        stringResource(id = R.string.maghrib) to currentDayPrayer.maghribTime.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
        stringResource(id = R.string.isha_a) to currentDayPrayer.ishaTime.let {
            Instant.ofEpochMilli(
                it
            ).atZone(ZoneId.systemDefault()).toLocalTime()
        },
    )

    val currentLocalTime = Instant.ofEpochMilli(currentTime)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
    val nextPrayer = prayerTimes
        .filterValues { it != null }
        .filterValues { it!! > currentLocalTime }
        .minByOrNull { it.value ?: LocalTime.MAX }


    if (currentPrayerName != null) {
        Text(
            text = currentPrayerName,
            color = textColor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    } else {
        nextPrayer?.let { (prayerName, time) ->
            Column {
                Text(
                    text = stringResource(R.string.next_prayer, prayerName),
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor
                )
                formatLocalTime(time)?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
            }
        }
    }
}