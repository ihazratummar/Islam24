package com.hazrat.hajjlive.ui.di

import com.hazrat.domain.repository.HajjLiveRepository
import com.hazrat.hajjlive.ui.HajjLiveViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 27/01/26
 */
fun getHajjliveUiModule() : Module = module {
    viewModel { HajjLiveViewModel(hajjLiveRepository = get()) }
}
 