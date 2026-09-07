package com.hazrat.utils.result.error

/**
 * @author Hazrat Ummar Shaikh
 */

enum class AuthError : Error {
    EMAIL_ALREADY_IN_USE,
    INVALID_CREDENTIALS,
    USER_NOT_FOUND,
    WRONG_PASSWORD,
    WEAK_PASSWORD,
    NO_INTERNET,
    USER_CANCELLED,
    UNKNOWN_ERROR
}