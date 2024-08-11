package com.hazrat.islam24.auth.presentation.profiledetails

import androidx.lifecycle.ViewModel
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import com.hazrat.islam24.auth.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
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

    fun onEvent(profileDetailsEvent: ProfileDetailsEvent) {
        when (profileDetailsEvent) {
            is ProfileDetailsEvent.UpdateName -> {
                profileRepository.updateName(profileDetailsEvent.name)
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
                profileRepository.updateBio(profileDetailsEvent.bio)
            }

            ProfileDetailsEvent.RefreshProfilePicture -> {
                profileRepository.refreshProfilePicture()
            }

            ProfileDetailsEvent.RefreshProfileBio -> {
                profileRepository.refreshBio()
            }
            ProfileDetailsEvent.RefreshProfileName -> {
                profileRepository.refreshName()
            }
        }
    }

}