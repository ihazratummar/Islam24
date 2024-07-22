package com.hazrat.islam24.core.domain.repository.prayertime

import com.hazrat.islam24.core.data.dao.PrayerSettingDao
import com.hazrat.islam24.core.data.entity.PrayerCalculationEntity
import com.hazrat.islam24.core.data.entity.PrayerJuristicEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrayerSettingRepository @Inject constructor(
    private val prayerSettingDao: PrayerSettingDao
) {

    fun getCalculationMethod(): Flow<PrayerCalculationEntity> = prayerSettingDao.getCalculationMethod()
    fun getJuristicMethod(): Flow<PrayerJuristicEntity> = prayerSettingDao.getJuristicMethod()
    suspend fun insertCalculationMethod(prayerSettingEntity: PrayerCalculationEntity) = prayerSettingDao.insertCalculationMethod(prayerSettingEntity)
    suspend fun deleteCalculationMethod() = prayerSettingDao.deleteAllMethod()
    suspend fun deleteJuristicMethod() = prayerSettingDao.deleteAllJuristic()
    suspend fun deleteMethod(prayerSettingEntity: PrayerCalculationEntity) = prayerSettingDao.deleteMethod(prayerSettingEntity)
    suspend fun insertJuristicMethod(prayerSettingEntity: PrayerJuristicEntity) = prayerSettingDao.insertJuristicMethod(prayerSettingEntity)
}