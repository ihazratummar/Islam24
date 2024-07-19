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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.presentation.AuthEvent
import com.hazrat.islam24.main.navigation.nvgraph.Route
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun ProfileScreen(
    navController: NavController,
    state: AuthState,
    authEvent: (AuthEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        authEvent(AuthEvent.Refresh)
    }
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .padding(
                top = MaterialTheme.dimens.size40,
                start = MaterialTheme.dimens.size20,
                end = MaterialTheme.dimens.size10
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ProfileHeader(navController = navController, state = state)
        ProfileComponent()
    }
}

@Composable
private fun ProfileComponent() {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size15),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.like),
                        contentDescription = "Rate us",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.size30))
                    Text(
                        text = "Rate us",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size20))
                HorizontalDivider(
                    thickness = MaterialTheme.dimens.size1,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.size15),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "Invite Your Friend",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.size30))
                    Text(
                        text = "Invite Friends",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.size20))
                HorizontalDivider(
                    thickness = MaterialTheme.dimens.size1,
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
                if (state == AuthState.Unauthenticated){
                    navController.navigate(Route.LoginScreen.route)
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
                            MaterialTheme.dimens.size10
                        )
                        .size(MaterialTheme.dimens.size60)
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
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.size15))
                when (state) {
                    AuthState.Unauthenticated -> {
                        Text(
                            text = "Login",
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
        IconButton(onClick = {
            navController.navigate(Route.ProfileSettingScreen.route)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}