package com.hazrat.islam24.auth.presentation.login

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface  LoginEvent {

    data class Login(val email: String, val password: String): LoginEvent

    data class SetEmail(val email: String): LoginEvent

    data class SetPassword(val password: String): LoginEvent

    data object OnPasswordVisibilityChanged: LoginEvent

    data object Refresh : LoginEvent
}