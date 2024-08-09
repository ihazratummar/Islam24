package com.hazrat.islam24.auth.presentation.appSetting

import com.hazrat.islam24.util.Languages

data class AppSettingState(
    val currentLanguage: String ,
    val isLanguageDialogOpen: Boolean = false,
    val isThemeDialogOpen: Boolean = false,
    val isDarkMode: Boolean,
    val currentTheme: Themes
)
