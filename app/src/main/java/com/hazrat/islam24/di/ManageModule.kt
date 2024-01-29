package com.hazrat.islam24.di

import com.hazrat.islam24.data.manager.LocalUserManagerImpl
import com.hazrat.islam24.domain.manager.LocalUserManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManageModule {

    @Binds
    @Singleton
    abstract fun bindLocalUseManager(localUserManagerImpl: LocalUserManagerImpl): LocalUserManager
}