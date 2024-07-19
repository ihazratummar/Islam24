package com.hazrat.islam24.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.islam24.auth.presentation.AuthEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState


    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun onEvent(event:AuthEvent){
        when(event){
            AuthEvent.SignOut -> {
                signOut()
                _authState.value = AuthState.Unauthenticated
            }

            AuthEvent.Refresh -> {
                checkAuthStatus()
            }
        }
    }


    fun signOut(){
        auth.signOut()
    }
}