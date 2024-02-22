package com.hazrat.islam24.domain.model

data class TasbihPhrase(
    val arText: String,
    val enText: String
)

val tasbihPhraseList = listOf(
    TasbihPhrase(
        arText = "سُبْحَانَ الله",
        enText = "SubhanAllah"
    ),
    TasbihPhrase(
        arText = "الْحَمْدُ لِلَّهِ",
        enText = "Alhamdulillah"
    ),
    TasbihPhrase(
        arText = "الله أكبر",
        enText = "Allahu Akbar"
    ),
    TasbihPhrase(
        arText = "لَا إِلٰهَ إِلَّا اللهُ",
        enText = "La ilaha illallah"
    ),
    TasbihPhrase(
        arText = "ٱلْرَّحْمَـانُ",
        enText = "Ar Rahman"
    ),
    TasbihPhrase(
        arText = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِالله",
        enText = "La hawla wa la quwwata illa billah "
    )
)
