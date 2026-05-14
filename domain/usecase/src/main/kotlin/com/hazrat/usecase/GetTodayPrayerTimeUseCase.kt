package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.model.MinimalPrayerData
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 13/05/26
 */

class GetTodayPrayerTimeUseCase(
    private val prayerTimeRepository : PrayerTimeRepository
) {

    suspend operator  fun invoke(): Flow<MinimalPrayerData> {
        return prayerTimeRepository.getTodayPrayerTime()
    }

}