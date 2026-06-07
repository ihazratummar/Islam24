package com.hazrat.auth.ui.di

import com.hazrat.auth.ui.appSetting.AppSettingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 24/01/26
 */

fun getAuthUiModule(): Module = module {
    viewModel {
        AppSettingViewModel(
            context = get(),
            dataStorePreference = get(),
            appDataStore = get(),
            userDataStore = get()
        )
    }

}