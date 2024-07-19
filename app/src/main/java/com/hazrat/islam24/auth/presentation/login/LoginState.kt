package com.hazrat.islam24.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    var passwordVisible: Boolean = false,
    val isFormValid: Boolean = false,
    )
