package com.hazrat.islam24.core.domain.repository.location

import android.location.Location
import androidx.lifecycle.LiveData
import com.hazrat.islam24.data.entity.LocationDetailsEntity
import com.hazrat.islam24.data.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

interface LocationRepository {
    val currentLocation: LiveData<Location>
    suspend fun saveLocation(latitude: Double, longitude: Double)
    suspend fun getLocation(): LocationEntity?
}