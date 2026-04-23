package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.quran.FavoriteAyahEntity
import kotlinx.coroutines.flow.Flow


/**
 * @author hazratummar
 * Created on 27/01/26
 */

@Dao
interface QuranDao {

    /// Favorite
    @Query("SELECT * FROM favorite_ayah ORDER BY createdAt DESC")
    fun getAllFavoriteAyah(): Flow<List<FavoriteAyahEntity>>

    @Query("""
        SELECT * FROM favorite_ayah 
        WHERE surahNumber = :surah AND ayahNumber = :ayah
        LIMIT 1
    """)
    suspend fun getFavorite(surah: Int, ayah: Int): FavoriteAyahEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteAyah(entity: FavoriteAyahEntity)

    @Query("DELETE FROM favorite_ayah WHERE surahNumber = :surah AND ayahNumber = :ayah")
    suspend fun deleteFavoriteAyah(surah: Int, ayah: Int)

}