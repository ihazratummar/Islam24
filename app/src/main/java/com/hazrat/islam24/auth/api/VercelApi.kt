package com.hazrat.islam24.auth.api


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

/**
 * @author Hazrat Ummar Shaikh
 */

@Singleton
interface VercelApi {
    @POST("request_otp")
    suspend fun requestOtp(@Body body: RequestOtpBody): Response<RequestOtpResponse>

    @POST("verify_otp")
    suspend fun verifyOtp(@Body body: VerifyOtpBody): Response<VerifyOtpResponse>

    @POST("reset_password")
    suspend fun resetPassword(@Body body: ResetPasswordBody): Response<ResetPasswordResponse>
}

data class RequestOtpBody(val email: String)
data class VerifyOtpBody(val email: String, val otp: String)
data class ResetPasswordBody(val email: String, val new_password: String)

data class RequestOtpResponse(val message: String)
data class VerifyOtpResponse(val message: String)
data class ResetPasswordResponse(val message: String)

