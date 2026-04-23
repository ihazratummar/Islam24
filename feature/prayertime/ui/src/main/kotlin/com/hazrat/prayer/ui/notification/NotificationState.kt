package com.hazrat.prayer.ui.notification

import com.hazrat.prayer.ui.component.listOfAzan

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
)
