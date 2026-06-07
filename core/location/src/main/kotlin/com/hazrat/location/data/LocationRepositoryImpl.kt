package com.hazrat.location.data

import com.hazrat.location.model.LocationConfig
import com.hazrat.location.model.LocationConfigs
import com.hazrat.location.model.LocationResult
import com.hazrat.location.repository.LocationRepository
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 23/01/26
 */

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource
) : LocationRepository {

    override suspend fun getLastKnownLocation(): LocationResult {
        return locationDataSource.getLastKnownLocation()
    }

    override suspend fun getCurrentLocation(): LocationResult {
        return locationDataSource.getCurrentLocation(locationConfig = LocationConfigs.Default)
    }

    override fun observeLocationUpdates(locationConfig: LocationConfig): Flow<LocationResult> {
        return locationDataSource.observeLocationUpdates()
    }

}