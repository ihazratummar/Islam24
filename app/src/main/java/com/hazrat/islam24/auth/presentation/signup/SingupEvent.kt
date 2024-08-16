package com.hazrat.islam24.auth.presentation.signup

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface SingupEvent {

    data class Signup(val name: String, val email: String, val password: String, val confirmPassword: String): SingupEvent

    data class SetName(val name: String): SingupEvent

    data class SetEmail(val email: String): SingupEvent

    data class SetPassword(val password: String): SingupEvent

    data class SetConfirmPassword(val confirmPassword: String): SingupEvent

    object OnPasswordVisibilityChanged: SingupEvent

    object OnConfirmPasswordVisibilityChanged: SingupEvent

    object Refresh: SingupEvent



}