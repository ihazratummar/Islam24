package com.hazrat.remote.api

import com.hazrat.remote.dto.AllahNamesDto
import com.hazrat.utils.Constants.BASE_URL_NAME
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Interface representing the API service for retrieving the names of Allah.
 */
interface NamesApi {
    suspend fun getAllNames(): AllahNamesDto
}

class NamesApiImpl(
    private val client: HttpClient
) : NamesApi {
    override suspend fun getAllNames(): AllahNamesDto {
        return client.get("${BASE_URL_NAME}99_Names_Of_Allah.json").body()
    }
}