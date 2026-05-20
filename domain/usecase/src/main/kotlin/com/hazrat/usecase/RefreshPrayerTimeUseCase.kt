package com.hazrat.usecase

import android.location.Location
import com.hazrat.database.dao.LocationNameDao
import com.hazrat.domain.repository.PrayerTimeRepositoryNew
import com.hazrat.location.model.LocationResult
import com.hazrat.location.repository.LocationRepository
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.PrayerTimeError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest


/**
 * @author hazratummar
 * Created on 20/05/26
 */

class RefreshPrayerTimeUseCase(
    private val prayerTimeRepositoryNew: PrayerTimeRepositoryNew,
    private val locationRepository: LocationRepository,
    private val locationDao: LocationNameDao
) {

    companion object {
        private const val PRAYER_THRESHOLD_METERS = 5000f
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Result<Int, PrayerTimeError>> {
        return locationRepository
            .observeLocationUpdates()
            .filterIsInstance<LocationResult.Success>()
            .map { it.location }
            .distinctUntilChanged { old, new ->
                old.distanceTo(new) < PRAYER_THRESHOLD_METERS
            }
            .mapLatest {location ->
                val cache = locationDao.getLocationDetails().firstOrNull()
                val shouldRefresh = when {
                    cache == null -> true
                    else -> {
                        val result = FloatArray(1)
                        Location.distanceBetween(
                            cache.latitude,
                            cache.longitude,
                            location.latitude,
                            location.longitude,
                            result
                        )
                        result[0] >= PRAYER_THRESHOLD_METERS
                    }
                }
                if (shouldRefresh){
                    prayerTimeRepositoryNew.refreshPrayerTimes()
                }else{
                    Result.Success(0)
                }
            }
    }

}