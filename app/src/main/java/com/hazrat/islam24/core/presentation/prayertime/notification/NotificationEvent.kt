package com.hazrat.islam24.core.presentation.prayertime.notification

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface NotificationEvent {

    data object ToggleFajrNotification : NotificationEvent
    data object ToggleDhuhrNotification : NotificationEvent
    data object ToggleAsrNotification : NotificationEvent
    data object ToggleMaghribNotification : NotificationEvent
    data object ToggleIshaNotification : NotificationEvent


}