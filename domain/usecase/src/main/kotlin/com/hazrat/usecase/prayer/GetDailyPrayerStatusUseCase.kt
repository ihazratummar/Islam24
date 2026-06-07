package com.hazrat.usecase.prayer

import com.hazrat.domain.repository.PrayerLogRepository
import com.hazrat.model.DailyPrayerStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


/**
 * @author hazratummar
 * Created on 19/05/26
 */

class GetDailyPrayerStatusUseCase(private val repository: PrayerLogRepository) {
    operator fun invoke(date: LocalDate): Flow<DailyPrayerStatus> =
        repository.observeDailyStatus(date)
}