//AppModule.kt

package com.hazrat.islam24.core.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.hazrat.islam24.core.data.dao.LocationDao
import com.hazrat.islam24.core.data.dao.PrayerSettingDao
import com.hazrat.islam24.core.data.dao.PrayerTimeDao
import com.hazrat.islam24.core.data.database.LocationDatabase
import com.hazrat.islam24.core.data.database.PrayerDatabase
import com.hazrat.islam24.core.data.repository.LocationRepositoryImpl
import com.hazrat.islam24.core.data.repository.PrayerSettingRepositoryImpl
import com.hazrat.islam24.core.data.repository.PrayerTimeRepositoryImpl
import com.hazrat.islam24.core.domain.repository.location.LocationRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerSettingRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.core.remote.api.PrayerTimeApi
import com.hazrat.islam24.util.ConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PrayerModule {


    //    /prayerTime repository
    @Singleton
    @Provides
    fun providePrayerTimeRepository(
        api: PrayerTimeApi,
        locationRepository: LocationRepositoryImpl,
        prayerSettingRepository: PrayerSettingRepository,
        prayerTimeDao: PrayerTimeDao,
        @ApplicationContext context: Context,
        connectivityObserver: ConnectivityObserver
    ): PrayerTimeRepository = PrayerTimeRepositoryImpl(
        api = api,
        locationRepository = locationRepository,
        prayerSettingRepository = prayerSettingRepository,
        prayerTimeDao = prayerTimeDao,
        context = context,
        connectivityObserver = connectivityObserver
    )


    //    /prayer time database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): PrayerDatabase {
        Log.d("AppDatabase", "Creating database instance")
        return Room.databaseBuilder(
                context.applicationContext,
                PrayerDatabase::class.java,
                "prayer-database"
            ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun providePrayerTimeDao(appDatabase: PrayerDatabase): PrayerTimeDao {
        return appDatabase.prayerTimeDao()
    }


    @Singleton
    @Provides
    fun providePrayerSettingDao(prayerDatabase: PrayerDatabase): PrayerSettingDao {
        return prayerDatabase.prayerSetting()
    }

    @Singleton
    @Provides
    fun providePrayerSettingRepository(
        methodDao: PrayerSettingDao
    ): PrayerSettingRepository {
        return PrayerSettingRepositoryImpl(methodDao)
    }


    @Singleton
    @Provides
    fun provideLocationDao(locationDatabase: LocationDatabase): LocationDao {
        return locationDatabase.locationDao()
    }

    @Provides
    fun provideLocationRepository(
        fusedLocationProviderClient: FusedLocationProviderClient,
        locationDao: LocationDao
    ): LocationRepository {
        return LocationRepositoryImpl(fusedLocationProviderClient, locationDao)
    }

}