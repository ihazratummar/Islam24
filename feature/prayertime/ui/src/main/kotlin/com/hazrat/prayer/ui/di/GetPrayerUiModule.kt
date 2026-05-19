package com.hazrat.prayer.ui.di

import com.hazrat.prayer.ui.prayertime.PrayerTimeViewModel
import com.hazrat.prayer.ui.setting.PrayerSettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun getPrayerUiModule(): Module = module {

    viewModel {
        PrayerTimeViewModel(
            context = androidContext(),
            repository = get(),
            prayerAlarmManager = get(),
            dataStorePreference = get(),
            mediaPlayerHelper = get(),
            dataStore = get(),
            connectivityObserver = get(),
            downloader = get(),
            getTodayPrayerTimeUseCase = get(),
            getLocationNameUseCase = get(),
            getDailyPrayerStatus = get(),
            togglePrayerUseCase = get(),
            clock = get(),
        )
    }

    viewModel {
        PrayerSettingViewModel(
            context = androidContext(),
            prayerTimeRepository = get(),
            prayerAlarmManager = get(),
            dataStorePreference = get(),
            connectivityObserver = get(),
            userDataStore = get()

        )
    }
}