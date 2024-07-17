package com.hazrat.islam24.core.domain.repository.prayertime

/**
 * @author Hazrat Ummar Shaikh
 */

import com.hazrat.islam24.core.data.entity.PrayerTimeEntity
import kotlinx.coroutines.flow.Flow

interface PrayerTimeRepository {
    suspend fun fetchAndSavePrayerTimesForMonth(): List<PrayerTimeEntity>
    suspend fun insertAllPrayerTimes(prayerTimes: List<PrayerTimeEntity>): List<PrayerTimeEntity>
    fun getAllPrayer(): Flow<List<PrayerTimeEntity>>
    suspend fun deletePrayerTime(prayerTimeEntity: List<PrayerTimeEntity>)
    suspend fun deleteAllPrayer()
}