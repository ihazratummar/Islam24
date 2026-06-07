package com.hazrat.athkar.mapper

import com.hazrat.athkar.domain.model.AthkarData
import com.hazrat.database.entity.AthkarDataEntity


/**
 * @author hazratummar
 * Created on 25/01/26
 */

fun AthkarDataEntity.toDomain() : AthkarData {
    return AthkarData(
        arabicText = arabicText,
        bismillah = bismillah,
        bnTranslation = bnTranslation,
        bnTransliteration = bnTransliteration,
        count = count,
        enTranslation = enTranslation,
        enTransliteration = enTransliteration,
        number = number
    )
}