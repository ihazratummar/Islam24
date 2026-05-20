package com.hazrat.usecase

import android.location.Location
import com.hazrat.domain.repository.LocationNameRepository
import com.hazrat.domain.repository.PrayerTimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import java.util.Timer


/**
 * @author hazratummar
 * Created on 20/05/26
 */

class RefreshPrayerTimeUseCase(
    private val locationNameRepository: LocationNameRepository,
    private val prayerTimeRepository: PrayerTimeRepository
) {

    companion object {
        private const val PRAYER_THRESHOLD = 5000f
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Unit> {
        return locationNameRepository.observeLocationInfo()
            .mapLatest { location ->
                println("REFRESH USECASE HIT")
                val prayerLocation = prayerTimeRepository.getCurrentPrayerLocation()
                Timber.tag("RefreshPrayerUseCase").d("Prayer Location Data ${prayerLocation?.latitude} ${prayerLocation?.longitude}")
                val result = FloatArray(1)
                Location.distanceBetween(
                    prayerLocation?.latitude?:21.42,
                    prayerLocation?.longitude?: 39.82,
                    location.latitude,
                    location.longitude,
                    result
                )

                if (result[0] >= PRAYER_THRESHOLD) {
                    prayerTimeRepository.refreshPrayerTimes()
                }
                Unit
            }
    }

}