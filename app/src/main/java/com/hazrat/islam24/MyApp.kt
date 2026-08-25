package com.hazrat.islam24

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.hazrat.domain.repository.AppSyncScheduler
import com.hazrat.islam24.di.initKoin
import com.hazrat.islam24.sync.SyncScheduler
import com.hazrat.notification.PrayerJanitorWorker
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import timber.log.Timber
import java.util.concurrent.TimeUnit

//MyApp.kt

class MyApp: Application() , ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        initKoin(this)
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        schedulePrayerJanitor()
        val appSync: AppSyncScheduler by inject()
        appSync.scheduleImmediateSync()
        appSync.schedulePeriodicSync()

        val widgetObserver: com.hazrat.islam24.widget.PrayerWidgetDatabaseObserver by inject()
        widgetObserver.startObserving()
    }

    private fun schedulePrayerJanitor() {
        val workRequest = PeriodicWorkRequestBuilder<PrayerJanitorWorker>(12, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "PrayerJanitorWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.03)
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }
}