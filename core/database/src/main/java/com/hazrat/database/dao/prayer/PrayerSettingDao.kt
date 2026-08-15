package com.hazrat.database.dao.prayer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.converter.NotificationSettingsMapEntity
import com.hazrat.database.entity.prayer.UserPrayerSettingEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant


/**
 * @author hazratummar
 * Created on 14/08/26
 */

@Dao
interface PrayerSettingDao {

    // ==========================================
    // 1. READ OPERATIONS
    // ==========================================

    /**
     * Observe setting reactively in jetpack compose ui
     */

    @Query("SELECT * FROM user_prayer_settings WHERE id = 1")
    fun observeSettings(): Flow<UserPrayerSettingEntity?>

    /**
     * One-Shot fetch for settings (during prayer time calculation and sync)
     */
    @Query("SELECT * FROM user_prayer_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): UserPrayerSettingEntity ?

    // ==========================================
    // 2. UPSERT & SYNC OPERATIONS
    // ==========================================

    /**
     * Inset or update settings.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(setting: UserPrayerSettingEntity)

    /**
     * Fetch setting only if they have been modified locally and need syncing
     */

    @Query("SELECT * FROM user_prayer_settings WHERE id=1 AND isSynced = 0 LIMIT 1")
    suspend fun getUnsyncedSettings() : UserPrayerSettingEntity?

    /**
     * Mark Settings as synced after the backend accepts them
     */
    @Query("UPDATE user_prayer_settings SET isSynced=1 WHERE id = 1")
    suspend fun markAsSynced()

    // ==========================================
    // 3. DIRECT UI UPDATES (Auto-marks isSynced = 0)
    // ==========================================

    /**
     * Update calculation method(e.g. MWL, ISNA, KARACHI)
     */

    @Query("""
        UPDATE user_prayer_settings 
        SET calculationMethod = :method, isSynced = 0, updatedAt = :updatedAt 
        WHERE id = 1
    """)
    suspend fun updateCalculationMethod(method: Int, updatedAt: Instant)

    /**
     * Update juristic method (0 = Shafi/Standard, 1 = Hanafi).
     */
    @Query("""
        UPDATE user_prayer_settings 
        SET juristicMethod = :method, isSynced = 0, updatedAt = :updatedAt 
        WHERE id = 1
    """)
    suspend fun updateJuristicMethod(method: Int, updatedAt: Instant)

    /**
     * Toggle master notification on/off.
     */
    @Query("""
        UPDATE user_prayer_settings 
        SET masterNotification = :enabled, isSynced = 0, updatedAt = :updatedAt 
        WHERE id = 1
    """)
    suspend fun updateMasterNotification(enabled: Boolean, updatedAt: kotlin.time.Instant)
    /**
     * Update per-prayer notification preferences (audio, offset, enabled).
     */
    @Query("""
        UPDATE user_prayer_settings 
        SET notificationSettingsJson = :settings, isSynced = 0, updatedAt = :updatedAt 
        WHERE id = 1
    """)
    suspend fun updateNotificationSettings(settings: NotificationSettingsMapEntity, updatedAt: Instant)



}