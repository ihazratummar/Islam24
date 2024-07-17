package com.hazrat.islam24.core.domain.repository.location

import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

interface LocationNameRepository {
    suspend fun getLocationName(): com.hazrat.islam24.core.domain.model.locationmodel.LocationNameFinder
    suspend fun fetchLocationName(): String?
    fun getLocationDetails(): Flow<List<LocationDetailsEntity>>
}