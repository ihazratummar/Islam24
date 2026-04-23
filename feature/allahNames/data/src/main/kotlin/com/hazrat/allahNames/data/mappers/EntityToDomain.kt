package com.hazrat.allahNames.data.mappers

import com.hazrat.allahNames.model.namesofallah.NameOfAllahData
import com.hazrat.database.entity.AllahNameEntity


/**
 * @author hazratummar
 * Created on 25/01/26
 */
 

fun AllahNameEntity.toDomain() : NameOfAllahData {
    return NameOfAllahData(
        number = number,
        enDesc = enDesc,
        enMeaning = enMeaning,
        found = found,
        name = name,
        transliteration = transliteration,
        bnTransliteration = bnTransliteration,
        bnMeaning = bnMeaning,
        bnDec = bnDec
    )
}

fun List<AllahNameEntity>.toDomainList() : List<NameOfAllahData> {
    return map { it.toDomain() }
}