package com.hazrat.prayer.ui.notification

import com.hazrat.datastore.NotificationType
import com.hazrat.datastore.PrayerName

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


    data class OnFajrAzanClick(val azanUrl: String, val fileName: String) : NotificationEvent
    data class OnDhuhrAzanClick(val azanUrl: String, val fileName: String) : NotificationEvent
    data class OnAsrAzanClick(val azanUrl: String, val fileName: String) : NotificationEvent
    data class OnMaghribAzanClick(val azanUrl: String, val fileName: String) : NotificationEvent
    data class OnIshaAzanClick(val azanUrl: String, val fileName: String) : NotificationEvent

    data class OnAzanPlayClick(val fileName: String, val aazanIndex: Int, val azanUrl: String) : NotificationEvent
    data object StopAzan : NotificationEvent

    data class OnDefaultNotificationClick(val azanName: PrayerName, val notificationType: NotificationType) : NotificationEvent
    data class OnSilentNotificationClick(val azanName: PrayerName, val notificationType: NotificationType) : NotificationEvent

    data class SelectFajrAzanOption(val index: Int) : NotificationEvent
    data class SelectDhuhrAzanOption(val index: Int) : NotificationEvent
    data class SelectAsrAzanOption(val index: Int) : NotificationEvent
    data class SelectMaghribAzanOption(val index: Int) : NotificationEvent
    data class SelectIshaAzanOption(val index: Int) : NotificationEvent


}

//enum class PrayerName {
//    FAJR,
//    DHUHR,
//    ASR,
//    MAGHRIB,
//    ISHA
//}
//
//enum class NotificationType {
//    DEFAULT,
//    AZAN,
//    SILENT
//}