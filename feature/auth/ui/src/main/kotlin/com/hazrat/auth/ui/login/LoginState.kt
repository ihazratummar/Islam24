package com.hazrat.auth.ui.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    var passwordVisible: Boolean = false,
    val isFormValid: Boolean = false,
    )
