package com.hazrat.islam24.sensor

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    @RotationSensorQualifier
    fun provideRotationSensor(@ApplicationContext context: Context): MeasurableSensor {
        return RotationSensor(context)
    }

    @Provides
    @Singleton
    @CompassSensorQualifier
    fun provideCompassSensor(@ApplicationContext context: Context): MeasurableSensor {
        return CompassSensor(context)
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RotationSensorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CompassSensorQualifier