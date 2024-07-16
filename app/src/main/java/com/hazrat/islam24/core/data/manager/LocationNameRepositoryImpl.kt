package com.hazrat.islam24.core.data.manager

import android.util.Log
import com.hazrat.islam24.core.network.LocationNameApi
import com.hazrat.islam24.data.entity.LocationEntity
import com.hazrat.islam24.data.entity.LocationDetailsEntity
import com.hazrat.islam24.data.dao.LocationNameDao
import com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder
import com.hazrat.islam24.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.domain.repository.location.LocationRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationNameRepositoryImpl @Inject constructor(
    private val locationNameApi: LocationNameApi,
    private val locationRepository: LocationRepositoryImpl,
    private val locationNameDao: LocationNameDao
) : LocationNameRepository {
    override suspend fun getLocationName(): com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder {

        val location: LocationEntity? = locationRepository.getLocation()
        val lat = location?.latitude ?: 24.628
        val lon = location?.longitude ?: 88.011
        Log.d("getLocationName", "$lat $lon")
        return locationNameApi.getLocationName(format = "json", lat = lat, lon = lon)
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
                Log.e("LocationName", "Error fetching location name: ${e.message}")
                null // Return null if there's an error
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
        getLocationName()
    }
}