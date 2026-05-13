package com.hazrat.islam24.di

import android.app.Application
import com.hazrat.alQuran.data.di.getAlQuranDataModule
import com.hazrat.alQuran.ui.di.getAlQuranUiModule
import com.hazrat.allahNames.data.di.getAllahNamesDataModule
import com.hazrat.allahNames.ui.di.getAllahNamesUiModule
import com.hazrat.athkar.di.getAthkarDataModule
import com.hazrat.athkar.ui.di.getAthkarUiModule
import com.hazrat.auth.data.di.getAuthDataModule
import com.hazrat.auth.domain.di.getAuthDomainModule
import com.hazrat.auth.ui.di.getAuthUiModule
import com.hazrat.database.getDatabaseModule
import com.hazrat.datastore.di.getDatastoreModule
import com.hazrat.downloader.di.getDownloadModule
import com.hazrat.home.ui.di.getHomeUiModule
import com.hazrat.location.di.getLocationModule
import com.hazrat.notification.di.getNotificationModule
import com.hazrat.prayer.ui.di.getPrayerUiModule
import com.hazrat.prayertime.data.di.getPrayerDataModule
import com.hazrat.qibla.data.di.getQiblaDataModule
import com.hazrat.qibla.ui.di.getQiblaUiModule
import com.hazrat.remote.di.getRemoteModule
import com.hazrat.sensor.geSensorModule
import com.hazrat.utils.di.getUtilsModule
import com.hazrat.zakat.di.getZakatModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


fun initKoin(app: Application) {
    startKoin {
        androidContext(app)
        modules(
            getAppModule(),
            getUtilsModule(),
            getDatastoreModule(),
            getDatabaseModule(),
            getDownloadModule(),
            getZakatModule(),
            getRemoteModule(),
            geSensorModule(),
            getLocationModule(),
            getQiblaUiModule(),
            getAuthUiModule(),
            getAuthDataModule(),
            getAuthDomainModule(),
            getNotificationModule(),
            getAllahNamesUiModule(),
            getAllahNamesDataModule(),
            getAthkarUiModule(),
            getAthkarDataModule(),
            getAlQuranUiModule(),
            getAlQuranDataModule(),
            getQiblaDataModule(),
            getPrayerUiModule(),
            getPrayerDataModule(),
            getHomeUiModule()
        )
    }
}