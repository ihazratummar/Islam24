package com.hazrat.islam24.core.presentation.qibla

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.UserDataStore
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.core.data.entity.LocationDetailsEntity
import com.hazrat.islam24.core.domain.repository.QiblaRepository
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.core.domain.repository.location.LocationRepository
import com.hazrat.islam24.sensor.CompassSensorQualifier
import com.hazrat.islam24.sensor.MeasurableSensor
import com.hazrat.islam24.sensor.RotationSensorQualifier
import com.hazrat.islam24.service.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
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
    locationNameRepository: LocationNameRepository,
    private val locationRepository: LocationRepository,
    @RotationSensorQualifier rotationSensor: MeasurableSensor,
    @CompassSensorQualifier compassSensor: MeasurableSensor,
    private val profileRepository: ProfileRepository,
    private val userDataStore: UserDataStore,
    private val qiblaRepository: QiblaRepository
) : ViewModel() {


    private val _qiblaState = MutableStateFlow(QiblaState())
    val qiblaState = combine(
        _qiblaState,
        userDataStore.getSelectedCompassId
    ) { state, compassId ->
        state.copy(selectedCompassId = compassId)
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000L),
        initialValue = _qiblaState.value,
    )

    val authState: LiveData<AuthState> = profileRepository.authState

    val locationName: StateFlow<List<LocationDetailsEntity>> = locationNameRepository.locationName

    private var previousDirection: Float = 0f

    init {
        syncCompass()
        profileRepository.checkAuthStatus()

        locationManager.onLocationReceived = { location ->
            updateCurrentLatLng(latitude = location.latitude, longitude = location.longitude)
            val qiblaDirection =
                calculateQiblaDirection(location.latitude, location.longitude).toFloat()
            updateQiblaDirection(qiblaDirection)
        }



        compassSensor.startListening()
        compassSensor.setOnSensorValuesChangedLister { values ->
            if (values.isNotEmpty()) {
                val direction = values[0]
                updateCurrentDirection(direction)
            }
        }
        locationManager.getLastKnownLocation()


        rotationSensor.startListening()
        rotationSensor.setOnSensorValuesChangedLister { values ->
            if (values.size >= 3) {
                val pitchValue = Math.toDegrees(values[0].toDouble()).toFloat()  // Pitch
                val rollValue = Math.toDegrees(values[1].toDouble()).toFloat()   // Roll
                val threshold = 1f // Adjust this value based on your needs
                val pitchThresholded = if (abs(pitchValue) < threshold) 0f else pitchValue
                val rollThresholded = if (abs(rollValue) < threshold) 0f else rollValue

                _qiblaState.update { it.copy(pitch = pitchThresholded, roll = rollThresholded) }
            }
        }
        viewModelScope.launch {
            isFacingQibla().collect { isFacing ->
                _qiblaState.update { it.copy(isFacingQibla = isFacing) }
            }
        }

        viewModelScope.launch {
            calculateTiltDifference().collect { difference ->
                _qiblaState.update { it.copy(qiblaDegreeDifference = difference) }
            }
        }
    }


    fun onEvent(event: QiblaEvent) {
        when (event) {
            is QiblaEvent.OnCompassClick -> {

                viewModelScope.launch {
                    userDataStore.saveSelectedCompassId(id = event.compassId)
                    qiblaRepository.syncCompassDataIfLoggedIn()
                }
                _qiblaState.update { it.copy(selectedCompassId = event.compassId) }
            }

            QiblaEvent.OnLoggedInRequiredCompassClick -> {
                _qiblaState.update { it.copy(isLoggedInRequiredPopupVisible = !it.isLoggedInRequiredPopupVisible) }
            }
        }
    }

    fun updateCurrentLatLng(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _qiblaState.update { it.copy(latitude = latitude, longitude = longitude) }
            Log.d(
                "QiblaViewModel",
                "updateCurrentLatLng: ${_qiblaState.value.latitude}, ${_qiblaState.value.longitude}"
            )
        }
    }

    private fun updateQiblaDirection(newDirection: Float) {
        _qiblaState.update {
            it.copy(
                qiblaDirection = newDirection
            )
        }
    }

    private fun updateCurrentDirection(newDirection: Float) {
        val alpha = 0.1f

        val smoothDirection = previousDirection + alpha * (newDirection - previousDirection)
        previousDirection = smoothDirection

        _qiblaState.update {
            it.copy(
                currentDirection = smoothDirection
            )
        }
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


    fun isFacingQibla(): Flow<Boolean> = flow {
        while (true) {
            val minTolerance = 8f // Adjusted tolerance range
            val maxTolerance = 8f // Adjusted tolerance range

            val directionDifference =
                _qiblaState.value.qiblaDirection - _qiblaState.value.currentDirection
            val normalizedDifference = (directionDifference + 360) % 360

            val isFacing = (normalizedDifference in 0.0..maxTolerance.toDouble()) ||
                    (normalizedDifference >= 360 - minTolerance && normalizedDifference <= 360)
            emit(isFacing)
            delay(100)
        }
    }

    fun calculateTiltDifference(): Flow<Float> = flow {
        while (true) {
            val qiblaDirection = _qiblaState.value.qiblaDirection
            val currentDirection = _qiblaState.value.currentDirection
            val difference = ((qiblaDirection - currentDirection + 540) % 360) - 180
            emit(difference)
            delay(300)
        }
    }
     private fun syncCompass(){
         viewModelScope.launch{
             qiblaRepository.syncCompassDataIfLoggedIn()
         }
     }
}
