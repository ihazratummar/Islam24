package com.hazrat.auth.ui.login

import android.content.Context

data class LoginState(
    val isLoading: Boolean = false
)

sealed interface LoginEvent {
    data class GoogleSignInClick(val context: Context) : LoginEvent
}


sealed interface LoginEffect {
    data class Error(val message: String) : LoginEffect
    data class Success(val message: String) : LoginEffect
    data object NavigateBack: LoginEffect
}
