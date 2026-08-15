package com.hazrat.remote.dto.sync

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class NotificationSettingDto(
    val enabled: Boolean = false,
    val offsetMinutes: Int = 0,
    val audio: String =  "default"
)

@Serializable
data class NotificationSettingsMapDto(
    val fajr: NotificationSettingDto,
    val dhuhr: NotificationSettingDto,
    val asr: NotificationSettingDto,
    val maghrib: NotificationSettingDto,
    val isha: NotificationSettingDto,
)


@Serializable
data class PrayerSettingSyncDto(
    val calculationMethod: Int,
    val juristicMethod: Int,
    val masterNotification: Boolean,
    val notificationSettings: NotificationSettingsMapDto,
    val updatedAt: Instant
)
