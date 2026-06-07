package com.hazrat.auth.ui.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false
)
