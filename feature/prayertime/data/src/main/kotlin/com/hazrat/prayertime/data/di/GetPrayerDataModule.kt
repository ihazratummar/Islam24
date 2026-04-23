package com.hazrat.prayertime.data.di

import com.hazrat.domain.repository.PrayerSettingRepository
import com.hazrat.domain.repository.PrayerTimeRepository
import com.hazrat.prayertime.data.repository.PrayerSettingRepositoryImpl
import com.hazrat.prayertime.data.repository.PrayerTimeRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun getPrayerDataModule(): Module = module {
    single<PrayerTimeRepository> {
        PrayerTimeRepositoryImpl(
            api = get(),
            locationRepository = get(),
            prayerSettingRepository = get(),
            prayerTimeDao = get(),
            context = get(),
            connectivityObserver = get()
        )
    }

    single <PrayerSettingRepository>{ PrayerSettingRepositoryImpl(prayerSettingDao = get()) }
}