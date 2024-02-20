package com.hazrat.islam24.domain.repository.prayertime

import com.hazrat.islam24.data.dao.PrayerSettingDao
import com.hazrat.islam24.data.entity.PrayerSettingEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrayerSettingRepository @Inject constructor(
    private val prayerSettingDao: PrayerSettingDao
) {


    fun getMethod(): Flow<List<PrayerSettingEntity>> = prayerSettingDao.getMethod()
    suspend fun insertMethod(prayerSettingEntity: PrayerSettingEntity) = prayerSettingDao.insertMethod(prayerSettingEntity)
    suspend fun updateMethod(prayerSettingEntity: PrayerSettingEntity) = prayerSettingDao.updateMethod(prayerSettingEntity)
    suspend fun deleteAllMethod() = prayerSettingDao.deleteAllMethod()
    suspend fun deleteMethod(prayerSettingEntity: PrayerSettingEntity) = prayerSettingDao.deleteMethod(prayerSettingEntity)


}