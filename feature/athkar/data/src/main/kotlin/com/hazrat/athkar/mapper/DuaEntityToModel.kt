package com.hazrat.athkar.mapper

import com.hazrat.database.entity.DuaCategoryEntity
import com.hazrat.database.entity.DuaItemEntity
import com.hazrat.model.DuaCategoryModel
import com.hazrat.model.DuaItemModel


fun DuaCategoryEntity.toCategoryModel(): DuaCategoryModel {
    return DuaCategoryModel(
        id = id,
        title = title
    )
}

fun List<DuaCategoryEntity>.toCategoryList(): List<DuaCategoryModel> {
    return this.map { it.toCategoryModel() }
}

fun DuaItemEntity.toItemModel(): DuaItemModel {
    return DuaItemModel(
        id = id,
        categoryId = categoryId,
        arabicText = arabicText,
        translation = translation,
        reference = reference,
        repeatCount = repeatCount
    )
}

fun List<DuaItemEntity>.toItemListModel() : List<DuaItemModel> {
    return this.map { it.toItemModel() }
}