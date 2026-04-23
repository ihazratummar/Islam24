package com.hazrat.athkar.ui.di

import com.hazrat.athkar.ui.AthkarViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 25/01/26
 */

fun getAthkarUiModule(): Module = module {
    viewModel { AthkarViewModel(athkarRepository = get(), connectivityObserver = get()) }
}