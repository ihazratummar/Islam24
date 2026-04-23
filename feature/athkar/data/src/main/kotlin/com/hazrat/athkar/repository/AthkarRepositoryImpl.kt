package com.hazrat.athkar.repository

import com.hazrat.athkar.domain.model.AthkarData
import com.hazrat.athkar.domain.repository.AthkarRepository
import com.hazrat.athkar.mapper.toDomain
import com.hazrat.athkar.mapper.toEntity
import com.hazrat.database.dao.AthkarDao
import com.hazrat.remote.api.AthkarApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * @author Hazrat Ummar Shaikh
 */

class AthkarRepositoryImpl(
    private val api: AthkarApiCall,
    private val athkarDao: AthkarDao
) : AthkarRepository {

    override suspend fun getAthkarFromApi(): List<AthkarData> {
        return withContext(Dispatchers.IO) {
            try {
                // First, attempt to get data from the local database
                val localData = athkarDao.getAllAthkar()
                if (localData.isNotEmpty()) {
                    // If local data exists, map it to the desired API model format
                    return@withContext localData.map { it.toDomain() }
                } else {
                    // Otherwise, fetch data from the API
                    val response = api.getAllAthkar()
                    if (response.isSuccessful) {
                        val apiData = response.body()?.data ?: emptyList()

                        // Map API data to entities and store in the local database
                        val entities = apiData.flatMap { it.toEntity()}
                        athkarDao.insertAthkar(entities)

                        return@withContext entities.map { it.toDomain() }
                    } else {
                        emptyList()
                    }
                }
            } catch (e: Exception) {
                // Handle the exception (log, rethrow, etc.)
                emptyList()
            } catch (e: HttpException) {
                emptyList()
            }
        }
    }


    override suspend fun getAthkarFromDb(): List<AthkarData> {
        return athkarDao.getAllAthkar().map { it.toDomain() }
    }
}