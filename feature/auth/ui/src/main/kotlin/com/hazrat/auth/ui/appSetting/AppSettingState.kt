package com.hazrat.auth.ui.appSetting


import com.hazrat.model.Languages

data class AppSettingState(
    val currentLanguage: Languages ? = null ,
    val isLanguageDialogOpen: Boolean = false,
    val isHapticFeedbackEnabled: Boolean = false,
    val toggleTheme: Boolean = false,
    val isRatingDialogOpen: Boolean = false
)
