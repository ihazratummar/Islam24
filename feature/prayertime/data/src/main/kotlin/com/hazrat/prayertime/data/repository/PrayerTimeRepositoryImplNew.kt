package com.hazrat.prayertime.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import com.hazrat.database.dao.PrayerTimeDao
import com.hazrat.database.entity.PrayerTimeEntity
import com.hazrat.domain.repository.PrayerSettingRepository
import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.domain.repository.PrayerTimeRepositoryNew
import com.hazrat.location.model.LocationResult
import com.hazrat.location.repository.LocationRepository
import com.hazrat.model.MinimalPrayerData
import com.hazrat.model.PrayerTimeModel
import com.hazrat.prayertime.data.mapper.toEntityList
import com.hazrat.prayertime.data.mapper.toMinimalPrayerData
import com.hazrat.prayertime.data.mapper.toPrayerModelList
import com.hazrat.remote.api.PrayerTimeApi
import com.hazrat.ui.R
import com.hazrat.utils.DateUtil
import com.hazrat.utils.network.ConnectivityObserver
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.PrayerTimeError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

/**
 * @author Hazrat Ummar Shaikh
 * Created on 17-12-2024
 *
 * Implementation of [PrayerTimeRepository].
 *
 * Responsibilities:
 *  - Fetch yearly prayer times from the remote API and persist them in the local database.
 *  - Serve prayer times to the rest of the app exclusively via observable [Flow]s.
 *  - Surface all failures as [PrayerTimeResult] instead of throwing raw exceptions.
 *
 * Threading: all DB / network work runs on [DispatcherProvider.io].
 * The repository itself holds NO mutable UI state — that belongs in the ViewModel.
 */
