package com.hazrat.islam24.core.presentation.al_quran

/**
 * @author Hazrat Ummar Shaikh
 * Created on 13-12-2024
 */

sealed interface QuranEvent {

    data class SaveLastRead(val surahNumber: Int, val ayahNumber: Int) : QuranEvent

    data object Refresh : QuranEvent

}