package com.hazrat.alQuran.data.repository

import com.hazrat.database.dao.quran.KhatamDao
import com.hazrat.database.entity.quran.KhatamPlanEntity
import com.hazrat.domain.repository.quran.KhatamRepository
import com.hazrat.model.quran.KhatamPlanModel
import com.hazrat.model.quran.KhatamStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID

class KhatamRepositoryImpl(
    private val khatamDao: KhatamDao
) : KhatamRepository {

    override fun getActiveKhatamPlan(): Flow<KhatamPlanModel?> {
        return khatamDao.getActiveKhatamPlan().map { entity ->
            entity?.toModel()
        }
    }

    override fun getKhatamPlanHistory(): Flow<List<KhatamPlanModel>> {
        return khatamDao.getAllKhatamPlans().map { list ->
            list.map { it.toModel() }
        }
    }

    override suspend fun startKhatamPlan(targetEndDateTimestamp: Long) {
        val currentActive = khatamDao.getActiveKhatamPlan().firstOrNull()
        if (currentActive != null) {
            khatamDao.endKhatamPlan(currentActive.id)
        }
        val newPlan = KhatamPlanEntity(
            id = UUID.randomUUID().toString(),
            title = "Khatam Quran",
            startDateTimestamp = System.currentTimeMillis(),
            targetEndDateTimestamp = targetEndDateTimestamp,
            lastReadSurahNumber = 1,
            lastReadAyahNumber = 1,
            lastReadGlobalAyahNumber = 1,
            completedAyahsCount = 0,
            status = "IN_PROGRESS",
            completedTimestamp = null,
            updatedTimestamp = System.currentTimeMillis(),
            isSynced = false
        )
        khatamDao.insertOrUpdateKhatamPlan(newPlan)
    }

    override suspend fun updateKhatamProgress(surahNumber: Int, ayahNumber: Int, globalAyahNumber: Int) {
        val activeEntity = khatamDao.getActiveKhatamPlan().firstOrNull() ?: return
        val isNewFurthest = globalAyahNumber >= activeEntity.lastReadGlobalAyahNumber

        val newSurah = if (isNewFurthest) surahNumber else activeEntity.lastReadSurahNumber
        val newAyah = if (isNewFurthest) ayahNumber else activeEntity.lastReadAyahNumber
        val newGlobalAyah = if (isNewFurthest) globalAyahNumber else activeEntity.lastReadGlobalAyahNumber
        val newCompletedCount = maxOf(activeEntity.completedAyahsCount, globalAyahNumber)

        val isCompleted = newCompletedCount >= 6236
        val newStatus = if (isCompleted) "COMPLETED" else activeEntity.status
        val completedTimestamp = if (isCompleted) System.currentTimeMillis() else activeEntity.completedTimestamp

        val updatedEntity = activeEntity.copy(
            lastReadSurahNumber = newSurah,
            lastReadAyahNumber = newAyah,
            lastReadGlobalAyahNumber = newGlobalAyah,
            completedAyahsCount = newCompletedCount,
            status = newStatus,
            completedTimestamp = completedTimestamp,
            updatedTimestamp = System.currentTimeMillis(),
            isSynced = false
        )
        khatamDao.insertOrUpdateKhatamPlan(updatedEntity)
    }

    override suspend fun updateKhatamTargetDate(planId: String, newTargetDateTimestamp: Long) {
        val history = khatamDao.getAllKhatamPlans().firstOrNull() ?: return
        val target = history.find { it.id == planId } ?: return
        val updated = target.copy(
            targetEndDateTimestamp = newTargetDateTimestamp,
            updatedTimestamp = System.currentTimeMillis(),
            isSynced = false
        )
        khatamDao.insertOrUpdateKhatamPlan(updated)
    }

    override suspend fun resetKhatamPlan(planId: String) {
        val history = khatamDao.getAllKhatamPlans().firstOrNull() ?: return
        val target = history.find { it.id == planId } ?: return
        val updated = target.copy(
            lastReadSurahNumber = 1,
            lastReadAyahNumber = 1,
            lastReadGlobalAyahNumber = 1,
            completedAyahsCount = 0,
            status = "IN_PROGRESS",
            completedTimestamp = null,
            updatedTimestamp = System.currentTimeMillis(),
            isSynced = false
        )
        khatamDao.insertOrUpdateKhatamPlan(updated)
    }

    override suspend fun endKhatamPlan(planId: String) {
        khatamDao.endKhatamPlan(planId)
    }

    private fun KhatamPlanEntity.toModel(): KhatamPlanModel {
        val parsedStatus = try {
            KhatamStatus.valueOf(this.status)
        } catch (_: Exception) {
            KhatamStatus.IN_PROGRESS
        }
        return KhatamPlanModel(
            id = this.id,
            title = this.title,
            startDateTimestamp = this.startDateTimestamp,
            targetEndDateTimestamp = this.targetEndDateTimestamp,
            lastReadSurahNumber = this.lastReadSurahNumber,
            lastReadAyahNumber = this.lastReadAyahNumber,
            lastReadGlobalAyahNumber = this.lastReadGlobalAyahNumber,
            completedAyahsCount = this.completedAyahsCount,
            status = parsedStatus,
            completedTimestamp = this.completedTimestamp,
            updatedTimestamp = this.updatedTimestamp,
        )
    }

    override suspend fun clearLocalQuranKhatam() {
        khatamDao.deleteAllPlans()
    }
}
