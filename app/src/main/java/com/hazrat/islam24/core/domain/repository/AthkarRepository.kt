package com.hazrat.islam24.core.domain.repository

import com.hazrat.islam24.core.data.entity.AthkarDataEntity
import com.hazrat.islam24.core.domain.model.athkar.AthkarApiModel

/**
 * @author Hazrat Ummar Shaikh
 */

interface AthkarRepository {

    suspend fun getAthkarFromApi(): List<AthkarApiModel>
    suspend fun getAthkarFromDb(): List<AthkarDataEntity>
}