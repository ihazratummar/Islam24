package com.hazrat.islam24.core.presentation.qibla

/**
 * @author Hazrat Ummar Shaikh
 * Created on 27-01-2025
 */

sealed interface QiblaEvent {

    data class OnCompassClick(val compassId: Int) : QiblaEvent

    data object OnLoggedInRequiredCompassClick : QiblaEvent

}