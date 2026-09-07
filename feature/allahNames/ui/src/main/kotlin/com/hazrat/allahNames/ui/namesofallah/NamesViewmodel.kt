package com.hazrat.allahNames.ui.namesofallah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.allahNames.model.namesofallah.NameOfAllahData
import com.hazrat.allahNames.repository.NamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class NameTab {
    ALL, FAVORITES
}

/**
 * @author Hazrat Ummar Shaikh
 */
class NamesViewmodel(
    private val namesRepository: NamesRepository
) : ViewModel() {

    private val _names = MutableStateFlow<List<NameOfAllahData>>(emptyList())
    val names = _names.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTab = MutableStateFlow(NameTab.ALL)
    val selectedTab: StateFlow<NameTab> = _selectedTab.asStateFlow()

    private val _favoriteNames = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteNames: StateFlow<Set<Int>> = _favoriteNames.asStateFlow()

    val filteredNames: StateFlow<List<NameOfAllahData>> = combine(
        _names,
        _searchQuery,
        _selectedTab,
        _favoriteNames
    ) { names, query, tab, favorites ->
        names.filter { name ->
            val matchesTab = when (tab) {
                NameTab.ALL -> true
                NameTab.FAVORITES -> favorites.contains(name.number)
            }
            val matchesQuery = query.isBlank() ||
                    name.transliteration.contains(query, ignoreCase = true) ||
                    name.enMeaning.contains(query, ignoreCase = true) ||
                    name.bnMeaning.contains(query, ignoreCase = true) ||
                    name.bnTransliteration.contains(query, ignoreCase = true) ||
                    name.name.contains(query, ignoreCase = true) ||
                    name.number.toString() == query.trim()
            matchesTab && matchesQuery
        }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    init {
        loadNames()
    }

    private fun loadNames() {
        viewModelScope.launch(Dispatchers.IO) {
            val dbNames = namesRepository.getAllahNamesFromDatabase()
            if (dbNames.isNotEmpty()) {
                _names.value = dbNames
            } else {
                val apiNames = namesRepository.getAllahNamesFromApi()
                _names.value = apiNames
            }
        }
    }

    fun toggleFavorite(number: Int) {
        _favoriteNames.update { current ->
            if (current.contains(number)) current - number else current + number
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onTabSelected(tab: NameTab) {
        _selectedTab.value = tab
    }
}