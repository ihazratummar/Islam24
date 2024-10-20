package com.hazrat.islam24.auth.api


import com.hazrat.islam24.auth.model.RequestOtpBody
import com.hazrat.islam24.auth.model.RequestOtpResponse
import com.hazrat.islam24.auth.model.ResetPasswordBody
import com.hazrat.islam24.auth.model.ResetPasswordResponse
import com.hazrat.islam24.auth.model.VerifyOtpBody
import com.hazrat.islam24.auth.model.VerifyOtpResponse
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



