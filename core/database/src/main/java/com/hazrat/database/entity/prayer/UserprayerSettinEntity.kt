package com.hazrat.database.entity.prayer

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hazrat.database.converter.NotificationSettingsMapEntity
import kotlinx.datetime.Instant

@Entity(
    tableName = "user_prayer_settings"
)
data class UserPrayerSettingEntity(
    @PrimaryKey
    val id: Int = 1,
    val calculationMethod: Int,
    val juristicMethod: Int,
    val masterNotification: Boolean = true,
    val notificationSettingsJson: NotificationSettingsMapEntity = NotificationSettingsMapEntity(),
    val isSynced: Boolean = false,
    val updatedAt: Instant
)
