package com.hazrat.islam24.auth.presentation.profileScreen

import android.net.Uri
import com.hazrat.islam24.auth.model.UserData

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface ProfileEvent {
    data object InviteFriend : ProfileEvent
    data object RateUs : ProfileEvent

}

