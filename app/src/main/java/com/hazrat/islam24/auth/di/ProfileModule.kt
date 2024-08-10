package com.hazrat.islam24.auth.di

import android.content.Context
import com.hazrat.islam24.auth.repository.ProfileRepository
import com.hazrat.islam24.auth.repository.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideProfileRepository(@ApplicationContext context: Context): ProfileRepository {
        return ProfileRepositoryImpl(context)
    }

}