package com.hazrat.islam24.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.auth.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val authState: LiveData<AuthState> = profileRepository.authState

    init {
        viewModelScope.launch {
            profileRepository.networkObserver()
            profileRepository.checkAuthStatus()
        }
    }
}