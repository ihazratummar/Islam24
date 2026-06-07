package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.model.IslamicEventsInfoModel
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 16/05/26
 */

class GetIslamicEventsUseCase(
    private val prayerTimeRepository: PrayerTimeRepository
) {

    operator fun invoke() : Flow<List<IslamicEventsInfoModel?>> {
        return prayerTimeRepository.getAllHolidayFromToday()
    }

}