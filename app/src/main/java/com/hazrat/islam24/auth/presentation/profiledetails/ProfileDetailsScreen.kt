package com.hazrat.islam24.auth.presentation.profiledetails

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.model.UserData
import com.hazrat.islam24.auth.presentation.component.CustomTextField
import com.hazrat.islam24.auth.presentation.profileScreen.ProfileState
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.Dimens
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    profileState: ProfileState,
    profileDetailsEvent: (ProfileDetailsEvent) -> Unit,
    userEvent: UserEvent?
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    LaunchedEffect(userEvent) {
        userEvent?.let {
            when (it) {
                is UserEvent.Error -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = it.error.asString(context),
                            duration = SnackbarDuration.Short,
                            withDismissAction = true
                        )
                    }
                }

                is UserEvent.Success -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = it.success.asString(context),
                            duration = SnackbarDuration.Short,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }
    val containerColor = if (userEvent is UserEvent.Error) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
    val contextColor = if (userEvent is UserEvent.Error) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                Snackbar(
                    modifier = Modifier,
                    snackbarData = data,
                    containerColor = containerColor,
                    contentColor = contextColor,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium,
                    actionOnNewLine = false,
                    dismissActionContentColor = contextColor
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceDim),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.backicon),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.surfaceDim
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            item {
                ProfilePicture(profileState, profileDetailsEvent, dimens)
                Spacer(modifier = Modifier.height(dimens.size50))
            }
            item {
                ProfileDataCards(
                    onClick = {
                        profileDetailsEvent(ProfileDetailsEvent.NameUpdateDialog)
                    },
                    label = stringResource(id = R.string.name),
                    value = if (profileState.userData?.fullName == null) "Not Set" else profileState.userData.fullName
                )
                ProfileDataCards(
                    onClick = {},
                    label = stringResource(id = R.string.emal),
                    value = if (profileState.userData?.email == null) "Not Set" else profileState.userData.email
                )
                ProfileDataCards(
                    onClick = { profileDetailsEvent(ProfileDetailsEvent.BioUpdateDialog) },
                    label = stringResource(id = R.string.bio),
                    value = if (profileState.userData?.bio == null) "Not Set" else profileState.userData.bio
                )
            }
        }
        if (profileState.isNameDialogOpen) {
            UpdateDataDetails(
                label = "Change name",
                value = profileState.userData?.fullName ?: "",
                onValueChange = { profileDetailsEvent(ProfileDetailsEvent.NameValue(it)) },
                onClick = {
                    profileDetailsEvent(
                        ProfileDetailsEvent.UpdateName(
                            UserData(
                                fullName = profileState.userData?.fullName ?: "",
                            )
                        )
                    )
                    profileDetailsEvent(ProfileDetailsEvent.NameUpdateDialog)
                },
                onDismiss = { profileDetailsEvent(ProfileDetailsEvent.NameUpdateDialog) }
            )
        }
        if (profileState.isBioDialogOpen) {
            UpdateDataDetails(
                label = "Change bio",
                value = profileState.userData?.bio ?: "",
                onValueChange = { profileDetailsEvent(ProfileDetailsEvent.NewBio(it)) },
                onClick = {
                    profileDetailsEvent(
                        ProfileDetailsEvent.UpdateBio(
                            UserData(
                                bio = profileState.userData?.bio ?: ""
                            )
                        )
                    )
                    profileDetailsEvent(ProfileDetailsEvent.BioUpdateDialog)
                },
                onDismiss = {
                    profileDetailsEvent(ProfileDetailsEvent.BioUpdateDialog)
                }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UpdateDataDetails(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ModalBottomSheet(
            modifier = Modifier.imePadding(),
            onDismissRequest = {
                onDismiss()
            },
            containerColor = MaterialTheme.colorScheme.surfaceDim
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimens.size10)
            ) {
                Text(
                    text = label,
                    modifier = Modifier
                        .padding(dimens.size15)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(dimens.size20))
                CustomTextField(
                    value = value,
                    onValueChange = { newValue ->
                        onValueChange(newValue)
                    }
                )
                Spacer(modifier = Modifier.height(dimens.size30))
                ButtonClick(
                    onClick = onClick
                )
                Spacer(modifier = Modifier.height(dimens.size30))
            }
        }
    }
}

@Composable
private fun ButtonClick(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.size60),
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = RoundedCornerShape(dimens.size10)
    ) {
        Text(text = "Save")
    }
}

@Composable
private fun ProfileDataCards(
    onClick: () -> Unit,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier
            .padding(dimens.size6)
            .fillMaxWidth()
            .clickable {
                onClick()

            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = dimens.size20, vertical = dimens.size15)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.height(dimens.size10))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Icon(
                modifier = Modifier
                    .weight(0.1f)
                    .padding(end = dimens.size10),
                painter = painterResource(id = R.drawable.arrowright),
                contentDescription = "ArrowRight"
            )
        }
    }
}

@Composable
private fun ProfilePicture(
    profileState: ProfileState,
    profileDetailsEvent: (ProfileDetailsEvent) -> Unit,
    dimens: Dimens
) {
    val imageUri = remember { mutableStateOf(profileState.userData?.profilePictureUrl) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUri.value ?: profileState.userData?.profilePictureUrl)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build()

    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri.value = it.toString()
            profileDetailsEvent(ProfileDetailsEvent.UpdateProfilePicture(uri = it))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier
                .width(dimens.size150)
                .height(dimens.size150)
                .clickable {
                    launcher.launch("image/*")
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),
            shape = CircleShape,
            border = BorderStroke(dimens.size5, color = MaterialTheme.colorScheme.primary)
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    CircularProgressIndicator()
                }

                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .padding(dimens.size6)
                            .clip(CircleShape),
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
        }
    }
}