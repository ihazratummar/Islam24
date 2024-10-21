package com.hazrat.islam24.auth.presentation.profileScreen

import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.navigation.Login
import com.hazrat.islam24.auth.navigation.ProfileDetailsScreen
import com.hazrat.islam24.auth.navigation.ProfileSettingScreen
import com.hazrat.islam24.auth.presentation.profileScreen.component.RatingBottomSheet
import com.hazrat.islam24.auth.presentation.profileScreen.component.profileCardShimmerEffect
import com.hazrat.islam24.main.mainActivity.MainActivity
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@Composable
fun ProfileScreen(
    navController: NavController,
    authState: AuthState,
    profileEvent: (ProfileEvent) -> Unit,
    profileState: ProfileState
) {
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
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            ProfileHeader(
                navController = navController,
                state = authState,
                profileState = profileState,
            )
            ProfileComponent(
                navController = navController,
                profileEvent = profileEvent
            )
        }

        if (profileState.isRatingDialogOpen) {
            RatingBottomSheet(profileEvent)
        }
    }
}


@Composable
private fun ProfileComponent(
    navController: NavController,
    profileEvent: (ProfileEvent) -> Unit
) {
    val activity: Activity = LocalContext.current as MainActivity
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ProfileScreenItemCard(
                painter = painterResource(id = R.drawable.settings),
                text = stringResource(id = R.string.setting),
                onClick = {
                    navController.navigate(ProfileSettingScreen) {
                        popUpTo(ProfileSettingScreen) {
                            inclusive = true
                            saveState = false
                        }
                        launchSingleTop = true
                    }
                },
                trailingIcon = painterResource(id = R.drawable.chevron_right)
            )

            ProfileScreenItemCard(
                painter = painterResource(id = R.drawable.like),
                text = stringResource(id = R.string.rate),
                onClick = { profileEvent(ProfileEvent.RateUs(activity)) },
                trailingIcon = painterResource(id = R.drawable.chevron_right)
            )
            ProfileScreenItemCard(
                painter = painterResource(id = R.drawable.share),
                text = stringResource(id = R.string.invite_a_friend),
                onClick = {
                    profileEvent(ProfileEvent.InviteFriend)
                },
                trailingIcon = painterResource(id = R.drawable.chevron_right)
            )
        }
        item {
            val versionName = context.packageName?.let {
                context.packageManager.getPackageInfo(it, PackageManager.GET_META_DATA).versionName
            }
            Text(text = "$versionName")
        }
    }
}

@Composable
private fun ProfileScreenItemCard(
    painter: Painter,
    text: String,
    onClick: () -> Unit = {},
    trailingIcon: Painter
) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = dimens.size5),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                Spacer(modifier = Modifier.weight(1f))
                Icon(painter = trailingIcon, contentDescription = "trailingIcon")
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    navController: NavController,
    state: AuthState,
    profileState: ProfileState,
) {
    when (state) {
        is AuthState.Authenticated -> {
            val imageUri = profileState.userData?.profilePictureUrl
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build()
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(ProfileDetailsScreen) {
                            popUpTo(ProfileDetailsScreen) {
                                inclusive = true
                                saveState = true
                            }
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.size20),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (painter.state) {
                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(horizontal = dimens.size10)
                                    .size(dimens.size100)
                                    .clip(CircleShape)
                                    .wrapContentSize()
                            )
                        }
                        is AsyncImagePainter.State.Success -> {
                            Image(
                                painter = painter,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .padding(horizontal = dimens.size10)
                                    .size(dimens.size100)
                                    .clip(CircleShape)
                                    .wrapContentSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        else -> {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "error"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(dimens.size10))
                    Text(
                        text = "Saalam, ${profileState.userData?.fullName ?: "User"}",
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        is AuthState.Loading -> {
            ProfileCard(
                text = "Saalam, ${profileState.userData?.fullName ?: "User"}",
                onClick = {},
                modifier = Modifier.profileCardShimmerEffect()
            )
        }

        is AuthState.Unauthenticated -> {
            ProfileCard(
                text = stringResource(R.string.login),
                onClick = { navController.navigate(Login) },
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        }

        else -> Unit
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    containerColor: Color = Color.Transparent
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.size10),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = dimens.size10)
                    .size(dimens.size60)
                    .clip(CircleShape),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "error",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.width(dimens.size10))
            Text(
                text = text,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.arrowright),
                contentDescription = "ArrowRight",
                modifier = Modifier.weight(0.1f)
            )
        }
    }
}