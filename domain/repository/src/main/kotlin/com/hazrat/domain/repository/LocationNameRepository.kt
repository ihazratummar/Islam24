package com.hazrat.domain.repository

import com.hazrat.database.entity.LocationDetailsEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

interface LocationNameRepository {

    fun observeLocationInfo() : Flow<LocationDetailsEntity>


}