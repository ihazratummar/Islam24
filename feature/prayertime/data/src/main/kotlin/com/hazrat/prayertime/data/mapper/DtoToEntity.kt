package com.hazrat.prayertime.data.mapper

import com.hazrat.database.entity.PrayerTimeEntity
import com.hazrat.remote.dto.DailyDataDto
import com.hazrat.utils.DateUtil
import com.hazrat.utils.DateUtil.timeStringToLong

/**
 * @author Hazrat Ummar Shaikh
 * Created on 17-12-2024
 */

fun DailyDataDto.toEntity(): PrayerTimeEntity {
    return PrayerTimeEntity(
        day = this.date.gregorian.day.toInt(),
        fajrTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Fajr}"),
        sunriseTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Sunrise}"),
        dhuhrTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Dhuhr}"),
        asrTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Asr}"),
        sunsetTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Sunset}"),
        maghribTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Maghrib}"),
        ishaTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Isha}"),
        imsakTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Imsak}"),
        midnightTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Midnight}"),
        firstThirdTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Firstthird}"),
        lastThirdTime = timeStringToLong("${this.date.gregorian.date} ${this.timings.Lastthird}"),
        readableDate = this.date.readable,
        gregorianDate = DateUtil.convertToDbFormat(this.date.gregorian.date),
        gregorianDay = this.date.gregorian.day,
        gregorianWeekday = this.date.gregorian.weekday.en,
        gregorianMonthNum = this.date.gregorian.month.number,
        gregorianMonthName = this.date.gregorian.month.en,
        gregorianYear = this.date.gregorian.year,
        hijriDate = this.date.hijri.date,
        hijriDay = this.date.hijri.day,
        hijriWeekdayEn = this.date.hijri.weekday.en,
        hijriWeekdayEr = this.date.hijri.weekday.ar,
        hijriMonthAr = this.date.hijri.month.ar,
        hijriMonthEn = this.date.hijri.month.en,
        hijriMonthNumber = this.date.hijri.month.number,
        hijriYear = this.date.hijri.year,
        hijriab = this.date.hijri.designation.abbreviated,
        timezone = this.meta.timezone,
        methodId = this.meta.method.id,
        methodName = this.meta.method.name,
        methodFajrParam = this.meta.method.params.Fajr,
        methodIshaParam = this.meta.method.params.Isha,
        latitudeAdjustmentMethod = this.meta.latitudeAdjustmentMethod,
        midnightMode = this.meta.midnightMode,
        school = this.meta.school

    )
}

fun List<DailyDataDto>.toEntityList(): List<PrayerTimeEntity> {
    return this.map { it.toEntity() }
}