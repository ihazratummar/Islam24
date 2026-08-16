package com.hazrat.database.dao.quran

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.quran.KhatamPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KhatamDao {

    @Query("SELECT * FROM khatam_plan WHERE status = 'IN_PROGRESS' AND isDeleted = 0 ORDER BY startDateTimestamp DESC LIMIT 1")
    fun getActiveKhatamPlan(): Flow<KhatamPlanEntity?>

    @Query("SELECT * FROM khatam_plan WHERE isDeleted = 0 ORDER BY startDateTimestamp DESC")
    fun getAllKhatamPlans(): Flow<List<KhatamPlanEntity>>

    @Query("SELECT * FROM khatam_plan WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): KhatamPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateKhatamPlan(plan: KhatamPlanEntity)

    @Query("UPDATE khatam_plan SET status = 'ENDED', isSynced = 0, updatedTimestamp = :timestamp WHERE id = :id")
    suspend fun endKhatamPlan(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE khatam_plan SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("SELECT * FROM khatam_plan WHERE isSynced = 0")
    suspend fun getUnSynced(): List<KhatamPlanEntity>

    @Query("UPDATE khatam_plan SET isDeleted = 1, isSynced = 0, updatedTimestamp = :timestamp WHERE id = :id")
    suspend fun softDeleteKhatam(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM khatam_plan WHERE id IN (:ids)")
    suspend fun deleteKhatamPlan(ids: List<String>)

    @Query("DELETE FROM khatam_plan")
    suspend fun deleteAllPlans()
}
