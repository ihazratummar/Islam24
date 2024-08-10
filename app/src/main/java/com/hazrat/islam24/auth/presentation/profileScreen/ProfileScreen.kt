package com.hazrat.islam24.auth.presentation.profileScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.navigation.Login
import com.hazrat.islam24.auth.navigation.ProfileSettingScreen
import com.hazrat.islam24.auth.presentation.AuthEvent
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun ProfileScreen(
    navController: NavController,
    state: AuthState,
    authEvent: (AuthEvent) -> Unit,
    profileEvent: (ProfileEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        authEvent(AuthEvent.Refresh)
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(
                    top = dimens.size40,
                    start = dimens.size20,
                    end = dimens.size10
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ProfileHeader(navController = navController, state = state)
            ProfileComponent(
                navController = navController,
                profileEvent = profileEvent
            )
        }
    }
}

@Composable
private fun ProfileComponent(
    navController: NavController,
    profileEvent: (ProfileEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        SettingCard(
            painter = painterResource(id = R.drawable.settings),
            text = stringResource(id = R.string.setting),
            onClick = { navController.navigate(ProfileSettingScreen) }
        )
        SettingCard(
            painter = painterResource(id = R.drawable.like),
            text = stringResource(id = R.string.rate),
            onClick = {}
        )
        SettingCard(
            painter = painterResource(id = R.drawable.share),
            text = stringResource(id = R.string.invite_a_friend),
            onClick = {
                profileEvent(ProfileEvent.InviteFriend)
            }
        )

    }
}

@Composable
private fun SettingCard(
    painter: Painter,
    text: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = dimens.size5),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.size10)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimens.size10),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(horizontal = dimens.size10),
                    painter = painter,
                    contentDescription = "Rate us",
                    tint = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.width(dimens.size30))
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    navController: NavController,
    state: AuthState
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Box(
            modifier = Modifier.clickable {
                if (state == AuthState.Unauthenticated) {
                    navController.navigate(Login)
                }
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Card(
                    modifier = Modifier
                        .padding(
                            dimens.size10
                        )
                        .size(dimens.size60)
                        .fillMaxSize(),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile",
                        )
                    }
                }
                Spacer(modifier = Modifier.width(dimens.size15))
                when (state) {
                    AuthState.Unauthenticated -> {
                        Text(
                            text = stringResource(R.string.login),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    AuthState.Authenticated -> {
                        Text(
                            text = "Salam User",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    else -> {
                        Text(text = "")
                    }
                }
            }
        }

    }
}