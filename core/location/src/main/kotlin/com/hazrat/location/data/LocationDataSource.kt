package com.hazrat.location.data

import com.hazrat.location.model.LocationConfig
import com.hazrat.location.model.LocationConfigs
import com.hazrat.location.model.LocationResult
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 23/01/26
 */

interface LocationDataSource {
    suspend fun getLastKnownLocation(): LocationResult
    suspend fun getCurrentLocation(locationConfig: LocationConfig = LocationConfigs.Default): LocationResult
    fun observeLocationUpdates(locationConfig: LocationConfig = LocationConfigs.Default): Flow<LocationResult>
}