package com.hazrat.islam24.auth.repository

import com.hazrat.islam24.util.error.ForgetPasswordError
import com.hazrat.islam24.util.error.ForgetPasswordSuccess
import com.hazrat.islam24.util.error.Result

/**
 * @author Hazrat Ummar Shaikh
 */

interface ForgetPasswordRepository {

    suspend fun requestOtp(email: String): Result<ForgetPasswordSuccess, ForgetPasswordError>
    suspend fun verifyOtp(email: String, otp: String) : Result<ForgetPasswordSuccess, ForgetPasswordError>
    suspend fun resetPassword(email: String, password: String) : Result<ForgetPasswordSuccess, ForgetPasswordError>
    suspend fun networkObserver()
}