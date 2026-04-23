package com.hazrat.auth.ui.profileScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.domain.repository.ProfileRepository
import com.hazrat.model.AuthState
import com.hazrat.utils.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */


class ProfileViewModel (
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()
    val authState: LiveData<AuthState> = profileRepository.authState

    init {
        viewModelScope.launch {
            profileRepository.checkAuthStatus()
        }
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            when(val result = profileRepository.fetchUserData()){
                is Result.Error -> {
                    _profileState.value = _profileState.value.copy(
                        userData = null
                    )
                }
                is Result.Success -> {
                    _profileState.value = _profileState.value.copy(
                        userData = result.data
                    )
                }
            }
        }
    }
    fun onEvent(event: ProfileEvent) {

    }
}