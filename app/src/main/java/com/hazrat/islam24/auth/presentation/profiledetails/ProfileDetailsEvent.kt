package com.hazrat.islam24.auth.presentation.profiledetails

import android.net.Uri
import com.hazrat.islam24.auth.model.UserData

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface ProfileDetailsEvent {

    data class UpdateProfilePicture(val uri: Uri) : ProfileDetailsEvent

    data class UpdateName(val name: UserData) : ProfileDetailsEvent
    data object NameUpdateDialog : ProfileDetailsEvent
    data class NameValue(val name: String) : ProfileDetailsEvent

    data object BioUpdateDialog : ProfileDetailsEvent
    data class NewBio(val bio: String) : ProfileDetailsEvent
    data class UpdateBio(val bio: UserData) : ProfileDetailsEvent
    data object Refresh : ProfileDetailsEvent

}

sealed class ProfileAction {
    data object Idle : ProfileAction()
    data object Success : ProfileAction()
    data class Error(val errorMessage: String) : ProfileAction()
    data object Loading : ProfileAction()
}
