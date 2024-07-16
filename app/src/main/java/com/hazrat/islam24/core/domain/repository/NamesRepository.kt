package com.hazrat.islam24.core.domain.repository

import com.hazrat.islam24.data.entity.NameEntity
import com.hazrat.islam24.core.domain.model.namesofallah.Data

/**
 * @author Hazrat Ummar Shaikh
 */

interface NamesRepository {
    suspend fun getAllNames(): List<com.hazrat.islam24.core.domain.model.namesofallah.Data>
    suspend fun getAllahNames(): List<NameEntity>
}