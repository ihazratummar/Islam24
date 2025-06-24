package com.hazrat.zakat.domain.repository


import com.hazrat.database.entity.zakat.NisabEntity
import com.hazrat.database.entity.zakat.ZakatEntity
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
    fun getZakatList(): Flow<List<ZakatEntity>>
    fun getZakatDetails(id: String): Flow<ZakatEntity>
    fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>>
    fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>>
    suspend fun syncData()

}