package com.hazrat.usecase.prayer

import com.hazrat.model.Prayer


/**
 * @author hazratummar
 * Created on 19/05/26
 */

class TogglePrayerUseCase(
    private val logPrayer: LogPrayerUseCase,
    private val unLogPrayer: UnLogPrayerUseCase
) {
    suspend operator fun invoke(
        prayer: Prayer,
        currentlyLogged: Boolean,
        date: Long
    ) = if (currentlyLogged) unLogPrayer(prayer, date)
    else logPrayer(date = date, prayer = prayer)
}