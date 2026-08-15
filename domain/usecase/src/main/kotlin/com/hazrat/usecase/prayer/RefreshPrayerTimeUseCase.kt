package com.hazrat.usecase.prayer

import com.hazrat.domain.repository.prayer.PrayerTimeRepository
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.PrayerTimeError


/**
 * @author hazratummar
 * Created on 15/08/26
 */

class RefreshPrayerTimeUseCase(
    private val prayerTimeRepository: PrayerTimeRepository
) {

    suspend operator fun invoke(): Result<Int, PrayerTimeError> {
        return prayerTimeRepository.refreshPrayerTimes()
    }

}