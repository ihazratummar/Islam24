package com.hazrat.remote.api.profile

import com.hazrat.remote.dto.UserSupportStatusDto
import com.hazrat.remote.dto.auth.UserDto


/**
 * @author hazratummar
 * Created on 07/08/26
 */

interface ProfileApi {

    suspend fun getProfile(): UserDto?
    suspend fun getSupportStatus(): UserSupportStatusDto?

}