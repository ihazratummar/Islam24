package com.hazrat.domain.repository

import com.hazrat.model.DailyPrayerStatus
import com.hazrat.model.Prayer
import com.hazrat.model.PrayerStreakInfo
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth


/**
 * @author hazratummar
 * Created on 18/05/26
 */

interface PrayerLogRepository {

    suspend fun logPrayer(date: LocalDate, prayer: Prayer)
    suspend fun unLogPrayer(date: LocalDate, prayer: Prayer)

    fun observeDailyStatus(date: LocalDate) : Flow<DailyPrayerStatus>
    fun observeMonthLogs(date: YearMonth) : Flow<Map<LocalDate, Set<Prayer>>>
    fun observeIsLogged(date: LocalDate, prayer: Prayer): Flow<Boolean>

    suspend fun computeStreakInfo(today: LocalDate): PrayerStreakInfo


}