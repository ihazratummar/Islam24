package com.hazrat.prayertime.data.repository

import com.hazrat.database.converter.NotificationSettingsMapEntity
import com.hazrat.database.dao.prayer.PrayerSettingDao
import com.hazrat.database.entity.prayer.UserPrayerSettingEntity
import com.hazrat.domain.repository.PrayerAlarmRescheduler
import com.hazrat.domain.repository.prayer.PrayerSettingRepository
import com.hazrat.model.Prayer
import com.hazrat.model.prayersettingmodel.UserPrayerSettingModel
import com.hazrat.prayertime.data.mapper.toModel
import com.hazrat.utils.result.Result
import com.hazrat.utils.result.error.DatabaseError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlin.time.Clock

/**
 * Clean implementation of PrayerSettingRepository supporting DataStore and Room DB sync.
 * @author Hazrat Ummar Shaikh
 */
class PrayerSettingRepositoryImpl(
    private val prayerSettingDao: PrayerSettingDao,
    private val prayerAlarmRescheduler: PrayerAlarmRescheduler? = null
) : PrayerSettingRepository {

    private suspend fun ensureSettingExists(): UserPrayerSettingEntity {
        val existing = prayerSettingDao.getSettings()
        if (existing != null) return existing

        val defaultSetting = UserPrayerSettingEntity(
            id = 1,
            calculationMethod = 1,
            juristicMethod = 0,
            masterNotification = true,
            notificationSettingsJson = NotificationSettingsMapEntity(),
            isSynced = false,
            updatedAt = Clock.System.now()
        )
        prayerSettingDao.upsert(defaultSetting)
        return defaultSetting
    }

    override suspend fun insertCalculationMethod(method: Int): Result<Boolean, DatabaseError> {
        return try {
            ensureSettingExists()
            val updatedAtInstant = Clock.System.now()
            prayerSettingDao.updateCalculationMethod(method = method, updatedAt = updatedAtInstant)
            prayerAlarmRescheduler?.rescheduleAlarms()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(error = DatabaseError.UnknownError)
        }
    }

    override suspend fun insertJuristicMethod(method: Int): Result<Boolean, DatabaseError> {
        return try {
            ensureSettingExists()
            val updatedAtInstant = Clock.System.now()
            prayerSettingDao.updateJuristicMethod(method = method, updatedAt = updatedAtInstant)
            prayerAlarmRescheduler?.rescheduleAlarms()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(error = DatabaseError.UnknownError)
        }
    }

    override suspend fun prayerNotificationEnabled(
        prayer: Prayer,
        enabled: Boolean
    ) {

        val currentSetting = ensureSettingExists()
        val currentMap = currentSetting.notificationSettingsJson
        val updatedMap = when (prayer) {
            Prayer.FAJR -> currentMap.copy(fajr = currentMap.fajr.copy(enabled = enabled))
            Prayer.DHUHR -> currentMap.copy(dhuhr = currentMap.dhuhr.copy(enabled = enabled))
            Prayer.ASR -> currentMap.copy(asr = currentMap.asr.copy(enabled = enabled))
            Prayer.MAGHRIB -> currentMap.copy(maghrib = currentMap.maghrib.copy(enabled = enabled))
            Prayer.ISHA -> currentMap.copy(isha = currentMap.isha.copy(enabled = enabled))
        }

        prayerSettingDao.updateNotificationSettings(
            settings = updatedMap,
            updatedAt = Clock.System.now()
        )
        prayerAlarmRescheduler?.rescheduleAlarms()
    }

    override fun getUserPrayerSetting(): Flow<UserPrayerSettingModel> {
        return prayerSettingDao.observeSettings().map { it?.toModel() ?: UserPrayerSettingModel() }
    }

    override suspend fun isPrayerNotificationEnabled(prayer: Prayer): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updatePrayerAudio(
        prayer: Prayer,
        audio: String,
        updatedAt: Instant
    ) {
        val currentSetting = ensureSettingExists()
        val currentMap = currentSetting.notificationSettingsJson
        val updatedMap = when (prayer) {
            Prayer.FAJR -> currentMap.copy(fajr = currentMap.fajr.copy(audio = audio))
            Prayer.DHUHR -> currentMap.copy(dhuhr = currentMap.dhuhr.copy(audio = audio))
            Prayer.ASR -> currentMap.copy(asr = currentMap.asr.copy(audio = audio))
            Prayer.MAGHRIB -> currentMap.copy(maghrib = currentMap.maghrib.copy(audio = audio))
            Prayer.ISHA -> currentMap.copy(isha = currentMap.isha.copy(audio = audio))
        }
        prayerSettingDao.updateNotificationSettings(settings = updatedMap, updatedAt = updatedAt)
        prayerAlarmRescheduler?.rescheduleAlarms()
    }

    override suspend fun updatePrayerOffset(
        prayer: Prayer,
        offset: Int,
        updatedAt: Instant
    ) {
        val currentSetting = ensureSettingExists()
        val currentMap = currentSetting.notificationSettingsJson
        val updatedMap = when (prayer) {
            Prayer.FAJR -> currentMap.copy(fajr = currentMap.fajr.copy(offsetMinutes = offset))
            Prayer.DHUHR -> currentMap.copy(dhuhr = currentMap.dhuhr.copy(offsetMinutes = offset))
            Prayer.ASR -> currentMap.copy(asr = currentMap.asr.copy(offsetMinutes = offset))
            Prayer.MAGHRIB -> currentMap.copy(maghrib = currentMap.maghrib.copy(offsetMinutes = offset))
            Prayer.ISHA -> currentMap.copy(isha = currentMap.isha.copy(offsetMinutes = offset))
        }
        prayerSettingDao.updateNotificationSettings(settings = updatedMap, updatedAt = updatedAt)
        prayerAlarmRescheduler?.rescheduleAlarms()
    }

    override suspend fun clearUserSetting() {
        prayerSettingDao.clearUserSetting()
        prayerAlarmRescheduler?.rescheduleAlarms()
    }
}
