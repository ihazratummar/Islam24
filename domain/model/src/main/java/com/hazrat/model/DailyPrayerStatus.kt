package com.hazrat.model

import java.time.LocalDate

enum class Prayer(
    val displayOrder: Int,
    val key: String,
    val notificationCode: Int,
    val notificationTitle: String,
    val notificationMessage: String,
    val notificationChannelId: String,
    val azanFileName: String
) {
    FAJR(
        displayOrder = 0,
        key = "fajr",
        notificationCode = 1,
        notificationTitle = "Fajr",
        notificationMessage = "It's time for Fajr prayer",
        notificationChannelId = "fajr_channel",
        azanFileName = "fajrAzan"
    ),
    DHUHR(
        displayOrder = 1,
        key = "dhuhr",
        notificationCode = 2,
        notificationTitle = "Dhuhr",
        notificationMessage = "It's time for Dhuhr prayer",
        notificationChannelId = "dhuhr_channel",
        azanFileName = "dhuhrAzan"
    ),
    ASR(
        displayOrder = 2,
        key = "asr",
        notificationCode = 3,
        notificationTitle = "Asr",
        notificationMessage = "It's time for Asr prayer",
        notificationChannelId = "asr_channel",
        azanFileName = "asrAzan"
    ),
    MAGHRIB(
        displayOrder = 3,
        key = "maghrib",
        notificationCode = 4,
        notificationTitle = "Maghrib",
        notificationMessage = "It's time for Maghrib prayer",
        notificationChannelId = "maghrib_channel",
        azanFileName = "maghribAzan"
    ),
    ISHA(
        displayOrder = 4,
        key = "isha",
        notificationCode = 5,
        notificationTitle = "Isha",
        notificationMessage = "It's time for Isha prayer",
        notificationChannelId = "isha_channel",
        azanFileName = "ishaAzan"
    );

    companion object {
        val ALL = entries.sortedBy { it.displayOrder }
        fun fromName(name: String) = entries.firstOrNull { it.name == name }

        fun fromKey(key: String) : Prayer? {
            return entries.firstOrNull{
                it.key == key
            }
        }
    }
}


data class DailyPrayerStatus(
    val date: LocalDate,
    val loggedPrayers: Set<Prayer>
) {
    val completionRatio: Float get() = loggedPrayers.size / 5f
    val completionPercentage: Int get() = (loggedPrayers.size * 20)
    val isComplete: Boolean get() = loggedPrayers.size == 5
    val isPartial: Boolean get() = loggedPrayers.isNotEmpty() && !isComplete
    val isEmpty: Boolean get() = loggedPrayers.isEmpty()

    fun isLogged(prayer: Prayer) = prayer in loggedPrayers
    fun missedPrayers() = Prayer.ALL.filter { it !in loggedPrayers }
}


data class PrayerStreakInfo(
    val currentStreak: Int,
    val longestStreak: Int,
    val todayProgress: Int,
    val weeklyCompletion: List<DailyPrayerStatus>
)
