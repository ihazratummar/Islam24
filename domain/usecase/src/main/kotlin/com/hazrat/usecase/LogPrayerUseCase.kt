package com.hazrat.usecase

import com.hazrat.domain.repository.PrayerLogRepository
import com.hazrat.model.Prayer
import com.hazrat.utils.DateUtil
import java.time.Clock


/**
 * @author hazratummar
 * Created on 19/05/26
 */

class LogPrayerUseCase(
    private val prayerLogRepository: PrayerLogRepository,
    private val clock: Clock = Clock.systemDefaultZone()
) {
    /**
     * EDGE CASES:
     * - Future prayer: rejected (can only log past/current)
     * - Next day's Fajr after midnight: uses hijri date logic if needed
     * - Already logged: repository upsert is idempotent — safe to call again
     */
    suspend operator fun invoke(
        date: Long,
        prayer: Prayer
    ): Result<Unit> = runCatching {

        require(date < System.currentTimeMillis()) {"Cannot log a future prayer"}
        val localDate = DateUtil.toLocalDate(date)
        prayerLogRepository.logPrayer(date = localDate, prayer = prayer)
    }

}