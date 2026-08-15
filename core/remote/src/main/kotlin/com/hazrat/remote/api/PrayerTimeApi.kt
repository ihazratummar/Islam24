package com.hazrat.remote.api

import com.hazrat.remote.dto.NewPrayerTimeDto
import com.hazrat.utils.Constants.PRAYER_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Interface representing the API service for retrieving prayer times.
 */
interface PrayerTimeApi {
    suspend fun newPrayerTimesRequest(
        year: Int,
        latitude: String,
        longitude: String,
        calculationMethod: Int,
        juristicMethod: Int
    ): NewPrayerTimeDto
}

class PrayerTimeApiImpl(
    private val client: HttpClient
) : PrayerTimeApi {
    override suspend fun newPrayerTimesRequest(
        year: Int,
        latitude: String,
        longitude: String,
        calculationMethod: Int,
        juristicMethod: Int
    ): NewPrayerTimeDto {
        return client.get("$PRAYER_BASE_URL$year") {
            parameter("latitude", latitude)
            parameter("longitude", longitude)
            parameter("method", calculationMethod)
            parameter("school", juristicMethod)
        }.body()
    }
}
