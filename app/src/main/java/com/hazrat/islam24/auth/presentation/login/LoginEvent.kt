package com.hazrat.islam24.auth.presentation.login

import com.hazrat.islam24.auth.presentation.signup.SingupEvent

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface  LoginEvent {

    data class Login(val email: String, val password: String): LoginEvent

    data class SetEmail(val email: String): LoginEvent

    data class SetPassword(val password: String): LoginEvent

    object OnPasswordVisibilityChanged: LoginEvent

    object Refresh : LoginEvent {

    }
}