package com.hazrat.islam24.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hazrat.islam24.domain.repository.location.LocationRepositoryImpl
import com.hazrat.islam24.service.LocationHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideLocationHandler(
        @ApplicationContext context: Context,
        locationRepository: LocationRepositoryImpl,
        fusedLocationProviderClient: FusedLocationProviderClient,
        // Provide the ComponentActivity here
    ): LocationHandler {
        return LocationHandler(context, locationRepository, fusedLocationProviderClient )
    }


}