package com.hazrat.prayertime.data.mapper

import com.hazrat.database.entity.PrayerTimeEntity
import com.hazrat.model.MinimalPrayerData


/**
 * @author hazratummar
 * Created on 13/05/26
 */
 

fun PrayerTimeEntity.toMinimalPrayerData(): MinimalPrayerData {
    return MinimalPrayerData(
        day = this.day,
        fajrTime = this.fajrTime,
        sunriseTime = this.sunriseTime,
        dhuhrTime = this.dhuhrTime,
        asrTime = this.asrTime,
        sunsetTime = this.sunriseTime,
        maghribTime = this.maghribTime,
        ishaTime = this.ishaTime,
        imsakTime = this.imsakTime,
        midnightTime = this.midnightTime,
        firstThirdTime = this.firstThirdTime,
        lastThirdTime = this.lastThirdTime,
        hijriDay = this.hijriDay,
        hijriMonthEn = this.hijriMonthEn,
        hijriYear = this.hijriYear,
        hijriAbbreviated = this.hijriab,
    )
}