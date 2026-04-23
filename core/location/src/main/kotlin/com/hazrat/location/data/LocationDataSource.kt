package com.hazrat.location.data

import com.hazrat.location.model.LocationResult
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 23/01/26
 */

interface LocationDataSource {
    suspend fun getLastKnownLocation(): LocationResult
    suspend fun getCurrentLocation(): LocationResult
    fun observeLocationUpdates(): Flow<LocationResult>
    fun stopLocationUpdates()
}