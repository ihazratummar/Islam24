package com.hazrat.islam24.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import com.hazrat.islam24.R
import com.hazrat.islam24.auth.AuthState
import com.hazrat.islam24.auth.presentation.component.CustomTextField
import com.hazrat.islam24.auth.presentation.profileScreen.component.profileCardShimmerEffect
import com.hazrat.islam24.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthLoginScreen(
    state: LoginState,
    loginEvent: (LoginEvent) -> Unit,
    authState: AuthState,
    navigateToSignup: () -> Unit,
    navigateToProfile: () -> Unit,
    onBackClick: () -> Unit,
    navigateToForgotPassword: () -> Unit
) {
    val context = LocalContext.current

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
//        loginEvent(LoginEvent.Refresh)
    }

    val onLoginClick = remember(state.email, state.password) {
        {
            loginEvent(LoginEvent.Login(email = state.email, password = state.password))
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { /*TODO*/ },
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
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = dimens.size20),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(dimens.size60))
                Text(
                    text = "Sign In",
                    fontFamily = FontFamily(Font(R.font.nunitobold)),
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(dimens.size20))
                Text(
                    text = "Enter your email and password",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily(Font(R.font.nunitobold)),
                    color = Color(0xFFA8A6A7)
                )
                Spacer(modifier = Modifier.height(dimens.size60))
                CustomTextField(
                    label = { Text(text = "Email") },
                    placeholder = { Text(text = "Enter Your Email") },
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.email),
                            contentDescription = null
                        )
                    },
                    value = state.email,
                    onValueChange = { loginEvent(LoginEvent.SetEmail(it)) },
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
                    value = state.password,
                    onValueChange = { loginEvent(LoginEvent.SetPassword(it)) },
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    trailingIcon = {
                        Text(
                            text = if (state.passwordVisible) "Hide" else "Show",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.clickable {
                                loginEvent(LoginEvent.OnPasswordVisibilityChanged)
                            },
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(dimens.size35))
            }
            item {
                if (authState == AuthState.Loading) {
                    LoginButton(
                        modifier = Modifier.profileCardShimmerEffect(),
                        onLoginClick = onLoginClick,
                        state = state,
                        authState = authState,
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent
                    )
                } else {
                    LoginButton(
                        onLoginClick = onLoginClick,
                        state = state,
                        authState = authState,
                    )
                }
                Spacer(modifier = Modifier.height(dimens.size15))
                Text(
                    text = "Forgotten Password?",
                    fontFamily = FontFamily(Font(R.font.nunitoregular)),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navigateToForgotPassword.invoke()
                    }
                )
                Spacer(modifier = Modifier.height(dimens.size30))
                Row {
                    Text(
                        text = "Don't have an account?",
                        fontFamily = FontFamily(Font(R.font.nunitoregular)),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(dimens.size4))
                    Text(
                        text = "Sign Up",
                        fontFamily = FontFamily(Font(R.font.nunitoregular)),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            navigateToSignup()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(dimens.size30))
            }
        }
    }
}

@Composable
private fun LoginButton(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    state: LoginState,
    authState: AuthState,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    Button(
        onClick = onLoginClick,
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        enabled = state.isFormValid && authState != AuthState.Loading,
        shape = RoundedCornerShape(dimens.size10)
    ) {
        Text(
            text = "LOGIN",
            fontFamily = FontFamily(Font(R.font.nunitoregular)),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}