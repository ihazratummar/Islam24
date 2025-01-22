package com.hazrat.islam24.auth.presentation.appSetting

import com.hazrat.islam24.util.Languages

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface AppSettingEvent {


    data object ClickLanguageDialog : AppSettingEvent
    data class SelectLanguage(val language: Languages) : AppSettingEvent


    data object ToggleTheme: AppSettingEvent

    data object OpenAppSetting : AppSettingEvent

    data object SignOut: AppSettingEvent

    data object RefreshAuth: AppSettingEvent

    data object HapticFeedbackClick: AppSettingEvent
}
