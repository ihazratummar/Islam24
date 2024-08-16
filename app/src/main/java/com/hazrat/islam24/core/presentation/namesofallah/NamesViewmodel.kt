package com.hazrat.islam24.core.presentation.namesofallah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.data.entity.NameEntity
import com.hazrat.islam24.core.domain.repository.NamesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class NamesViewmodel @Inject constructor(
    private val namesRepository: NamesRepository,
) : ViewModel() {

    /**
     * allah' names
     */
    private val _names = MutableStateFlow<List<NameEntity>>(emptyList())
    val names = _names.asStateFlow()


    init {
        viewModelScope.launch {
            _names.value = namesRepository.getAllahNamesFromDatabase()
        }
    }
}