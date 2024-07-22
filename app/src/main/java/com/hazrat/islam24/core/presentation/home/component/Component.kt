package com.hazrat.islam24.core.presentation.home.component

import com.hazrat.islam24.core.data.entity.PrayerTimeEntity

/**
 * @author Hazrat Ummar Shaikh
 */

fun isPrayerTime(
    data: List<PrayerTimeEntity>,
    gregorianDay: Int,
    hijriDay: String
): Boolean {
    val currentDayPrayerTimes =
        data.find { it.gregorianDay == gregorianDay.toString() && it.hijriDay == hijriDay }
    val currentTime = System.currentTimeMillis()
    val isFajrTime = currentTime in (currentDayPrayerTimes?.fajrTime
        ?: 0)..(currentDayPrayerTimes?.sunriseTime?.minus(300000) ?: 0)
    val isSunriseTime = currentTime in (currentDayPrayerTimes?.sunriseTime?.minus(300000)
        ?: 0)..(currentDayPrayerTimes?.sunriseTime?.plus(300000) ?: 0)
    val isDhuhrTime = currentTime in (currentDayPrayerTimes?.dhuhrTime
        ?: 0)..((currentDayPrayerTimes?.dhuhrTime?.plus(3600000)) ?: 0)
    val isAsrTime = currentTime in (currentDayPrayerTimes?.asrTime
        ?: 0)..((currentDayPrayerTimes?.maghribTime?.minus(3600000)) ?: 0)
    val isMaghribTime = currentTime in (currentDayPrayerTimes?.maghribTime
        ?: 0)..((currentDayPrayerTimes?.ishaTime?.minus(1800000)) ?: 0)
    val isIshaTime = currentTime in (currentDayPrayerTimes?.ishaTime
        ?: 0)..(currentDayPrayerTimes?.midnightTime ?: 0)

    return isFajrTime || isSunriseTime || isDhuhrTime || isAsrTime || isMaghribTime || isIshaTime
}