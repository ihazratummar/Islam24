package com.hazrat.auth.ui.profiledetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.auth.domain.usecase.UpdateBioUseCase
import com.hazrat.auth.domain.usecase.UpdateNameUseCase
import com.hazrat.auth.domain.usecase.UpdateProfilePictureUseCase
import com.hazrat.auth.ui.profileScreen.ProfileState
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

class ProfileDetailsViewModel(
    private val updateNameUseCase: UpdateNameUseCase,
    private val updateBioUseCase: UpdateBioUseCase,
    private val updateProfilePictureUseCase: UpdateProfilePictureUseCase
) : ViewModel() {

    private val eventChannel = Channel<UserEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

    fun onEvent(event: ProfileDetailsEvent) {
        when (event) {
            is ProfileDetailsEvent.UpdateName -> {
                viewModelScope.launch {
                    when (val result = updateNameUseCase(event.name.fullName ?: "")) {
                        is Result.Error -> eventChannel.send(UserEvent.Error(result.error.asUiText()))
                        is Result.Success -> eventChannel.send(UserEvent.Success(result.data.asSuccessUiText()))
                    }
                }
            }
            is ProfileDetailsEvent.UpdateProfilePicture -> {
                viewModelScope.launch {
                    updateProfilePictureUseCase(event.uri)
                }
            }
            ProfileDetailsEvent.NameUpdateDialog -> {
                _profileState.update { it.copy(isNameDialogOpen = !it.isNameDialogOpen) }
            }
            is ProfileDetailsEvent.NameValue -> {
                _profileState.update { state ->
                    state.copy(userData = state.userData?.copy(fullName = event.name))
                }
            }
            is ProfileDetailsEvent.NewBio -> {
                _profileState.update { state ->
                    state.copy(userData = state.userData?.copy(bio = event.bio))
                }
            }
            ProfileDetailsEvent.BioUpdateDialog -> {
                _profileState.update { it.copy(isBioDialogOpen = !it.isBioDialogOpen) }
            }
            is ProfileDetailsEvent.UpdateBio -> {
                viewModelScope.launch {
                    when (val result = updateBioUseCase(event.bio.bio ?: "")) {
                        is Result.Error -> eventChannel.send(UserEvent.Error(result.error.asUiText()))
                        is Result.Success -> eventChannel.send(UserEvent.Success(result.data.asSuccessUiText()))
                    }
                }
            }
            ProfileDetailsEvent.Refresh -> {}
        }
    }
}

sealed interface UserEvent {
    data class Error(val error: UiText) : UserEvent
    data class Success(val success: UiText) : UserEvent
}