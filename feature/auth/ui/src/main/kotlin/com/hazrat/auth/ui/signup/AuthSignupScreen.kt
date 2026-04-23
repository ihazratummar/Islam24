package com.hazrat.auth.ui.signup

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.hazrat.auth.ui.component.CustomTextField
import com.hazrat.common.BasicTopBar
import com.hazrat.model.AuthState
import com.hazrat.ui.R
import com.hazrat.ui.profileCardShimmerEffect
import com.hazrat.ui.theme.dimens
import com.hazrat.utils.hapticFeedbacks

/**
 * @author Hazrat Ummar Shaikh
 */


@Composable
fun AuthSignupScreen(
    signUpState: SingupState,
    onEvent: (SingupEvent) -> Unit,
    authState: AuthState,
    onBackClick: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToProfile: () -> Unit,
    isHapticFeedback: Boolean = false
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navigateToProfile()
            }

            is AuthState.Error -> {
                Toast.makeText(context, authState.message, Toast.LENGTH_LONG).show()
            }

            else -> Unit
        }
        onEvent(SingupEvent.Refresh)
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.height(dimens.size30))
            BasicTopBar(
                onBackClick = { onBackClick.invoke() }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimens.size20),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(dimens.size30))
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Spacer(modifier = Modifier.height(dimens.size20))
                    Text(
                        text = "First create your account",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFA8A6A7)
                    )
                    Spacer(modifier = Modifier.height(dimens.size40))
                    CustomTextField(
                        label = { Text(text = "Full name") },
                        placeholder = { Text(text = "Enter your full name") },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.editcontained),
                                contentDescription = null
                            )
                        },
                        value = signUpState.name,
                        onValueChange = {
                            onEvent(SingupEvent.SetName(it))
                        },
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                    Spacer(modifier = Modifier.height(dimens.size5))
                    CustomTextField(
                        label = { Text(text = "Email") },
                        placeholder = { Text(text = "Enter Your Email") },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.email),
                                contentDescription = null
                            )
                        },
                        value = signUpState.email,
                        onValueChange = {
                            onEvent(SingupEvent.SetEmail(it))
                        },
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                    Spacer(modifier = Modifier.height(dimens.size5))
                    CustomTextField(
                        label = { Text(text = "Password") },
                        placeholder = { Text(text = "Enter Your Password") },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.password),
                                contentDescription = null
                            )
                        },
                        value = signUpState.password,
                        onValueChange = {
                            onEvent(SingupEvent.SetPassword(it))

                        },
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        trailingIcon = {
                            Text(
                                text = if (signUpState.passwordVisible) "Hide" else "Show",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.clickable {
                                    onEvent(SingupEvent.OnPasswordVisibilityChanged)
                                    hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
                                },
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        visualTransformation = if (signUpState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(dimens.size5))
                    CustomTextField(
                        label = { Text(text = "Confirm password") },
                        placeholder = { Text(text = "Enter Your Password") },
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.password),
                                contentDescription = null
                            )
                        },
                        value = signUpState.confirmPassword,
                        onValueChange = {
                            onEvent(SingupEvent.SetConfirmPassword(it))
                        },
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        trailingIcon = {
                            Text(
                                text = if (signUpState.confirmPasswordVisible) "Hide" else "Show",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.clickable {
                                    onEvent(SingupEvent.OnConfirmPasswordVisibilityChanged)
                                    hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
                                },
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        visualTransformation = if (signUpState.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(dimens.size2))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (signUpState.confirmPassword.isNotBlank() && !signUpState.isPasswordValid) {
                            Icon(
                                painter = painterResource(id = R.drawable.alert),
                                contentDescription = "Alert",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(dimens.size5))
                            Text(
                                text = "Password do not match",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(dimens.size35))
                    SignupButton(
                        onEvent = onEvent,
                        signUpState = signUpState,
                        authState = authState
                    )
                    Spacer(modifier = Modifier.height(dimens.size15))
                    Row {
                        Text(
                            text = "Already have an account?",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(modifier = Modifier.width(dimens.size2))
                        Text(
                            text = "Sign In",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                navigateToLogin()
                                hapticFeedbacks(isEnable = isHapticFeedback, hapticFeedback = hapticFeedback)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
private fun SignupButton(
    modifier: Modifier = Modifier,
    onEvent: (SingupEvent) -> Unit,
    signUpState: SingupState,
    authState: AuthState
) {
    val hapticFeedback = LocalHapticFeedback.current
    Button(
        onClick = {
            onEvent(
                SingupEvent.Signup(
                    signUpState.name,
                    signUpState.email,
                    signUpState.password,
                    signUpState.confirmPassword
                )
            )
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        modifier = modifier.fillMaxWidth()
            .let { if (authState == AuthState.Loading) it.profileCardShimmerEffect() else it },
        colors = ButtonColors(
            containerColor = if (authState == AuthState.Loading) Color.Transparent else MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = if (authState == AuthState.Loading) Color.Transparent else MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        enabled = signUpState.isFormValid && signUpState.isPasswordValid && authState != AuthState.Loading,
        shape = RoundedCornerShape(dimens.size10)
    ) {
        Text(
            text = "SIGN UP",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}