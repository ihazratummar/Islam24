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
    val priority: Int =  Priority.PRIORITY_HIGH_ACCURACY,
    val timeout: Long = 10_000L,
    val maxWaitTimeMs: Long = 5_000L,
    val minUpdateDistanceMeters: Float = 2000f // 2km
)

