package com.hazrat.hajjlive.data.di

import com.hazrat.domain.repository.HajjLiveRepository
import com.hazrat.hajjlive.data.repository.HajjLiveRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun getHajjliveDataModule() : Module = module {
    single<HajjLiveRepository> { HajjLiveRepositoryImpl(api = get()) }
}