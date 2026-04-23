package com.hazrat.remote.api

import com.hazrat.remote.dto.RequestOtpBody
import com.hazrat.remote.dto.RequestOtpResponse
import com.hazrat.remote.dto.ResetPasswordBody
import com.hazrat.remote.dto.ResetPasswordResponse
import com.hazrat.remote.dto.VerifyOtpBody
import com.hazrat.remote.dto.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author Hazrat Ummar Shaikh
 */


interface Islam24BackendApi {
    @POST("request_otp")
    suspend fun requestOtp(@Body body: RequestOtpBody): Response<RequestOtpResponse>

    @POST("verify_otp")
    suspend fun verifyOtp(@Body body: VerifyOtpBody): Response<VerifyOtpResponse>

    @POST("reset_password")
    suspend fun resetPassword(@Body body: ResetPasswordBody): Response<ResetPasswordResponse>
}