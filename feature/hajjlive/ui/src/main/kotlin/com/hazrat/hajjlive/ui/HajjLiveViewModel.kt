package com.hazrat.hajjlive.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hazrat.model.hajjlive.HajjLiveYoutubeModel
import com.hazrat.domain.repository.HajjLiveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-03-2025
 */

class HajjLiveViewModel(
    private val hajjLiveRepository: HajjLiveRepository
) : ViewModel(){

    var hajjLiveYoutubeModel = MutableStateFlow(HajjLiveYoutubeModel())
        private set


    init {
        viewModelScope.launch{
            hajjLiveYoutubeModel.value = hajjLiveRepository.getHajjLiveVideoId()
        }
    }

}