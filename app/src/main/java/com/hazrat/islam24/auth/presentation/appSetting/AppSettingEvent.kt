package com.hazrat.islam24.auth.presentation.appSetting

import com.hazrat.islam24.util.Languages

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface AppSettingEvent {


    data object ClickLanguageDialog : AppSettingEvent
    data class SelectLanguage(val language: Languages) : AppSettingEvent


    data class ChangeTheme(val theme: Themes) : AppSettingEvent
    data object ClickThemeDialog : AppSettingEvent
}

enum class Themes {
    DARK,
    LIGHT
}
