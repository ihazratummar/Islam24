package com.hazrat.model

import java.time.LocalDate

enum class Prayer(val arabicName: String, val displayOrder: Int) {
    FAJR("الفجر", 0),
    DHUHR("الظهر", 1),
    ASR("العصر", 2),
    MAGHRIB("المغرب", 3),
    ISHA("العشاء", 4);

    companion object {
        val ALL = entries.sortedBy { it.displayOrder }
        fun fromName(name: String) = entries.firstOrNull { it.name == name }
    }
}


data class DailyPrayerStatus(
    val date: LocalDate,
    val loggedPrayers: Set<Prayer>
){
    val completionRatio: Float get() = loggedPrayers.size / 5f
    val completionPercentage: Int get() = (loggedPrayers.size * 20)
    val isComplete: Boolean get() = loggedPrayers.size == 5
    val isPartial : Boolean get() = loggedPrayers.isNotEmpty() && !isComplete
    val isEmpty : Boolean get() = loggedPrayers.isEmpty()

    fun isLogged(prayer: Prayer) = prayer in loggedPrayers
    fun missedPrayers()  = Prayer.ALL.filter { it !in loggedPrayers }
}


data class PrayerStreakInfo(
    val currentStreak: Int,
    val longestStreak: Int,
    val todayProgress: Int,
    val weeklyCompletion: List<DailyPrayerStatus>
)
