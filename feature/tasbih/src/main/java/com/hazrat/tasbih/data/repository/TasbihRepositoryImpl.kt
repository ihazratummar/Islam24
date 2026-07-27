package com.hazrat.tasbih.data.repository

import com.hazrat.database.dao.TasbihDao
import com.hazrat.database.entity.TasbihEntity
import com.hazrat.database.entity.TasbihLogEntity
import com.hazrat.tasbih.domain.model.Tasbih
import com.hazrat.tasbih.domain.repository.TasbihRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TasbihRepositoryImpl(
    private val tasbihDao: TasbihDao
) : TasbihRepository {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private fun getTodayDateString(): String = dateFormat.format(Date())

    override fun getAllTasbihs(): Flow<List<Tasbih>> {
        return tasbihDao.getAllTasbihs().map { entities ->
            if (entities.isEmpty()) {
                // Seed default Tasbihs
                seedDefaultTasbihs()
            }
            entities.map { it.toDomain() }
        }
    }

    override fun getTasbihById(id: Int): Flow<Tasbih?> {
        return tasbihDao.getTasbihById(id).map { it?.toDomain() }
    }

    override fun getTodayTotalDhikr(dateString: String): Flow<Int> {
        return tasbihDao.getTodayTotalCount(dateString).map { it ?: 0 }
    }

    override suspend fun incrementCount(id: Int, currentCount: Int, lifetimeCount: Int) {
        val newCurrent = currentCount + 1
        val newLifetime = lifetimeCount + 1
        val timestamp = System.currentTimeMillis()
        tasbihDao.updateCount(id, newCurrent, newLifetime, timestamp)
        tasbihDao.insertLog(
            TasbihLogEntity(
                tasbihId = id,
                countAdded = 1,
                dateString = getTodayDateString(),
                timestamp = timestamp
            )
        )
    }

    override suspend fun undoCount(id: Int, currentCount: Int, lifetimeCount: Int) {
        if (currentCount <= 0) return
        val newCurrent = currentCount - 1
        val newLifetime = if (lifetimeCount > 0) lifetimeCount - 1 else 0
        val timestamp = System.currentTimeMillis()
        tasbihDao.updateCount(id, newCurrent, newLifetime, timestamp)
        tasbihDao.insertLog(
            TasbihLogEntity(
                tasbihId = id,
                countAdded = -1,
                dateString = getTodayDateString(),
                timestamp = timestamp
            )
        )
    }

    override suspend fun resetCount(id: Int) {
        tasbihDao.resetCount(id)
    }

    override suspend fun toggleFavorite(id: Int, isFavorite: Boolean) {
        tasbihDao.toggleFavorite(id, isFavorite)
    }

    override suspend fun addCustomTasbih(tasbih: Tasbih): Long {
        return tasbihDao.insertTasbih(tasbih.toEntity(isCustom = true))
    }

    override suspend fun deleteCustomTasbih(id: Int) {
        tasbihDao.deleteCustomTasbih(id)
    }

    private suspend fun seedDefaultTasbihs() {
        val defaults = listOf(
            TasbihEntity(
                arabicText = "سُبْحَانَ اللَّهِ",
                transliteration = "SubhanAllah",
                translatedName = "Glory be to Allah",
                defaultTarget = 33
            ),
            TasbihEntity(
                arabicText = "الْحَمْدُ لِلَّهِ",
                transliteration = "Alhamdulillah",
                translatedName = "Praise be to Allah",
                defaultTarget = 33
            ),
            TasbihEntity(
                arabicText = "اللَّهُ أَكْبَرُ",
                transliteration = "Allahu Akbar",
                translatedName = "Allah is the Greatest",
                defaultTarget = 33
            ),
            TasbihEntity(
                arabicText = "أَسْتَغْفِرُ اللَّهَ",
                transliteration = "Astaghfirullah",
                translatedName = "I seek forgiveness from Allah",
                defaultTarget = 100
            ),
            TasbihEntity(
                arabicText = "لَا إِلَهَ إِلَّا اللَّهُ",
                transliteration = "La ilaha illallah",
                translatedName = "There is no god but Allah",
                defaultTarget = 100
            ),
            TasbihEntity(
                arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
                transliteration = "SubhanAllah wa Bihamdihi",
                translatedName = "Glory be to Allah and His Praise",
                defaultTarget = 100
            )
        )
        tasbihDao.insertAll(defaults)
    }

    private fun TasbihEntity.toDomain() = Tasbih(
        id = id,
        arabicText = arabicText,
        transliteration = transliteration,
        translatedName = translatedName,
        defaultTarget = defaultTarget,
        currentCount = currentCount,
        totalLifetimeCount = totalLifetimeCount,
        isFavorite = isFavorite,
        isCustom = isCustom,
        lastUpdatedTimestamp = lastUpdatedTimestamp
    )

    private fun Tasbih.toEntity(isCustom: Boolean = false) = TasbihEntity(
        id = id,
        arabicText = arabicText,
        transliteration = transliteration,
        translatedName = translatedName,
        defaultTarget = defaultTarget,
        currentCount = currentCount,
        totalLifetimeCount = totalLifetimeCount,
        isFavorite = isFavorite,
        isCustom = isCustom,
        lastUpdatedTimestamp = lastUpdatedTimestamp
    )
}
