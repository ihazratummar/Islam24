package com.hazrat.islam24.auth.presentation.appSetting

import com.hazrat.islam24.util.Languages

data class AppSettingState(
    val currentLanguage: Languages ? = null ,
    val isLanguageDialogOpen: Boolean = false,
    val isHapticFeedbackEnabled: Boolean = false,
    val toggleTheme: Boolean = false,
    val isRatingDialogOpen: Boolean = false
)
