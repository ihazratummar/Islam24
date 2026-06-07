package com.hazrat.athkar.domain.repository

import com.hazrat.athkar.domain.model.AthkarData

/**
 * @author Hazrat Ummar Shaikh
 */

interface AthkarRepository {

    suspend fun getAthkarFromApi(): List<AthkarData>
    suspend fun getAthkarFromDb(): List<AthkarData>
}