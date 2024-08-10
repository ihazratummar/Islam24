package com.hazrat.islam24.auth.presentation.appSetting

import com.hazrat.islam24.util.Languages

data class AppSettingState(
    val currentLanguage: Languages ,
    val isLanguageDialogOpen: Boolean = false,
    val isThemeDialogOpen: Boolean = false,
    val isDarkMode: Boolean = true,
    val currentTheme: Themes,
)
