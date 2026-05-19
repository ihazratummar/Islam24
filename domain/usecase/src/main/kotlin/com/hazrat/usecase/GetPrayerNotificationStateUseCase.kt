package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerSettingRepository
import com.hazrat.model.PrayerNotificationSettings
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 19/05/26
 */

class GetPrayerNotificationStateUseCase(
    private val prayerSettingRepository: PrayerSettingRepository
) {


    operator fun invoke(): Flow<PrayerNotificationSettings> {
        return prayerSettingRepository.getNotificationEnable()
    }

}