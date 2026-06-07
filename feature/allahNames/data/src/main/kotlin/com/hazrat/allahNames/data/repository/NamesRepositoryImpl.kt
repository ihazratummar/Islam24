package com.hazrat.allahNames.data.repository

import com.hazrat.allahNames.data.mappers.toDomain
import com.hazrat.allahNames.data.mappers.toDomainList
import com.hazrat.allahNames.data.mappers.toEntity
import com.hazrat.allahNames.model.namesofallah.NameOfAllahData
import com.hazrat.allahNames.repository.NamesRepository
import com.hazrat.database.dao.AllahNameDao
import com.hazrat.remote.api.NamesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Implementation of the NamesRepository interface.
 * This class is responsible for interacting with the Names API and local database to manage names data.
 *
 * @property api The NamesApi instance for making network requests.
 * @property nameDao The NameDao instance for interacting with the local database.
 */
class NamesRepositoryImpl(
    private val api: NamesApi,
    private val nameDao: AllahNameDao
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
                    localNames.map { it.toDomain() }
                } else {
                    // Fetch names from API and insert into local database
                    val remoteNames = api.getAllNames()
                    val entities = remoteNames.toEntity()
                    nameDao.insertName(entities)
                    remoteNames.toDomain()
                }
            } catch (e: Exception) {
                Timber.tag("NamesRepositoryImpl").e("Error fetching names: ${e.message}")
                // Handle exceptions and return an empty list
                emptyList()
            }
        }
    }


    /**
     * Retrieves all Allah's names from the local database.
     *
     * @return A list of NameEntity objects representing the names retrieved from the database.
     */
    override fun getAllahNamesFromDatabase(): List<NameOfAllahData> {
        return nameDao.getAllNames().toDomainList()
    }
}