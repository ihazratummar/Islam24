package com.hazrat.islam24.core.presentation.qibla

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hazrat.islam24.service.CompassSensorManager
import com.hazrat.islam24.service.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class QiblaViewModel @Inject constructor(
    locationManager: LocationManager,
    compassSensorManager: CompassSensorManager
): ViewModel() {


    private val _qiblaState = MutableStateFlow(QiblaState())
    val qiblaState = _qiblaState.asStateFlow()

    init {
        locationManager.onLocationReceived = { location ->
            Log.d("LocationManager", "Location received: ${location.latitude}, ${location.longitude}")
            val qiblaDirection = calculateQiblaDirection(location.latitude, location.longitude).toFloat()
            updateQiblaDirection(qiblaDirection)
        }

        // Handle compass direction changes
        compassSensorManager.onDirectionChanged = { direction ->
            Log.d("CompassSensorManager", "Compass direction changed: $direction")
            updateCurrentDirection(direction)
        }

        compassSensorManager.registerListeners()
        locationManager.getLastKnownLocation()
    }

    private fun updateQiblaDirection(newDirection: Float) {
        Log.d("QiblaViewModel", "Updating Qibla Direction to $newDirection")
        _qiblaState.update {
            it.copy(
                qiblaDirection = newDirection
            )
        }
    }

    private fun updateCurrentDirection(newDirection: Float) {
        _qiblaState.update {
            it.copy(
                currentDirection = newDirection
            )
        }
        Log.d("ViewModel direction", "Updating currentDirection to $newDirection")
    }

    private fun calculateQiblaDirection(latitude: Double, longitude: Double): Double {
        val kaabaLatitude = 21.4225
        val kaabaLongitude = 39.8262

        val latDifference = Math.toRadians(kaabaLatitude - latitude)
        val lonDifference = Math.toRadians(kaabaLongitude - longitude)
        val y = sin(lonDifference) * cos(Math.toRadians(kaabaLatitude))
        val x = cos(Math.toRadians(latitude)) * sin(Math.toRadians(kaabaLatitude)) -
                sin(Math.toRadians(latitude)) * cos(Math.toRadians(kaabaLatitude)) * cos(lonDifference)
        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }

}