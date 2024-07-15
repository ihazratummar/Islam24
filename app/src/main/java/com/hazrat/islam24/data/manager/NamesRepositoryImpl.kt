package com.hazrat.islam24.data.manager


import com.hazrat.islam24.data.dao.NameDao
import com.hazrat.islam24.data.entity.NameEntity
import com.hazrat.islam24.domain.model.namesofallah.Data
import com.hazrat.islam24.domain.model.namesofallah.En
import com.hazrat.islam24.domain.repository.NamesRepository
import com.hazrat.islam24.network.NamesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NamesRepositoryImpl @Inject constructor(
    private val api: NamesApi,
    private val nameDao: NameDao
): NamesRepository {


    override suspend fun getAllNames(): List<Data> {
        return withContext(Dispatchers.IO) {
            try {
                val localNames = nameDao.getAllNames()
                if (localNames.isNotEmpty()) {
                    localNames.map { NameEntityToData(it) }
                } else {
                    val remoteNames = api.getAllNames().data
                    val entities = remoteNames.map { NameDataToEntity(it) }
                    nameDao.insertName(entities)
                    remoteNames
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    private fun NameDataToEntity(data: Data): NameEntity {
        return NameEntity(
            enDec = data.en.desc,
            meaning = data.en.meaning,
            found = data.found,
            name = data.name,
            number = data.number,
            transliteration = data.transliteration
        )
    }

    private fun NameEntityToData(entity: NameEntity): Data {
        return Data(
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