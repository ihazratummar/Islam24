package com.hazrat.islam24.notification.di

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.hazrat.islam24.notification.NotificationHelper
import com.hazrat.islam24.notification.PrayerAlarmManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Hazrat Ummar Shaikh
 */


@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {


    @Singleton
    @Provides
    fun providePrayerAlarmManager(@ApplicationContext context: Context): PrayerAlarmManager {
        return PrayerAlarmManager(context)
    }

    @Singleton
    @Provides
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper {
        return NotificationHelper(context)
    }

    @Singleton
    @Provides
    fun provideNotificationManagerCompat(@ApplicationContext context: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }
}