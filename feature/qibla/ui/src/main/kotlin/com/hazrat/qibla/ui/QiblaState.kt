package com.hazrat.qibla.ui

/**
 * @author Hazrat Ummar Shaikh
 */

data class QiblaState (
    val qiblaDirection: Float = 0f,
    val currentDirection: Float = 0f,
    var hasVibrated : Boolean = false,
    val isFacingQibla: Boolean = false,
    val qiblaDegreeDifference: Float = 0f,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val pitch : Float = 0f,
    val roll : Float = 0f,
    val isLocationEnabled: Boolean = false,
    val isLoggedInRequiredPopupVisible: Boolean = false,
    val selectedCompassId: Int = 1,
)