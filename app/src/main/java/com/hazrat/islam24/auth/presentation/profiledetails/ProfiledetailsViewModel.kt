package com.hazrat.islam24.auth.presentation.profiledetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.presentation.UiText
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.utils.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    init {
        profileRepository.checkAuthStatus()
    }

    private val eventChannel = Channel<UserEvent>()
    val events = eventChannel.receiveAsFlow()


    val profileState: StateFlow<ProfileState> = profileRepository.profileState

    val profileActionState:LiveData<ProfileAction> = profileRepository.profileActionState

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
                profileRepository.clickNameUpdateDialog()
            }
            is ProfileDetailsEvent.NameValue -> {
                profileRepository.updateNameValue(profileDetailsEvent.name)
            }

            is ProfileDetailsEvent.NewBio -> {
                profileRepository.updateBioValue(profileDetailsEvent.bio)
            }

            ProfileDetailsEvent.BioUpdateDialog -> {
                profileRepository.clickBioUpdateDialog()
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
                profileRepository.refreshProfile()
            }
        }
    }

}

sealed interface UserEvent{
    data class Error(val error: UiText): UserEvent
    data class Success(val success: UiText): UserEvent
}