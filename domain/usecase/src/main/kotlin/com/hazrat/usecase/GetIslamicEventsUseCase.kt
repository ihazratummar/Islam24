package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerTimeRepositoryNew
import com.hazrat.model.IslamicEventsInfoModel


/**
 * @author hazratummar
 * Created on 16/05/26
 */

class GetIslamicEventsUseCase(
    private val prayerTimeRepositoryNew: PrayerTimeRepositoryNew
) {

    suspend operator fun invoke() : List<IslamicEventsInfoModel> {
        return prayerTimeRepositoryNew.getAllHolidayFromToday()
    }

}