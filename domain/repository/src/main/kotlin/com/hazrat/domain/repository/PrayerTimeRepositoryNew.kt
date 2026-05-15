package com.hazrat.domain.repository

import com.hazrat.model.MinimalPrayerData
import com.hazrat.model.PrayerTimeModel
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.PrayerTimeError
import kotlinx.coroutines.flow.Flow

/**
 * @author Hazrat Ummar Shaikh
 */

/**
 * Contract for all prayer-time data operations.
 *
 * Rules:
 *  - Every function returns a [Flow] — no blocking suspend funs in the public surface.
 *  - Errors are wrapped in [PrayerTimeResult.Error], never thrown raw.
 *  - No Android framework types (Context, Intent) in signatures.
 */
interface PrayerTimeRepositoryNew {

    /**
     * Emits today's prayer data. Triggers a network fetch if the DB has no
     * entry for today. Errors are surfaced as [Result.Error].
     */
    fun getTodayPrayerTime(): Flow<Result<MinimalPrayerData , PrayerTimeError>>

    /**
     * Emits the full list of all locally cached prayer times.
     */
    fun getAllPrayer(): Flow<List<PrayerTimeModel>>


    /**
     * Force-refreshes prayer times from the network for the current year,
     * overwriting cached data. Designed for user-triggered pull-to-refresh.
     *
     * Returns [Result.Success] with the count of saved entries,
     * or [Result.Error] with a user-facing message.
     */
    suspend fun refreshPrayerTimes(): Result<Int, PrayerTimeError>

    /**
     * Bootstrap + sync flow. Collect this once from [ViewModel.viewModelScope].
     * Handles: empty DB, old-format migration, current-year missing, December pre-fetch.
     */
    fun observeAndSyncPrayerTimes(): Flow<Result<List<PrayerTimeModel>, PrayerTimeError>>

    /**
     * Returns the Hijri day for today from the provided list, or null if unavailable.
     * Pure function — no DB / network side effects.
     */
    fun getHijriDay(prayerTimes: List<PrayerTimeModel>): Int?

    /**
     * Builds a plain-text share string for today's prayer times.
     * Returns null if today's data is not present in [prayerTimes].
     * The caller (ViewModel) owns launching the share Intent.
     */
    fun buildShareText(prayerTimes: List<PrayerTimeModel>): String?

}