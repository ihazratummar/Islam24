package com.hazrat.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazrat.database.entity.DuaCategoryEntity
import com.hazrat.database.entity.DuaItemEntity
import com.hazrat.database.entity.RecentDuaEntity
import kotlinx.coroutines.flow.Flow

data class DuaChapterWithCountEntity(
    val id: Int,
    val title: String,
    val bnTitle: String? = null,
    val audioUrl: String?,
    val duaCount: Int
)

/**
 * @author hazratummar
 * Created on 27/01/26
 */
@Dao
interface DuaDao {

    @Query("SELECT * FROM dua_category ORDER BY id ASC")
    fun getDuaCategory(): Flow<List<DuaCategoryEntity>>

    @Query("SELECT * FROM dua_item WHERE categoryId = :categoryId")
    fun getDua(categoryId: Int): Flow<List<DuaItemEntity>>

    @Query("""
        SELECT * FROM dua_category
        WHERE title LIKE '%' || :title || '%' OR (bnTitle IS NOT NULL AND bnTitle LIKE '%' || :title || '%')
    """)
    fun searchDuaCategory(title: String): Flow<List<DuaCategoryEntity>>

    @Query("""
        SELECT c.id, c.title, c.bnTitle, c.audioUrl, COUNT(i.id) AS duaCount 
        FROM dua_category c 
        LEFT JOIN dua_item i ON c.id = i.categoryId 
        WHERE c.id IN (:chapterIds)
        GROUP BY c.id 
        ORDER BY c.id ASC
    """)
    fun getChaptersWithDuaCount(chapterIds: List<Int>): Flow<List<DuaChapterWithCountEntity>>

    @Query("""
        SELECT c.id, c.title, c.bnTitle, c.audioUrl, COUNT(i.id) AS duaCount 
        FROM dua_category c 
        LEFT JOIN dua_item i ON c.id = i.categoryId 
        GROUP BY c.id 
        ORDER BY c.id ASC
    """)
    fun getAllChaptersWithDuaCount(): Flow<List<DuaChapterWithCountEntity>>

    @Query("""
        SELECT c.id, c.title, c.bnTitle, c.audioUrl, COUNT(i.id) AS duaCount 
        FROM dua_category c 
        LEFT JOIN dua_item i ON c.id = i.categoryId 
        WHERE c.title LIKE '%' || :query || '%' OR (c.bnTitle IS NOT NULL AND c.bnTitle LIKE '%' || :query || '%')
        GROUP BY c.id 
        ORDER BY c.id ASC
    """)
    fun searchChaptersWithDuaCount(query: String): Flow<List<DuaChapterWithCountEntity>>

    @Query("SELECT * FROM dua_item WHERE isBookmarked = 1")
    fun getBookmarkedDuas(): Flow<List<DuaItemEntity>>

    @Query("UPDATE dua_item SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: Int, isBookmarked: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentDua(recent: RecentDuaEntity)

    @Query("SELECT * FROM recent_dua ORDER BY timestamp DESC")
    fun getRecentDuas(): Flow<List<RecentDuaEntity>>

    @Query("DELETE FROM recent_dua WHERE chapterId = :chapterId")
    suspend fun deleteRecentDua(chapterId: Int)
}