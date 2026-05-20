package com.hazrat.location.model

import android.location.Location
import com.google.android.gms.location.Priority


/**
 * @author hazratummar
 * Created on 23/01/26
 */

sealed class LocationResult {
    data class Success(val location: Location) : LocationResult()
    data class  Error(val error: LocationError) : LocationResult()
}

sealed class LocationError {
    data object PermissionDenied : LocationError()
    data object PermissionDeniedPermanently : LocationError()
    data object LocationDisabled : LocationError()
    data object LocationUnavailable: LocationError()
    data class Unknown(val throwable: Throwable) : LocationError()
}

data class LocationConfig(
    val priority: Int =  Priority.PRIORITY_BALANCED_POWER_ACCURACY,
    val intervalMillis: Long = 5 * 60 * 1000L,
    val maxWaitTimeMs: Long = 5_000L,
    val minUpdateIntervalMillis: Long = 2 * 60 * 1000L,
    val minUpdateDistanceMeters: Float = 2000f // 2km
)

object LocationConfigs {
    val Default = LocationConfig(
        priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY,
        intervalMillis = 5 * 60 * 1000L,
        minUpdateIntervalMillis = 2 * 60 * 1000L,
        minUpdateDistanceMeters = 2000f
    )

    val Qibla = LocationConfig(
        priority = Priority.PRIORITY_HIGH_ACCURACY,
        intervalMillis = 1000L,
        minUpdateIntervalMillis = 1000L,
        minUpdateDistanceMeters = 0f
    )
}

