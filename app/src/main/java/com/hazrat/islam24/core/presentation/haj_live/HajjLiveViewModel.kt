package com.hazrat.islam24.core.presentation.haj_live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.islam24.core.domain.model.hajjlive.HajjLiveYoutubeModel
import com.hazrat.islam24.core.domain.repository.HajjLiveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-03-2025
 */

@HiltViewModel
class HajjLiveViewModel @Inject constructor(
    private val hajjLiveRepository: HajjLiveRepository
) : ViewModel(){

    var hajjLiveYoutubeModel = MutableStateFlow(HajjLiveYoutubeModel("", emptyList(), ""))
        private set


    init {
        viewModelScope.launch{
            hajjLiveYoutubeModel.value = hajjLiveRepository.getHajjLiveVideoId()
        }
    }

}