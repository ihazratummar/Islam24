package com.hazrat.model

import androidx.compose.runtime.Immutable


/**
 * @author hazratummar
 * Created on 27/01/26
 */

data class PrayerTimeModel(
    val day: Int,
    val fajrTime: Long,
    val sunriseTime: Long,
    val dhuhrTime: Long,
    val asrTime: Long,
    val sunsetTime: Long,
    val maghribTime: Long,
    val ishaTime: Long,
    val imsakTime: Long,
    val midnightTime: Long,
    val firstThirdTime: Long,
    val lastThirdTime: Long,

    val readableDate: String,
    val gregorianDate: String,
    val gregorianDay: String,
    val gregorianWeekday: String,
    val gregorianMonthNum: Int,
    val gregorianMonthName: String,
    val gregorianYear: String,

    val hijriDate: String,
    val hijriDay: Int,
    val hijriWeekdayEn: String,
    val hijriWeekdayAr: String,
    val hijriMonthAr: String,
    val hijriMonthEn: String,
    val hijriMonthNumber: Int,
    val hijriYear: Int,
    val hijriAbbreviated: String,

    val timezone: String,
    val methodId: Int,
    val methodName: String?,
    val methodFajrParam: Double,
    val methodIshaParam: Double,
    val latitudeAdjustmentMethod: String,
    val midnightMode: String,
    val school: String,


)

@Immutable
data class MinimalPrayerData(
    val day: Int = 0,
    val fajrTime: Long= 0L,
    val sunriseTime: Long= 0L,
    val dhuhrTime: Long= 0L,
    val asrTime: Long= 0L,
    val sunsetTime: Long= 0L,
    val maghribTime: Long= 0L,
    val ishaTime: Long= 0L,
    val imsakTime: Long= 0L,
    val midnightTime: Long= 0L,
    val firstThirdTime: Long= 0L,
    val lastThirdTime: Long= 0L,

    val hijriDay: Int = 0,
    val hijriMonthEn: String = "",
    val hijriYear: Int = 0,
    val hijriAbbreviated: String = "",
)