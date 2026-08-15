package com.hazrat.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


/**
 * @author hazratummar
 * Created on 14/08/26
 */



@Serializable
data class NotificationSettingEntity(
    val enabled: Boolean = true,
    val offsetMinutes: Int = 0,
    val audio: String = "default"
)
@Serializable
data class NotificationSettingsMapEntity(
    val fajr: NotificationSettingEntity = NotificationSettingEntity(),
    val dhuhr: NotificationSettingEntity = NotificationSettingEntity(),
    val asr: NotificationSettingEntity = NotificationSettingEntity(),
    val maghrib: NotificationSettingEntity = NotificationSettingEntity(),
    val isha: NotificationSettingEntity = NotificationSettingEntity()
)

class NotificationSettingsConverter {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun fromNotificationSettings(setting: NotificationSettingsMapEntity?) : String {
        return if (setting == null) "" else json.encodeToString(setting)
    }

    @TypeConverter
    fun toNotificationSettings(jsonString: String?): NotificationSettingsMapEntity {
        return if (jsonString.isNullOrBlank()) {
            NotificationSettingsMapEntity() // fallback to defaults
        } else {
            try {
                json.decodeFromString<NotificationSettingsMapEntity>(jsonString)
            } catch (e: Exception) {
                NotificationSettingsMapEntity()
            }
        }
    }
}