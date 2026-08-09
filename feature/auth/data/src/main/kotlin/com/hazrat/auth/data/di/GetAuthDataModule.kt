package com.hazrat.auth.data.di

import androidx.credentials.CredentialManager
import com.hazrat.auth.data.billing.BillingRepositoryImpl
import com.hazrat.auth.data.billing.RevenueCatBillingDataSource
import com.hazrat.auth.data.repository.AuthRepositoryImpl
import com.hazrat.auth.data.repository.ProfileRepositoryImpl
import com.hazrat.domain.repository.AuthRepository
import com.hazrat.domain.repository.BillingRepository
import com.hazrat.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author hazratummar
 * Created on 24/01/26
 */

fun getAuthDataModule(): Module = module {

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    // RevenueCat Data Source
    single {
        RevenueCatBillingDataSource(context = androidContext())
    }

    // Billing Repository
    single<BillingRepository> {
        BillingRepositoryImpl(
            revenueCatBillingDataSource = get(),
            userDataStore = get()
        )
    }

    single { CredentialManager.create(androidContext()) }
    single<AuthRepository> {
        AuthRepositoryImpl(
            credentialManager = get(),
            authApiCall = get(),
            tokenStorage = get(),
            profileApi = get(),
            profileRepository = get()
        )
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            dao = get(),
            webSocketApi = get(),
            profileApi = get(),
            userSupportStatusDao = get(),
            supporterTickerDao = get()
        )
    }
}