package com.hazrat.prayertime.data.di

import com.hazrat.domain.repository.prayer.PrayerLogRepository
import com.hazrat.domain.repository.prayer.PrayerSettingRepository
import com.hazrat.domain.repository.prayer.PrayerTimeRepository
import com.hazrat.prayertime.data.mapper.PrayerLogMapper
import com.hazrat.prayertime.data.repository.DefaultDispatcherProvider
import com.hazrat.prayertime.data.repository.DispatcherProvider
import com.hazrat.prayertime.data.repository.PrayerLogsRepositoryImpl
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
            prayerTimeDao = get(),
            context = get(),
            dispatchers = get(),
            connectivityObserver = get(),
            userDataStore = get(),
            prayerSettingDao = get()
        )
    }

    single<DispatcherProvider> { DefaultDispatcherProvider() }

    single<PrayerSettingRepository> { PrayerSettingRepositoryImpl(userDataStore = get(), prayerSettingDao = get()) }

    single { PrayerLogMapper }

    single <PrayerLogRepository>{
        PrayerLogsRepositoryImpl(
            prayerLogMapper = get(),
            prayerLogDao = get()
        )
    }
}