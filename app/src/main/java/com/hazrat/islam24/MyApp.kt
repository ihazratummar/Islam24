package com.hazrat.islam24

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.hazrat.islam24.di.initKoin
import timber.log.Timber

//MyApp.kt

class MyApp: Application() , ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        initKoin(this)
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
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