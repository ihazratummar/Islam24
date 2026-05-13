package com.hazrat.auth.ui.signup

data class SingupState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val isFormValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isLoading: Boolean = false
)
