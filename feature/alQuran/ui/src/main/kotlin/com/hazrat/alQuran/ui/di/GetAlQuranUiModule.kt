package com.hazrat.alQuran.ui.di

import com.hazrat.alQuran.ui.ayah.AyahViewModel
import com.hazrat.alQuran.ui.surah.SurahViewModel
import com.hazrat.usecase.khatam.EndKhatamPlanUseCase
import com.hazrat.usecase.khatam.GetActiveKhatamPlanUseCase
import com.hazrat.usecase.khatam.GetKhatamHistoryUseCase
import com.hazrat.usecase.khatam.ResetKhatamPlanUseCase
import com.hazrat.usecase.khatam.StartKhatamPlanUseCase
import com.hazrat.usecase.khatam.UpdateKhatamProgressUseCase
import com.hazrat.usecase.khatam.UpdateKhatamTargetDateUseCase
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
            getActiveKhatamPlanUseCase = getOrNull<GetActiveKhatamPlanUseCase>(),
            getKhatamHistoryUseCase = getOrNull<GetKhatamHistoryUseCase>(),
            startKhatamPlanUseCase = getOrNull<StartKhatamPlanUseCase>(),
            updateKhatamTargetDateUseCase = getOrNull<UpdateKhatamTargetDateUseCase>(),
            resetKhatamPlanUseCase = getOrNull<ResetKhatamPlanUseCase>(),
            endKhatamPlanUseCase = getOrNull<EndKhatamPlanUseCase>()
        )
    }
    viewModel { param ->
        AyahViewModel(
            surahNumber = param[0],
            initialTargetAyahNumber = param[1],
            isFromBookmark = param[2],
            isFromKhatam = if (param.size() > 3) param[3] else false,
            getSurahAyahsUseCase = get(),
            saveRecentSurahUseCase = get(),
            deleteRecentSurahUseCase = get(),
            dataStorePreference = get(),
            controlQuranAudioUseCase = getOrNull<ControlQuranAudioUseCase>(),
            toggleAyahBookmarkUseCase = get(),
            updateKhatamProgressUseCase = getOrNull<UpdateKhatamProgressUseCase>()
        )
    }
}