package com.hazrat.islam24.auth.presentation.forgetPassword

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface ForgetPasswordEvent {

    data class EnterEmail(val email: String) : ForgetPasswordEvent
    data class SubmitEmail(val email: String) : ForgetPasswordEvent
    data class EnterOtp(val otp: String) : ForgetPasswordEvent
    data class SubmitOtp(val email: String, val otp: String) : ForgetPasswordEvent
    data class EnterPassword(val password: String) : ForgetPasswordEvent
    data class EnterConfirmPassword(val confirmPassword: String) : ForgetPasswordEvent
    data class SubmitPassword(val email: String, val password: String) : ForgetPasswordEvent
    data object StepBack : ForgetPasswordEvent
    data object OnPasswordVisibilityToggle : ForgetPasswordEvent
    data object OnConfirmPasswordVisibilityToggle : ForgetPasswordEvent

}