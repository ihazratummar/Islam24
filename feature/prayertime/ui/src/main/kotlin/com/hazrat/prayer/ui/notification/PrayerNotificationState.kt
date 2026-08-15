package com.hazrat.prayer.ui.notification

import com.hazrat.model.Prayer
import com.hazrat.model.prayersettingmodel.NotificationSettingMapModel
import com.hazrat.model.prayersettingmodel.NotificationSettingModel
import com.hazrat.model.prayersettingmodel.UserPrayerSettingModel

data class PrayerNotificationState(
    val vibrationStates: Map<Prayer, Boolean> = mapOf(
        Prayer.FAJR to true,
        Prayer.DHUHR to true,
        Prayer.ASR to true,
        Prayer.MAGHRIB to true,
        Prayer.ISHA to true
    ),
    val expandedPrayers: Set<Prayer> = setOf(Prayer.FAJR),
    val activePreAlertSheetPrayer: Prayer? = null,
    val activeAzanSoundSheetPrayer: Prayer? = null,
    val userPrayerSettingModel: UserPrayerSettingModel? = null
) {
    // 1. Dynamic Map of Prayer to Enabled State (Derived from userPrayerSettingModel)
    val enabledPrayers: Map<Prayer, Boolean>
        get() {
            val settings = userPrayerSettingModel?.notificationSettings ?:  NotificationSettingMapModel()
            return mapOf(
                Prayer.FAJR to settings.fajr.enabled,
                Prayer.DHUHR to settings.dhuhr.enabled,
                Prayer.ASR to settings.asr.enabled,
                Prayer.MAGHRIB to settings.maghrib.enabled,
                Prayer.ISHA to settings.isha.enabled
            )
        }

    // 2. Dynamic Map of Prayer to Pre-Alert Offsets (Derived from userPrayerSettingModel)
    val preAlertOffsets: Map<Prayer, Int>
        get() {
            val settings = userPrayerSettingModel?.notificationSettings ?:  NotificationSettingMapModel()
            return mapOf(
                Prayer.FAJR to settings.fajr.offsetMinutes,
                Prayer.DHUHR to settings.dhuhr.offsetMinutes,
                Prayer.ASR to settings.asr.offsetMinutes,
                Prayer.MAGHRIB to settings.maghrib.offsetMinutes,
                Prayer.ISHA to settings.isha.offsetMinutes
            )
        }

    // 3. Dynamic Map of Prayer to Azan Sounds (Derived from userPrayerSettingModel)
    val azanSounds: Map<Prayer, String>
        get() {
            val settings = userPrayerSettingModel?.notificationSettings ?:  NotificationSettingMapModel()
            return mapOf(
                Prayer.FAJR to settings.fajr.audio,
                Prayer.DHUHR to settings.dhuhr.audio,
                Prayer.ASR to settings.asr.audio,
                Prayer.MAGHRIB to settings.maghrib.audio,
                Prayer.ISHA to settings.isha.audio
            )
        }

    // Helper method to get setting for a single prayer
    fun getSetting(prayer: Prayer): NotificationSettingModel {
        val settings = userPrayerSettingModel?.notificationSettings ?:  NotificationSettingMapModel()
        return when (prayer) {
            Prayer.FAJR -> settings.fajr
            Prayer.DHUHR -> settings.dhuhr
            Prayer.ASR -> settings.asr
            Prayer.MAGHRIB -> settings.maghrib
            Prayer.ISHA -> settings.isha
        }
    }

    val enabledCount: Int
        get() = enabledPrayers.values.count { it }

    val isAllActive: Boolean
        get() = enabledCount == 5
}
