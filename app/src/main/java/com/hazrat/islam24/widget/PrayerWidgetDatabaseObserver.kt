package com.hazrat.islam24.widget

import com.hazrat.database.dao.prayer.PrayerLogDao
import com.hazrat.domain.repository.WidgetUpdater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

/**
 * Enterprise-grade reactive observer that listens directly to Room database changes on `prayer_logs`.
 * Whenever any prayer is logged or unlogged anywhere in the app, Room automatically emits on `observeAllLogs()`,
 * and this observer immediately refreshes the Glance widgets in real time without needing button callbacks.
 *
 * @author hazratummar
 */
class PrayerWidgetDatabaseObserver(
    private val prayerLogDao: PrayerLogDao,
    private val widgetUpdater: WidgetUpdater,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) {
    fun startObserving() {
        scope.launch {
            prayerLogDao.observeAllLogs()
                .distinctUntilChanged()
                .drop(1) // Drop initial emission so we only react to active changes
                .collect {
                    widgetUpdater.updateAllPrayerWidgets()
                }
        }
    }
}
