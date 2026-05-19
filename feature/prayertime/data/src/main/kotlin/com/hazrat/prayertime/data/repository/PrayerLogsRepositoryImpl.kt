package com.hazrat.prayertime.data.repository

import com.hazrat.database.dao.PrayerLogDao
import com.hazrat.domain.repository.PrayerLogRepository
import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.Prayer
import com.hazrat.model.PrayerStreakInfo
import com.hazrat.prayertime.data.mapper.PrayerLogMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.sql.Date
import java.time.LocalDate
import java.time.YearMonth


/**
 * @author hazratummar
 * Created on 18/05/26
 */

class PrayerLogsRepositoryImpl(
    private val prayerLogMapper: PrayerLogMapper,
    private val prayerLogDao: PrayerLogDao
) : PrayerLogRepository {

    override suspend fun logPrayer(date: LocalDate, prayer: Prayer) =
        withContext(Dispatchers.IO) {

            val entity = prayerLogMapper.toEntity(date = date, prayer = prayer)
            prayerLogDao.upsertLog(entity = entity)
        }

    override suspend fun unLogPrayer(date: LocalDate, prayer: Prayer) =
        withContext(Dispatchers.IO){
            prayerLogDao.hardDelete(date = date.toString(), prayer = prayer.name)
        }

    override fun observeDailyStatus(date: LocalDate): Flow<DailyPrayerStatus> =
        prayerLogDao.observeDailyLogs(date = date.toString())
            .map { entities ->
                prayerLogMapper.toDailyStatusList(date = date, prayers = entities)
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)

    override fun observeMonthLogs(date: YearMonth): Flow<Map<LocalDate, Set<Prayer>>> {
        TODO("Not yet implemented")
    }

    override fun observeIsLogged(
        date: LocalDate,
        prayer: Prayer
    ): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun computeStreakInfo(today: LocalDate): PrayerStreakInfo {
        TODO("Not yet implemented")
    }
}