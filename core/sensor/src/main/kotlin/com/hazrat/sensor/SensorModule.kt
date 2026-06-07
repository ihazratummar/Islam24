package com.hazrat.sensor

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module


val RotationSensorQualifier: Qualifier = qualifier("RotationSensor")
val CompassSensorQualifier: Qualifier = qualifier("CompassSensor")

fun geSensorModule() : Module = module {
    single<MeasurableSensor> ( qualifier = RotationSensorQualifier){ RotationSensor(androidContext()) }
    single<MeasurableSensor> ( qualifier = CompassSensorQualifier){ CompassSensor(androidContext()) }
}