class PrayerTimeRepositoryImplNew(
    private val api: PrayerTimeApi,
    private val locationRepository: LocationRepository,
    private val prayerSettingRepository: PrayerSettingRepository,
    private val prayerTimeDao: PrayerTimeDao,
    private val context: Context,
    private val connectivityObserver: ConnectivityObserver,
    private val dispatchers: DispatcherProvider,  // Injected — never hardcode Dispatchers.IO
) : PrayerTimeRepositoryNew {

    // ─────────────────────────────────────────────────────────────────────────
    // Companion
    // ─────────────────────────────────────────────────────────────────────────

    companion object {
        private const val TAG = "PrayerTimeRepository"
        private const val FALLBACK_LATITUDE = 21.42   // Mecca — only used as last resort
        private const val FALLBACK_LONGITUDE = 39.82
        private const val SHARE_URL =
            "https://play.google.com/store/apps/details?id=com.hazrat.islam24"
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Public API — all flows, no blocking suspend funs in the public surface
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Emits today's minimal prayer data.
     *
     * If the database has no entry for today, a network fetch is triggered
     * transparently. Errors are wrapped in [Result.Error] so the
     * ViewModel can react without try/catch.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTodayPrayerTime(): Flow<Result<MinimalPrayerData, PrayerTimeError>> =
        prayerTimeDao
            .getPrayerTimeForToday(currentDate = DateUtil.getCurrentDate())
            .flatMapLatest { entity ->
                if (entity != null) {
                    flowOf(
                        Result.Success(entity.toMinimalPrayerData())
                    )
                } else {
                    flow<Result<MinimalPrayerData, PrayerTimeError>> {
                        val result = fetchAndSaveYearlyPrayerTimes(
                            year = DateUtil.getCurrentYear(),
                            month = DateUtil.getCurrentMonth(),
                        )
                        when (result) {
                            is Result.Success -> {
                                val today = result.data.find {
                                    it.gregorianDate == DateUtil.getCurrentDate()
                                }
                                if (today != null) {
                                    emit(Result.Success(today.toMinimalPrayerData()))
                                } else {
                                    emit(
                                        Result.Error(PrayerTimeError.Local.EMPTY_RESPONSE)
                                    )
                                }
                            }
                            is Result.Error -> {
                                emit(Result.Error(result.error))
                            }
                        }
                    }
                }
            }
            .catch { e ->
                Timber.tag(TAG).e(e, "getTodayPrayerTime failed")
                emit(
                    Result.Error(
                        PrayerTimeError.Local.EMPTY_RESPONSE
                    )
                )
            }
            .flowOn(dispatchers.io)

    /**
     * Emits the full list of all cached prayer times.
     * Returns an empty list until the DB is populated.
     */
    override fun getAllPrayer(): Flow<List<PrayerTimeModel>> =
        prayerTimeDao
            .getAllPrayer()
            .distinctUntilChanged()
            .conflate()
            .map { it.toPrayerModelList() }
            .catch { e ->
                Timber.tag(TAG).e(e, "getAllPrayer DB read failed")
                emit(emptyList())
            }
            .flowOn(dispatchers.io)

    /**
     * Force-refreshes prayer times from the network for the current year,
     * overwriting the cached data. Intended for user-triggered pull-to-refresh.
     *
     * - Deletes all existing DB rows for the current year before re-inserting,
     *   so stale/partial data is never served after a partial failure.
     * - Also pre-fetches next year if it is December.
     * - Returns [Result.Success] with the count of saved entries,
     *   or [Result.Error] with a user-facing message.
     */
    override suspend fun refreshPrayerTimes(): Result<Int, PrayerTimeError> {
        val currentYear = DateUtil.getCurrentYear()
        val currentMonth = DateUtil.getCurrentMonth()

        // Guard : no point trying to without network
        val isNetworkAvailable = connectivityObserver.observer().first() == ConnectivityObserver.Status.Available
        if (!isNetworkAvailable){
            return Result.Error(PrayerTimeError.Network.NO_CONNECTION)
        }

        // Wipe current Prayer times
        prayerTimeDao.deleteAllPrayer()
        val result = fetchAndSaveYearlyPrayerTimes(year = currentYear, month = currentMonth)

        // Opportunistic: pre-fetch next year while we're already online in December

        if (currentMonth == 12 && result is Result.Success ){
            prefetchNextYearIfNeeded(
                prayerList = emptyList(),
                currentYear = currentYear,
                isNetworkAvailable = true
            )
        }

        return when(result){
            is Result.Error  -> Result.Error(PrayerTimeError.ErrorMessage("Something Went Wrong"))
            is Result.Success  -> Result.Success(result.data.size)
        }
    }

    /**
     * Bootstraps prayer-time data and keeps it up to date.
     *
     * Designed to be collected once from [ViewModel.viewModelScope]. Returns a
     * [Flow] so the ViewModel controls the lifecycle; it does NOT block forever
     * as a bare suspend function.
     *
     * Emits [Result] for each significant state change so the UI can
     * show loading/error indicators.
     */
    override fun observeAndSyncPrayerTimes(): Flow<Result<List<PrayerTimeModel>, PrayerTimeError>> =
        getAllPrayer()
            .onEach { prayerList ->
                val currentYear = DateUtil.getCurrentYear()
                val hasOldFormat = prayerList.any { it.gregorianDate.indexOf("-") == 2 }
                val hasCurrentYearData = prayerList.any { it.gregorianYear.toInt() == currentYear }
                val isNetworkAvailable =
                    connectivityObserver.observer().first() == ConnectivityObserver.Status.Available

                when {
                    // ── Case 1: DB is empty or data is stale ──────────────────
                    prayerList.isEmpty() || hasOldFormat || !hasCurrentYearData -> {
                        if (!isNetworkAvailable) {
                            Timber.tag(TAG).w("No data and no network — cannot sync.")
                            return@onEach
                        }
                        if (hasOldFormat) {
                            Timber.tag(TAG)
                                .i("Old date format detected — clearing DB before re-fetch.")
                            prayerTimeDao.deleteAllPrayer()
                        }
                        fetchAndSaveYearlyPrayerTimes(currentYear, DateUtil.getCurrentMonth())
                    }

                    // ── Case 2: Data is current — maybe pre-fetch next year ───
                    else -> {
                        prefetchNextYearIfNeeded(prayerList, currentYear, isNetworkAvailable)
                    }
                }
            }
            .map { prayerList ->
                if (prayerList.isEmpty()) Result.Error<List<PrayerTimeModel>, PrayerTimeError>(PrayerTimeError.Local.EMPTY_RESPONSE)
                else Result.Success<List<PrayerTimeModel>, PrayerTimeError>(prayerList)
            }
            .catch { e ->
                Timber.tag(TAG).e(e, "observeAndSyncPrayerTimes failed")
                emit(Result.Error<List<PrayerTimeModel>, PrayerTimeError>(PrayerTimeError.Local.DB_READ_FAILED))
            }
            .flowOn(Dispatchers.IO)

    /**
     * Returns the Hijri day for today from the given prayer list,
     * or null if the data is not yet available (ViewModel handles null gracefully).
     */
    override fun getHijriDay(prayerTimes: List<PrayerTimeModel>): Int? {
        val today = DateUtil.getCurrentDate()
        return prayerTimes.find { it.gregorianDate == today }?.hijriDay
    }

    /**
     * Builds a shareable plain-text summary of today's prayer times.
     * Returns null if today's data is not present in [prayerTimes].
     *
     * Caller (ViewModel) is responsible for launching the share Intent — the
     * repository does not touch Android framework APIs.
     */
    override fun buildShareText(prayerTimes: List<PrayerTimeModel>): String? {
        val today = DateUtil.getCurrentDate()
        val todayEntry = prayerTimes.find { it.gregorianDate == today } ?: return null

        return buildString {
            appendLine("Today's prayer times")
            appendLine("${todayEntry.gregorianDate} // ${todayEntry.hijriDate}")
            appendLine()
            appendLine("${context.getString(R.string.fajr)}: ${DateUtil.dateLongToString(todayEntry.fajrTime)}")
            appendLine("${context.getString(R.string.dhuhr)}: ${DateUtil.dateLongToString(todayEntry.dhuhrTime)}")
            appendLine("${context.getString(R.string.asr)}: ${DateUtil.dateLongToString(todayEntry.asrTime)}")
            appendLine(
                "${context.getString(R.string.maghrib)}: ${
                    DateUtil.dateLongToString(
                        todayEntry.maghribTime
                    )
                }"
            )
            appendLine(
                "${context.getString(R.string.isha_a)}: ${
                    DateUtil.dateLongToString(
                        todayEntry.ishaTime
                    )
                }"
            )
            appendLine()
            append("For More Visit, $SHARE_URL")
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fetches prayer times for [year]/[month] (annual=true) from the API,
     * persists all returned entities, and returns a [PrayerTimeResult].
     *
     * Never throws — all exceptions are caught and wrapped.
     */
    @WorkerThread
    private suspend fun fetchAndSaveYearlyPrayerTimes(
        year: Int,
        month: Int,
    ): Result<List<PrayerTimeEntity>, PrayerTimeError> {
        return try {
            // ── Network guard ────────────────────────────────────────────────
            val networkStatus = connectivityObserver.observer().first()
            if (networkStatus != ConnectivityObserver.Status.Available) {
                return Result.Error(error = PrayerTimeError.Network.NO_CONNECTION)
            }

            // ── Location — immutable val, never silent fallback ──────────────
            val (latitude, longitude) = resolveCoordinates()

            // ── Settings snapshot ────────────────────────────────────────────
            val method = prayerSettingRepository.getCalculationMethod().firstOrNull()?.method ?: 1
            val school = prayerSettingRepository.getJuristicMethod().firstOrNull()?.school ?: 0

            // ── API call ─────────────────────────────────────────────────────
            val apiResponse = api.newPrayerTimesRequest(
                year = year,
                month = month,
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                method = method,
                school = school,
                annual = true,
            )

            val entities = apiResponse.data.values.flatten().toEntityList()
            prayerTimeDao.insertAllPrayerTimes(entities)

            Timber.tag(TAG).d("Fetched and saved %d entries for %d/%d", entities.size, year, month)
            Result.Success(entities)

        } catch (e: HttpException) {
            Timber.tag(TAG).e("HTTP error fetching $year: ${e.code()} ${e.message()}")
            Result.Error(PrayerTimeError.ErrorMessage(("Server error (${e.code()}) while fetching prayer times.")))
        } catch (e: IOException) {
            Timber.tag(TAG).e("Network IO error for $year: ${e.message}")
            Result.Error(PrayerTimeError.ErrorMessage(("Network error — please check your connection.")))
        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Unexpected error fetching $year")
            Result.Error(PrayerTimeError.ErrorMessage(e.message ?: "Unexpected error."))
        }
    }

    /**
     * Pre-fetches next year's data in December if it isn't cached yet.
     * Errors are logged but not propagated — this is a best-effort background task.
     */
    private suspend fun prefetchNextYearIfNeeded(
        prayerList: List<PrayerTimeModel>,
        currentYear: Int,
        isNetworkAvailable: Boolean,
    ) {
        if (DateUtil.getCurrentMonth() != 12) return
        val hasNextYearData = prayerList.any { it.gregorianYear.toInt() == currentYear + 1 }
        if (hasNextYearData || !isNetworkAvailable) return

        Timber.tag(TAG).d("December detected — pre-fetching ${currentYear + 1} data.")
        fetchAndSaveYearlyPrayerTimes(year = currentYear + 1, month = 1)
    }

    /**
     * Resolves device coordinates.
     *
     * Returns actual GPS coordinates on success, or the Mecca fallback with a
     * Timber warning so the failure is always visible in logs.
     *
     * Returns a [Pair] of (latitude, longitude) as immutable vals.
     */
    private suspend fun resolveCoordinates(): Pair<Double, Double> =
        when (val result = locationRepository.getCurrentLocation()) {
            is LocationResult.Success -> result.location.latitude to result.location.longitude
            is LocationResult.Error -> {
                Timber.tag(TAG).w(
                    "Location unavailable (%s) — falling back to Mecca coordinates.",
                    result.error,
                )
                FALLBACK_LATITUDE to FALLBACK_LONGITUDE
            }
        }


}

// ─────────────────────────────────────────────────────────────────────────────
// Result wrapper  (put this in a shared domain module)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Sealed domain result type for all repository operations.
 * Replaces raw exception throwing in the public API surface.
 */


// ─────────────────────────────────────────────────────────────────────────────
// DispatcherProvider interface  (put this in a shared utils module)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Abstraction over [kotlinx.coroutines.CoroutineDispatcher] so tests can
 * inject [kotlinx.coroutines.test.UnconfinedTestDispatcher] without reflection.
 */
interface DispatcherProvider {
    val main: kotlinx.coroutines.CoroutineDispatcher
    val io: kotlinx.coroutines.CoroutineDispatcher
    val default: kotlinx.coroutines.CoroutineDispatcher
}

/** Production implementation — wire via DI (Hilt/Koin). */
class DefaultDispatcherProvider : DispatcherProvider {
    override val main = kotlinx.coroutines.Dispatchers.Main
    override val io = kotlinx.coroutines.Dispatchers.IO
    override val default = kotlinx.coroutines.Dispatchers.Default
}