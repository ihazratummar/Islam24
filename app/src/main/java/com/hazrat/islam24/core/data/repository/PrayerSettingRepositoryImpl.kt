package com.hazrat.islam24.core.data.repository

import android.util.Log
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
    override suspend fun getCalculationMethod(): Flow<PrayerCalculationEntity?> {
        Log.d("PrayerSettingRepositoryImpl", "Calculation method: ${prayerSettingDao.getCalculationMethod()}")
        return prayerSettingDao.getCalculationMethod().transform { entity ->
            Log.d("PrayerSettingRepositoryImpl", "Calculation method: $entity")
            if (entity == null) {
                Log.d("PrayerSettingRepositoryImpl", "Calculation method is null, inserting default method")
                insertCalculationMethod(PrayerCalculationEntity(method = 1))
                emit(PrayerCalculationEntity(method = 1))
            }else{
                Log.d("PrayerSettingRepositoryImpl", "Calculation method is not null, emitting method: $entity")
                emit(entity)
            }
        }
    }

    override suspend fun getJuristicMethod(): Flow<PrayerJuristicEntity?> {
        Log.d("PrayerSettingRepositoryImpl", "Juristic method: ${prayerSettingDao.getJuristicMethod()}")
        return prayerSettingDao.getJuristicMethod().transform { entity ->
            Log.d("PrayerSettingRepositoryImpl", "Juristic method: $entity")
            if (entity == null) {
                Log.d("PrayerSettingRepositoryImpl", "Juristic method is null, inserting default method")
                insertJuristicMethod(PrayerJuristicEntity(school = 1))
                emit(PrayerJuristicEntity(school = 1))
            } else {
                Log.d("PrayerSettingRepositoryImpl", "Juristic method is not null, emitting method: $entity")
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