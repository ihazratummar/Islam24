//AppModule.kt

package com.hazrat.islam24.core.di.prayertime

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hazrat.islam24.core.data.dao.LocationDao
import com.hazrat.islam24.core.data.dao.LocationNameDao
import com.hazrat.islam24.core.data.dao.PrayerSettingDao
import com.hazrat.islam24.core.data.dao.PrayerTimeDao
import com.hazrat.islam24.core.data.database.LocationDatabase
import com.hazrat.islam24.core.data.database.PrayerDatabase
import com.hazrat.islam24.core.data.manager.LocationNameRepositoryImpl
import com.hazrat.islam24.core.data.manager.PrayerTimeRepositoryImpl
import com.hazrat.islam24.core.domain.repository.location.LocationNameRepository
import com.hazrat.islam24.core.domain.repository.location.LocationRepository
import com.hazrat.islam24.core.domain.repository.location.LocationRepositoryImpl
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerSettingRepository
import com.hazrat.islam24.core.domain.repository.prayertime.PrayerTimeRepository
import com.hazrat.islam24.core.network.LocationNameApi
import com.hazrat.islam24.core.network.PrayerTimeApi
import com.hazrat.islam24.util.Constants.BASE_URL
import com.hazrat.islam24.util.Constants.LOCATION_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PrayerModule {


    ///prayerTime repository
    @Singleton
    @Provides
    fun providePrayerTimeRepository(
        api: PrayerTimeApi,
        locationRepository: LocationRepositoryImpl,
        prayerSettingRepository: PrayerSettingRepository,
        prayerTimeDao: PrayerTimeDao
    ) : PrayerTimeRepository = PrayerTimeRepositoryImpl(api, locationRepository, prayerSettingRepository, prayerTimeDao)


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
    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()


    //Prayer Time Api
    @Singleton
    @Provides
    fun providePrayerApi(): PrayerTimeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
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
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationNameApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationNameRepository(
        api: LocationNameApi,
        locationRepository: LocationRepositoryImpl,
        locationNameDao: LocationNameDao
    ): LocationNameRepository = LocationNameRepositoryImpl(api, locationRepository, locationNameDao)

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