package com.hazrat.islam24.domain.repository


import com.hazrat.hijricaneldar.domain.model.gregoriantohijri.GregorianToHijriResponse
import com.hazrat.islam24.data.entity.GregorianToHijriEntity
import kotlinx.coroutines.flow.Flow

interface GregorianToHijriRepository {
    suspend fun getGregorianToHijriDate(): GregorianToHijriResponse

    fun gregorianToHijriEntity(): Flow<List<GregorianToHijriEntity>>
}