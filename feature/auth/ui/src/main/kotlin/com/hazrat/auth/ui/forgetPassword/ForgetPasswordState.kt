package com.hazrat.auth.ui.forgetPassword

data class ForgetPasswordState(
    val email: String = "",
    val otp: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val currentStep: Int = 0,
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isPasswordValid: Boolean = false,
    val passwordUpdateSuccess: Boolean = false,

)
