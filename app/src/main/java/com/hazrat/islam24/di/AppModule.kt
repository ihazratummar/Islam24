package com.hazrat.islam24.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.hazrat.islam24.auth.presentation.appSetting.AppSettingViewModel
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileViewModel
import com.hazrat.islam24.auth.presentation.profiledetails.ProfileDetailsViewModel
import com.hazrat.islam24.main.mainActivity.MainViewModel
import com.hazrat.islam24.service.UpdateManager
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 22/01/26
 */


fun getAppModule(): Module = module {

    viewModel {
        MainViewModel(
            profileRepository = get(),
            locationRepository = get(),
            appDataStore = get()
        )
    }

    single<AppUpdateManager> { AppUpdateManagerFactory.create(get<Context>()) }
    single { UpdateManager(context = androidApplication(), appUpdateManager = get()) }

    viewModel {
        AppSettingViewModel(
            context = get(),
            profileRepository = get(),
            dataStorePreference = get(),
            quranRepository = get(),
            appDataStore = get(),
            userDataStore = get()
        )
    }

    viewModel {ProfileDetailsViewModel(profileRepository = get()) }
    viewModel { ProfileViewModel(profileRepository = get()) }

}