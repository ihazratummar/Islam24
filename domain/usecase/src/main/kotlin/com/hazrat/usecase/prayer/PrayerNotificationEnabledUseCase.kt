package com.hazrat.usecase.prayer

import com.hazrat.domain.repository.PrayerSettingRepository
import com.hazrat.model.Prayer


/**
 * @author hazratummar
 * Created on 19/05/26
 */

class PrayerNotificationEnabledUseCase(
    private val prayerSettingRepository: PrayerSettingRepository
) {

    suspend operator fun invoke(prayer: Prayer, enabled: Boolean){
        prayerSettingRepository.prayerNotificationEnabled(prayer = prayer, enabled = enabled)
    }

}