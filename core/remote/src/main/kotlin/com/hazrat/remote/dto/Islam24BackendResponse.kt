package com.hazrat.remote.dto

/**
 * @author Hazrat Ummar Shaikh
 */

data class RequestOtpBody(val email: String)
data class VerifyOtpBody(val email: String, val otp: String)
data class ResetPasswordBody(val email: String, val new_password: String)

data class RequestOtpResponse(val message: String)
data class VerifyOtpResponse(val message: String)
data class ResetPasswordResponse(val message: String)