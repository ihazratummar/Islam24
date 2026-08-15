package com.hazrat.usecase.prayer

import com.hazrat.domain.repository.prayer.PrayerSettingRepository
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError


/**
 * @author hazratummar
 * Created on 15/08/26
 */

class UpdateCalculationMethodUseCase(
    private val prayerSettingRepository: PrayerSettingRepository
) {

    suspend operator fun invoke(method: Int) : Result<Boolean, DatabaseError> {
        return prayerSettingRepository.insertCalculationMethod(method = method)
    }

}