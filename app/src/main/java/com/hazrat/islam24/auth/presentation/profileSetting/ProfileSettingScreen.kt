package com.hazrat.islam24.auth.presentation.profileSetting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.presentation.AuthEvent

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: AuthState,
    authEvent: (AuthEvent) -> Unit,
) {
    LaunchedEffect(Unit) {
        authEvent(AuthEvent.Refresh)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                        authEvent(AuthEvent.Refresh)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                if (state == AuthState.Authenticated) {
                    authEvent(AuthEvent.SignOut)
                    authEvent(AuthEvent.Refresh)
                } else {
                    navController.popBackStack()
                    authEvent(AuthEvent.Refresh)
                }
            }) {
                if (state == AuthState.Authenticated) {
                    Text(text = "Logout")
                } else {
                    Text(text = "Login")
                }
            }
        }
    }

}