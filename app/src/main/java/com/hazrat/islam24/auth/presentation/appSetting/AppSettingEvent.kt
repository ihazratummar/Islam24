package com.hazrat.islam24.auth.presentation.appSetting

import com.hazrat.islam24.auth.presentation.AuthEvent
import com.hazrat.islam24.util.Languages

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface AppSettingEvent {


    data object ClickLanguageDialog : AppSettingEvent
    data class SelectLanguage(val language: Languages) : AppSettingEvent


    data class ChangeTheme(val theme: Themes) : AppSettingEvent
    data object ClickThemeDialog : AppSettingEvent

    data object OpenAppSetting : AppSettingEvent

    data object SignOut: AppSettingEvent

    data object RefreshAuth: AppSettingEvent
}

enum class Themes {
    DARK,
    LIGHT
}
