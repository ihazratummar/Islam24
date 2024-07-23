package com.hazrat.islam24.core.domain.repository


import com.hazrat.islam24.core.domain.model.gregoriantohijri.GregorianToHijriResponse
import com.hazrat.islam24.core.data.entity.GregorianToHijriEntity
import kotlinx.coroutines.flow.Flow

interface GregorianToHijriRepository {
    suspend fun getGregorianToHijriDate(): com.hazrat.islam24.core.domain.model.gregoriantohijri.GregorianToHijriResponse

    fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>>
}