package com.hazrat.islam24.auth.presentation.profiledetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import com.hazrat.ui.UiText.UiText
import com.hazrat.ui.UiText.asSuccessUiText
import com.hazrat.ui.UiText.asUiText
import com.hazrat.utils.result.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */


class ProfileDetailsViewModel (
    private val profileRepository: ProfileRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            profileRepository.checkAuthStatus()
        }
    }

    private val eventChannel = Channel<UserEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()



    fun onEvent(profileDetailsEvent: ProfileDetailsEvent) {
        when (profileDetailsEvent) {
            is ProfileDetailsEvent.UpdateName -> {
                viewModelScope.launch {
                    when(val result = profileRepository.updateName(profileDetailsEvent.name)){
                        is Result.Error -> {
                            val errorMessage = result.error.asUiText()
                            eventChannel.send(UserEvent.Error(errorMessage))
                        }
                        is Result.Success -> {
                            val successMessage = result.data.asSuccessUiText()
                            eventChannel.send(UserEvent.Success(successMessage))
                        }
                    }
                }
            }
            is ProfileDetailsEvent.UpdateProfilePicture -> {
                profileRepository.updateProfilePicture(profileDetailsEvent.uri)
            }
            ProfileDetailsEvent.NameUpdateDialog -> {
                _profileState.update {
                    it.copy(
                        isNameDialogOpen = !_profileState.value.isNameDialogOpen
                    )
                }
            }
            is ProfileDetailsEvent.NameValue -> {
                _profileState.update {
                    it.copy(
                        userData = it.userData?.copy(
                            fullName = profileDetailsEvent.name
                        )
                    )
                }
            }

            is ProfileDetailsEvent.NewBio -> {
                _profileState.update {
                    it.copy(
                        userData = it.userData?.copy(
                            bio = profileDetailsEvent.bio
                        )
                    )
                }
            }

            ProfileDetailsEvent.BioUpdateDialog -> {
                _profileState.update {
                    it.copy(
                        isBioDialogOpen = !_profileState.value.isBioDialogOpen
                    )
                }
            }
            is ProfileDetailsEvent.UpdateBio -> {
                viewModelScope.launch {
                    when(val result =profileRepository.updateBio(profileDetailsEvent.bio)){
                        is Result.Error -> {
                            val errorMessage = result.error.asUiText()
                            eventChannel.send(UserEvent.Error(errorMessage))
                        }
                        is Result.Success -> {
                            val successMessage = result.data.asSuccessUiText()
                            eventChannel.send(UserEvent.Success(successMessage))
                        }
                    }
                }
            }

            ProfileDetailsEvent.Refresh -> {
//                profileRepository.refreshProfile()
            }
        }
    }

}

sealed interface UserEvent{
    data class Error(val error: UiText): UserEvent
    data class Success(val success: UiText): UserEvent
}