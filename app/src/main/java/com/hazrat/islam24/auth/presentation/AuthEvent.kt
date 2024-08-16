package com.hazrat.islam24.auth.presentation

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface AuthEvent {

    data object SignOut: AuthEvent
}