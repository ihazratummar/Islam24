package com.hazrat.location.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hazrat.domain.repository.LocationNameRepository
import com.hazrat.location.data.FusedLocationDataSource
import com.hazrat.location.data.LocationDataSource
import com.hazrat.location.data.LocationNameRepositoryImpl
import com.hazrat.location.data.LocationRepositoryImpl
import com.hazrat.location.data.PermissionChecker
import com.hazrat.location.model.LocationConfig
import com.hazrat.location.repository.LocationRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 24/01/26
 */

fun getLocationModule(): Module = module {
    single { LocationConfig() }

    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(
            androidContext()
        )
    }

    single { PermissionChecker(androidContext()) }

    single<LocationDataSource> {
        FusedLocationDataSource(
            context = androidContext(),
            fusedLocationClient = get(),
            permissionChecker = get()
        )
    }

    single<LocationRepository> { LocationRepositoryImpl(locationDataSource = get()) }
    single<LocationNameRepository> {
        LocationNameRepositoryImpl(
            locationNameApi = get(),
            locationRepository = get(),
            locationNameDao = get(),
            prayerTimeRepository = get()
        )
    }
}