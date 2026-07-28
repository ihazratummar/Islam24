package com.hazrat.alQuran.ui.di

import com.hazrat.alQuran.ui.ayah.AyahViewModel
import com.hazrat.alQuran.ui.surah.SurahViewModel
import com.hazrat.usecase.quran.ControlQuranAudioUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun getAlQuranUiModule(): Module = module {
    viewModel {
        SurahViewModel(
            getAllSurahListUseCase = get(),
            getRecentSurahsUseCase = get(),
            getBookmarkedAyahsUseCase = get(),
            dataStorePreference = get()
        )
    }
    viewModel { param ->
        AyahViewModel(
            surahNumber = param[0],
            initialTargetAyahNumber = param[1],
            isFromBookmark = param[2],
            getSurahAyahsUseCase = get(),
            saveRecentSurahUseCase = get(),
            deleteRecentSurahUseCase = get(),
            dataStorePreference = get(),
            controlQuranAudioUseCase = getOrNull<ControlQuranAudioUseCase>(),
            toggleAyahBookmarkUseCase = get()
        )
    }
}