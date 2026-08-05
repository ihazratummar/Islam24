package com.hazrat.remote.api

import com.hazrat.remote.dto.AthkarDto
import com.hazrat.utils.Constants.ATHKAR_BASE_URL_NAME
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * @author Hazrat Ummar Shaikh
 */

interface AthkarApiCall {
    suspend fun getAllAthkar(): AthkarDto
}

class AthkarApiCallImpl(
    private val client: HttpClient
) : AthkarApiCall {
    override suspend fun getAllAthkar(): AthkarDto {
        return client.get("$ATHKAR_BASE_URL_NAME/athkar.json").body()
    }
}