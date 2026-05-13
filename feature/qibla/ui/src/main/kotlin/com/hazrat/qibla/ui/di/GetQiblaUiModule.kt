package com.hazrat.qibla.ui.di

import com.hazrat.qibla.ui.QiblaViewModel
import com.hazrat.sensor.CompassSensorQualifier
import com.hazrat.sensor.MeasurableSensor
import com.hazrat.sensor.RotationSensorQualifier
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 24/01/26
 */

fun getQiblaUiModule(): Module = module {
    viewModel {
        QiblaViewModel(
            rotationSensor = get<MeasurableSensor>(qualifier = RotationSensorQualifier),
            compassSensor = get<MeasurableSensor>(qualifier = CompassSensorQualifier),
            userDataStore = get(),
            qiblaRepository = get(),
            locationRepository = get(),
            observeAuthStateUseCase = get()
        )
    }
}