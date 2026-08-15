package com.hazrat.usecase.prayer

import com.hazrat.domain.repository.prayer.PrayerSettingRepository
import com.hazrat.model.Prayer


/**
 * @author hazratummar
 * Created on 15/08/26
 */

class UpdatePrayerOffsetMinuteUseCase(
    private val prayerSettingRepository: PrayerSettingRepository
) {

    suspend operator fun invoke(prayer: Prayer, offset: Int){
        prayerSettingRepository.updatePrayerOffset(prayer = prayer, offset = offset)
    }

}