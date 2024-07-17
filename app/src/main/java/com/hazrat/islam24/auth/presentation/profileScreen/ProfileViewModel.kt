package com.hazrat.islam24.auth.presentation.profileScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.main.navigation.nvgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author Hazrat Ummar Shaikh
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth : FirebaseAuth
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

}