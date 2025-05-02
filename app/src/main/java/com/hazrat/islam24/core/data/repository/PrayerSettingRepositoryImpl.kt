package com.hazrat.islam24.core.data.repository

import com.hazrat.islam24.core.data.dao.PrayerSettingDao
import com.hazrat.islam24.core.data.entity.PrayerCalculationEntity
import com.hazrat.islam24.core.data.entity.PrayerJuristicEntity
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerSettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-05-2025
 */

class PrayerSettingRepositoryImpl @Inject constructor(
    private val prayerSettingDao: PrayerSettingDao
) : PrayerSettingRepository {
    override fun getCalculationMethod(): Flow<PrayerCalculationEntity?> {
        return prayerSettingDao.getCalculationMethod().transform { entity ->
            if (entity == null) {
                insertCalculationMethod(PrayerCalculationEntity(method = 1))
                emit(PrayerCalculationEntity(method = 1))
            }
        }
    }

    override fun getJuristicMethod(): Flow<PrayerJuristicEntity?> {
        return prayerSettingDao.getJuristicMethod().transform { entity ->
            if (entity == null) {
                insertJuristicMethod(PrayerJuristicEntity(school = 1))
                emit(PrayerJuristicEntity(school = 1))
            } else {
                emit(entity)
            }
        }
    }


    override suspend fun insertCalculationMethod(prayerSettingEntity: PrayerCalculationEntity) {
        prayerSettingDao.insertCalculationMethod(prayerSettingEntity)
    }

    override suspend fun insertJuristicMethod(prayerSettingEntity: PrayerJuristicEntity) {
        prayerSettingDao.insertJuristicMethod(prayerSettingEntity)
    }
}