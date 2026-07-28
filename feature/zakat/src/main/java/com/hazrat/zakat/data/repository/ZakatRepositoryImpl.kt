package com.hazrat.zakat.data.repository

import android.content.Context
import com.hazrat.database.dao.ZakatDao
import com.hazrat.database.entity.zakat.NisabEntity
import com.hazrat.database.entity.zakat.ZakatEntity
import com.hazrat.zakat.domain.repository.ZakatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.util.UUID

/**
 * @author Hazrat Ummar Shaikh
 */

class ZakatRepositoryImpl (
    private val dao: ZakatDao,
    private val context: Context
) : ZakatRepository {
    override suspend fun insertNisab(nisabEntity: NisabEntity) {
        dao.insertNisab(nisabEntity)
    }

    override suspend fun deleteNisab(nisabEntity: NisabEntity) {
        dao.deleteNisab(nisabEntity)
    }

    override fun getNisab(): Flow<NisabEntity> {
        return dao.getNisab().transform { entity ->
            if (entity == null){
                insertNisab(NisabEntity(silverPrice = 0.0))
                emit(NisabEntity(silverPrice = 0.0))
            } else {
                emit(entity)
            }
        }
    }

    override suspend fun insertZakat(zakatEntity: ZakatEntity) {
        val newZakatEntity = zakatEntity.copy(id = UUID.randomUUID().toString())
        dao.insertZakatDetails(zakatEntity = newZakatEntity)
    }

    override suspend fun deleteZakat(zakatId: String) {
        dao.deleteZakatDetails(zakatId)
    }

    override fun getZakatList(): Flow<List<ZakatEntity>> {
        return dao.getZakatList()
    }

    override fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>> {
        return dao.getZakatDetailsByDateDesc()
    }

    override fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>> {
        return dao.getZakatDetailsByDateAsc()
    }

    override suspend fun syncData() {
    }

    override fun getZakatDetails(id: String): Flow<ZakatEntity> {
        return dao.getZakatDetails(id = id)
    }
}