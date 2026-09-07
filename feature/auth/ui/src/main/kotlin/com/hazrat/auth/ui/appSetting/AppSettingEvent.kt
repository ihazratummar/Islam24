package com.hazrat.auth.ui.appSetting

import android.app.Activity

/**
 * @author Hazrat Ummar Shaikh
 */
sealed interface AppSettingEvent {

    data object ToggleTheme : AppSettingEvent
    data object ToggleMasterNotification : AppSettingEvent
    data object OpenAppSetting : AppSettingEvent
    data object HapticFeedbackClick : AppSettingEvent

    data object ShareApp : AppSettingEvent
    data object SupportIslam24 : AppSettingEvent
    data class RateUs(val activity: Activity) : AppSettingEvent
    data object OpenRatingDialog : AppSettingEvent
    data object GoToRate : AppSettingEvent
    data class UpdateLanguage(val languageCode: String) : AppSettingEvent

    data object LogOut : AppSettingEvent
}


sealed interface ProfileEffect {
    data class Error(val error: String) : ProfileEffect
}