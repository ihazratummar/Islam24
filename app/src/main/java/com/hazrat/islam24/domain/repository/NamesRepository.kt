package com.hazrat.islam24.domain.repository

import com.hazrat.islam24.data.entity.NameEntity
import com.hazrat.islam24.domain.model.namesofallah.Data

/**
 * @author Hazrat Ummar Shaikh
 */

interface NamesRepository {
    suspend fun getAllNames(): List<Data>
    suspend fun getAllahNames(): List<NameEntity>
}