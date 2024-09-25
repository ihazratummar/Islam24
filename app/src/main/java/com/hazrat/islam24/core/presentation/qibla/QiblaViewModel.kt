package com.hazrat.islam24.core.presentation.qibla

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.service.CompassSensorManager
import com.hazrat.islam24.service.LocationManager
import com.hazrat.islam24.util.vibrateDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class QiblaViewModel @Inject constructor(
    @ApplicationContext context: Context,
    locationManager: LocationManager,
    compassSensorManager: CompassSensorManager,
    locationNameRepository: LocationNameRepository
) : ViewModel() {


    private val _qiblaState = MutableStateFlow(QiblaState())
    val qiblaState = _qiblaState.asStateFlow()

    val locationName: StateFlow<List<LocationDetailsEntity>> = locationNameRepository.locationName

    init {
        locationManager.onLocationReceived = { location ->
            Log.d(
                "LocationManager",
                "Location received: ${location.latitude}, ${location.longitude}"
            )
            val qiblaDirection =
                calculateQiblaDirection(location.latitude, location.longitude).toFloat()
            updateQiblaDirection(qiblaDirection)
//            viewModelScope.launch {
//                locationNameRepository.locationName()
//            }
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
                sin(Math.toRadians(latitude)) * cos(Math.toRadians(kaabaLatitude)) * cos(
            lonDifference
        )
        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }


    fun isFacingQibla(): Boolean {
        val minTolerance = 2.9f // Adjusted tolerance range
        val maxTolerance = 3.8f // Adjusted tolerance range

        val directionDifference =
            _qiblaState.value.qiblaDirection - _qiblaState.value.currentDirection
        val normalizedDifference = (directionDifference + 360) % 360
        return (normalizedDifference in 0.0..maxTolerance.toDouble()) ||
                (normalizedDifference >= 360 - minTolerance && normalizedDifference <= 360)

    }


    // Vibrate when facing Qibla and not already vibrated

}