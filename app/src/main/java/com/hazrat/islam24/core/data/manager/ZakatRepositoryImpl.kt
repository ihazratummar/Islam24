package com.hazrat.islam24.core.data.manager

import com.hazrat.islam24.core.data.dao.ZakatDao
import com.hazrat.islam24.core.domain.model.zakat.NisabEntity
import com.hazrat.islam24.core.domain.model.zakat.ZakatEntity
import com.hazrat.islam24.core.domain.repository.ZakatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

class ZakatRepositoryImpl @Inject  constructor(
    private  val dao: ZakatDao
): ZakatRepository {
    override suspend fun insertNisab(nisabEntity: NisabEntity) {

        dao.insertNisab(nisabEntity)
    }

    override suspend fun deleteNisab(nisabEntity: NisabEntity) {
        dao.deleteNisab(nisabEntity)
    }

    override fun getNisab(): Flow<NisabEntity> {
        return dao.getNisab()
    }

    override suspend fun insertZakat(zakatEntity: ZakatEntity) {
        dao.insertZakatDetails(zakatEntity = zakatEntity)
    }

    override suspend fun deleteZakat(zakatId:Int) {
        dao.deleteZakatDetails(zakatId)
    }

    override fun getZakatDetails(): Flow<List<ZakatEntity>> {
        return  dao.getZakatDetails()
    }

    override fun getZakatDetailsByDateDesc(): Flow<List<ZakatEntity>> {
        return  dao.getZakatDetailsByDateDesc()
    }

    override fun getZakatDetailsByDateAsc(): Flow<List<ZakatEntity>> {
        return dao.getZakatDetailsByDateAsc()
    }

}