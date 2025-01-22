package com.hazrat.islam24.auth.presentation.forgetPassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.presentation.component.ButtonLoading
import com.hazrat.islam24.auth.presentation.component.CustomTextField
import com.hazrat.islam24.auth.presentation.component.OtpInputField
import com.hazrat.islam24.ui.theme.dimens
import com.hazrat.islam24.util.hapticFeedbacks
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPassword(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    forgetPasswordState: ForgetPasswordState,
    forgetPasswordEvent: (ForgetPasswordEvent) -> Unit,
    channelEvent: ForgetPasswordChannelEvent?,
    navigateToLogin: () -> Unit,
    isHapticFeedback: Boolean = false
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(channelEvent) {
        channelEvent?.let {
            when (it) {
                is ForgetPasswordChannelEvent.Error -> {
                    coroutineScope.launch {
                        snackBarHostState.showSnackbar(
                            message = it.error.asString(context),
                            duration = SnackbarDuration.Short,
                            withDismissAction = true
                        )
                    }
                }

                is ForgetPasswordChannelEvent.Success -> {
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
    LaunchedEffect(forgetPasswordState.passwordUpdateSuccess) {
        if (forgetPasswordState.passwordUpdateSuccess) {
            delay(3000)
            navigateToLogin()
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val containerColor =
        if (channelEvent is ForgetPasswordChannelEvent.Error) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
    val contextColor =
        if (channelEvent is ForgetPasswordChannelEvent.Error) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                Snackbar(
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
                title = {
                    Text(
                        text = when (forgetPasswordState.currentStep) {
                            0 -> "Forget Password"
                            1 -> "Verification"
                            2 -> "New Password"
                            else -> ""
                        },
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.backicon),
                            contentDescription = "Back"
                        )
                    }
                },
                windowInsets = WindowInsets(top = dimens.size20),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        contentWindowInsets = WindowInsets(bottom = 0.dp)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimens.size20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxSize(),
                        visible = forgetPasswordState.currentStep == 0,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CustomTextField(
                                label = { Text(text = "Email") },
                                placeholder = { Text(text = "Enter Your Email") },
                                leadingIcon = {
                                    Icon(
                                        painterResource(id = R.drawable.email),
                                        contentDescription = null
                                    )
                                },
                                value = forgetPasswordState.email,
                                onValueChange = {
                                    forgetPasswordEvent(
                                        ForgetPasswordEvent.EnterEmail(
                                            it
                                        )
                                    )
                                },
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            )
                            Spacer(Modifier.height(dimens.size10))
                            Text(
                                text = "You may receive Email from us for security purpose",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(Modifier.height(dimens.size10))
                            if (forgetPasswordState.isLoading) {
                                ButtonLoading(
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        keyboardController?.hide()
                                        forgetPasswordEvent(
                                            ForgetPasswordEvent.SubmitEmail(
                                                forgetPasswordState.email
                                            )
                                        )
                                        hapticFeedbacks(
                                            isEnable = isHapticFeedback,
                                            hapticFeedback = hapticFeedback
                                        )
                                    },
                                    enabled = forgetPasswordState.email.isNotBlank() && forgetPasswordState.email.contains(
                                        "@"
                                    ),
                                    shape = RoundedCornerShape(dimens.size10)
                                ) {
                                    Text("Submit")
                                }
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = forgetPasswordState.currentStep == 1,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Enter Verification Code",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        OtpInputField(
                            otp = forgetPasswordState.otp,
                            onOtpChanged = { forgetPasswordEvent(ForgetPasswordEvent.EnterOtp(it)) }
                        )
                        Spacer(Modifier.height(dimens.size10))
                        if (forgetPasswordState.isLoading) {
                            ButtonLoading()
                        } else {
                            Button(
                                modifier = Modifier,
                                onClick = {
                                    forgetPasswordEvent(
                                        ForgetPasswordEvent.SubmitOtp(
                                            email = forgetPasswordState.email,
                                            otp = forgetPasswordState.otp
                                        )
                                    )
                                    hapticFeedbacks(
                                        isEnable = isHapticFeedback,
                                        hapticFeedback = hapticFeedback
                                    )
                                },
                                shape = RoundedCornerShape(dimens.size10),
                                enabled = forgetPasswordState.otp.isNotBlank() && forgetPasswordState.otp.length == 4
                            ) {
                                Text("Submit")
                            }
                        }
                        Spacer(Modifier.height(dimens.size4))
                        IconButton(
                            onClick = {
                                forgetPasswordEvent(ForgetPasswordEvent.StepBack)
                                hapticFeedbacks(
                                    isEnable = isHapticFeedback,
                                    hapticFeedback = hapticFeedback
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.backicon),
                                contentDescription = "Back"
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = forgetPasswordState.currentStep == 2,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CustomTextField(
                            label = { Text(text = "New Password") },
                            placeholder = { Text(text = "Enter New Password") },
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.password),
                                    contentDescription = null
                                )
                            },
                            value = forgetPasswordState.password,
                            onValueChange = {
                                forgetPasswordEvent(
                                    ForgetPasswordEvent.EnterPassword(
                                        it
                                    )
                                )
                            },
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                            visualTransformation = if (forgetPasswordState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                TextButton(
                                    onClick = {
                                        forgetPasswordEvent(ForgetPasswordEvent.OnPasswordVisibilityToggle)
                                        hapticFeedbacks(
                                            isEnable = isHapticFeedback,
                                            hapticFeedback = hapticFeedback
                                        )
                                    }
                                ) {
                                    Text(
                                        text = if (forgetPasswordState.isPasswordVisible) "Hide" else "Show",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        )
                        Spacer(Modifier.height(dimens.size30))
                        CustomTextField(
                            label = { Text(text = "Confirm Password") },
                            placeholder = { Text(text = "Enter New Confirm Password") },
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.password),
                                    contentDescription = null
                                )
                            },
                            value = forgetPasswordState.confirmPassword,
                            onValueChange = {
                                forgetPasswordEvent(
                                    ForgetPasswordEvent.EnterConfirmPassword(
                                        it
                                    )
                                )
                            },
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                            visualTransformation = if (forgetPasswordState.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                TextButton(
                                    onClick = {
                                        forgetPasswordEvent(ForgetPasswordEvent.OnConfirmPasswordVisibilityToggle)
                                        hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
                                    }
                                ) {
                                    Text(
                                        text = if (forgetPasswordState.isConfirmPasswordVisible) "Hide" else "Show",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        )
                        Spacer(Modifier.height(dimens.size5))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (forgetPasswordState.password.length == forgetPasswordState.confirmPassword.length && forgetPasswordState.password.isNotBlank() && forgetPasswordState.confirmPassword.isNotBlank()) {
                                if (!forgetPasswordState.isPasswordValid) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.alert),
                                        contentDescription = "Alert",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(dimens.size5))
                                    Text(
                                        text = "Password do not match",
                                        fontFamily = FontFamily(Font(R.font.nunitoregular)),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(dimens.size30))
                        if (forgetPasswordState.isLoading) {
                            ButtonLoading(
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    keyboardController?.hide()
                                    forgetPasswordEvent(
                                        ForgetPasswordEvent.SubmitPassword(
                                            email = forgetPasswordState.email,
                                            password = forgetPasswordState.password
                                        )
                                    )
                                    hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
                                },
                                enabled = forgetPasswordState.isPasswordValid,
                                shape = RoundedCornerShape(dimens.size10),
                            ) {
                                Text("Submit")
                            }
                        }
                    }
                }
            }
        }
        if (forgetPasswordState.passwordUpdateSuccess) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = "Password Update Successful") },
                text = { Text(text = "Your password has been updated successfully.") },
                confirmButton = {},
                dismissButton = {},
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(dimens.size10)
            )
        }
    }
}

