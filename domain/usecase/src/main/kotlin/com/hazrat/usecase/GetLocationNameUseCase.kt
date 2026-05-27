package com.hazrat.usecase

import android.location.Location
import com.hazrat.database.entity.LocationDetailsEntity
import com.hazrat.domain.repository.LocationNameRepository
import com.hazrat.domain.repository.PrayerTimeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest


/**
 * @author hazratummar
 * Created on 15/05/26
 */

class GetLocationNameUseCase(
    private val locationNameRepository: LocationNameRepository,
    private val prayerTimeRepository: PrayerTimeRepository
) {

    companion object {
        private const val PRAYER_THRESHOLD = 5000f
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<LocationDetailsEntity> {
        return locationNameRepository.observeLocationInfo()
            .mapLatest {location->
                val prayerLocation = prayerTimeRepository.getCurrentPrayerLocation()

                val result = FloatArray(1)

                Location.distanceBetween(
                    prayerLocation?.latitude ?: 21.42,
                    prayerLocation?.longitude ?: 39.82,
                    location.latitude,
                    location.longitude,
                    result
                )

                if (result[0] >= PRAYER_THRESHOLD) {

                    prayerTimeRepository
                        .refreshPrayerTimes()
                }

                location

            }
    }

}