package com.hazrat.athkar.mapper

import com.hazrat.database.entity.AthkarDataEntity
import com.hazrat.remote.dto.AthkarDataDto
import com.hazrat.remote.dto.AthkarDto


/**
 * @author hazratummar
 * Created on 25/01/26
 */

fun AthkarDataDto.toEntity() : List<AthkarDataEntity> {
    return morning.map { item ->
        AthkarDataEntity(
            number = item.number,
            type = "morning",
            bismillah = item.bismillah,
            arabicText = item.arabicText,
            enTranslation = item.enTranslation,
            enTransliteration = item.enTransliteration,
            bnTranslation = item.bnTranslation,
            bnTransliteration = item.bnTransliteration,
            count = item.count
        )
    }
}

