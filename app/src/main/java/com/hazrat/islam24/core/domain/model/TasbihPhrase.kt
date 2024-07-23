package com.hazrat.islam24.core.domain.model

data class TasbihPhrase(
    val arText: String,
    val enText: String
)

val tasbihPhraseList = listOf(
    com.hazrat.islam24.core.domain.model.TasbihPhrase(
        arText = "سُبْحَانَ الله",
        enText = "SubhanAllah"
    ),
    com.hazrat.islam24.core.domain.model.TasbihPhrase(
        arText = "الْحَمْدُ لِلَّهِ",
        enText = "Alhamdulillah"
    ),
    com.hazrat.islam24.core.domain.model.TasbihPhrase(
        arText = "الله أكبر",
        enText = "Allahu Akbar"
    ),
    com.hazrat.islam24.core.domain.model.TasbihPhrase(
        arText = "لَا إِلٰهَ إِلَّا اللهُ",
        enText = "La ilaha illallah"
    ),
    com.hazrat.islam24.core.domain.model.TasbihPhrase(
        arText = "ٱلْرَّحْمَـانُ",
        enText = "Ar Rahman"
    ),
    com.hazrat.islam24.core.domain.model.TasbihPhrase(
        arText = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِالله",
        enText = "La hawla wa la quwwata illa billah "
    ),
    com.hazrat.islam24.core.domain.model.TasbihPhrase(
        arText = "سُبْحَانَ ٱللَّهِ خَالِقِ كُلِّ شَيْءٍ",
        enText = "subhana allahi khaliqi kulli shay'in"
    )
)
