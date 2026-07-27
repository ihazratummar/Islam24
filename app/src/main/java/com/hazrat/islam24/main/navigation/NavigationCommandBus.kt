package com.hazrat.islam24.main.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton navigation command bus. Notification receivers and services set a
 * pending navigation target via [navigateTo] or [navigateToAyah], and
 * AppNavigator consumes it once via [consumePendingNavigation].
 *
 * This avoids all the pitfalls of URI-based deep links with Navigation Compose
 * (singleTask activity, synthetic back stacks, saveState/restoreState failures).
 *
 * @author hazratummar
 */

/**
 * Sealed interface representing all possible in-app navigation targets
 * that can be triggered from outside the Compose navigation graph
 * (notifications, services, broadcast receivers).
 */
sealed interface NavigationTarget {
    data class Ayah(val surahNumber: Int, val ayahNumber: Int) : NavigationTarget
    data object PrayerTime : NavigationTarget
    data object Zakat : NavigationTarget
}

object NavigationCommandBus {
    private val _pendingNavigation = MutableStateFlow<NavigationTarget?>(null)
    val pendingNavigation: StateFlow<NavigationTarget?> = _pendingNavigation.asStateFlow()

    /** Navigate to a specific Quran Ayah screen. */
    fun navigateToAyah(surahNumber: Int, ayahNumber: Int) {
        _pendingNavigation.value = NavigationTarget.Ayah(surahNumber, ayahNumber)
    }

    /** Navigate to any supported screen target. */
    fun navigateTo(target: NavigationTarget) {
        _pendingNavigation.value = target
    }

    /** Mark the pending navigation as consumed so it doesn't re-trigger. */
    fun consumePendingNavigation() {
        _pendingNavigation.value = null
    }
}
