package com.hazrat.islam24.core.data.manager


import com.hazrat.islam24.core.data.dao.NameDao
import com.hazrat.islam24.core.data.entity.NameEntity
import com.hazrat.islam24.core.domain.model.namesofallah.En
import com.hazrat.islam24.core.domain.model.namesofallah.NameOfAllahData
import com.hazrat.islam24.core.domain.repository.NamesRepository
import com.hazrat.islam24.core.network.NamesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NamesRepositoryImpl @Inject constructor(
    private val api: NamesApi,
    private val nameDao: NameDao
): NamesRepository {


    override suspend fun getAllNames(): List<com.hazrat.islam24.core.domain.model.namesofallah.NameOfAllahData> {
        return withContext(Dispatchers.IO) {
            try {
                val localNames = nameDao.getAllNames()
                if (localNames.isNotEmpty()) {
                    localNames.map { nameEntityToData(it) }
                } else {
                    val remoteNames = api.getAllNames().data
                    val entities = remoteNames.map { nameDataToEntity(it) }
                    nameDao.insertName(entities)
                    remoteNames
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    private fun nameDataToEntity(data: NameOfAllahData): NameEntity {
        return NameEntity(
            enDec = data.en.desc,
            meaning = data.en.meaning,
            found = data.found,
            name = data.name,
            number = data.number,
            transliteration = data.transliteration
        )
    }

    private fun nameEntityToData(entity: NameEntity): com.hazrat.islam24.core.domain.model.namesofallah.NameOfAllahData {
        return NameOfAllahData(
            en = En(entity.enDec, entity.meaning),
            found = entity.found,
            name = entity.name,
            number = entity.number,
            transliteration = entity.transliteration
        )
    }

override suspend fun getAllahNames(): List<NameEntity> {
        return nameDao.getAllNames()
    }

}