package com.hazrat.islam24.auth.presentation.profileScreen

import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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
import com.hazrat.islam24.core.presentation.home.component.shimmerEffect
import com.hazrat.islam24.main.mainActivity.MainActivity
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.getCacheProfilePicture
import com.hazrat.islam24.util.toUri

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimens.size10)
            .statusBarsPadding(),
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProfileScreenItemCard(
    painter: Painter,
    text: String,
    onClick: () -> Unit = {},
    trailingIcon: Painter
) {
    Card(
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
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
            val context = LocalContext.current
            val cacheFile = getCacheProfilePicture(context = context)
            val imageUri = remember { mutableStateOf(cacheFile?.toUri()?.toString() ) }
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(imageUri.value )
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build()

            )
            MainTopProfileCard(
                painter = painter,
                text = { Text("Saalam, ${profileState.userData?.fullName ?: "User"}") },
                onClick = {
                    navController.navigate(ProfileDetailsScreen) {
                        popUpTo(ProfileDetailsScreen) {
                            inclusive = true
                            saveState = true
                        }
                    }
                },
                isLoggedIn = true
            )

        }

        is AuthState.Loading -> {
            MainTopProfileCard(
                isLoading = true,
                text = {}
            )
        }

        is AuthState.Unauthenticated -> {
            MainTopProfileCard(
                painter = null,
                text = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle()) {
                                append("Tap to, ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                append(stringResource(R.string.login))
                            }
                        },
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                onClick = {
                    navController.navigate(Login)
                }
            )

        }

        else -> Unit
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainTopProfileCard(
    modifier: Modifier = Modifier,
    painter: AsyncImagePainter? = null,
    text: @Composable () -> Unit,
    onClick: () -> Unit = {},
    isLoggedIn: Boolean = false,
    isLoading: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.size200)
            .combinedClickable(
                onClick = { onClick() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .let { if (isLoading) it.profileCardShimmerEffect() else it },
        colors = CardDefaults.cardColors(
            containerColor = if (!isLoading) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (painter?.state) {
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
                    Card(
                        modifier = Modifier
                            .size(dimens.size100),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                        shape = CircleShape,
                        border = BorderStroke(
                            dimens.size5,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                else -> {

                    if (!isLoggedIn) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(dimens.size20),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (!isLoading) {
                                    Icon(
                                        painter = painterResource(R.drawable.profile),
                                        contentDescription = "error",
                                        modifier = Modifier
                                            .padding(start = dimens.size10)
                                            .size(dimens.size80)
                                    )
                                    Spacer(Modifier.width(dimens.size20))
                                    text.invoke()
                                    Icon(
                                        painter = painterResource(R.drawable.chevron_right),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(dimens.size20)
                                    )
                                } else {
                                    Box(
                                        Modifier
                                            .padding(start = dimens.size10)
                                            .size(dimens.size80)
                                            .clip(CircleShape)
                                            .shimmerEffect()
                                    )
                                    Spacer(Modifier.width(dimens.size20))
                                    Box(
                                        modifier = Modifier
                                            .padding(dimens.size10)
                                            .width(dimens.size250)
                                            .size(dimens.size20)
                                            .shimmerEffect()
                                    )
                                }
                            }
                        }
                    }else{
                        Icon(
                            painter = painterResource(R.drawable.profile),
                            contentDescription = "error",
                            modifier = Modifier
                                .padding(start = dimens.size10)
                                .size(dimens.size80)
                        )
                    }
                }
            }
            if (isLoggedIn) {
                text()
            }
        }
    }
}