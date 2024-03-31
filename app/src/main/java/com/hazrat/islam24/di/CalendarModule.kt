package com.hazrat.islam24.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.hazrat.islam24.data.dao.GregorianToHijriDao
import com.hazrat.islam24.data.dao.HijriCalendarDao
import com.hazrat.islam24.data.manager.HijriCalendarRepositoryImpl
import com.hazrat.islam24.domain.repository.GregorianToHijriRepository
import com.hazrat.islam24.domain.repository.HijriCalendarRepository
import com.hazrat.islam24.network.GregorianToHijriApi
import com.hazrat.islam24.network.HijriCalendarApi
import com.hazrat.islam24.data.database.CalendarDatabase
import com.hazrat.islam24.data.manager.GregorianToHijriRepositoryImpl
import com.hazrat.islam24.util.Constants.GTH_BASE_URL
import com.hazrat.islam24.util.Constants.HIJRI_CALENDAR_URL
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
object CalendarModule {

    @Singleton
    @Provides
    fun provideGtoHManager(api: GregorianToHijriApi, gregorianToHijriDao: GregorianToHijriDao): GregorianToHijriRepository {
        return GregorianToHijriRepositoryImpl(api,gregorianToHijriDao)
    }

    @Singleton
    @Provides
    fun provideGregorianToHijriApi(): GregorianToHijriApi {
         return Retrofit.Builder()
             .baseUrl(GTH_BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .build()
             .create(GregorianToHijriApi::class.java)
    }



    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): CalendarDatabase {
        Log.d("AppDatabase", "Creating database instance")
        return Room.databaseBuilder(
            context.applicationContext,
            CalendarDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideGregorianToHijriDao(appDatabase: CalendarDatabase): GregorianToHijriDao {
        return appDatabase.gregorianToHijriDao()
    }

    @Singleton
    @Provides
    fun provideHijriCalenderDao(appDatabase: CalendarDatabase): HijriCalendarDao {
        return appDatabase.hijriCalendarDao()
    }


    @Singleton
    @Provides
    fun hijriCalendarManager(api: HijriCalendarApi, dao: GregorianToHijriDao, hijriCalendarDao: HijriCalendarDao): HijriCalendarRepository {
        return HijriCalendarRepositoryImpl(api, dao,hijriCalendarDao)
    }

    @Singleton
    @Provides
    fun provideHijriCalendarApi(): HijriCalendarApi {
        return  Retrofit.Builder()
            .baseUrl(HIJRI_CALENDAR_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HijriCalendarApi::class.java)
    }
}