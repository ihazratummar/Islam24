package com.hazrat.zakat.di

import com.hazrat.zakat.data.repository.ZakatRepositoryImpl
import com.hazrat.zakat.domain.repository.ZakatRepository
import com.hazrat.zakat.domain.usecase.GetZakatDetailsUseCase
import com.hazrat.zakat.screen.zakat.ZakatViewModel
import com.hazrat.zakat.screen.zakat.screen.zakat_screen.ZakatScreenViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 22/01/26
 */

fun getZakatModule(): Module = module {

    single<ZakatRepository> {
        ZakatRepositoryImpl(
            dao = get(),
            auth = get(),
            fireStore = get(),
            context = get()
        )
    }

    single { GetZakatDetailsUseCase(zakatRepository = get()) }

    viewModel { ZakatViewModel(repository = get())}
    viewModel { ZakatScreenViewModel(repository = get(), getZakatDetailsUseCase = get(), dataStorePreference = get()) }

}