package com.hazrat.islam24.domain.repository.location

import android.util.Log
import com.hazrat.islam24.network.LocationNameApi
import com.hazrat.islam24.data.entity.LocationEntity
import com.hazrat.islam24.data.entity.LocationDetailsEntity
import com.hazrat.islam24.data.dao.LocationNameDao
import com.hazrat.islam24.domain.model.locationmodel.LocationNameFinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationNameRepository @Inject constructor(
    private val locationNameApi: LocationNameApi,
    private val locationRepository: LocationRepository,
    private val locationNameDao: LocationNameDao
) {
    suspend fun getLocationName(): LocationNameFinder {

        val location: LocationEntity? =  locationRepository.getLocation()
        val lat = location?.latitude?: 24.628
        val lon = location?.longitude?: 88.011
        Log.d("getLocationName", "$lat $lon")
        return locationNameApi.getLocationName(format = "json", lat = lat, lon = lon)
    }

    suspend fun fetchLocationName(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = getLocationName()
                val locationName = response.address.village ?: response.address.city ?: response.address.town

                val locationEntity = LocationDetailsEntity(
                    village = response.address.village,
                    city = response.address.city,
                    town = response.address.town
                )
                saveLocation(locationEntity)

                // Return the locationName
                locationName
            } catch (e: Exception) {
                Log.e("LocationName", "Error fetching location name: ${e.message}")
                null // Return null if there's an error
            }
        }
    }

    fun getLocationDetails(): Flow<List<LocationDetailsEntity>> = locationNameDao.getLocationDetails().flowOn(Dispatchers.IO)

    private suspend fun deleteOtherLocationDetails() {
        locationNameDao.deleteOtherLocationDetails()
    }

    private suspend fun saveLocation(locationDetailsEntity: LocationDetailsEntity) {
         locationNameDao.saveLocationDetails(locationDetailsEntity)
        getLocationName()
    }
}