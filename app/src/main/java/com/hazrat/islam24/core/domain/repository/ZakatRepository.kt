package com.hazrat.islam24.core.domain.repository

import com.hazrat.islam24.core.domain.model.zakat.NisabEntity
import com.hazrat.islam24.core.domain.model.zakat.ZakatEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

interface ZakatRepository {
    /*
    Nisab
     */
    suspend fun insertNisab(nisabEntity: NisabEntity)
    suspend fun deleteNisab(nisabEntity: NisabEntity)
    fun getNisab(): Flow<NisabEntity>

    /*
    Zakat Details edit get delete
     */
    suspend fun insertZakat(zakatEntity: ZakatEntity)
    suspend fun deleteZakat(zakatId: String)
    fun getZakatDetails(): Flow<List<ZakatEntity>>
    fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>>
    fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>>
    suspend fun syncData()

}