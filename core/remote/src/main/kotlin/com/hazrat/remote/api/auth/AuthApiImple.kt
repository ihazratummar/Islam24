package com.hazrat.remote.api.auth

import android.util.Log
import com.hazrat.remote.api.AuthApiCall
import com.hazrat.remote.dto.auth.AuthResponse
import com.hazrat.remote.dto.auth.GoogleLoginRequest
import com.hazrat.remote.dto.auth.LogoutRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode

/**
 * @author hazratummar
 * Created on 05/08/26
 */

class AuthApiImple(
    private val httpClient: HttpClient
) : AuthApiCall {

    override suspend fun googleLogin(request: GoogleLoginRequest): AuthResponse? {
        try {
            val response = httpClient.post("auth/google") {
                setBody(request)
            }
            Log.d("AuthImpl", "Request URL: ${response.request.url}")
            Log.d("AuthImpl", "Google Response Status: ${response.status}")
            return if (response.status.value in 200..299) {
                response.body<AuthResponse>()
            } else {
                Log.e("AuthError", "Error login ${response.status}: ${response.bodyAsText()}")
                null
            }
        } catch (e: Exception) {
            Log.e("AuthError", "Error login exception: $e", e)
            return null
        }
    }

    override suspend fun logout(request: LogoutRequest): Boolean {
        return try {
            val response = httpClient.post("auth/logout") {
                setBody(request)
            }
            response.status == HttpStatusCode.NoContent
        } catch (e: Exception) {
            Log.e("AuthError", "Failed to logout $e")
            false
        }
    }
}