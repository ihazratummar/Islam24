package com.hazrat.prayer.ui.notification

import com.hazrat.prayer.ui.component.listOfAzan
import com.hazrat.ui.common.PrayerType
import com.hazrat.ui.common.PrayerType.ASR
import com.hazrat.ui.common.PrayerType.DHUHR
import com.hazrat.ui.common.PrayerType.FAJR
import com.hazrat.ui.common.PrayerType.ISHA
import com.hazrat.ui.common.PrayerType.MAGHRIB
import com.hazrat.ui.common.PrayerType.SUNRISE

data class NotificationState(
    val isFajrNotification: Boolean = false,
    val isDhuhrNotification: Boolean = false,
    val isAsrNotification: Boolean = false,
    val isMaghribNotification: Boolean = false,
    val isIshaNotification: Boolean = false,

    val isAzanPlaying: List<Boolean> = List(listOfAzan.size) { false },

    val selectedFajrAzan: Int = 0,
    val selectedDhuhrAzan: Int = 0,
    val selectedAsrAzan: Int = 0,
    val selectedMaghribAzan: Int = 0,
    val selectedIshaAzan: Int = 0,

    val isAzanDownloading : Boolean = false
){
    fun isEnable(prayer: PrayerType): Boolean {
        return when(prayer){
            FAJR -> isFajrNotification
            DHUHR -> isDhuhrNotification
            ASR -> isAsrNotification
            MAGHRIB -> isMaghribNotification
            ISHA -> isIshaNotification
            SUNRISE -> false
        }
    }

    fun notificationCount(): Int {
        return listOf(
            isFajrNotification,
            isDhuhrNotification,
            isAsrNotification,
            isMaghribNotification,
            isIshaNotification
        ).count{it}
    }
}
