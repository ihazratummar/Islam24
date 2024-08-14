package com.hazrat.islam24.util.error

/**
 * @author Hazrat Ummar Shaikh
 */

enum class UserDataError : Error{
    NO_INTERNET,
    INVALID_NAME,
    INVALID_BIO,
    INVALID_USER_ID,
    UNKNOWN_ERROR
}

enum class UserDataSuccess {
    SUCCESS_NAME_UPDATE,
    SUCCESS_BIO_UPDATE
}