package com.hazrat.auth.ui.di

import com.hazrat.auth.ui.appSetting.ProfileViewModel
import com.hazrat.auth.ui.login.LoginViewModel
import com.hazrat.auth.ui.support.SupportViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * @author hazratummar
 * Created on 24/01/26
 */
fun getAuthUiModule(): Module = module {
    viewModel {
        ProfileViewModel(
            context = get(),
            appDataStore = get(),
            userDataStore = get(),
            prayerLogRepository = getOrNull(),
            quranRepository = getOrNull(),
            logoutUseCase = get(),
            getProfileDataUseCase = get(),
            isLoggedInUseCase = get()
        )
    }

    viewModel {
        SupportViewModel(
            billingRepository = getOrNull(),
            connectivityObserver = getOrNull(),
            listenToSupportTickerUseCase = get(),
            getRecentTickersUseCase = get(),
            getSupporterStatusUseCase = get(),
            syncSupporterStatusUseCase = getOrNull(),
            saveTickerUseCase = getOrNull()
        )
    }

    viewModel {
        LoginViewModel(
            googleSignInUseCase = get(),

        )
    }
}