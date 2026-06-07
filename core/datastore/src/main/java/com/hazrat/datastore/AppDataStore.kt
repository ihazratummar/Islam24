package com.hazrat.datastore

import android.content.res.Configuration
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * @author Hazrat Ummar Shaikh
 * Created on 22-01-2025
 */

class AppDataStore (
    private val appDataStore: DataStore<Preferences>
) {

    private object DataStoreKeys {
        ///Theme
        const val THEME_CONST = "THEME_KEY"
        const val HAPTIC_CONST = "HAPTIC_KEY"
        const val LAST_SEEN_VERSION_CONST = "LAST_SEEN_VERSION_KEY"

        /*
       ******************--------------------------*************************
        */
        //Theme
        val themeKey = booleanPreferencesKey(THEME_CONST)
        val hapticKey = booleanPreferencesKey(HAPTIC_CONST)
        val lastSeenVersionKey = androidx.datastore.preferences.core.intPreferencesKey(LAST_SEEN_VERSION_CONST)
    }

    private val systemTheme =
        when (Resources.getSystem().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                true
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                false
            }

            else -> {
                false
            }
        }

    suspend fun enableDarkTheme(enable: Boolean) {
        appDataStore.edit {preference ->
            preference[DataStoreKeys.themeKey] = enable
        }
    }

    val isDarkModeEnabled: Flow<Boolean> = appDataStore.data.map { preference ->
        preference[DataStoreKeys.themeKey] ?: systemTheme
    }

    suspend fun getDarkModeEnabled(): Boolean {
        return appDataStore.data.map { preference ->
            preference[DataStoreKeys.themeKey] ?: systemTheme
        }.first()
    }

    suspend fun enableHaptic(enable: Boolean) {
        appDataStore.edit {preference ->
            preference[DataStoreKeys.hapticKey] = enable
        }
    }

    val isHapticEnabled: Flow<Boolean> = appDataStore.data.map { preference ->
        preference[DataStoreKeys.hapticKey] == true
    }

    suspend fun getHapticEnabled(): Boolean {
        return appDataStore.data.map { preference ->
            preference[DataStoreKeys.hapticKey] == true
        }.first()
    }

    suspend fun setLastSeenVersionCode(versionCode: Int) {
        appDataStore.edit { preference ->
            preference[DataStoreKeys.lastSeenVersionKey] = versionCode
        }
    }

    suspend fun getLastSeenVersionCode(): Int {
        return appDataStore.data.map { preference ->
            preference[DataStoreKeys.lastSeenVersionKey] ?: 0
        }.first()
    }


}