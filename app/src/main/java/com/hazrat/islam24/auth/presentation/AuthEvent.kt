package com.hazrat.islam24.auth.presentation

/**
 * @author Hazrat Ummar Shaikh
 */

sealed interface AuthEvent {

    object SignOut: AuthEvent

    object Refresh: AuthEvent

}