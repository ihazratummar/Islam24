package com.hazrat.location.data

import android.util.Log
import com.hazrat.database.dao.LocationNameDao
import com.hazrat.database.entity.LocationDetailsEntity
import com.hazrat.domain.repository.LocationNameRepository
import com.hazrat.location.data.mapper.toLocationNameFinder
import com.hazrat.location.model.LocationResult
import com.hazrat.location.repository.LocationRepository
import com.hazrat.remote.api.LocationNameApi
import com.hazrat.remote.dto.LocationNameDto
import com.hazrat.utils.Constants.LOCATION_IQ_API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import kotlin.math.abs

class LocationNameRepositoryImpl(
    private val locationNameApi: LocationNameApi,
    private val locationRepository: LocationRepository,
    private val locationNameDao: LocationNameDao
) : LocationNameRepository {

    // Cache for last known coordinates
    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null

    companion object {
        private const val LONGITUDE_THRESHOLD = 0.03
        private const val LATITUDE_THRESHOLD = 0.6
        private const val TAG = "LocationNameRepository"
    }



    override suspend fun locationName(): Flow<String> = flow {
        val locationName = fetchLocationName()
        emit(locationName ?: "")
    }.flowOn(Dispatchers.IO)


    override suspend fun fetchLocationName(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val locationName = getLocationName()
                if (locationName.isNotBlank()) {
                    val locationEntity = LocationDetailsEntity(locationName = locationName)
                    saveLocation(locationEntity)
                    locationName
                } else {
                    null
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Error fetching location name")
                null
            }
        }
    }

    override suspend fun getLocationName(): String {
        return try {
            val coordinates = fetchCurrentCoordinates() ?: return ""
            Timber.tag(TAG).d("Fetched coordinates: $coordinates")
            // Check if we can use cached location
            if (shouldUseCachedLocation(coordinates)) {
                getCachedLocationName() ?: fetchAndCacheLocationName(coordinates)
            } else {
                fetchAndCacheLocationName(coordinates)
            }
        } catch (e: LocationError) {
            Timber.tag(TAG).e(e, "Location API error: ${e.message}")
            ""
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Unknown error: ${e.message}")
            ""
        }
    }


    private suspend fun fetchCurrentCoordinates(): Coordinates? {
        return when (val result = locationRepository.getCurrentLocation()) {
            is LocationResult.Error -> {
                Timber.tag(TAG).e("Location error: ${result.error}")
                null
            }

            is LocationResult.Success -> {
                Coordinates(
                    latitude = result.location.latitude,
                    longitude = result.location.longitude
                ).also {
                    Timber.tag(TAG).d("Current coordinates: $it")
                }
            }
        }
    }

    private fun shouldUseCachedLocation(coordinates: Coordinates): Boolean {
        val previousLat = lastLatitude ?: return false
        val previousLon = lastLongitude ?: return false

        val latDiff = abs(coordinates.latitude - previousLat)
        val lonDiff = abs(coordinates.longitude - previousLon)

        return latDiff < LATITUDE_THRESHOLD && lonDiff < LONGITUDE_THRESHOLD
    }

    private suspend fun getCachedLocationName(): String? {
        return locationNameDao.getLocationDetails()
            .firstOrNull()
            ?.toLocationNameFinder()
            ?.takeIf { it.isNotBlank() }
    }

    private suspend fun fetchAndCacheLocationName(coordinates: Coordinates): String {
        // Update cache
        lastLatitude = coordinates.latitude
        lastLongitude = coordinates.longitude

        val response = locationNameApi.getLocationName(
            key = LOCATION_IQ_API_KEY,
            lat = coordinates.latitude,
            lon = coordinates.longitude,
            format = "json"
        )

        return handleApiResponse(response)
    }

    private fun handleApiResponse(response: Response<LocationNameDto>): String {
        return if (response.isSuccessful) {
            response.body()?.toLocationNameFinder()
                ?: throw LocationError.UnknownError("Empty response body")
        } else {
            throw handleApiError(response.code(), response.message())
        }
    }

    private suspend fun saveLocation(locationDetailsEntity: LocationDetailsEntity) {
        locationNameDao.saveLocationDetails(locationDetailsEntity)
    }

    private fun handleApiError(errorCode: Int, errorMessage: String): LocationError {
        return when (errorCode) {
            400 -> LocationError.InvalidRequest("Invalid request: $errorMessage")
            401 -> LocationError.InvalidKey("Invalid API key: $errorMessage")
            403 -> LocationError.AccessRestricted("Access restricted: $errorMessage")
            404 -> LocationError.UnableToGeocode("Unable to geocode: $errorMessage")
            429 -> LocationError.RateLimited("Rate limited: $errorMessage")
            500 -> LocationError.InternalServerError("Internal server error: $errorMessage")
            else -> LocationError.UnknownError("Unknown error ($errorCode): $errorMessage")
        }
    }

    // Data class for better type safety
    private data class Coordinates(
        val latitude: Double,
        val longitude: Double
    )
}