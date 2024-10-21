package com.hazrat.islam24.core.data.repository


import com.hazrat.islam24.core.data.dao.NameDao
import com.hazrat.islam24.core.data.entity.NameEntity
import com.hazrat.islam24.core.domain.model.namesofallah.En
import com.hazrat.islam24.core.domain.model.namesofallah.NameOfAllahData
import com.hazrat.islam24.core.domain.repository.NamesRepository
import com.hazrat.islam24.core.api.NamesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the NamesRepository interface.
 * This class is responsible for interacting with the Names API and local database to manage names data.
 *
 * @property api The NamesApi instance for making network requests.
 * @property nameDao The NameDao instance for interacting with the local database.
 */
class NamesRepositoryImpl @Inject constructor(
    private val api: NamesApi,
    private val nameDao: NameDao
) : NamesRepository {

    /**
     * Retrieves Allah's names from the API.
     * First attempts to get names from the local database. If no names are found, it fetches from the API
     * and stores the result in the local database.
     *
     * @return A list of NameOfAllahData objects representing the names retrieved.
     */
    override suspend fun getAllahNamesFromApi(): List<NameOfAllahData> {
        return withContext(Dispatchers.IO) {
            try {
                // Retrieve names from local database
                val localNames = nameDao.getAllNames()
                if (localNames.isNotEmpty()) {
                    // Map local names to data objects
                    localNames.map { nameEntityToData(it) }
                } else {
                    // Fetch names from API and insert into local database
                    val remoteNames = api.getAllNames().data
                    val entities = remoteNames.map { nameDataToEntity(it) }
                    nameDao.insertName(entities)
                    remoteNames
                }
            } catch (e: Exception) {
                // Handle exceptions and return an empty list
                emptyList()
            }
        }
    }

    /**
     * Converts NameOfAllahData to NameEntity.
     *
     * @param data The NameOfAllahData object to be converted.
     * @return A NameEntity object representing the converted data.
     */
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

    /**
     * Converts NameEntity to NameOfAllahData.
     *
     * @param entity The NameEntity object to be converted.
     * @return A NameOfAllahData object representing the converted entity.
     */
    private fun nameEntityToData(entity: NameEntity): NameOfAllahData {
        return NameOfAllahData(
            en = En(entity.enDec, entity.meaning),
            found = entity.found,
            name = entity.name,
            number = entity.number,
            transliteration = entity.transliteration
        )
    }

    /**
     * Retrieves all Allah's names from the local database.
     *
     * @return A list of NameEntity objects representing the names retrieved from the database.
     */
    override suspend fun getAllahNamesFromDatabase(): List<NameEntity> {
        return nameDao.getAllNames()
    }
}
