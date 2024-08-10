package com.hazrat.islam24.core.data.manager

import com.hazrat.islam24.core.data.dao.LocationNameDao
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.data.entity.LocationEntity
import com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.core.network.LocationNameApi
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
    override suspend fun getLocationName():LocationNameFinder {

        val location: LocationEntity? = locationRepository.getLocation()
        val lat = location?.latitude ?: 24.628
        val lon = location?.longitude ?: 88.011
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