package com.hazrat.islam24.core.data.repository

import android.util.Log
import com.hazrat.islam24.core.api.LocationNameApi
import com.hazrat.islam24.core.data.dao.LocationNameDao
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.LocationEntity
import com.hazrat.islam24.core.data.error.LocationError
import com.hazrat.islam24.core.domain.model.locationmodel.Address
import com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.util.Constants.LOCATION_IQ_API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private val lonThreshold = 0.03
    private val latThreshold = 0.6

    override suspend fun getLocationName(): LocationNameFinder {
        return try {
            val location: LocationEntity? = locationRepository.getLocation()
            val lat = location?.latitude ?: 24.628
            val lon = location?.longitude ?: 88.011

            if (lastLatitude != null && lastLongitude != null) {
                val latDiff = abs(lat - lastLatitude!!)
                val lonDiff = abs(lon - lastLongitude!!)
                Log.d("LocationNameRepository", "latDiff: $latDiff, lonDiff: $lonDiff")
                val addressList = locationNameDao.getLocationDetails().firstOrNull()
                if (addressList.isNullOrEmpty()) {
                    if (latDiff < latThreshold && lonDiff < lonThreshold) {
                        // Coordinates haven't changed, no need to call the API
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
            }

            lastLatitude = lat
            lastLongitude = lon
            Log.d("LocationNameRepository", "lat: $lat, lon: $lon")

            // Make the API call and get the full response including headers
            val response = locationNameApi.getLocationName(
                key = LOCATION_IQ_API_KEY,
                lat = lat,
                lon = lon,
                format = "json"
            )

            if (response.isSuccessful) {
                // If the response is successful, return the response body
                response.body() ?: throw Exception("Empty response body")
            } else {
                // Handle errors based on the status code
                throw handleApiError(response.code(), errorMessage = response.message())
            }
        } catch (e: LocationError) {
            Log.e("LocationNameRepository", "Location API error: ${e.message}", e)
            // Return default LocationNameFinder in case of LocationError
            LocationNameFinder(address = Address(village = null, city = null, town = null, suburb = null))
        } catch (e: Exception) {
            Log.e("LocationNameRepository", "Unknown error: ${e.message}", e)
            // Return default LocationNameFinder in case of any other exception
            LocationNameFinder(address = Address(village = null, city = null, town = null, suburb = null))
        }
    }




    override suspend fun fetchLocationName(): String? {
        return withContext(Dispatchers.IO) {
            try {

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


    private fun  handleApiError(errorCode : Int, errorMessage : String) : LocationError {
        return when(errorCode){
            400 -> LocationError.InvalidRequest("Invalid request $errorMessage")
            401 -> LocationError.InvalidKey("Invalid Api $errorMessage")
            403 -> LocationError.AccessRestricted("AccessRestricted $errorMessage")
            404 -> LocationError.UnableToGeocode("UnableToGeocode $errorMessage")
            429 -> LocationError.RateLimited("RateLimited $errorMessage")
            500 -> LocationError.InternalServerError("InternalServerError $errorMessage")
            else -> LocationError.UnknownError("UnknownError $errorMessage")
        }
    }
}
