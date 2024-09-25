package com.hazrat.islam24.core.data.repository

import android.util.Log
import com.hazrat.islam24.core.data.dao.LocationNameDao
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.LocationEntity
import com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.core.api.LocationNameApi
import com.hazrat.islam24.core.domain.model.locationmodel.Address
import com.hazrat.islam24.util.Constants.LOCATION_IQ_API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.abs

class LocationNameRepositoryImpl @Inject constructor(
    private val locationNameApi: LocationNameApi,
    private val locationRepository: LocationRepositoryImpl,
    private val locationNameDao: LocationNameDao
) : LocationNameRepository {

    private val _locationName = MutableStateFlow<List<LocationDetailsEntity>>(emptyList())
    override val locationName = _locationName.asStateFlow()

    // Variables to cache the last known latitude and longitude
    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null

    private val LON_THRESHOLD = 0.03
    private val LAT_THRESHOLD = 0.6

    override suspend fun getLocationName(): LocationNameFinder {
        val location: LocationEntity? = locationRepository.getLocation()
        val lat = location?.latitude ?: 24.628
        val lon = location?.longitude ?: 88.011

        if (lastLatitude != null && lastLongitude != null) {
            val latDiff = abs(lat - lastLatitude!!)
            val lonDiff = abs(lon - lastLongitude!!)
            Log.d("LocationNameRepository", "latDiff: $latDiff, lonDiff: $lonDiff")

            if (latDiff < LAT_THRESHOLD && lonDiff < LON_THRESHOLD) {
                // Coordinates haven't changed, no need to call the API
                val addressList = locationNameDao.getLocationDetails().firstOrNull()
                if (!addressList.isNullOrEmpty()) {
                    val address = addressList.first()
                    return LocationNameFinder(
                        address = Address(
                            village = address.village,
                            city = address.city,
                            town = address.town,
                            suburb = address.suburb
                        )
                    )
                }

            }
        }

        lastLatitude = lat
        lastLongitude = lon
        Log.d("LocationNameRepository", "lat: $lat, lon: $lon")

        return locationNameApi.getLocationName(
            key = LOCATION_IQ_API_KEY,
            lat = lat,
            lon = lon,
            format = "json"
        )
    }


    override suspend fun fetchLocationName(): String? {
        return withContext(Dispatchers.IO) {
            try {

                val location: LocationEntity? = locationRepository.getLocation()
                val lat = location?.latitude ?: 24.628
                val lon = location?.longitude ?: 88.011
                if (lastLatitude != null && lastLongitude != null) {
                    val latDiff = abs(lat - lastLatitude!!)
                    val lonDiff = abs(lon - lastLongitude!!)
                    Log.d("LocationNameRepository", "latDiff: $latDiff, lonDiff: $lonDiff")
                    if (latDiff < LAT_THRESHOLD && lonDiff < LON_THRESHOLD) {

                        // Coordinates haven't changed, no need to call the API

                    }
                }
                lastLatitude = lat
                lastLongitude = lon

                val response = getLocationName()
                val locationName =
                    response.address.village ?: response.address.city ?: response.address.town
                    ?: response.address.suburb

                val locationEntity = LocationDetailsEntity(
                    village = response.address.village,
                    city = response.address.city,
                    town = response.address.town,
                    suburb = response.address.suburb
                )
                saveLocation(locationEntity)

                // Return the locationName
                locationName
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun getLocationDetails(): Flow<List<LocationDetailsEntity>> =
        locationNameDao.getLocationDetails().flowOn(Dispatchers.IO)

    private suspend fun deleteOtherLocationDetails() {
        locationNameDao.deleteOtherLocationDetails()
    }

    private suspend fun saveLocation(locationDetailsEntity: LocationDetailsEntity) {
        locationNameDao.saveLocationDetails(locationDetailsEntity)
    }

    override suspend fun locationName() {
        getLocationDetails().distinctUntilChanged()
            .collectLatest { locationName: List<LocationDetailsEntity> ->
                if (locationName.isEmpty()) {
                    fetchLocationName()
                } else {
                    _locationName.value = locationName
                }
            }
    }
}
