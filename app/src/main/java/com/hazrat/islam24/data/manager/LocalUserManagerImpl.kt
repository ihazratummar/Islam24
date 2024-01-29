package com.hazrat.islam24.data.manager

import android.app.Application
import android.content.Context

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.hazrat.islam24.domain.manager.LocalUserManager
import com.hazrat.islam24.util.Constants.USER_SETTINGS
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.hazrat.islam24.util.Constants
import kotlinx.coroutines.flow.map

class LocalUserManagerImpl @Inject constructor(
    private val application: Application
):LocalUserManager {
    override suspend fun saveAppEntry() {
        application.dataStore.edit { settings ->
            settings[PreferenceKey.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<Boolean> {
        return application.dataStore.data.map {preferences ->
            preferences[PreferenceKey.APP_ENTRY]?: false
        }
    }
}

private val  readOnlyProperty = preferencesDataStore(name = USER_SETTINGS)

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKey{
    val APP_ENTRY = booleanPreferencesKey(Constants.APP_ENTRY)
}