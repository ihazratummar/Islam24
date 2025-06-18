package com.hazrat.islam24.auth.repository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.hazrat.islam24.auth.api.Islam24BackendApi
import com.hazrat.islam24.auth.model.RequestOtpBody
import com.hazrat.islam24.auth.model.ResetPasswordBody
import com.hazrat.islam24.auth.model.VerifyOtpBody
import com.hazrat.islam24.util.ConnectivityObserver
import com.hazrat.islam24.util.error.ForgetPasswordError
import com.hazrat.islam24.util.error.ForgetPasswordSuccess
import com.hazrat.islam24.util.error.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikhṣ
 */

class ForgetPasswordRepositoryImpl @Inject constructor(
    private val api: Islam24BackendApi,
    private val connectivityObserver: ConnectivityObserver
) : ForgetPasswordRepository {

    private val _networkStatus = mutableStateOf(ConnectivityObserver.Status.Unavailable)


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
            if (_networkStatus.value == ConnectivityObserver.Status.Available) {
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
            if (_networkStatus.value == ConnectivityObserver.Status.Available) {
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


    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override suspend fun networkObserver() {
        connectivityObserver.observer().onEach { status ->
            _networkStatus.value = status
            Log.d("ForgetPasswordRepositoryImpl", "Network Status: $status")
        }.launchIn(repositoryScope)
    }
}