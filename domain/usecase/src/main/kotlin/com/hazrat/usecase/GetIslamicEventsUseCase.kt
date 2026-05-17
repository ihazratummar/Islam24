package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerTimeRepositoryNew
import com.hazrat.model.IslamicEventsInfoModel
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 16/05/26
 */

class GetIslamicEventsUseCase(
    private val prayerTimeRepositoryNew: PrayerTimeRepositoryNew
) {

    operator fun invoke() : Flow<List<IslamicEventsInfoModel?>> {
        return prayerTimeRepositoryNew.getAllHolidayFromToday()
    }

}