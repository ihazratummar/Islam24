package com.hazrat.usecase.prayer

import com.hazrat.domain.repository.prayer.PrayerSettingRepository
import com.hazrat.model.prayersettingmodel.UserPrayerSettingModel
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 15/08/26
 */

class UserPrayerSettingUseCase(
    private val prayerSettingRepository: PrayerSettingRepository
) {

    operator fun invoke(): Flow<UserPrayerSettingModel> {
        return prayerSettingRepository.getUserPrayerSetting()
    }

}