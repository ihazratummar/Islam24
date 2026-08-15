package com.hazrat.islam24.core.data.di

import com.hazrat.domain.repository.SyncRepository
import com.hazrat.islam24.core.data.repository.SyncRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 14/08/26
 */
 


fun getDataModule() : Module = module {
    single <SyncRepository>{ SyncRepositoryImpl(
        syncApi = get(),
        prayerLogDao = get(),
        syncDatastore = get(),
        prayerSettingDao = get(),
        tokenStorage = get()
    ) }
}