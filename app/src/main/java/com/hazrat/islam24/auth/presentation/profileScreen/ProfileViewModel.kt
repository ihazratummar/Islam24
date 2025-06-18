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
    profileRepository: ProfileRepository
) : ViewModel() {


    val profileState: StateFlow<ProfileState> = profileRepository.profileState
    val authState: LiveData<AuthState> = profileRepository.authState
    init {
        profileRepository.checkAuthStatus()
    }
    fun onEvent(event: ProfileEvent) {

    }
}