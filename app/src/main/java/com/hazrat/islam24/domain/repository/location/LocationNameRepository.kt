package com.hazrat.islam24.domain.repository.location

import com.hazrat.islam24.data.entity.LocationDetailsEntity
import com.hazrat.islam24.domain.model.locationmodel.LocationNameFinder
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

interface LocationNameRepository {
    suspend fun getLocationName(): LocationNameFinder
    suspend fun fetchLocationName(): String?
    fun getLocationDetails(): Flow<List<LocationDetailsEntity>>
}