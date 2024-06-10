package com.hazrat.islam24.domain.repository


import android.util.Log
import com.hazrat.islam24.data.dao.NameDao
import com.hazrat.islam24.data.entity.NameEntity
import com.hazrat.islam24.data.entity.TasbihCounterEntity
import com.hazrat.islam24.domain.model.namesofallah.Data
import com.hazrat.islam24.domain.model.namesofallah.En
import com.hazrat.islam24.network.NamesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NamesRepository @Inject constructor(
    private val api: NamesApi,
    private val nameDao: NameDao
) {


    suspend fun getAllNames(): List<Data> {
        return withContext(Dispatchers.IO) {
            try {
                val localNames = nameDao.getAllNames()
                if (localNames.isNotEmpty()) {
                    Log.d("NamesRepository", "Returning names from local database")
                    localNames.map { NameEntityToData(it) }
                } else {
                    Log.d("NamesRepository", "Fetching names from API")
                    val remoteNames = api.getAllNames().data
                    Log.d("NamesRepository", "Fetched ${remoteNames.size} names from API")
                    val entities = remoteNames.map { NameDataToEntity(it) }
                    nameDao.insertName(entities)
                    Log.d("NamesRepository", "Inserted ${entities.size} names into local database")
                    remoteNames
                }
            } catch (e: Exception) {
                Log.e("NamesRepository", "Error fetching names: ${e.message}", e)
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

suspend fun getAllahNames(): List<NameEntity> {
        return nameDao.getAllNames()
    }

}