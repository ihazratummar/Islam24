package com.hazrat.home.ui.component

import androidx.compose.ui.graphics.Color
import com.hazrat.model.EventType
import com.hazrat.ui.R


/**
 * @author hazratummar
 * Created on 16/05/26
 */

fun EventType.color() : Color {
    return when(this){
        EventType.SPECIAL -> Color(0xFFf59e0c)
        EventType.NIGHT_PRAYER -> Color(0xFF6366f1)
        EventType.URS -> Color(0xFF7c3bed)
        EventType.BIRTHDAY -> Color(0xFF3e63eb)
        EventType.HAJJ -> Color(0xFF92400e)
        else -> Color(0xFF6366f1)
    }
}

fun EventType.containerColor() : Color {
    return when(this){
        EventType.SPECIAL -> Color(0xFF1c1033)
        EventType.NIGHT_PRAYER -> Color(0xFF060d24)
        EventType.URS -> Color(0xFF160826)
        EventType.BIRTHDAY -> Color(0xFF0d1b3e)
        EventType.HAJJ -> Color(0xFF1a0f00)
        EventType.JUMMA -> Color(0xFF14213d)
        else -> Color(0xFF14213d)
    }
}

fun EventType.icon() : Int {
    return when(this){
        EventType.SPECIAL -> R.drawable.special
        EventType.NIGHT_PRAYER ->R.drawable.night_prayer
        EventType.URS -> R.drawable.urs
        EventType.BIRTHDAY -> R.drawable.birthday
        EventType.HAJJ -> R.drawable.hajj
        EventType.JUMMA -> R.drawable.jummah
        else -> R.drawable.special
    }
}