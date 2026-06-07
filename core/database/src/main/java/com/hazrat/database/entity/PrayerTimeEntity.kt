package com.hazrat.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "prayer_times")
data class PrayerTimeEntity(
    val day: Int,
    @ColumnInfo("fajrTime")
    val fajrTime: Long,
    @ColumnInfo("sunriseTime")
    val sunriseTime: Long,
    @ColumnInfo("dhuhrTime")
    val dhuhrTime: Long,
    @ColumnInfo("asrTime")
    val asrTime: Long,
    @ColumnInfo("sunsetTime")
    val sunsetTime: Long,
    @ColumnInfo("maghribTime")
    val maghribTime: Long,
    @ColumnInfo("ishaTime")
    val ishaTime: Long,
    @ColumnInfo("imsakTime")
    val imsakTime: Long,
    @ColumnInfo("midnightTime")
    val midnightTime: Long,
    @ColumnInfo("firstThirdTime")
    val firstThirdTime: Long,
    @ColumnInfo("lastThirdTime")
    val lastThirdTime: Long,
    @ColumnInfo("readableDate")
    val readableDate: String,
    @ColumnInfo("gregorianDate")
    @PrimaryKey val gregorianDate: String,
    @ColumnInfo("timestamp")
    val timestamp: Long,
    @ColumnInfo("gregorianDay")
    val gregorianDay: String,
    @ColumnInfo("gregorianWeekday")
    val gregorianWeekday: String,
    val gregorianMonthNum: Int,
    val gregorianMonthName: String,
    val gregorianYear: String,
    @ColumnInfo("hijriDate")
    val hijriDate: String,
    @ColumnInfo("hijriDay")
    val hijriDay: Int,
    @ColumnInfo("hijriWeekdayEn")
    val hijriWeekdayEn: String,
    @ColumnInfo("hijriWeekdayEr")
    val hijriWeekdayEr: String,
    @ColumnInfo("hijriMonthAr")
    val hijriMonthAr: String,
    @ColumnInfo("hijriMonthEn")
    val hijriMonthEn: String,
    @ColumnInfo("hijriMonthNumber")
    val hijriMonthNumber: Int,
    @ColumnInfo("hijriYear")
    val hijriYear: Int,
    @ColumnInfo("hijriab")
    val hijriab: String,
    val timezone: String,
    val methodId: Int,
    val methodName: String?,
    val methodFajrParam: Double,
    val methodIshaParam: Double,
    val latitudeAdjustmentMethod: String,
    val midnightMode: String,
    val school: String,
    val holidays: List<String> = emptyList(),
    val latitude: Double?,
    val longitude: Double?,
    @ColumnInfo("hijriSortKey")
    val hijriSortKey: Int // compute as : hijriYear * 1000 + hijriMonthNumber * 100 + hijriDay
)
