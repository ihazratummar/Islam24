package com.hazrat.islam24.auth.presentation.profiledetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import com.hazrat.islam24.auth.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
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

    val profileState: StateFlow<ProfileState> = profileRepository.profileState

    val profileActionState:LiveData<ProfileAction> = profileRepository.profileActionState

    fun onEvent(profileDetailsEvent: ProfileDetailsEvent) {
        when (profileDetailsEvent) {
            is ProfileDetailsEvent.UpdateName -> {
                viewModelScope.launch {
                    profileRepository.updateName(profileDetailsEvent.name)
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
                    profileRepository.updateBio(profileDetailsEvent.bio)
                }
            }

            ProfileDetailsEvent.Refresh -> {
                profileRepository.refreshProfile()
            }
        }
    }

}