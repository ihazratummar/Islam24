package com.hazrat.remote.api.apiData

import android.util.Log
import com.hazrat.remote.api.profile.ProfileApi
import com.hazrat.remote.dto.UserSupportStatusDto
import com.hazrat.remote.dto.auth.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.request


/**
 * @author hazratummar
 * Created on 07/08/26
 */

class ProfileApiImpl(
    private val httpClient: HttpClient
) : ProfileApi {

    companion object {
        private val TAG = "ProfileApiImpl"
    }

    override suspend fun getProfile(): UserDto? {
        try {
            val response = httpClient.get("profile/me")

            return if (response.status.value == 200) {
                response.body<UserDto>()
            } else {
                Log.e(TAG, "Failed to the profile ${response.status} ${response.request.url}")
                null
            }

        } catch (e: Exception) {
            Log.e(TAG, "Failed to the profile ${e.message}")
            return null
        }
    }

    override suspend fun getSupportStatus(): UserSupportStatusDto? {
        return try {
            val response  = httpClient.get("profile/support-status")
            if (response.status.value == 200 ){
                response.body<UserSupportStatusDto>()
            }else{
                Log.e(TAG, "Failed to get support status: ${response.status}")
                null
            }
        }catch (e: Exception){
            Log.e(TAG, "Failed to get support status: ${e.message}")
            null
        }
    }
}