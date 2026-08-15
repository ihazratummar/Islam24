package com.hazrat.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant


/**
 * @author hazratummar
 * Created on 14/08/26
 */

class SyncDatastore(
    private val datastore: DataStore<Preferences>
) {

    companion object {
        private val KEY_LAST_SYNCED_AT = stringPreferencesKey("key_last_synced_at")
    }

    suspend fun getLastSyncedAt() : Instant? {
        val iosString = datastore.data.map { it[KEY_LAST_SYNCED_AT] }.firstOrNull()
        return iosString?.let { Instant.parse(it) }
    }

    suspend fun setLastSyncedAt(timestamp: Instant) {
        datastore.edit { pref->
            pref[KEY_LAST_SYNCED_AT] = timestamp.toString()
        }
    }

    suspend fun clearLastSyncedAt() {
        datastore.edit { pref->
            pref.remove(KEY_LAST_SYNCED_AT)
        }
    }

}