package com.hazrat.usecase.prayer

import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.model.MinimalPrayerData
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.PrayerTimeError
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 13/05/26
 */

class GetTodayPrayerTimeUseCase(
    private val prayerTimeRepository : PrayerTimeRepository
) {

    operator  fun invoke(): Flow<Result<MinimalPrayerData, PrayerTimeError>> {
        return prayerTimeRepository.getTodayPrayerTime()
    }

}