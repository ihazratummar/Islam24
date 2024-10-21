package com.hazrat.islam24.core.presentation.prayertime.notification

data class NotificationState(
    val isFajrNotification: Boolean = false,
    val isDhuhrNotification: Boolean = false,
    val isAsrNotification: Boolean = false,
    val isMaghribNotification: Boolean = false,
    val isIshaNotification: Boolean = false,
)
