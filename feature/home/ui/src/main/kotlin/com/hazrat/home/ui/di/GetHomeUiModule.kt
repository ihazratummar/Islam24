package com.hazrat.home.ui.di

import com.hazrat.home.ui.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun getHomeUiModule(): Module = module {
    viewModelOf(::HomeViewModel)
}