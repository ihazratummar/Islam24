package com.hazrat.prayertime.data.mapper

import com.hazrat.database.entity.PrayerTimeEntity
import com.hazrat.model.PrayerTimeModel


/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun PrayerTimeEntity.toModel(): PrayerTimeModel {
    return PrayerTimeModel(
        day = day,
        fajrTime = fajrTime,
        sunriseTime = sunriseTime,
        dhuhrTime = dhuhrTime,
        asrTime = asrTime,
        sunsetTime = sunsetTime,
        maghribTime = maghribTime,
        ishaTime = ishaTime,
        imsakTime = imsakTime,
        midnightTime = midnightTime,
        firstThirdTime = firstThirdTime,
        lastThirdTime = lastThirdTime,

        readableDate = readableDate,
        gregorianDate = gregorianDate,
        gregorianDay = gregorianDay,
        gregorianWeekday = gregorianWeekday,
        gregorianMonthNum = gregorianMonthNum,
        gregorianMonthName = gregorianMonthName,
        gregorianYear = gregorianYear,

        hijriDate = hijriDate,
        hijriDay = hijriDay,
        hijriWeekdayEn = hijriWeekdayEn,
        hijriWeekdayAr = hijriWeekdayEr,
        hijriMonthAr = hijriMonthAr,
        hijriMonthEn = hijriMonthEn,
        hijriMonthNumber = hijriMonthNumber,
        hijriYear = hijriYear,
        hijriAbbreviated = hijriab,

        timezone = timezone,
        methodId = methodId,
        methodName = methodName,
        methodFajrParam = methodFajrParam,
        methodIshaParam = methodIshaParam,
        latitudeAdjustmentMethod = latitudeAdjustmentMethod,
        midnightMode = midnightMode,
        school = school,
        isFallbackData = this.isFallBackData
    )
}

fun List<PrayerTimeEntity>.toPrayerModelList(): List<PrayerTimeModel> {
    return this.map {
        it.toModel()
    }
}