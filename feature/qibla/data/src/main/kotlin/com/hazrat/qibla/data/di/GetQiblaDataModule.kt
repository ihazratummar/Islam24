package com.hazrat.qibla.data.di

import com.hazrat.domain.repository.QiblaRepository
import com.hazrat.qibla.data.repository.QiblaRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun getQiblaDataModule() : Module = module {
    single<QiblaRepository> { QiblaRepositoryImpl(userDataStore = get(), firebaseAuth = get(), firebaseFirestore = get()) }
}