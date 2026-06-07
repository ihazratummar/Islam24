package com.hazrat.athkar.ui.di

import com.hazrat.athkar.ui.azkar.AthkarViewModel
import com.hazrat.athkar.ui.dua.category.DuaViewModel
import com.hazrat.athkar.ui.dua.dua_details.DuaItemViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 25/01/26
 */

fun getAthkarUiModule(): Module = module {
    viewModel { AthkarViewModel(athkarRepository = get(), connectivityObserver = get()) }
    viewModelOf(::DuaViewModel)

    viewModel { params ->
        DuaItemViewModel(
            duaCategoryId = params.get(),
            getDuaItemListUseCase = get(),
        )
    }
}