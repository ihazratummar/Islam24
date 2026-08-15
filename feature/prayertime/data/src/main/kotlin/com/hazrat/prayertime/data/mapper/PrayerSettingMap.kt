package com.hazrat.prayertime.data.mapper

import com.hazrat.database.converter.NotificationSettingEntity
import com.hazrat.database.converter.NotificationSettingsMapEntity
import com.hazrat.database.entity.prayer.UserPrayerSettingEntity
import com.hazrat.model.prayersettingmodel.NotificationSettingMapModel
import com.hazrat.model.prayersettingmodel.NotificationSettingModel
import com.hazrat.model.prayersettingmodel.UserPrayerSettingModel


/**
 * @author hazratummar
 * Created on 15/08/26
 */
 


fun UserPrayerSettingEntity.toModel(): UserPrayerSettingModel {
    return UserPrayerSettingModel(
        calculationMethod = calculationMethod,
        juristicMethod = juristicMethod,
        masterNotification = masterNotification,
        notificationSettings = notificationSettingsJson.toModel()
    )
}

fun NotificationSettingsMapEntity.toModel(): NotificationSettingMapModel {
    return NotificationSettingMapModel(
        fajr = fajr.toModel(),
        dhuhr = dhuhr.toModel(),
        asr = asr.toModel(),
        maghrib = maghrib.toModel(),
        isha = isha.toModel()
    )
}

fun NotificationSettingEntity.toModel(): NotificationSettingModel {
    return NotificationSettingModel(
        enabled = enabled,
        offsetMinutes =offsetMinutes,
        audio = audio
    )
}