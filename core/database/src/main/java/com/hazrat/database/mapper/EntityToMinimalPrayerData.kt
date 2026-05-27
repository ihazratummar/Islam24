package com.hazrat.database.mapper

import com.hazrat.database.entity.PrayerTimeEntity
import com.hazrat.model.MinimalPrayerData

/**
 * Maps PrayerTimeEntity to MinimalPrayerData domain model.
 */
fun PrayerTimeEntity.toMinimalPrayerData(): MinimalPrayerData {
    return MinimalPrayerData(
        day = this.day,
        fajrTime = this.fajrTime,
        sunriseTime = this.sunriseTime,
        dhuhrTime = this.dhuhrTime,
        asrTime = this.asrTime,
        sunsetTime = this.sunriseTime, // Note: Existing code had sunriseTime here too, verify if this was intended
        maghribTime = this.maghribTime,
        ishaTime = this.ishaTime,
        imsakTime = this.imsakTime,
        midnightTime = this.midnightTime,
        firstThirdTime = this.firstThirdTime,
        lastThirdTime = this.lastThirdTime,
        hijriDay = this.hijriDay,
        hijriMonthEn = this.hijriMonthEn,
        hijriMonthNumber = this.hijriMonthNumber,
        hijriYear = this.hijriYear,
        hijriAbbreviated = this.hijriab,
        timeStamp = this.timestamp
    )
}
