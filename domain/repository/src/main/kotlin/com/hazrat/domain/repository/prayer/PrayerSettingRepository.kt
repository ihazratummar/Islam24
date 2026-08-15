package com.hazrat.domain.repository.prayer

import com.hazrat.model.Prayer
import com.hazrat.model.PrayerNotificationSettings
import com.hazrat.model.prayersettingmodel.UserPrayerSettingModel
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlin.time.Clock

interface PrayerSettingRepository {

    /**
     * Inserts or updates a prayer calculation method into the database.
     * If a method with the same primary key already exists, it will be replaced.
     *
     * @param method The PrayerCalculationEntity object to be inserted or updated.
     */
    suspend fun insertCalculationMethod(method: Int): Result<Boolean, DatabaseError>

    /**
     * Inserts or updates a prayer juristic method into the database.
     * If a method with the same primary key already exists, it will be replaced.
     *
     * @param method The PrayerJuristicEntity object to be inserted or updated.
     */
    suspend fun insertJuristicMethod(method: Int): Result<Boolean, DatabaseError>

    suspend fun prayerNotificationEnabled(prayer: Prayer, enabled: Boolean)

    fun getUserPrayerSetting(): Flow<UserPrayerSettingModel>

    suspend fun isPrayerNotificationEnabled(prayer: Prayer): Boolean

    suspend fun updatePrayerAudio(
        prayer: Prayer,
        audio: String,
        updatedAt: Instant = Clock.System.now()
    )

    suspend fun updatePrayerOffset(
        prayer: Prayer,
        offset: Int,
        updatedAt: Instant = Clock.System.now()
    )
}