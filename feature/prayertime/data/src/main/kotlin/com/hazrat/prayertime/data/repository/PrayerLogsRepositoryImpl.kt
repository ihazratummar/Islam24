package com.hazrat.prayertime.data.repository

import com.hazrat.database.dao.prayer.PrayerLogDao
import com.hazrat.domain.repository.prayer.PrayerLogRepository
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
import java.time.LocalDate
import java.time.YearMonth

/**
 * Clean Implementation of PrayerLogRepository using Room local database.
 * @author hazratummar
 */
class PrayerLogsRepositoryImpl(
    private val prayerLogMapper: PrayerLogMapper,
    private val prayerLogDao: PrayerLogDao
) : PrayerLogRepository {

    override suspend fun logPrayer(date: LocalDate, prayer: Prayer) =
        withContext(Dispatchers.IO) {
            val kDate = kotlinx.datetime.LocalDate.parse(date.toString())
            val existing = prayerLogDao.getLogByDate(kDate)
            val updated = prayerLogMapper.toEntity(
                date = kDate,
                prayer = prayer,
                isLogged = true,
                existing = existing
            )
            prayerLogDao.upsert(updated)
        }

    override suspend fun unLogPrayer(date: LocalDate, prayer: Prayer) =
        withContext(Dispatchers.IO) {
            val kDate = kotlinx.datetime.LocalDate.parse(date.toString())
            val existing = prayerLogDao.getLogByDate(kDate) ?: return@withContext
            val updated = prayerLogMapper.toEntity(
                date = kDate,
                prayer = prayer,
                isLogged = false,
                existing = existing
            )
            prayerLogDao.upsert(updated)
        }

    override fun observeDailyStatus(date: LocalDate): Flow<DailyPrayerStatus> {
        val kDate = kotlinx.datetime.LocalDate.parse(date.toString())
        return prayerLogDao.observeLogByDate(kDate)
            .map { entity ->
                prayerLogMapper.toDailyStatus(date = date, entity = entity)
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override fun observeMonthLogs(date: YearMonth): Flow<Map<LocalDate, Set<Prayer>>> {
        val startLocalDate = date.atDay(1)
        val endLocalDate = date.atEndOfMonth()
        val kStart = kotlinx.datetime.LocalDate.parse(startLocalDate.toString())
        val kEnd = kotlinx.datetime.LocalDate.parse(endLocalDate.toString())

        return prayerLogDao.observeLogsBetween(kStart, kEnd)
            .map { entities ->
                val resultMap = mutableMapOf<LocalDate, Set<Prayer>>()
                for (entity in entities) {
                    val jDate = LocalDate.parse(entity.logDate.toString())
                    val dailyStatus = prayerLogMapper.toDailyStatus(jDate, entity)
                    if (dailyStatus.loggedPrayers.isNotEmpty()) {
                        resultMap[jDate] = dailyStatus.loggedPrayers
                    }
                }
                resultMap
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override fun observeIsLogged(date: LocalDate, prayer: Prayer): Flow<Boolean> {
        val kDate = kotlinx.datetime.LocalDate.parse(date.toString())
        return prayerLogDao.observeLogByDate(kDate)
            .map { entity ->
                prayerLogMapper.toDailyStatus(date, entity).isLogged(prayer)
            }
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }

    override fun observeTotalLoggedPrayers(): Flow<Int> =
        prayerLogDao.observeAllLogs().map { logs ->
            logs.sumOf { entity ->
                var count = 0
                if (entity.fajr) count++
                if (entity.dhuhr) count++
                if (entity.asr) count++
                if (entity.maghrib) count++
                if (entity.isha) count++
                count
            }
        }.distinctUntilChanged().flowOn(Dispatchers.IO)

    override fun observePrayerStreak(): Flow<Int> =
        prayerLogDao.getAllLoggedEntities().map { entities ->
            calculateCurrentStreak(entities)
        }.distinctUntilChanged().flowOn(Dispatchers.IO)

    override suspend fun computeStreakInfo(today: LocalDate): PrayerStreakInfo =
        withContext(Dispatchers.IO) {
            val startOfWeek = today.minusDays(today.dayOfWeek.value.toLong() - 1)
            val weeklyList = (0..6).map { dayOffset ->
                val d = startOfWeek.plusDays(dayOffset.toLong())
                val kD = kotlinx.datetime.LocalDate.parse(d.toString())
                val entity = prayerLogDao.getLogByDate(kD)
                prayerLogMapper.toDailyStatus(d, entity)
            }

            val todayStatus = weeklyList.find { it.date == today } ?: DailyPrayerStatus(today, emptySet())
            val kToday = kotlinx.datetime.LocalDate.parse(today.toString())
            val todayEntity = prayerLogDao.getLogByDate(kToday)
            val currentStreak = calculateCurrentStreak(listOfNotNull(todayEntity))

            PrayerStreakInfo(
                currentStreak = currentStreak,
                longestStreak = currentStreak,
                todayProgress = todayStatus.completionPercentage,
                weeklyCompletion = weeklyList
            )
        }

    private fun calculateCurrentStreak(entities: List<com.hazrat.database.entity.prayer.PrayerLogEntity>): Int {
        if (entities.isEmpty()) return 0
        val entityMap = entities.associateBy { LocalDate.parse(it.logDate.toString()) }

        var streak = 0
        var checkDate = LocalDate.now()

        val todayStatus = prayerLogMapper.toDailyStatus(checkDate, entityMap[checkDate])
        if (todayStatus.isEmpty) {
            checkDate = checkDate.minusDays(1)
        }

        while (true) {
            val status = prayerLogMapper.toDailyStatus(checkDate, entityMap[checkDate])
            if (status.isComplete || status.isPartial) {
                streak++
                checkDate = checkDate.minusDays(1)
            } else {
                break
            }
        }
        return streak
    }

    override suspend fun clearData() {
        prayerLogDao.clearAll()
    }
}