package com.hazrat.allahNames.ui.di

import com.hazrat.allahNames.ui.namesofallah.NamesViewmodel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 25/01/26
 */

fun getAllahNamesUiModule() : Module = module {
    viewModel { NamesViewmodel(namesRepository = get(), connectivityObserver = get()) }
}