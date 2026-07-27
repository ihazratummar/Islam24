package com.hazrat.zakat.di

import com.hazrat.zakat.data.repository.ZakatRepositoryImpl
import com.hazrat.zakat.domain.repository.ZakatRepository
import com.hazrat.zakat.domain.usecase.GetZakatDetailsUseCase
import com.hazrat.zakat.zakat_calculation.ZakatCalculationViewModel
import com.hazrat.zakat.zakat_list.ZakatListViewModel
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

    viewModel { ZakatListViewModel(zakatRepository = get(), zakatAlarmScheduler = get()) }
    viewModel { ZakatCalculationViewModel(zakatRepository = get(), zakatAlarmScheduler = get()) }
}