package com.hazrat.prayer.ui.notification

import com.hazrat.model.Prayer

data class PrayerNotificationState(
    val enabledPrayers: Map<Prayer, Boolean> = mapOf(
        Prayer.FAJR to false,
        Prayer.DHUHR to false,
        Prayer.ASR to false,
        Prayer.MAGHRIB to false,
        Prayer.ISHA to false
    ),
    val preAlertOffsets: Map<Prayer, Int> = mapOf(
        Prayer.FAJR to 15,
        Prayer.DHUHR to 10,
        Prayer.ASR to 10,
        Prayer.MAGHRIB to 5,
        Prayer.ISHA to 10
    ),
    val vibrationStates: Map<Prayer, Boolean> = mapOf(
        Prayer.FAJR to true,
        Prayer.DHUHR to true,
        Prayer.ASR to true,
        Prayer.MAGHRIB to true,
        Prayer.ISHA to true
    ),
    val azanSounds: Map<Prayer, String> = mapOf(
        Prayer.FAJR to "System Default",
        Prayer.DHUHR to "System Default",
        Prayer.ASR to "System Default",
        Prayer.MAGHRIB to "System Default",
        Prayer.ISHA to "System Default"
    ),
    val expandedPrayers: Set<Prayer> = setOf(Prayer.FAJR),
    val activePreAlertSheetPrayer: Prayer? = null,
    val activeAzanSoundSheetPrayer: Prayer? = null
) {
    val enabledCount: Int
        get() = enabledPrayers.values.count { it }

    val isAllActive: Boolean
        get() = enabledCount == 5
}
