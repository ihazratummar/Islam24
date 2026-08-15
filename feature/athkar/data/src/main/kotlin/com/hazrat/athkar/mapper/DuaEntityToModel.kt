package com.hazrat.athkar.mapper

import com.hazrat.database.dao.DuaChapterWithCountEntity
import com.hazrat.database.entity.DuaCategoryEntity
import com.hazrat.database.entity.DuaItemEntity
import com.hazrat.database.entity.RecentDuaEntity
import com.hazrat.model.DuaCategoryModel
import com.hazrat.model.DuaChapterWithCountModel
import com.hazrat.model.DuaItemModel
import com.hazrat.model.RecentReadDua

fun DuaCategoryEntity.toCategoryModel(): DuaCategoryModel {
    return DuaCategoryModel(
        id = id,
        title = title
    )
}

fun List<DuaCategoryEntity>.toCategoryList(): List<DuaCategoryModel> {
    return this.map { it.toCategoryModel() }
}

fun DuaChapterWithCountEntity.toChapterWithCountModel(): DuaChapterWithCountModel {
    return DuaChapterWithCountModel(
        id = id,
        title = title,
        audioUrl = audioUrl,
        duaCount = duaCount
    )
}

fun List<DuaChapterWithCountEntity>.toChapterWithCountList(): List<DuaChapterWithCountModel> {
    return this.map { it.toChapterWithCountModel() }
}

fun DuaItemEntity.toItemModel(): DuaItemModel {
    return DuaItemModel(
        id = id,
        categoryId = categoryId,
        arabicText = arabicText,
        translation = translation,
        transliteration = transliteration,
        reference = reference,
        repeatCount = repeatCount,
        audioUrl = audioUrl,
        isBookmarked = isBookmarked == 1
    )
}

fun List<DuaItemEntity>.toItemListModel(): List<DuaItemModel> {
    return this.map { it.toItemModel() }
}

fun RecentDuaEntity.toRecentReadDua(): RecentReadDua {
    return RecentReadDua(
        chapterId = chapterId,
        title = title,
        duaCount = duaCount,
        formattedDate = formattedDate,
        timestamp = timestamp
    )
}

fun List<RecentDuaEntity>.toRecentReadDuaList(): List<RecentReadDua> {
    return this.map { it.toRecentReadDua() }
}