package com.hazrat.islam24.auth.presentation.profileScreen

import android.app.Activity

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface ProfileEvent {
    data object InviteFriend : ProfileEvent
    data class RateUs(val activity: Activity) : ProfileEvent
    data object OpenRatingDialog: ProfileEvent
    data object GoToRate: ProfileEvent

}

