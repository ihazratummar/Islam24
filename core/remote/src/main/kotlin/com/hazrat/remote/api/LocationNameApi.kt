package com.hazrat.remote.api

import com.hazrat.remote.dto.LocationNameDto
import com.hazrat.utils.Constants.LOCATION_IQ_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Interface representing the API service for retrieving location names based on latitude and longitude.
 */
interface LocationNameApi {
    suspend fun getLocationName(
        key: String,
        lat: Double,
        lon: Double,
        format: String = "json"
    ): LocationNameDto
}

class LocationNameApiImpl(
    private val client: HttpClient
) : LocationNameApi {
    override suspend fun getLocationName(
        key: String,
        lat: Double,
        lon: Double,
        format: String
    ): LocationNameDto {
        return client.get("${LOCATION_IQ_BASE_URL}reverse") {
            parameter("key", key)
            parameter("lat", lat)
            parameter("lon", lon)
            parameter("format", format)
        }.body()
    }
}
