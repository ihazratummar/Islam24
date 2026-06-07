package com.hazrat.auth.ui.profileScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hazrat.auth.domain.usecase.ObserveAuthStateUseCase
import com.hazrat.auth.domain.usecase.ObserveUserUseCase
import com.hazrat.model.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */

class ProfileViewModel(
    private val observeUserUseCase: ObserveUserUseCase,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {

    val authState: LiveData<AuthState> = observeAuthStateUseCase().asLiveData()

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

    init {
        observeUserProfile()
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            observeUserUseCase().collect { user ->
                _profileState.update { it.copy(userData = user) }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        // Handle events if any
    }
}