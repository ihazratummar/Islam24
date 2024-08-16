package com.hazrat.islam24.core.presentation.qibla

/**
 * @author Hazrat Ummar Shaikh
 */

data class QiblaState (
    val qiblaDirection: Float = 0f,
    val currentDirection: Float = 0f,
    var hasVibrated : Boolean = false
)