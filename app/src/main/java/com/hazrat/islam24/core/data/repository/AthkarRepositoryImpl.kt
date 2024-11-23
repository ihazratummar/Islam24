package com.hazrat.islam24.core.data.repository

import com.hazrat.islam24.core.api.AthkarApiCall
import com.hazrat.islam24.core.data.dao.AthkarDao
import com.hazrat.islam24.core.data.entity.AthkarDataEntity
import com.hazrat.islam24.core.domain.model.athkar.AthkarApiModel
import com.hazrat.islam24.core.domain.model.athkar.Data
import com.hazrat.islam24.core.domain.model.athkar.MorningAkhtarData
import com.hazrat.islam24.core.domain.repository.AthkarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

class AthkarRepositoryImpl @Inject constructor(
    private val api: AthkarApiCall,
    private val athkarDao: AthkarDao
) : AthkarRepository {

    override suspend fun getAthkarFromApi(): List<AthkarApiModel> {
        return withContext(Dispatchers.IO) {
            try {
                // First, attempt to get data from the local database
                val localData = athkarDao.getAllAthkar()
                if (localData.isNotEmpty()) {
                    // If local data exists, map it to the desired API model format
                    return@withContext localData.map { athkarEntityToData(it) }
                } else {
                    // Otherwise, fetch data from the API
                    val response = api.getAllAthkar()
                    if (response.isSuccessful) {
                        val apiData = response.body()?.data ?: emptyList()

                        // Map API data to entities and store in the local database
                        val entities = apiData.flatMap { athkarDataToEntity(it) }
                        athkarDao.insertAthkar(entities)

                        // Convert the API response data to the return type
                        return@withContext apiData.map {
                            AthkarApiModel(
                                listOf(it),
                                status = "success"
                            )
                        }
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


    override suspend fun getAthkarFromDb(): List<AthkarDataEntity> {
        return athkarDao.getAllAthkar()
    }

    private fun athkarDataToEntity(data: Data): List<AthkarDataEntity> {
        return data.morning.map { morningAthkar ->
            AthkarDataEntity(
                type = morningAthkar.toString(),
                bismillah = morningAthkar.bismillah,
                arabicText = morningAthkar.arabicText,
                enTranslation = morningAthkar.enTranslation,
                enTransliteration = morningAthkar.enTransliteration,
                bnTranslation = morningAthkar.bnTranslation,
                bnTransliteration = morningAthkar.bnTransliteration,
                count = morningAthkar.count,
                number = morningAthkar.number
            )
        }
    }


    private fun athkarEntityToData(entity: AthkarDataEntity): AthkarApiModel {
        val morningAkhtarData = MorningAkhtarData(
            bismillah = entity.bismillah,
            arabicText = entity.arabicText,
            enTranslation = entity.enTranslation,
            enTransliteration = entity.enTransliteration,
            bnTranslation = entity.bnTranslation,
            bnTransliteration = entity.bnTransliteration,
            count = entity.count,
            number = entity.number
        )
        val data = Data(morning = listOf(morningAkhtarData))
        return AthkarApiModel(data = listOf(data), status = "success")
    }
}