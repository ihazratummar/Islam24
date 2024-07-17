package com.hazrat.islam24.core.domain.repository

import com.hazrat.islam24.core.data.entity.NameEntity

/**
 * @author Hazrat Ummar Shaikh
 */

interface NamesRepository {
    suspend fun getAllNames(): List<com.hazrat.islam24.core.domain.model.namesofallah.NameOfAllahData>
    suspend fun getAllahNames(): List<NameEntity>
}