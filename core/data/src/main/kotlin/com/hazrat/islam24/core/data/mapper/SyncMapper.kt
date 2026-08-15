package com.hazrat.islam24.core.data.mapper

import com.hazrat.database.converter.NotificationSettingEntity
import com.hazrat.database.converter.NotificationSettingsMapEntity
import com.hazrat.database.entity.prayer.PrayerLogEntity
import com.hazrat.database.entity.prayer.UserPrayerSettingEntity
import com.hazrat.remote.dto.sync.NotificationSettingDto
import com.hazrat.remote.dto.sync.NotificationSettingsMapDto
import com.hazrat.remote.dto.sync.PrayerLogSyncDto
import com.hazrat.remote.dto.sync.PrayerSettingSyncDto


/**
 * @author hazratummar
 * Created on 14/08/26
 */




fun UserPrayerSettingEntity.toDto(): PrayerSettingSyncDto {
    return PrayerSettingSyncDto(
        calculationMethod = calculationMethod,
        juristicMethod = juristicMethod,
        masterNotification = masterNotification,
        notificationSettings = notificationSettingsJson.toDtoMap(),
        updatedAt = updatedAt
    )
}

fun PrayerSettingSyncDto.toEntity(isSynced: Boolean) : UserPrayerSettingEntity {
    return UserPrayerSettingEntity(
        calculationMethod = calculationMethod,
        juristicMethod = juristicMethod,
        masterNotification = masterNotification,
        notificationSettingsJson = notificationSettings.toEntity(),
        isSynced = isSynced,
        updatedAt = updatedAt
    )
}


fun NotificationSettingsMapDto.toEntity() : NotificationSettingsMapEntity {
    return NotificationSettingsMapEntity(
        fajr = fajr.toEntity(),
        dhuhr = dhuhr.toEntity(),
        asr = asr.toEntity(),
        maghrib = maghrib.toEntity(),
        isha = isha.toEntity()
    )
}

fun NotificationSettingsMapEntity.toDtoMap(): NotificationSettingsMapDto {
    return NotificationSettingsMapDto(
        fajr = fajr.toDto(),
        dhuhr = dhuhr.toDto(),
        asr = asr.toDto(),
        maghrib = maghrib.toDto(),
        isha = isha.toDto()
    )
}

fun NotificationSettingDto.toEntity(): NotificationSettingEntity {
    return NotificationSettingEntity(
        enabled = enabled,
        offsetMinutes = offsetMinutes,
        audio = audio
    )
}

fun NotificationSettingEntity.toDto() : NotificationSettingDto{
    return NotificationSettingDto(
        enabled = enabled,
        offsetMinutes = offsetMinutes,
        audio = audio
    )
}


fun PrayerLogEntity.toDto() : PrayerLogSyncDto {
    return PrayerLogSyncDto(
        logDate = logDate,
        fajr = fajr,
        dhuhr = dhuhr,
        asr = asr,
        maghrib = maghrib,
        isha = isha,
        updatedAt = updatedAt
    )
}

fun  PrayerLogSyncDto.toEntity(isSynced: Boolean): PrayerLogEntity {
    return PrayerLogEntity(
        logDate = logDate,
        fajr = fajr,
        dhuhr = dhuhr,
        asr = asr,
        maghrib = maghrib,
        isha = isha,
        isSynced = isSynced,
        updatedAt = updatedAt
    )
}

