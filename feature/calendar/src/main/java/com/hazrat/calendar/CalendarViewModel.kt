package com.hazrat.calendar

import androidx.lifecycle.ViewModel
import com.hazrat.usecase.DarkModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */


@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val darkModeUseCase: DarkModeUseCase
): ViewModel() {




}