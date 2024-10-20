package com.hazrat.islam24.auth.presentation.profileScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {


    val profileState: StateFlow<ProfileState> = profileRepository.profileState
    init {
        profileRepository.checkAuthStatus()
    }
    val authState: LiveData<AuthState> = profileRepository.authState
    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.RateUs -> {
                profileRepository.rateUs(event.activity)
//                profileRepository.openRatingDialog()
            }
            ProfileEvent.InviteFriend -> {
                profileRepository.inviteFriend()
            }

            ProfileEvent.OpenRatingDialog -> {
                profileRepository.openRatingDialog()
            }
            ProfileEvent.GoToRate -> {
                profileRepository.goToRate()
            }
        }
    }
}