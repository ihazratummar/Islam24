package com.hazrat.islam24.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory

import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.service.UpdateManager
import com.hazrat.utils.ChangelogRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 22/01/26
 */


fun getAppModule(): Module = module {

    single { ChangelogRepository(androidApplication()) }

    viewModel {
        MainViewModel(
            locationRepository = get(),
            appDataStore = get(),
            changelogRepository = get()
        )
    }

    single<AppUpdateManager> { AppUpdateManagerFactory.create(get<Context>()) }
    single { UpdateManager(context = androidApplication(), appUpdateManager = get()) }



}