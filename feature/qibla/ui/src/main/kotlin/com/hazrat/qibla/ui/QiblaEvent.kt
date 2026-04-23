package com.hazrat.qibla.ui

/**
 * @author Hazrat Ummar Shaikh
 * Created on 27-01-2025
 */

sealed interface QiblaEvent {

    data class OnCompassClick(val compassId: Int) : QiblaEvent

    data object OnLoggedInRequiredCompassClick : QiblaEvent

}