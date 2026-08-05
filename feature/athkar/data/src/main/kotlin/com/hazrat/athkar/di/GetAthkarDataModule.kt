package com.hazrat.athkar.di

import com.hazrat.athkar.repository.DuaRepositoryImpl
import com.hazrat.domain.repository.DuaRepository
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 25/01/26
 */

fun getAthkarDataModule(): Module = module {
    single<DuaRepository> { DuaRepositoryImpl(duaDao = get()) }
}