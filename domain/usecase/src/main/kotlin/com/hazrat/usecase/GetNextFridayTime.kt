package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerTimeRepositoryNew
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 17/05/26
 */

class GetNextFridayTime(private val prayerTimeRepositoryNew: PrayerTimeRepositoryNew) {

    operator fun invoke() : Flow<Long?>{
        return prayerTimeRepositoryNew.getNextFridayTime()
    }

}