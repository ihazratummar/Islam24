package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.model.MinimalPrayerData


/**
 * @author hazratummar
 * Created on 13/05/26
 */

class GetTodayPrayerTimeUseCase(
    private val prayerTimeRepository : PrayerTimeRepository
) {

    suspend operator  fun invoke(): MinimalPrayerData {
        return prayerTimeRepository.getTodayPrayerTime()
    }

}