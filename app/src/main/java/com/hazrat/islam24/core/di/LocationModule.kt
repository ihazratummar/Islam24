package com.hazrat.islam24.core.di

import android.content.Context
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hazrat.islam24.BuildConfig
import com.hazrat.islam24.core.api.LocationNameApi
import com.hazrat.islam24.core.data.dao.LocationNameDao
import com.hazrat.islam24.core.data.database.LocationDatabase
import com.hazrat.islam24.core.data.repository.LocationNameRepositoryImpl
import com.hazrat.islam24.core.data.repository.LocationRepositoryImpl
import com.hazrat.islam24.core.domain.repository.NetworkRepository
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.service.LocationHandler
import com.hazrat.islam24.service.LocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
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

    @Singleton
    @Provides
    fun provideLocationManager(@ApplicationContext context: Context, fusedLocationProviderClient: FusedLocationProviderClient): LocationManager{
        return LocationManager(context, fusedLocationProviderClient)
    }


    @Provides
    @Singleton
    fun provideLocationNameRepository(
        api: LocationNameApi,
        locationRepository: LocationRepositoryImpl,
        locationNameDao: LocationNameDao,
        networkRepository: NetworkRepository
    ): LocationNameRepository = LocationNameRepositoryImpl(api, locationRepository, locationNameDao, networkRepository)

    @Singleton
    @Provides
    fun provideLocationNameDao(locationDatabase: LocationDatabase): LocationNameDao {
        return locationDatabase.locationNameDao()
    }



    @Singleton
    @Provides
    fun provideLocationDatabase(@ApplicationContext context: Context): LocationDatabase {
        val passPhrase = SQLiteDatabase.getBytes(BuildConfig.MY_PASS_PHRASE.toCharArray())
        val factory = SupportFactory(passPhrase)


        return Room.databaseBuilder(
            context.applicationContext,
            LocationDatabase::class.java,
            "location_database")
            .fallbackToDestructiveMigration()
            .build()
    }


}