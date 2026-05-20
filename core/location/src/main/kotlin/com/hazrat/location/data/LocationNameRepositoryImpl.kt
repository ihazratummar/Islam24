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
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber

class LocationNameRepositoryImpl(
    private val locationNameApi: LocationNameApi,
    private val locationRepository: LocationRepository,
    private val locationNameDao: LocationNameDao
) : LocationNameRepository {

    companion object {
        private const val ADDRESS_THRESHOLD_METERS = 5000f // 2km
        private const val TAG = "LocationNameRepository"
    }

    override fun observeLocationInfo(): Flow<LocationDetailsEntity> = flow {

        getCachedLocationInfo()?.let {
            emit(it)
        }

        emitAll(
            locationRepository.observeLocationUpdates()
                .filterIsInstance<LocationResult.Success>()
                .map { it.location }
                .distinctUntilChanged { old, new ->
                    old.distanceTo(new) < ADDRESS_THRESHOLD_METERS
                }
                .map { location ->
                    if (shouldUpdateName(location)) {
                        fetchAndCacheLocationInfo(location)
                    } else {
                        getCachedLocationInfo()
                            ?: fetchAndCacheLocationInfo(location)
                    }
                }
                .distinctUntilChanged()
        )
    }.flowOn(Dispatchers.IO)

    private suspend fun shouldUpdateName(newLocation: Location): Boolean {
        val cache = locationNameDao.getLocationDetails().firstOrNull() ?: return true

        val result = FloatArray(1)

        Location.distanceBetween(
            cache.latitude,
            cache.longitude,
            newLocation.latitude,
            newLocation.longitude,
            result
        )

        return result[0] >= ADDRESS_THRESHOLD_METERS
    }



    private suspend fun getCachedLocationInfo(): LocationDetailsEntity? {
        return locationNameDao.getLocationDetails().firstOrNull()
    }

    private suspend fun fetchAndCacheLocationInfo(location: Location) : LocationDetailsEntity {
        return try {
            val response = locationNameApi.getLocationName(
                key = LOCATION_IQ_API_KEY,
                lat = location.latitude,
                lon = location.longitude,
                format = "json"
            )

            if (response.isSuccessful) {
                val name = response.body()?.toLocationNameFinder().orEmpty()
                val entity = LocationDetailsEntity(
                    locationName = name,
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                saveLocation(locationDetailsEntity = entity)
                entity
            } else {
                Timber.tag(TAG).e("API Error: ${response.code()}")
                getCachedLocationInfo()
                    ?: throw IllegalStateException("No cached location")
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Failed to fetch name from API")
            getCachedLocationInfo()
                ?: throw IllegalStateException("No cached location")
        }

    }

    private suspend fun saveLocation(locationDetailsEntity: LocationDetailsEntity) {
        locationNameDao.saveLocationDetails(locationDetailsEntity)
    }
}