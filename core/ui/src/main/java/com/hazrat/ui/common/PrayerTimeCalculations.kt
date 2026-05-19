package com.hazrat.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.hazrat.model.MinimalPrayerData
import com.hazrat.model.Prayer
import com.hazrat.ui.R
import com.hazrat.ui.theme.AsrGradient
import com.hazrat.ui.theme.DhuhrGradient
import com.hazrat.ui.theme.FajrGradient
import com.hazrat.ui.theme.IshaGradient
import com.hazrat.ui.theme.MaghribGradient
import com.hazrat.ui.theme.SunriseGradient
import kotlinx.coroutines.delay
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Enum representing the types of prayers with their corresponding string resources.
 */
enum class PrayerType(
    @StringRes val nameRes: Int,
    @DrawableRes val icon: Int,
    val gradient: List<Color>,
    val color: Color,
    val prayer: Prayer
) {
    FAJR(
        nameRes   = R.string.fajr,
        icon      = R.drawable.dawn,
        gradient  = FajrGradient,
        color     = Color(0xFF6d63f2),
        prayer = Prayer.FAJR
    ),
    SUNRISE(
        nameRes   = R.string.sunrise,
        icon      = R.drawable.sunrise,
        gradient  = SunriseGradient,
        color     = Color(0xFFFACA96),
        prayer = Prayer.FAJR
    ),
    DHUHR(
        nameRes   = R.string.dhuhr,
        icon      = R.drawable.sun,
        gradient  = DhuhrGradient,
        color     = Color(0xFFFFB752),
        prayer = Prayer.DHUHR
    ),
    ASR(
        nameRes   = R.string.asr,
        icon      = R.drawable.asr,
        gradient  = AsrGradient,
        color     = Color(0xFFFF8E00),
        prayer = Prayer.ASR
    ),
    MAGHRIB(
        nameRes   = R.string.maghrib,
        icon      = R.drawable.evening,
        gradient  = MaghribGradient,
        color     = Color(0xFFfa716a),
        prayer = Prayer.MAGHRIB
    ),
    ISHA(
        nameRes   = R.string.isha_a,
        icon      = R.drawable.isha,
        gradient  = IshaGradient,
        color     = Color(0xFF42D6FF),
        prayer = Prayer.ISHA
    ),
}

/**
 * Data class representing the current state of prayer times.
 */
data class PrayerState(
    val currentPrayer: PrayerType?,
    val currentPrayerTime: Long,
    val nextPrayer: PrayerType?,
    val prayerIcon: Int,
    val nextPrayerIcon: Int,
    val nextPrayerGradient: List<Color>,
    val nextPrayerTimeMillis: Long,
    val countdownText: String,
    val isNow: Boolean,
    val nextPrayerIconColor: Color,
    val currentPrayerIconColor: Color
)

/**
 * Remembers and updates the prayer state every second.
 */
@Composable
fun rememberPrayerState(prayerTimes: MinimalPrayerData): PrayerState {
    var currentTimeMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(key1 = prayerTimes) {
        while (true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000)
        }
    }

    return remember(prayerTimes, currentTimeMillis) {
        calculatePrayerState(prayerTimes, currentTimeMillis)
    }
}


/**
 * Logic to calculate the current and next prayer based on the current time.
 */
private fun calculatePrayerState(prayerTimes: MinimalPrayerData, currentTime: Long): PrayerState {
    val prayers = listOf(
        PrayerType.FAJR to prayerTimes.fajrTime,
        PrayerType.SUNRISE to prayerTimes.sunriseTime,
        PrayerType.DHUHR to prayerTimes.dhuhrTime,
        PrayerType.ASR to prayerTimes.asrTime,
        PrayerType.MAGHRIB to prayerTimes.maghribTime,
        PrayerType.ISHA to prayerTimes.ishaTime
    )

    // Find current active prayer
    val currentPrayer = when {
        currentTime in prayerTimes.fajrTime until prayerTimes.sunriseTime -> PrayerType.FAJR
        currentTime in prayerTimes.sunriseTime until (prayerTimes.sunriseTime + TimeUnit.MINUTES.toMillis(15)) -> PrayerType.SUNRISE
        currentTime in (prayerTimes.sunriseTime + TimeUnit.MINUTES.toMillis(15))  until prayerTimes.asrTime -> PrayerType.DHUHR
        currentTime in prayerTimes.asrTime until prayerTimes.maghribTime -> PrayerType.ASR
        currentTime in prayerTimes.maghribTime until prayerTimes.ishaTime -> PrayerType.MAGHRIB
        currentTime >= prayerTimes.ishaTime || currentTime < prayerTimes.lastThirdTime -> PrayerType.ISHA
        else -> null
    }

    // Find next prayer
    val nextPrayerInfo = prayers.firstOrNull { it.second > currentTime }
        ?: Pair(PrayerType.FAJR, prayerTimes.fajrTime + TimeUnit.DAYS.toMillis(1))

    val (nextPrayer, nextPrayerTime) = nextPrayerInfo

    val currentPrayerInfo = prayers.find {
        it.first.name == currentPrayer?.name
    } ?: Pair(PrayerType.FAJR, prayerTimes.fajrTime + TimeUnit.DAYS.toMillis(1))

    val (_, currentPrayerTime) = currentPrayerInfo


    // "NOW" logic: if within 15 minutes of prayer start (except Sunrise)
    val isNow = when (currentPrayer) {
        PrayerType.FAJR -> {
            currentTime in prayerTimes.fajrTime..
                    (prayerTimes.sunriseTime - TimeUnit.MINUTES.toMillis(10))
        }
        PrayerType.SUNRISE -> false

        PrayerType.DHUHR -> {
            currentTime in prayerTimes.dhuhrTime..
                    (prayerTimes.asrTime - TimeUnit.MINUTES.toMillis(60))
        }

        PrayerType.ASR -> {
            currentTime in prayerTimes.asrTime..
                    (prayerTimes.maghribTime - TimeUnit.MINUTES.toMillis(30))
        }

        PrayerType.MAGHRIB -> {
            currentTime in prayerTimes.maghribTime..
                    (prayerTimes.ishaTime - TimeUnit.MINUTES.toMillis(30))
        }

        PrayerType.ISHA -> {
            currentTime in prayerTimes.ishaTime until prayerTimes.midnightTime

        }
        else -> false
    }


    val countdownText = if (nextPrayerTime > currentTime) {
        formatCountdown(nextPrayerTime - currentTime)
    } else ""



    return PrayerState(
        currentPrayer = currentPrayer,
        currentPrayerTime = currentPrayerTime,
        nextPrayer = nextPrayer,
        nextPrayerTimeMillis = nextPrayerTime,
        countdownText = countdownText,
        isNow = isNow,
        prayerIcon = currentPrayer!!.icon,
        nextPrayerIcon = nextPrayer.icon,
        nextPrayerGradient = nextPrayer.gradient,
        nextPrayerIconColor = nextPrayer.color,
        currentPrayerIconColor = currentPrayer.color
    )
}

/**
 * Formats milliseconds into a HH:mm:ss countdown string.
 */
private fun formatCountdown(diffMillis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(diffMillis) % 60
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
}
