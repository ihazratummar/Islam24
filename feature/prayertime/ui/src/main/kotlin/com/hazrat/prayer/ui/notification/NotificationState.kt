package com.hazrat.prayer.ui.notification

import com.hazrat.model.prayersettingmodel.NotificationSettingMapModel
import com.hazrat.model.prayersettingmodel.NotificationSettingModel
import com.hazrat.model.prayersettingmodel.UserPrayerSettingModel
import com.hazrat.prayer.ui.component.listOfAzan
import com.hazrat.ui.common.PrayerType
import com.hazrat.ui.common.PrayerType.ASR
import com.hazrat.ui.common.PrayerType.DHUHR
import com.hazrat.ui.common.PrayerType.FAJR
import com.hazrat.ui.common.PrayerType.ISHA
import com.hazrat.ui.common.PrayerType.MAGHRIB
import com.hazrat.ui.common.PrayerType.SUNRISE

data class NotificationState(
    val userPrayerSettingModel: UserPrayerSettingModel? = null,
    val isAzanPlaying: List<Boolean> = List(listOfAzan.size) { false },
    val selectedFajrAzan: Int = 0,
    val selectedDhuhrAzan: Int = 0,
    val selectedAsrAzan: Int = 0,
    val selectedMaghribAzan: Int = 0,
    val selectedIshaAzan: Int = 0,
    val isAzanDownloading: Boolean = false
) {
    fun isEnable(prayer: PrayerType): Boolean {
        val notification =
            userPrayerSettingModel?.notificationSettings ?: NotificationSettingMapModel()
        return when (prayer) {
            FAJR -> notification.fajr.enabled
            DHUHR -> notification.dhuhr.enabled
            ASR -> notification.asr.enabled
            MAGHRIB -> notification.maghrib.enabled
            ISHA -> notification.isha.enabled
            SUNRISE -> false
        }
    }

    val notificationCount: Int
        get() {
            val notification = userPrayerSettingModel?.notificationSettings ?: NotificationSettingMapModel()
            return listOf(
                notification.fajr.enabled,
                notification.dhuhr.enabled,
                notification.asr.enabled,
                notification.maghrib.enabled,
                notification.isha.enabled
            ).count { it }
        }
}
