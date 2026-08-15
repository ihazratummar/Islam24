package com.hazrat.model.prayersettingmodel

data class UserPrayerSettingModel(
    val calculationMethod: Int = 1,
    val juristicMethod: Int = 0,
    val masterNotification: Boolean= true,
    val notificationSettings: NotificationSettingMapModel = NotificationSettingMapModel()
)


data class NotificationSettingMapModel(
    val fajr: NotificationSettingModel = NotificationSettingModel(),
    val dhuhr: NotificationSettingModel = NotificationSettingModel(),
    val asr: NotificationSettingModel = NotificationSettingModel(),
    val maghrib: NotificationSettingModel = NotificationSettingModel(),
    val isha: NotificationSettingModel = NotificationSettingModel()
)

data class NotificationSettingModel(
    val enabled: Boolean = true,
    val offsetMinutes: Int = 0,
    val audio: String = "default"
)