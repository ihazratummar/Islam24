package com.hazrat.alQuran.data.di

import com.hazrat.alQuran.data.repository.QuranRepositoryImpl
import com.hazrat.domain.repository.QuranRepository
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun getAlQuranDataModule(): Module = module {

    single<QuranRepository> {
        QuranRepositoryImpl(
            quranDao = get()
        )
    }
}