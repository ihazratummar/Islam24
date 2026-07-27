package com.hazrat.tasbih.di

import com.hazrat.tasbih.data.repository.TasbihRepositoryImpl
import com.hazrat.tasbih.domain.repository.TasbihRepository
import com.hazrat.tasbih.domain.usecase.AddCustomTasbihUseCase
import com.hazrat.tasbih.domain.usecase.GetTasbihListUseCase
import com.hazrat.tasbih.domain.usecase.GetTodayTotalDhikrUseCase
import com.hazrat.tasbih.domain.usecase.IncrementTasbihUseCase
import com.hazrat.tasbih.domain.usecase.ResetTasbihUseCase
import com.hazrat.tasbih.domain.usecase.ToggleFavoriteTasbihUseCase
import com.hazrat.tasbih.domain.usecase.UndoTasbihUseCase
import com.hazrat.tasbih.ui.TasbihViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val tasbihModule = module {
    single<TasbihRepository> { TasbihRepositoryImpl(get()) }

    factory { GetTasbihListUseCase(get()) }
    factory { GetTodayTotalDhikrUseCase(get()) }
    factory { IncrementTasbihUseCase(get()) }
    factory { UndoTasbihUseCase(get()) }
    factory { ResetTasbihUseCase(get()) }
    factory { ToggleFavoriteTasbihUseCase(get()) }
    factory { AddCustomTasbihUseCase(get()) }

    viewModel {
        TasbihViewModel(
            getTasbihListUseCase = get(),
            getTodayTotalDhikrUseCase = get(),
            incrementTasbihUseCase = get(),
            undoTasbihUseCase = get(),
            resetTasbihUseCase = get(),
            toggleFavoriteTasbihUseCase = get(),
            addCustomTasbihUseCase = get(),
            appDataStore = get()
        )
    }
}
