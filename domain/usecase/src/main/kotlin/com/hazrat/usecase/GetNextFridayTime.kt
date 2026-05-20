package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerTimeRepository
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 17/05/26
 */

class GetNextFridayTime(private val prayerTimeRepository: PrayerTimeRepository) {

    operator fun invoke() : Flow<Long?>{
        return prayerTimeRepository.getNextFridayTime()
    }

}