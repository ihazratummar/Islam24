package com.hazrat.islam24.auth.presentation.profileScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun onEvent(event: ProfileEvent){
        when(event){
            ProfileEvent.InviteFriend -> {
                profileRepository.inviteFriend()
            }
            ProfileEvent.RateUs -> TODO()
        }
    }
}