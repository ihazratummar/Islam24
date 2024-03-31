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

/**
 * Implementation of the LocalUserManager interface for managing user preferences locally using DataStore.
 *
 * @param application The Application instance for accessing DataStore.
 */
class LocalUserManagerImpl @Inject constructor(
    private val application: Application
) : LocalUserManager {

    /**
     * Saves the app entry flag in DataStore.
     */
    override suspend fun saveAppEntry() {
        application.dataStore.edit { settings ->
            settings[PreferenceKey.APP_ENTRY] = true
        }
    }

    /**
     * Reads the app entry flag from DataStore.
     *
     * @return Flow emitting a Boolean value indicating whether the app entry flag is set.
     */
    override fun readAppEntry(): Flow<Boolean> {
        return application.dataStore.data.map { preferences ->
            preferences[PreferenceKey.APP_ENTRY] ?: false
        }
    }
}

/**
 * Provides read-only access to the DataStore<Preferences> instance.
 */
private val readOnlyProperty = preferencesDataStore(name = USER_SETTINGS)

/**
 * Extension property for accessing DataStore<Preferences> in a Context.
 */
val Context.dataStore: DataStore<Preferences> by readOnlyProperty

/**
 * Object defining keys for preferences stored in DataStore.
 */
private object PreferenceKey {
    val APP_ENTRY = booleanPreferencesKey(Constants.APP_ENTRY)
}
