/////PrayerTimingsData.kt


package com.example.zakat_calculator.domain.model

import android.util.Log

data class PrayerTimingsData(
    val fajr: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String
)

object PrayerTimingsStorage {
    private var currentPrayerTimings: PrayerTimingsData? = null

    fun setCurrentPrayerTimings(
        fajr: String,
        dhuhr: String,
        asr: String,
        maghrib: String,
        isha: String
    ): PrayerTimingsData? {
        currentPrayerTimings = PrayerTimingsData(fajr, dhuhr, asr, maghrib, isha)
        Log.d("PrayerTimingsStorage", "Current prayer timings set $currentPrayerTimings")
        return currentPrayerTimings
    }


    fun getCurrentPrayerTimings(): PrayerTimingsData? {
        return currentPrayerTimings
//        Log.d("PrayerTimingsStorage", "Current prayer timings set ${currentPrayerTimings?.isha}")
    }
}