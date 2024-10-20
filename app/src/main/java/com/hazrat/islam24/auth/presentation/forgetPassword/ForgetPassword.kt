package com.hazrat.islam24.auth.presentation.forgetPassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.presentation.component.CustomTextField
import com.hazrat.islam24.auth.presentation.profileScreen.component.profileCardShimmerEffect
import com.hazrat.islam24.ui.theme.dimens
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
    navigateToLogin: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
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
    Scaffold(snackbarHost = {
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
                title = {
                    when (forgetPasswordState.currentStep) {
                        0 -> Text(text = "Forget Password")
                        1 -> Text(text = "Verification")
                        2 -> Text(text = "New Password")
                    }

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
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(dimens.size20),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = forgetPasswordState.currentStep == 0,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
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
                        onValueChange = { forgetPasswordEvent(ForgetPasswordEvent.EnterEmail(it)) },
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                    Spacer(Modifier.height(dimens.size10))
                    Text(
                        text = "You may receive Email from us for security purpose",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(Modifier.height(dimens.size10))
                    if (forgetPasswordState.isLoading){
                        ButtonLoading(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }else{
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                keyboardController?.hide()
                                forgetPasswordEvent(ForgetPasswordEvent.SubmitEmail(forgetPasswordState.email))
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
                    if (forgetPasswordState.isLoading){
                        ButtonLoading()
                    }else{
                        Button(
                            modifier = Modifier,
                            onClick = {
                                forgetPasswordEvent(
                                    ForgetPasswordEvent.SubmitOtp(
                                        email = forgetPasswordState.email,
                                        otp = forgetPasswordState.otp
                                    )
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
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

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
                        onValueChange = { forgetPasswordEvent(ForgetPasswordEvent.EnterPassword(it)) },
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        visualTransformation = if (forgetPasswordState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            TextButton(
                                onClick = { forgetPasswordEvent(ForgetPasswordEvent.OnPasswordVisibilityToggle) }
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
                                onClick = { forgetPasswordEvent(ForgetPasswordEvent.OnConfirmPasswordVisibilityToggle) }
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
                    if (forgetPasswordState.isLoading){
                        ButtonLoading(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }else{
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
                            },
                            enabled = forgetPasswordState.isPasswordValid,
                            shape = RoundedCornerShape(dimens.size10)
                        ) {
                            Text("Submit")
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

@Composable
private fun ButtonLoading(modifier: Modifier = Modifier) {
    Button(
        modifier = modifier
            .profileCardShimmerEffect(),
        onClick = {},
        shape = RoundedCornerShape(dimens.size10),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text("Submit")
    }
}


@Composable
fun OtpInputField(
    otp: String,
    onOtpChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = otp,
        onValueChange = {
            if (it.length <= 4) {
                onOtpChanged(it)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(dimens.size10)
        ) {
            repeat(4) { index ->
                val number = when {
                    index >= otp.length -> ""
                    else -> otp[index]
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimens.size6),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = number.toString(), style = MaterialTheme.typography.titleLarge)
                    Box(
                        modifier = Modifier
                            .width(dimens.size40)
                            .height(dimens.size2)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}