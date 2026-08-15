package com.hazrat.datastore.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.hazrat.datastore.AppDataStore
import com.hazrat.datastore.DataStorePreference
import com.hazrat.datastore.SyncDatastore
import com.hazrat.datastore.TokenStorage
import com.hazrat.datastore.UserDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module


/**
 * @author hazratummar
 * Created on 22/01/26
 */

const val APP_DATA_STORE = "APP_DATA_STORE"

const val USER_DATA_SORE = "USER_DATA_SORE"

const val SYNC_DATA_STORE = "SYNC_DATA_STORE"


val Context.appPreferenceDataStore by preferencesDataStore(name = APP_DATA_STORE)
val Context.userPreferenceDataStore by preferencesDataStore(name = USER_DATA_SORE)
val Context.syncDatastore by preferencesDataStore(name = SYNC_DATA_STORE)

fun getDatastoreModule () : Module = module {
    single (qualifier = named(APP_DATA_STORE)){ get<Context>().appPreferenceDataStore }
    single (qualifier = named(USER_DATA_SORE)){ get<Context>().userPreferenceDataStore }
    single (qualifier = named(SYNC_DATA_STORE)){ get<Context>().syncDatastore }

    single { AppDataStore(appDataStore = get(qualifier = named(APP_DATA_STORE))) }
    single { UserDataStore(userDataStore = get(qualifier = named(USER_DATA_SORE))) }
    single { SyncDatastore(datastore = get(qualifier = named(SYNC_DATA_STORE))) }

    single { DataStorePreference(context = get()) }

    single { TokenStorage(androidContext()) }

}