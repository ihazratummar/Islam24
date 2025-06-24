package com.hazrat.utils.result.error



/**
 * @author Hazrat Ummar Shaikh
 */

enum class UserDataError : Error {
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

enum class ForgetPasswordSuccess{
    SUCCESS_EMAIL_SENT,
    SUCCESS_EMAIL_VERIFIED,
    SUCCESS_PASSWORD_RESET
}

enum class ForgetPasswordError: Error {
    NO_INTERNET,
    INVALID_EMAIL,
    INVALID_OTP,
    INVALID_PASSWORD,
    UNKNOWN_ERROR
}