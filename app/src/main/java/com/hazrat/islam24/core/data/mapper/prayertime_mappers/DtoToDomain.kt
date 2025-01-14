package com.hazrat.islam24.core.data.mapper.prayertime_mappers

import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.DailyData
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.Designation
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.Gregorian
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.Hijri
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.MonthGregorian
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.MonthHijri
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.Offset
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.PrayerLocation
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.PrayerMethods
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.PrayerParams
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.PrayerTimeDate
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.PrayerTimeMetaData
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.Timings
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.Weekday
import com.hazrat.islam24.core.domain.model.prayertime.prayer_times_models.WeekdayHijri
import com.hazrat.islam24.core.remote.dto.DailyDataDto
import com.hazrat.islam24.core.remote.dto.DesignationDto
import com.hazrat.islam24.core.remote.dto.GregorianDto
import com.hazrat.islam24.core.remote.dto.HijriDto
import com.hazrat.islam24.core.remote.dto.MonthGregorianDto
import com.hazrat.islam24.core.remote.dto.MonthHijriDto
import com.hazrat.islam24.core.remote.dto.OffsetDto
import com.hazrat.islam24.core.remote.dto.PrayerLocationDto
import com.hazrat.islam24.core.remote.dto.PrayerMethodsDto
import com.hazrat.islam24.core.remote.dto.PrayerParamsDto
import com.hazrat.islam24.core.remote.dto.PrayerTimeDateDto
import com.hazrat.islam24.core.remote.dto.PrayerTimeMetaDataDto
import com.hazrat.islam24.core.remote.dto.TimingsDto
import com.hazrat.islam24.core.remote.dto.WeekdayDto
import com.hazrat.islam24.core.remote.dto.WeekdayHijriDto

/**
 * @author Hazrat Ummar Shaikh
 * Created on 17-12-2024
 */

//fun NewPrayerTimeDto.toDomain(): NewPrayerTimeResponse {
//    return NewPrayerTimeResponse(
//        code = this.code,
//        data = Pair(this.data.keys, this.data.values.toDomainList()),
//        status = this.status
//    )
//}


fun DailyDataDto.toDomain(): DailyData {
    return DailyData(
        date = this.date.toDomain(),
        meta = this.meta.toDomain(),
        timings = this.timings.toDomain()
    )
}

fun List<DailyDataDto>.toDomainList(): List<DailyData> {
    return this.map { it.toDomain() }
}

fun TimingsDto.toDomain(): Timings {
    return Timings(
        Imsak = this.Imsak,
        Fajr = this.Fajr,
        Dhuhr = this.Dhuhr,
        Asr = this.Asr,
        Maghrib = this.Maghrib,
        Isha = this.Isha,
        Firstthird = this.Firstthird,
        Lastthird = this.Lastthird,
        Sunset = this.Sunset,
        Sunrise = this.Sunrise,
        Midnight = this.Midnight
    )
}


fun PrayerTimeDateDto.toDomain(): PrayerTimeDate {
    return PrayerTimeDate(
        gregorian = this.gregorian.toDomain(),
        hijri = this.hijri.toDomain(),
        readable = this.readable,
        timestamp = this.timestamp
    )
}

fun HijriDto.toDomain(): Hijri {
    return Hijri(
        date = this.date,
        day = this.day,
        designation = this.designation.toDomain(),
        format = this.format,
        holidays = this.holidays,
        month = this.month.toDomain(),
        weekday = this.weekday.toDomain(),
        year = this.year
    )
}

fun WeekdayHijriDto.toDomain(): WeekdayHijri {
    return WeekdayHijri(
        ar = this.ar,
        en = this.en
    )
}


fun MonthHijriDto.toDomain(): MonthHijri {
    return MonthHijri(
        ar = this.ar,
        en = this.en,
        number = this.number
    )
}

fun DesignationDto.toDomain(): Designation {
    return Designation(
        abbreviated = this.abbreviated,
        expanded = this.expanded
    )
}

fun GregorianDto.toDomain(): Gregorian {
    return Gregorian(
        date = this.date,
        day = this.day,
        designation = this.designation.toDomain(),
        format = this.format,
        month = this.month.toDomain(),
        weekday = this.weekday.toDomain(),
        year = this.year
    )
}

fun MonthGregorianDto.toDomain(): MonthGregorian {
    return MonthGregorian(
        en = this.en,
        number = this.number
    )
}

fun WeekdayDto.toDomain(): Weekday {
    return Weekday(
        en = this.en
    )
}


fun PrayerTimeMetaDataDto.toDomain(): PrayerTimeMetaData {
    return PrayerTimeMetaData(
        latitude = this.latitude,
        latitudeAdjustmentMethod = this.latitudeAdjustmentMethod,
        longitude = this.longitude,
        method = this.method.toDomain(),
        midnightMode = this.midnightMode,
        offset = this.offset.toDomain(),
        school = this.school,
        timezone = this.timezone

    )
}

fun OffsetDto.toDomain(): Offset {
    return Offset(
        Imsak = this.Imsak,
        Fajr = this.Fajr,
        Dhuhr = this.Dhuhr,
        Asr = this.Asr,
        Maghrib = this.Maghrib,
        Isha = this.Isha,
        Sunset = this.Sunset,
        Sunrise = this.Sunrise,
        Midnight = this.Midnight
    )
}

fun PrayerMethodsDto.toDomain(): PrayerMethods {
    return PrayerMethods(
        id = this.id,
        location = this.location.toDomain(),
        name = this.name,
        params = this.params.toDomain()
    )
}

fun PrayerLocationDto.toDomain(): PrayerLocation {
    return PrayerLocation(
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun PrayerParamsDto.toDomain(): PrayerParams {
    return PrayerParams(
        Fajr = this.Fajr,
        Isha = this.Isha
    )
}