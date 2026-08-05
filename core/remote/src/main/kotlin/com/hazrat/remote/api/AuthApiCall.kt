package com.hazrat.remote.api

import com.hazrat.remote.dto.auth.AuthResponse
import com.hazrat.remote.dto.auth.GoogleLoginRequest
import com.hazrat.remote.dto.auth.LogoutRequest


/**
 * @author hazratummar
 * Created on 05/08/26
 */

interface AuthApiCall {


    suspend fun googleLogin(request: GoogleLoginRequest): AuthResponse ?

    suspend fun logout(request: LogoutRequest): Boolean

}