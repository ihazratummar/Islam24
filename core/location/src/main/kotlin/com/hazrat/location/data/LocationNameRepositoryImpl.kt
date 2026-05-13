package com.hazrat.location.data

import android.location.Location
import com.hazrat.database.dao.LocationNameDao
import com.hazrat.database.entity.LocationDetailsEntity
import com.hazrat.domain.repository.LocationNameRepository
import com.hazrat.location.data.mapper.toLocationNameFinder
import com.hazrat.location.model.LocationResult
import com.hazrat.location.repository.LocationRepository
import com.hazrat.remote.api.LocationNameApi
import com.hazrat.utils.Constants.LOCATION_IQ_API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

class LocationNameRepositoryImpl(
    private val locationNameApi: LocationNameApi,
    private val locationRepository: LocationRepository,
    private val locationNameDao: LocationNameDao
) : LocationNameRepository {

    private var lastLocation: Location? = null

    companion object {
        private const val DISTANCE_THRESHOLD_METERS = 2000f // 2km
        private const val TAG = "LocationNameRepository"
    }

    override suspend fun locationName(): Flow<String> = flow {
        // Emit cached value first for instant UI response
        val cached = getCachedLocationName()
        if (cached != null) emit(cached)

        // Then observe location updates and refresh name if user moves > 2km
        emitAll(
            locationRepository.observeLocationUpdates()
                .filterIsInstance<LocationResult.Success>()
                .map { it.location }
                .distinctUntilChanged { old, new ->
                    old.distanceTo(new) < DISTANCE_THRESHOLD_METERS
                }
                .map { location ->
                    if (shouldUpdateName(location)) {
                        fetchAndCacheLocationName(location)
                    } else {
                        getCachedLocationName() ?: fetchAndCacheLocationName(location)
                    }
                }
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
        )
    }.flowOn(Dispatchers.IO)

    private fun shouldUpdateName(newLocation: Location): Boolean {
        val last = lastLocation ?: return true
        return newLocation.distanceTo(last) >= DISTANCE_THRESHOLD_METERS
    }

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
            val result = locationRepository.getCurrentLocation()
            if (result is LocationResult.Success) {
                val location = result.location
                if (shouldUpdateName(location)) {
                    fetchAndCacheLocationName(location)
                } else {
                    getCachedLocationName() ?: fetchAndCacheLocationName(location)
                }
            } else ""
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Unknown error: ${e.message}")
            ""
        }
    }

    private suspend fun getCachedLocationName(): String? {
        return locationNameDao.getLocationDetails()
            .firstOrNull()
             ?.toLocationNameFinder()
            ?.takeIf { it.isNotBlank() }
    }

    private suspend fun fetchAndCacheLocationName(location: Location): String {
        lastLocation = location

        return try {
            val response = locationNameApi.getLocationName(
                key = LOCATION_IQ_API_KEY,
                lat = location.latitude,
                lon = location.longitude,
                format = "json"
            )

            if (response.isSuccessful) {
                val name = response.body()?.toLocationNameFinder() ?: ""
                if (name.isNotBlank()) {
                    saveLocation(LocationDetailsEntity(locationName = name))
                }
                name
            } else {
                Timber.tag(TAG).e("API Error: ${response.code()}")
                ""
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Failed to fetch name from API")
            ""
        }
    }

    private suspend fun saveLocation(locationDetailsEntity: LocationDetailsEntity) {
        locationNameDao.saveLocationDetails(locationDetailsEntity)
    }
}