package com.hazrat.usecase.prayer

import com.hazrat.domain.repository.PrayerLogRepository
import com.hazrat.model.Prayer
import com.hazrat.utils.DateUtil
import java.time.Clock


/**
 * @author hazratummar
 * Created on 19/05/26
 */

class UnLogPrayerUseCase (
    private val prayerLogRepository: PrayerLogRepository,
    private val clock: Clock = Clock.systemDefaultZone()
) {

    /**
     * EDGE CASES:
     * - Not logged: soft-delete/hard-delete on non-existent row is safe (0 rows affected)
     * - Past dates: allow unlogging any past date (user correction)
     */

    suspend operator  fun invoke(prayer: Prayer, date: Long) {
        prayerLogRepository.unLogPrayer(date = DateUtil.toLocalDate(date), prayer = prayer)
    }

}