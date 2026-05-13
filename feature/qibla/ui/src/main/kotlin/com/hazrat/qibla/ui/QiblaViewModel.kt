package com.hazrat.qibla.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hazrat.auth.domain.usecase.ObserveAuthStateUseCase
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.QiblaRepository
import com.hazrat.location.model.LocationError
import com.hazrat.location.model.LocationResult
import com.hazrat.location.repository.LocationRepository
import com.hazrat.model.AuthState
import com.hazrat.sensor.MeasurableSensor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Hazrat Ummar Shaikh
 */

class QiblaViewModel (
    rotationSensor: MeasurableSensor,
    compassSensor: MeasurableSensor,
    private val userDataStore: UserDataStore,
    private val qiblaRepository: QiblaRepository,
    private val locationRepository: LocationRepository,
    observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {


    private val _qiblaState = MutableStateFlow(QiblaState())
    val qiblaState = combine(
        _qiblaState,
        userDataStore.getSelectedCompassId
    ) { state, compassId ->
        state.copy(selectedCompassId = compassId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = _qiblaState.value,
    )

    val authState: LiveData<AuthState> = observeAuthStateUseCase().asLiveData()


    private var previousDirection: Float = 0f

    init {
        syncCompass()
        observerLocation()

        compassSensor.startListening()
        compassSensor.setOnSensorValuesChangedLister { values ->
            if (values.isNotEmpty()) {
                val direction = values[0]
                updateCurrentDirection(direction)
            }
        }


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

    fun observerLocation() {
        viewModelScope.launch {
            locationRepository.observeLocationUpdates().collect {locationResult ->
                when(locationResult){
                    is LocationResult.Error -> {
                        val errorMessage = when(locationResult.error){
                            LocationError.LocationDisabled -> "Please enable location service"
                            LocationError.LocationUnavailable -> "Unable to get your location. Please try again"
                            LocationError.PermissionDenied -> "Please grant location permission to use this feature"
                            LocationError.PermissionDeniedPermanently -> "Please enable location permission in settings."
                            is LocationError.Unknown -> "An error occurred: ${(locationResult.error as LocationError.Unknown).throwable.message}"
                        }
                        _qiblaState.update { it.copy(isLocationEnabled = false) }
                    }
                    is LocationResult.Success -> {
                        _qiblaState.update { it.copy(isLocationEnabled = true) }
                        updateCurrentLatLng(latitude = locationResult.location.latitude, longitude = locationResult.location.longitude)
                        val qiblaDirection = calculateQiblaDirection(latitude = locationResult.location.latitude, longitude = locationResult.location.longitude)
                        Log.d("QiblaViewModel", "observerLocation: $qiblaDirection")
                        updateQiblaDirection(qiblaDirection.toFloat())
                    }
                }
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
