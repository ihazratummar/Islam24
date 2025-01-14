package com.hazrat.islam24.core.presentation.prayertime.notification

import com.hazrat.islam24.util.datastore.NotificationType
import com.hazrat.islam24.util.datastore.PrayerName

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface NotificationEvent {

    data object ToggleFajrNotification : NotificationEvent
    data object ToggleDhuhrNotification : NotificationEvent
    data object ToggleAsrNotification : NotificationEvent
    data object ToggleMaghribNotification : NotificationEvent
    data object ToggleIshaNotification : NotificationEvent

    data object RefreshNotificationState: NotificationEvent


    data class OnFajrAzanClick(val resourceInd: Int) : NotificationEvent
    data class OnDhuhrAzanClick(val resourceInd: Int) : NotificationEvent
    data class OnAsrAzanClick(val resourceInd: Int) : NotificationEvent
    data class OnMaghribAzanClick(val resourceInd: Int) : NotificationEvent
    data class OnIshaAzanClick(val resourceInd: Int) : NotificationEvent

    data class OnAzanPlayClick(val resourceInd: Int, val aazanIndex: Int) : NotificationEvent
    data object StopAzan : NotificationEvent

    data class OnDefaultNotificationClick(val azanName: PrayerName, val notificationType: NotificationType) : NotificationEvent
    data class OnSilentNotificationClick(val azanName: PrayerName, val notificationType: NotificationType) : NotificationEvent

    data class SelectFajrAzanOption(val index: Int) : NotificationEvent
    data class SelectDhuhrAzanOption(val index: Int) : NotificationEvent
    data class SelectAsrAzanOption(val index: Int) : NotificationEvent
    data class SelectMaghribAzanOption(val index: Int) : NotificationEvent
    data class SelectIshaAzanOption(val index: Int) : NotificationEvent


}