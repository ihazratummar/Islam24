//AppModule.kt

package com.hazrat.islam24.di.prayertime

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hazrat.islam24.data.prayertime.PrayerDatabase
import com.hazrat.islam24.data.prayertime.PrayerSettingDao
import com.hazrat.islam24.data.prayertime.PrayerTimeDao
import com.hazrat.islam24.network.LocationNameApi
import com.hazrat.islam24.network.PrayerTimeApi
import com.hazrat.islam24.util.Constants.BASE_URL
import com.hazrat.islam24.util.Constants.LOCATION_BASE_URL
import com.hazrat.islam24.data.location.coordinents.LocationDao
import com.hazrat.islam24.data.location.coordinents.LocationDatabase
import com.hazrat.islam24.data.location.locationdetails.LocationNameDao
import com.hazrat.islam24.domain.repository.location.LocationRepository
import com.hazrat.islam24.domain.repository.prayertime.PrayerSettingRepository
import com.hazrat.islam24.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.domain.repository.location.LocationNameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PrayerModule {


    ///prayerTime repository
    @Singleton
    @Provides
    fun providePrayerTimeRepository(
        api: PrayerTimeApi,
        locationRepository: LocationRepository,
        prayerSettingRepository: PrayerSettingRepository,
        prayerTimeDao: PrayerTimeDao
    ) =
        PrayerTimeRepository(api, locationRepository, prayerSettingRepository, prayerTimeDao)


//    /prayer time database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): PrayerDatabase {
        Log.d("AppDatabase", "Creating database instance")
        return Room.databaseBuilder(
            context.applicationContext,
            PrayerDatabase::class.java,
            "app-database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePrayerTimeDao(appDatabase: PrayerDatabase): PrayerTimeDao {
        return appDatabase.prayerTimeDao()
    }


    //Prayer Time Api
    @Singleton
    @Provides
    fun providePrayerApi(): PrayerTimeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PrayerTimeApi::class.java)
    }


    //LocationApi Name
    @Singleton
    @Provides
    fun provideLocationNameApi(): LocationNameApi {
        return Retrofit.Builder()
            .baseUrl(LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationNameApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationNameRepository(
        api: LocationNameApi,
        locationRepository: LocationRepository,
        locationNameDao: LocationNameDao
    ): LocationNameRepository {
        return LocationNameRepository(api, locationRepository, locationNameDao)
    }

    @Singleton
    @Provides
    fun provideLocationNameDao(locationDatabase: LocationDatabase): LocationNameDao {
        return locationDatabase.locationNameDao()
    }


    @Singleton
    @Provides
    fun provideLocationDatabase(@ApplicationContext context: Context): LocationDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LocationDatabase::class.java,
            "location_database"
        ).fallbackToDestructiveMigration()
            .build()
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
        return PrayerSettingRepository(methodDao)
    }


    //location service
    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Singleton
    @Provides
    fun provideLocationDao(locationDatabase: LocationDatabase): LocationDao {
        return locationDatabase.locationDao()
    }

    @Provides
    fun provideLocationRepository(
        @ApplicationContext context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient,
        locationDao: LocationDao
    ): LocationRepository {
        return LocationRepository(context, fusedLocationProviderClient, locationDao)
    }

}