package com.hazrat.auth.domain.repository

import com.hazrat.utils.result.error.ForgetPasswordError
import com.hazrat.utils.result.error.ForgetPasswordSuccess
import com.hazrat.utils.result.Result

/**
 * @author Hazrat Ummar Shaikh
 */

interface ForgetPasswordRepository {

    suspend fun requestOtp(email: String): Result<ForgetPasswordSuccess, ForgetPasswordError>
    suspend fun verifyOtp(email: String, otp: String) : Result<ForgetPasswordSuccess, ForgetPasswordError>
    suspend fun resetPassword(email: String, password: String) : Result<ForgetPasswordSuccess, ForgetPasswordError>
}