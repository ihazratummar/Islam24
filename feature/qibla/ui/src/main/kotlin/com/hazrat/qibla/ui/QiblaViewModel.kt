package com.hazrat.qibla.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.datastore.UserDataStore
import com.hazrat.domain.repository.QiblaRepository
import com.hazrat.location.model.LocationConfigs
import com.hazrat.location.model.LocationError
import com.hazrat.location.model.LocationResult
import com.hazrat.location.repository.LocationRepository
import com.hazrat.sensor.MeasurableSensor
import com.hazrat.usecase.GetLocationNameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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
class QiblaViewModel(
    private val rotationSensor: MeasurableSensor,
    private val compassSensor: MeasurableSensor,
    private val userDataStore: UserDataStore,
    private val qiblaRepository: QiblaRepository,
    private val locationRepository: LocationRepository,
    private val getLocationNameUseCase: GetLocationNameUseCase? = null,
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

    private var previousDirection: Float = 0f
    private var locationJob: Job? = null
    private var isSensorsAndLocationActive: Boolean = false

    init {
        preloadLastKnownLocation()
        syncCompass()
        observeLocationProviderStatus()
        observeLocationName()
    }

    fun startSensorsAndLocation() {
        isSensorsAndLocationActive = true

        compassSensor.startListening()
        compassSensor.setOnSensorValuesChangedLister { values ->
            if (values.isNotEmpty()) {
                val direction = values[0]
                updateCurrentDirection(direction)
            }
        }

        compassSensor.setOnAccuracyChangedListener { accuracy ->
            _qiblaState.update { it.copy(sensorAccuracy = accuracy) }
        }

        rotationSensor.startListening()
        rotationSensor.setOnSensorValuesChangedLister { values ->
            if (values.size >= 3) {
                val pitchValue = Math.toDegrees(values[0].toDouble()).toFloat()
                val rollValue = Math.toDegrees(values[1].toDouble()).toFloat()
                val threshold = 1f
                val pitchThresholded = if (abs(pitchValue) < threshold) 0f else pitchValue
                val rollThresholded = if (abs(rollValue) < threshold) 0f else rollValue

                _qiblaState.update { it.copy(pitch = pitchThresholded, roll = rollThresholded) }
            }
        }

        observerLocation()
    }

    fun stopSensorsAndLocation() {
        isSensorsAndLocationActive = false
        compassSensor.stopListening()
        rotationSensor.stopListening()
        locationJob?.cancel()
        locationJob = null
    }

    private fun preloadLastKnownLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Check DataStore cached location
            val savedLocation = userDataStore.getLastKnownLocationSync()
            if (savedLocation != null) {
                val (lat, lng) = savedLocation
                val qiblaDir = calculateQiblaDirection(lat, lng)
                _qiblaState.update {
                    it.copy(
                        latitude = lat,
                        longitude = lng,
                        qiblaDirection = qiblaDir.toFloat(),
                        isQiblaCalculated = true
                    )
                }
            }

            // 2. Try fast device FusedLocation cache
            val lastLocationResult = locationRepository.getLastKnownLocation()
            if (lastLocationResult is LocationResult.Success) {
                val lat = lastLocationResult.location.latitude
                val lng = lastLocationResult.location.longitude
                val qiblaDir = calculateQiblaDirection(lat, lng)
                userDataStore.saveLastKnownLocation(lat, lng)
                _qiblaState.update {
                    it.copy(
                        latitude = lat,
                        longitude = lng,
                        qiblaDirection = qiblaDir.toFloat(),
                        isQiblaCalculated = true
                    )
                }
            }
        }
    }

    private fun observeLocationProviderStatus() {
        viewModelScope.launch {
            locationRepository.observeLocationProviderStatus().collectLatest { isEnabled ->
                _qiblaState.update { it.copy(isLocationEnabled = isEnabled) }
                if (isEnabled && isSensorsAndLocationActive) {
                    observerLocation()
                } else {
                    locationJob?.cancel()
                    locationJob = null
                }
            }
        }
    }

    private fun observeLocationName() {
        viewModelScope.launch {
            getLocationNameUseCase?.invoke()?.collectLatest { location ->
                _qiblaState.update { it.copy(locationName = location.locationName) }
            }
        }
    }

    fun observerLocation() {
        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            locationRepository.observeLocationUpdates(locationConfig = LocationConfigs.Qibla)
                .collectLatest { locationResult ->
                    when (locationResult) {
                        is LocationResult.Error -> {
                            _qiblaState.update { it.copy(isLocationEnabled = false) }
                        }

                        is LocationResult.Success -> {
                            val lat = locationResult.location.latitude
                            val lng = locationResult.location.longitude
                            viewModelScope.launch(Dispatchers.IO) {
                                userDataStore.saveLastKnownLocation(lat, lng)
                            }
                            _qiblaState.update { it.copy(isLocationEnabled = true) }
                            updateCurrentLatLng(latitude = lat, longitude = lng)
                            val qiblaDirection = calculateQiblaDirection(latitude = lat, longitude = lng)
                            updateQiblaDirection(qiblaDirection.toFloat())
                        }
                    }
                }
        }
    }

    fun onEvent(event: QiblaEvent) {
        when (event) {
            QiblaEvent.StartSensorsAndLocation -> {
                startSensorsAndLocation()
            }

            QiblaEvent.StopSensorsAndLocation -> {
                stopSensorsAndLocation()
            }

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

            is QiblaEvent.ToggleCalibrationDialog -> {
                _qiblaState.update { it.copy(isCalibrationDialogVisible = event.isVisible) }
            }

            is QiblaEvent.OnLocationStatusChanged -> {
                _qiblaState.update { it.copy(isLocationEnabled = event.isEnabled) }
                if (event.isEnabled) {
                    observerLocation()
                } else {
                    locationJob?.cancel()
                    locationJob = null
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopSensorsAndLocation()
    }

    fun updateCurrentLatLng(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _qiblaState.update { it.copy(latitude = latitude, longitude = longitude) }
        }
    }

    private fun updateQiblaDirection(newDirection: Float) {
        _qiblaState.update { state ->
            val diff = ((newDirection - state.currentDirection + 540) % 360) - 180
            val normDiff = (newDirection - state.currentDirection + 360) % 360
            val isFacing = (normDiff in 0.0..8.0) || (normDiff >= 352.0 && normDiff <= 360.0)
            state.copy(
                qiblaDirection = newDirection,
                qiblaDegreeDifference = diff,
                isFacingQibla = isFacing,
                isQiblaCalculated = true
            )
        }
    }

    private fun updateCurrentDirection(newDirection: Float) {
        val alpha = 0.25f
        val smoothDirection = previousDirection + alpha * (newDirection - previousDirection)
        previousDirection = smoothDirection

        val state = _qiblaState.value
        val isCalculated = state.isQiblaCalculated
        val qiblaDir = state.qiblaDirection
        val diff = if (isCalculated) ((qiblaDir - smoothDirection + 540) % 360) - 180 else 0f
        val normDiff = (qiblaDir - smoothDirection + 360) % 360
        val isFacing = isCalculated && ((normDiff in 0.0..8.0) || (normDiff >= 352.0 && normDiff <= 360.0))

        _qiblaState.update {
            it.copy(
                currentDirection = smoothDirection,
                qiblaDegreeDifference = diff,
                isFacingQibla = isFacing
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

    private fun syncCompass() {
        viewModelScope.launch {
            qiblaRepository.syncCompassDataIfLoggedIn()
        }
    }
}
