package com.hazrat.islam24.core.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class NewPrayerTimeDto(

    @Serializable
    val code: Int,

    @Serializable
    val `data`: Map<String, List<DailyDataDto>>,

    @Serializable
    val status: String
)


@Serializable
data class DailyDataDto(
    @Serializable
    val date: PrayerTimeDateDto,

    @Serializable
    val meta: PrayerTimeMetaDataDto,

    @Serializable
    val timings: TimingsDto

)

@Serializable
data class TimingsDto(
    @Serializable
    val Asr: String,
    @Serializable
    val Dhuhr: String,
    @Serializable
    val Fajr: String,
    @Serializable
    val Firstthird: String,
    @Serializable
    val Imsak: String,
    @Serializable
    val Isha: String,
    @Serializable
    val Lastthird: String,
    @Serializable
    val Maghrib: String,
    @Serializable
    val Midnight: String,
    @Serializable
    val Sunrise: String,
    @Serializable
    val Sunset: String
)

@Serializable
data class PrayerTimeMetaDataDto(
    @Serializable
    val latitude: Double,
    @Serializable
    val latitudeAdjustmentMethod: String,
    @Serializable
    val longitude: Double,
    @Serializable
    val method: PrayerMethodsDto,
    @Serializable
    val midnightMode: String,
    @Serializable
    val offset: OffsetDto,
    @Serializable
    val school: String,
    @Serializable
    val timezone: String
)

@Serializable
data class OffsetDto(
    @Serializable
    val Asr: Int,
    @Serializable
    val Dhuhr: Int,
    @Serializable
    val Fajr: Int,
    @Serializable
    val Imsak: Int,
    @Serializable
    val Isha: Int,
    @Serializable
    val Maghrib: Int,
    @Serializable
    val Midnight: Int,
    @Serializable
    val Sunrise: Int,
    @Serializable
    val Sunset: Int
)

@Serializable
data class PrayerMethodsDto(
    @Serializable
    val id: Int,
    @Serializable
    val location: PrayerLocationDto,
    @Serializable
    val name: String,
    @Serializable
    val params: PrayerParamsDto
)

@Serializable
data class PrayerParamsDto(
    @Serializable
    val Fajr: Double,
    @Serializable
    val Isha: Double
)

@Serializable
data class PrayerLocationDto(
    @Serializable
    val latitude: Double,
    @Serializable
    val longitude: Double
)

@Serializable
data class PrayerTimeDateDto(

    @Serializable
    val gregorian: GregorianDto,
    @Serializable
    val hijri: HijriDto,
    @Serializable
    val readable: String,
    @Serializable
    val timestamp: String
)

@Serializable
data class HijriDto(
    @Serializable
    val date: String,
    @Serializable
    val day: Int,
    @Serializable
    val designation: DesignationDto,
    @Serializable
    val format: String,
    @Serializable
    val holidays: List<String?>,
    @Serializable
    val month: MonthHijriDto,
    @Serializable
    val weekday: WeekdayHijriDto,
    @Serializable
    val year: Int
)

@Serializable
data class DesignationDto(
    @Serializable
    val abbreviated: String,
    @Serializable
    val expanded: String
)

@Serializable
data class MonthHijriDto(
    @Serializable
    val ar: String,
    @Serializable
    val en: String,
    @Serializable
    val number: Int
)

@Serializable
data class WeekdayHijriDto(
    @Serializable
    val ar: String,
    @Serializable
    val en: String
)

@Serializable
data class GregorianDto(
    @Serializable
    val date: String,
    @Serializable
    val day: String,
    @Serializable
    val designation: DesignationDto,
    @Serializable
    val format: String,
    @Serializable
    val month: MonthGregorianDto,
    @Serializable
    val weekday: WeekdayDto,
    @Serializable
    val year: String
)

@Serializable
data class MonthGregorianDto(
    @Serializable
    val en: String,
    @Serializable
    val number: Int
)

@Serializable
data class WeekdayDto(
    @Serializable
    val en: String
)