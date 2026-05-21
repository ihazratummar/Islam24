package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.model.MinimalPrayerData
import com.hazrat.utils.HijriDateUtils
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.PrayerTimeError
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 21/05/26
 */

class GetPrayerTimeWindowForDaysUseCase(
    private val prayerTimeRepository: PrayerTimeRepository
) {

    operator fun invoke(
        anchor: HijriDateUtils.HijriDate,
        daysBefore: Int = 7,
        daysAfter: Int = 7
    ): Flow<Result<List<MinimalPrayerData>, PrayerTimeError>>{
        val from = HijriDateUtils.offset(from = anchor, day = -daysBefore)
        val to = HijriDateUtils.offset(from = anchor, day = daysAfter)
        return prayerTimeRepository.getPrayerTimesInHijriRange(fromKey = from.toShortKey(), toKey = to.toShortKey())
    }

}