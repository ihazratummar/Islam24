package com.hazrat.allahNames.data.mappers

import com.hazrat.allahNames.model.namesofallah.NameOfAllahData
import com.hazrat.database.entity.AllahNameEntity
import com.hazrat.remote.dto.AllahNamesDto


/**
 * @author hazratummar
 * Created on 25/01/26
 */

fun AllahNamesDto.toDomain() : List<NameOfAllahData> {
    return data.map { item ->
        NameOfAllahData(
            number = item.number,
            enDesc = item.en.desc,
            enMeaning = item.en.meaning,
            found = item.found,
            name = item.name,
            transliteration = item.transliteration,
            bnTransliteration = item.bntransliteration,
            bnMeaning = item.bn.meaning,
            bnDec = item.bn.desc
        )
    }
}