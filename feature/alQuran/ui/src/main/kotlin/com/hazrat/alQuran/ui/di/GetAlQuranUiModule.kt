package com.hazrat.alQuran.ui.di

import com.hazrat.alQuran.ui.ayah.AyahViewModel
import com.hazrat.alQuran.ui.surah.SurahViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun getAlQuranUiModule(): Module = module {
    viewModel { SurahViewModel(quranRepository = get()) }
    viewModel { param ->
        AyahViewModel(
            surahNumber = param.get(),
            quranRepository = get()
        )
    }
}