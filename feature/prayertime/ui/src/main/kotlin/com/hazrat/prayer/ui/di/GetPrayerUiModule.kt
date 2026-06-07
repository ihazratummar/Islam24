package com.hazrat.prayer.ui.di

import com.hazrat.prayer.ui.prayertime.PrayerTimeViewModel
import com.hazrat.prayer.ui.setting.PrayerSettingViewModel
import org.koin.android.ext.koin.androidApplication
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
            application = androidApplication(),
            repository = get(),
            prayerAlarmManager = get(),
            getLocationNameUseCase = get(),
            getDailyPrayerStatusUseCase = get(),
            togglePrayerUseCase = get(),
            clock = get(),
            prayerNotificationEnabledUseCase = get(),
            getPrayerNotificationStateUseCase = get(),
            getPrayerTimeWindowForDaysUseCase = get(),
        )
    }

    viewModel {
        PrayerSettingViewModel(
            application = androidApplication(),
            prayerTimeRepository = get(),
            connectivityObserver = get(),
            userDataStore = get()
        )
    }
}
