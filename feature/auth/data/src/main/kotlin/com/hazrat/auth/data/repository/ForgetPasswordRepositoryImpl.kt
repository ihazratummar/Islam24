package com.hazrat.auth.data.repository

import android.util.Log
import com.hazrat.auth.domain.repository.ForgetPasswordRepository
import com.hazrat.remote.api.Islam24BackendApi
import com.hazrat.remote.dto.RequestOtpBody
import com.hazrat.remote.dto.ResetPasswordBody
import com.hazrat.remote.dto.VerifyOtpBody
import com.hazrat.utils.network.ConnectivityObserver
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.ForgetPasswordError
import com.hazrat.utils.result.error.ForgetPasswordSuccess
import kotlinx.coroutines.flow.first

/**
 * @author Hazrat Ummar Shaikh
 */

class ForgetPasswordRepositoryImpl  (
    private val api: Islam24BackendApi,
    private val connectivityObserver: ConnectivityObserver
) : ForgetPasswordRepository {

    override suspend fun requestOtp(email: String): Result<ForgetPasswordSuccess, ForgetPasswordError> {
        return try {
            val response = api.requestOtp(RequestOtpBody(email))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(data = ForgetPasswordSuccess.SUCCESS_EMAIL_SENT)
                } else {
                    Result.Error(error = ForgetPasswordError.INVALID_EMAIL)
                }
            } else {
                Log.e("ForgetPasswordRepositoryImpl", "resetPassword 1 ")
                Result.Error(error = ForgetPasswordError.INVALID_EMAIL)
            }
        } catch (e: Exception) {
            Log.e("ForgetPasswordRepositoryImpl", "resetPassword: $e")
            return Result.Error(error = ForgetPasswordError.UNKNOWN_ERROR)
        }
    }

    override suspend fun verifyOtp(
        email: String,
        otp: String
    ): Result<ForgetPasswordSuccess, ForgetPasswordError> {
        return try {
            val networkStatus = connectivityObserver.observer().first()
            if (networkStatus == ConnectivityObserver.Status.Available) {
                val response = api.verifyOtp(VerifyOtpBody(email = email, otp = otp))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.Success(data = ForgetPasswordSuccess.SUCCESS_EMAIL_VERIFIED)
                    } else {
                        Result.Error(error = ForgetPasswordError.INVALID_OTP)
                    }
                } else {
                    Result.Error(error = ForgetPasswordError.INVALID_OTP)
                }
            } else {
                return Result.Error(error = ForgetPasswordError.NO_INTERNET)
            }

        } catch (_: Exception) {
            return Result.Error(error = ForgetPasswordError.UNKNOWN_ERROR)
        }
    }

    override suspend fun resetPassword(
        email: String,
        password: String
    ): Result<ForgetPasswordSuccess, ForgetPasswordError> {
        return try {
            val networkStatus = connectivityObserver.observer().first()
            if (networkStatus == ConnectivityObserver.Status.Available) {
                val response =
                    api.resetPassword(ResetPasswordBody(email = email, new_password = password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.Success(data = ForgetPasswordSuccess.SUCCESS_PASSWORD_RESET)
                    } else {
                        Result.Error(error = ForgetPasswordError.INVALID_PASSWORD)
                    }
                } else {
                    Log.e("ForgetPasswordRepositoryImpl", "resetPassword: Error")
                    Result.Error(error = ForgetPasswordError.UNKNOWN_ERROR)
                }
            } else {
                return Result.Error(error = ForgetPasswordError.NO_INTERNET)
            }
        } catch (e: Exception) {
            Log.e("ForgetPasswordRepositoryImpl", "resetPassword: ${e.message}")
            return Result.Error(error = ForgetPasswordError.UNKNOWN_ERROR)
        }
    }
}