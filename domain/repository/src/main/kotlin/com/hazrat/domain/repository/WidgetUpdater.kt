package com.hazrat.domain.repository

/**
 * Abstraction for triggering widget updates from feature modules
 * that cannot directly reference Glance widget classes in the app module.
 * @author hazratummar
 */
interface WidgetUpdater {
    suspend fun updateAllPrayerWidgets()
}
