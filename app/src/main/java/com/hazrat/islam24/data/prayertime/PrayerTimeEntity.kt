package com.hazrat.islam24.data.prayertime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_times")
data class PrayerTimeEntity(
    @PrimaryKey val day: Int,

    @ColumnInfo("Fajr Time")
    val fajrTime: String,

    @ColumnInfo("Sunrise Time")
    val sunriseTime: String,
    @ColumnInfo("Dhuhr Time")
    val dhuhrTime: String,
    @ColumnInfo("AsrTime")
    val asrTime: String,
    @ColumnInfo("Sunset Time")
    val sunsetTime: String,
    @ColumnInfo("Maghrib Time")
    val maghribTime: String,
    @ColumnInfo("Isha Time")
    val ishaTime: String,
    @ColumnInfo("ImsakTime")
    val imsakTime: String,
    @ColumnInfo("MidNight Time")
    val midnightTime: String,
    @ColumnInfo("FirstThird Time")
    val firstThirdTime: String,
    @ColumnInfo("LastThird Time")
    val lastThirdTime: String,
    @ColumnInfo("Readable Date")
    val readableDate: String,
    @ColumnInfo("GregorianDate")
    val gregorianDate: String,
    @ColumnInfo("GregorianDay")
    val gregorianDay: String,
    @ColumnInfo("GregorianWeekDay")
    val gregorianWeekday: String,
    val gregorianMonthNum: Int,
    val gregorianMonthName: String,
    val gregorianYear: String,
    @ColumnInfo("HijriDate")
    val hijriDate: String,
    @ColumnInfo("HijriDay")
    val hijriDay: String,
    @ColumnInfo("HijriWeekDayEn")
    val hijriWeekdayEn: String,
    @ColumnInfo("HijriWeekDayAr")
    val hijriWeekdayEr: String,
    val hijriMonthAr: String,
    val hijriMonthEn: String,
    val hijriMonthNumber:Int,
    val hijriYear: String,
    val hijriab: String,
    val timezone: String,
    val methodId: Int,
    val methodName: String?,
    val methodFajrParam: Int,
    val methodIshaParam: Int,
    val latitudeAdjustmentMethod: String,
    val midnightMode: String,
    val school: String
    // Add other fields as needed
)
