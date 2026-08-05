package com.hazrat.auth.ui.appSetting

import com.hazrat.model.profile.UserModel

data class ProfileState(
    val isLoading: Boolean = false,
    val isHapticFeedbackEnabled: Boolean = false,
    val toggleTheme: Boolean = false,
    val isRatingDialogOpen: Boolean = false,
    val totalPrayersLogged: Int = 0,
    val prayerStreak: Int = 0,
    val totalBookmarkedAyahs: Int = 0,
    val isMasterNotificationEnabled: Boolean = true,
    val userModel: UserModel? = null
)
