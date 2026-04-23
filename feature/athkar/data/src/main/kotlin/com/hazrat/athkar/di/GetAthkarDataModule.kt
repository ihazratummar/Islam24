package com.hazrat.athkar.di

import com.hazrat.athkar.domain.repository.AthkarRepository
import com.hazrat.athkar.repository.AthkarRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 25/01/26
 */

fun getAthkarDataModule(): Module = module {
    single<AthkarRepository> { AthkarRepositoryImpl(api = get(), athkarDao = get()) }
}