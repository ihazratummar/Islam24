package com.hazrat.alQuran.data.mapper

import com.hazrat.database.entity.quran.FavoriteAyahEntity
import com.hazrat.model.al_quran_model.FavoriteAyah


/**
 * @author hazratummar
 * Created on 27/01/26
 */

fun List<FavoriteAyahEntity>.toModel() : List<FavoriteAyah> {
    return this.map { item ->
        FavoriteAyah(
            surahNumber = item.surahNumber,
            ayahNumber = item.ayahNumber
        )
    }
}