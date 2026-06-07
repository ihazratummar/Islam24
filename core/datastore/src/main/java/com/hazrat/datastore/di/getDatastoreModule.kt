package com.hazrat.datastore.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.hazrat.datastore.AppDataStore
import com.hazrat.datastore.DataStorePreference
import com.hazrat.datastore.UserDataStore
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 22/01/26
 */

const val APP_DATA_STORE = "APP_DATA_STORE"

const val USER_DATA_SORE = "USER_DATA_SORE"


val Context.appPreferenceDataStore by preferencesDataStore(name = APP_DATA_STORE)
val Context.userPreferenceDataStore by preferencesDataStore(name = USER_DATA_SORE)

fun getDatastoreModule () : Module = module {
    single (qualifier = named(APP_DATA_STORE)){ get<Context>().appPreferenceDataStore }
    single (qualifier = named(USER_DATA_SORE)){ get<Context>().userPreferenceDataStore }

    single { AppDataStore(appDataStore = get(qualifier = named(APP_DATA_STORE))) }
    single { UserDataStore(userDataStore = get(qualifier = named(USER_DATA_SORE))) }

    single { DataStorePreference(context = get()) }

}