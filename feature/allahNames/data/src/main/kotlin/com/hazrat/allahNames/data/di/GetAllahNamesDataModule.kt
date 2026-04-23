package com.hazrat.allahNames.data.di

import com.hazrat.allahNames.data.repository.NamesRepositoryImpl
import com.hazrat.allahNames.repository.NamesRepository
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 25/01/26
 */

fun getAllahNamesDataModule() : Module = module {
    single <NamesRepository>{ NamesRepositoryImpl(api = get(), nameDao = get()) }
}