package com.hazrat.auth.data.di

import com.hazrat.auth.data.billing.BillingRepositoryImpl
import com.hazrat.auth.data.billing.RevenueCatBillingDataSource
import com.hazrat.domain.repository.BillingRepository
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
}