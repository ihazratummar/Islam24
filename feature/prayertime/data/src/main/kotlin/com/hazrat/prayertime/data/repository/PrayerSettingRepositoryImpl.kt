package com.hazrat.prayertime.data.repository

import com.hazrat.database.dao.PrayerSettingDao
import com.hazrat.database.entity.PrayerCalculationEntity
import com.hazrat.database.entity.PrayerJuristicEntity
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.PrayerSettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import timber.log.Timber

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-05-2025
 */

class PrayerSettingRepositoryImpl(
    private val prayerSettingDao: PrayerSettingDao,
    private val userDataStore: UserDataStore
) : PrayerSettingRepository {
    override suspend fun getCalculationMethod(): Flow<PrayerCalculationEntity?> {
        Timber.tag("PrayerSettingRepositoryImpl")
            .d("Calculation method: ${prayerSettingDao.getCalculationMethod()}")
        return prayerSettingDao.getCalculationMethod().transform { entity ->
            Timber.tag("PrayerSettingRepositoryImpl").d("Calculation method: $entity")
            if (entity == null) {
                Timber.tag("PrayerSettingRepositoryImpl")
                    .d("Calculation method is null, inserting default method")
                insertCalculationMethod(method = 1)
                emit(PrayerCalculationEntity(method = 1))
            }else{
                Timber.tag("PrayerSettingRepositoryImpl")
                    .d("Calculation method is not null, emitting method: $entity")
                emit(entity)
            }
        }
    }

    override suspend fun getJuristicMethod(): Flow<PrayerJuristicEntity?> {
        Timber.tag("PrayerSettingRepositoryImpl")
            .d("Juristic method: ${prayerSettingDao.getJuristicMethod()}")
        return prayerSettingDao.getJuristicMethod().transform { entity ->
            Timber.tag("PrayerSettingRepositoryImpl").d("Juristic method: $entity")
            if (entity == null) {
                Timber.tag("PrayerSettingRepositoryImpl")
                    .d("Juristic method is null, inserting default method")
                insertJuristicMethod(method = 1)
                emit(PrayerJuristicEntity(school = 1))
            } else {
                Timber.tag("PrayerSettingRepositoryImpl")
                    .d("Juristic method is not null, emitting method: $entity")
                emit(entity)
            }
        }
    }


    override suspend fun insertCalculationMethod(method: Int) {
        userDataStore.saveSetPrayerCalculationMethod(calculationMethod = method)
    }

    override suspend fun insertJuristicMethod(method: Int) {
        userDataStore.savePrayerJuristicMethod(method= method)
    }
}