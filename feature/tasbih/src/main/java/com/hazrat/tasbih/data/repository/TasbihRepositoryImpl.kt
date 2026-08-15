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
    private var hasCheckedDefaults = false

    private val defaultTasbihs = listOf(
        TasbihEntity(
            arabicText = "سُبْحَانَ اللَّهِ",
            transliteration = "SubhanAllah",
            translatedName = "Glory be to Allah",
            defaultTarget = 33
        ),
        TasbihEntity(
            arabicText = "الْحَمْدُ لِلَّهِ",
            transliteration = "Alhamdulillah",
            translatedName = "All praise is due to Allah",
            defaultTarget = 33
        ),
        TasbihEntity(
            arabicText = "اللَّهُ أَكْبَرُ",
            transliteration = "Allahu Akbar",
            translatedName = "Allah is the Greatest",
            defaultTarget = 34
        ),
        TasbihEntity(
            arabicText = "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ",
            transliteration = "Astaghfirullah wa Atubu Ilayh",
            translatedName = "I seek forgiveness of Allah and turn to Him in repentance",
            defaultTarget = 100
        ),
        TasbihEntity(
            arabicText = "لَا إِلَهَ إِلَّا اللَّهُ",
            transliteration = "La ilaha illallah",
            translatedName = "There is no deity worthy of worship except Allah",
            defaultTarget = 100
        ),
        TasbihEntity(
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            transliteration = "SubhanAllah wa Bihamdihi",
            translatedName = "Glory be to Allah and His is all Praise",
            defaultTarget = 100
        ),
        TasbihEntity(
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            transliteration = "SubhanAllahi wa Bihamdihi, SubhanAllahil Azeem",
            translatedName = "Two words beloved to the Most Merciful, heavy on the Scale",
            defaultTarget = 100
        ),
        TasbihEntity(
            arabicText = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            transliteration = "La Hawla Wala Quwwata Illa Billah",
            translatedName = "There is no power and no might except by Allah (Treasure of Jannah)",
            defaultTarget = 100
        ),
        TasbihEntity(
            arabicText = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ",
            transliteration = "Allahumma Salli 'Ala Muhammad",
            translatedName = "O Allah, send blessings upon Muhammad and the family of Muhammad",
            defaultTarget = 100
        ),
        TasbihEntity(
            arabicText = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "La Ilaha Illallahu Wahdahu La Sharika Lah",
            translatedName = "None has the right to be worshipped except Allah alone, without partner",
            defaultTarget = 100
        ),
        TasbihEntity(
            arabicText = "لَا إِلَهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
            transliteration = "La Ilaha Illa Anta Subhanaka Inni Kuntu Minaz-Zalimeen",
            translatedName = "Dua of Prophet Yunus: Relief from distress, depression and grief",
            defaultTarget = 100
        ),
        TasbihEntity(
            arabicText = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            transliteration = "Hasbunallahu wa Ni'mal Wakeel",
            translatedName = "Allah is Sufficient for us, and He is the Best Disposer of affairs",
            defaultTarget = 100
        ),
        TasbihEntity(
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ عَدَدَ خَلْقِهِ وَرِضَا نَفْسِهِ وَزِنَةَ عَرْشِهِ وَمِدَادَ كَلِمَاتِهِ",
            transliteration = "SubhanAllahi 'Adada Khalqih",
            translatedName = "Praise of Allah according to the number of His creation and weight of His Throne",
            defaultTarget = 33
        )
    )

    private fun getTodayDateString(): String = dateFormat.format(Date())

    override fun getAllTasbihs(): Flow<List<Tasbih>> {
        return tasbihDao.getAllTasbihs().map { entities ->
            if (!hasCheckedDefaults) {
                hasCheckedDefaults = true
                val existingTransliterations = entities.map { it.transliteration }.toSet()
                val missing = defaultTasbihs.filter { it.transliteration !in existingTransliterations }
                if (missing.isNotEmpty()) {
                    tasbihDao.insertAll(missing)
                }
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
