package com.hazrat.islam24.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "names")
data class NameEntity(
    @PrimaryKey
    val number: Int,
    val enDesc: String,
    val enMeaning:String,
    val found:String,
    val name: String,
    val transliteration: String,
    val bnTransliteration: String,
    val bnMeaning: String,
    val bnDec: String?
)
